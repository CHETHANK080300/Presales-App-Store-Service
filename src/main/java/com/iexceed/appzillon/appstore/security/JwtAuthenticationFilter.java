package com.iexceed.appzillon.appstore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
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
import com.iexceed.appzillon.appstore.entity.UserSessionEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private UserSessionRepository sessionRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token);
                String userId = claims.getBody().getSubject();
                String role = claims.getBody().get("role", String.class);
                // verify session exists and is active (prevents reuse after logout)
                java.util.Optional<UserSessionEntity> sesOpt = sessionRepository.findById(token);
                if (sesOpt.isEmpty()) {
                    logger.warn("JWT valid but session not found (possibly logged out): userId={}", userId);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"status\":\"EXPIRED_OR_INVALID\",\"error\":\"Session not found\"}");
                    response.getWriter().flush();
                    return;
                }
                UserSessionEntity ses = sesOpt.get();
                if (!"ACTIVE".equalsIgnoreCase(ses.getStatus()) || ses.getExpireAt() == null || java.time.Instant.now().isAfter(ses.getExpireAt())) {
                    logger.warn("JWT session inactive or expired for userId={}", userId);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"status\":\"EXPIRED_OR_INVALID\",\"error\":\"Session expired or inactive\"}");
                    response.getWriter().flush();
                    return;
                }
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.debug("JWT valid for userId={} role={}", userId, role);
            } catch (Exception e) {
                // invalid token, clear context
                logger.warn("Invalid/expired JWT: {}", e.getMessage());
                SecurityContextHolder.clearContext();
                    // Immediately return 401 JSON response so client receives feedback for expired/invalid tokens
                    try {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json;charset=UTF-8");
                        String msg = e.getMessage() == null ? "Invalid token" : e.getMessage();
                        String json = String.format("{\"status\":\"EXPIRED_OR_INVALID\",\"error\":\"%s\"}", msg.replaceAll("\"","\\\""));
                        response.getWriter().write(json);
                        response.getWriter().flush();
                    } catch (Exception ex) {
                        logger.error("Failed to write unauthorized response", ex);
                    }
                    return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
