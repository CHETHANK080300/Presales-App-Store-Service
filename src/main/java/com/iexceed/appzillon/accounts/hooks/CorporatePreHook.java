package com.iexceed.appzillon.accounts.hooks;

import org.springframework.stereotype.Component;

@Component
public class CorporatePreHook implements PreHookProcessor {
    @Override
    public <T> T process(T request, String interfaceName) {
        if (interfaceName.toLowerCase().contains("corporatedetails")) {
            // CorporateDetails-specific post-hook logic
        } else if (interfaceName != null && (interfaceName.toLowerCase().contains("fundtransfernapas") || interfaceName.toLowerCase().contains("fundtransfercitad"))) {
            // FundTransfer-specific pre-hook logic
            if (request instanceof com.iexceed.appzillon.accounts.dto.ApiRequest) {
                com.iexceed.appzillon.accounts.dto.ApiRequest<?> apiReq = (com.iexceed.appzillon.accounts.dto.ApiRequest<?>) request;
                Object body = apiReq.getRequestBody();
                if (body instanceof com.iexceed.appzillon.accounts.dto.FundTransferRequestBody) {
                    com.iexceed.appzillon.accounts.dto.FundTransferRequestBody ftBody = (com.iexceed.appzillon.accounts.dto.FundTransferRequestBody) body;
                    String transferType = ftBody.getTransferType();
                    com.iexceed.appzillon.accounts.dto.FundTransferRequestBody.XferInfo xferInfo = ftBody.getXferInfo();
                    if (transferType != null && xferInfo != null && xferInfo.getAmount() != null) {
                        try {
                            double amount = Double.parseDouble(xferInfo.getAmount());
                            double threshold = 500_000_000d;
                            if ("NAPAS".equalsIgnoreCase(transferType)) {
                                if (amount > threshold) {
                                    throw new RuntimeException("Amount exceeds 500M. Please change transfer type to CITAD to initiate high value transaction.");
                                }
                            } else if ("CITAD".equalsIgnoreCase(transferType)) {
                                if (amount <= threshold) {
                                    throw new RuntimeException("Amount is 500M or less. Please use NAPAS for low value transaction.");
                                }
                            }
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid amount format in Fund Transfer request.");
                        }
                    }
                }
            }
        }
        return request;
    }
}
