package com.iexceed.appzillon.accounts.hooks;

import org.springframework.stereotype.Component;

@Component
public class CorporatePostHook implements PostHookProcessor {
    @Override
    public <T> T process(T response) {
        // Manipulate ACC_STATUS in accounts array if present
        /*if (response instanceof java.util.Map) {
            java.util.Map<?,?> respMap = (java.util.Map<?,?>) response;
            Object accountsObj = respMap.get("accounts");
            if (accountsObj instanceof java.util.List) {
                java.util.List<?> accounts = (java.util.List<?>) accountsObj;
                for (Object accObj : accounts) {
                    if (accObj instanceof java.util.Map) {
                        java.util.Map accMap = (java.util.Map) accObj;
                        Object accStatus = accMap.get("ACC_STATUS");
                        if (accStatus != null) {
                            String statusStr = accStatus.toString();
                            if ("0".equals(statusStr)) {
                                accMap.put("ACC_STATUS", "IN-ACTIVE");
                            } else if ("1".equals(statusStr)) {
                                accMap.put("ACC_STATUS", "ACTIVE");
                            }
                        }
                    }
                }
            }
        }*/
        return response;
    }
}
