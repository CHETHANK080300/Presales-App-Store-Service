package com.iexceed.appzillon.accounts.hooks;

public interface PreHookProcessor {
    <T> T process(T request, String interfaceName);
}
