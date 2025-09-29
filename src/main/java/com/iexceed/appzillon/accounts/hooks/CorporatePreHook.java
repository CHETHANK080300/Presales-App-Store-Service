package com.iexceed.appzillon.accounts.hooks;

import org.springframework.stereotype.Component;

@Component
public class CorporatePreHook implements PreHookProcessor {
    @Override
    public <T> T process(T request) {
        // Add your pre-processing logic here
        return request;
    }
}
