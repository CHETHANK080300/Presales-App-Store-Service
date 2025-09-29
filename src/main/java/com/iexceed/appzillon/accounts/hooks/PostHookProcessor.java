package com.iexceed.appzillon.accounts.hooks;

public interface PostHookProcessor {
    <T> T process(T response);
}
