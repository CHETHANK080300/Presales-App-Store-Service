package com.iexceed.appzillon.accounts.controller;

import com.iexceed.appzillon.accounts.dto.ApiRequest;
import com.iexceed.appzillon.accounts.dto.FundTransferRequestBody;
import com.iexceed.appzillon.accounts.dto.ApiResponse;
import com.iexceed.appzillon.accounts.service.FundTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@RequestMapping("/api/fundTransfer")
public class FundTransferController {

    @Autowired
    private FundTransferService fundTransferService;

    @PostMapping
    @Operation(
        summary = "Process Fund Transfer (NAPAS/CITAD)",
        description = "Initiate a fund transfer using NAPAS or CITAD integration.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = ApiRequest.class, subTypes = FundTransferRequestBody.class),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "FundTransferRequest",
                        value = "{\n  \"requestHeader\": {\n    \"appID\": \"APP001\",\n    \"interfaceName\": \"FundTransfer\",\n    \"corporateID\": \"CORP123\",\n    \"deviceID\": \"DEV001\",\n    \"channel\": \"MOBILE\"\n  },\n  \"requestBody\": {\n    \"transferType\": \"NAPAS\",\n    \"operation\": \"FT\",\n    \"xferInfo\": {\n      \"amount\": \"1000.00\",\n      \"creditBranch\": {\n        \"bankNo\": \"001\"\n      },\n      \"debitAccount\": {\n        \"acctNo\": \"9876543210\",\n        \"currency\": \"VND\"\n      },\n      \"remark\": \"Payment for invoice\",\n      \"transTime\": \"12:00:00\",\n      \"transDate\": \"2025-09-29\",\n      \"channel\": \"MOBILE\"\n    },\n    \"beneficiaryAct\": \"1234567890\",\n    \"customer\": {\n      \"benCustomer\": \"John Doe\"\n    },\n    \"feeInfo\": {\n      \"code\": \"FEE01\",\n      \"amount\": \"10.00\"\n    },\n    \"t24\": {\n      \"receivingAddr\": \"Hanoi\"\n    },\n    \"CITADIndirVCB\": \"Y\",\n    \"officerAcctBranch\": \"001\"\n  }\n}"
                    )
                }
            )
        )
    )
    public ApiResponse processFundTransfer(@org.springframework.web.bind.annotation.RequestBody ApiRequest<FundTransferRequestBody> request) {
        return fundTransferService.processFundTransfer(request);
    }
}
