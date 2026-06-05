package com.iexceed.appzillon.appstore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import com.iexceed.appzillon.appstore.repository.UserSessionRepository;
import com.iexceed.appzillon.appstore.repository.ActiveUserRepository;
import com.iexceed.appzillon.appstore.entity.UserSessionEntity;
import com.iexceed.appzillon.appstore.entity.ActiveUserEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private UserSessionRepository sessionRepository;

    @Autowired
    private ActiveUserRepository activeUserRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userId = null;
            String role = null;
            boolean isExpired = false;

            try {
                Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token);
                userId = claims.getBody().getSubject();
                role = claims.getBody().get("role", String.class);
            } catch (ExpiredJwtException e) {
                logger.info("JWT expired: {}", e.getMessage());
                userId = e.getClaims().getSubject();
                role = e.getClaims().get("role", String.class);
                isExpired = true;
            } catch (Exception e) {
                logger.warn("Invalid JWT: {}", e.getMessage());
                handleUnauthorized(response, "Invalid token");
                return;
            }

            Optional<UserSessionEntity> sesOpt = sessionRepository.findById(token);
            if (sesOpt.isEmpty()) {
                logger.warn("Session not found for token: userId={}", userId);
                handleUnauthorized(response, "Session not found");
                return;
            }

            UserSessionEntity ses = sesOpt.get();
            if (!"ACTIVE".equalsIgnoreCase(ses.getStatus())) {
                logger.warn("Session inactive for userId={}", userId);
                handleUnauthorized(response, "Session inactive");
                return;
            }

            Instant now = Instant.now();
            if (isExpired) {
                // Check if refresh token is still valid
                if (ses.getRefreshExpireAt() != null && now.isBefore(ses.getRefreshExpireAt())) {
                    logger.info("Internal token refresh for userId={}", userId);
                    // Update expiry
                    Instant newExpireAt = now.plusSeconds(jwtExpiration);
                    ses.setExpireAt(newExpireAt);
                    sessionRepository.save(ses);

                    Optional<ActiveUserEntity> activeOpt = activeUserRepository.findById(token);
                    if (activeOpt.isPresent()) {
                        ActiveUserEntity au = activeOpt.get();
                        au.setExpireAt(newExpireAt);
                        activeUserRepository.save(au);
                    }
                } else {
                    logger.warn("Refresh token expired for userId={}", userId);
                    handleUnauthorized(response, "Session expired");
                    return;
                }
            } else if (ses.getExpireAt() != null && now.isAfter(ses.getExpireAt())) {
                 // Even if JWT parser didn't throw (e.g. clock skew), check DB expiry
                 if (ses.getRefreshExpireAt() != null && now.isBefore(ses.getRefreshExpireAt())) {
                     Instant newExpireAt = now.plusSeconds(jwtExpiration);
                     ses.setExpireAt(newExpireAt);
                     sessionRepository.save(ses);
                 } else {
                     handleUnauthorized(response, "Session expired");
                     return;
                 }
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.debug("Authenticated userId={} role={}", userId, role);
        }
        filterChain.doFilter(request, response);
    }

    private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String json = String.format("{\"status\":\"EXPIRED_OR_INVALID\",\"error\":\"%s\"}", message);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
