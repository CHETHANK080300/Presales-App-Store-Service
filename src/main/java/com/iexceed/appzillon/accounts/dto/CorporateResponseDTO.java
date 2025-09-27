package com.iexceed.appzillon.accounts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CorporateResponseDTO {

    @JsonProperty("corporateDetails")
    private CorporateDetailsDTO corporateDetails;

    @JsonProperty("accounts")
    private List<AccountDTO> accounts;

    // getters and setters
    public CorporateDetailsDTO getCorporateDetails() {
        return corporateDetails;
    }

    public void setCorporateDetails(CorporateDetailsDTO corporateDetails) {
        this.corporateDetails = corporateDetails;
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDTO> accounts) {
        this.accounts = accounts;
    }
}
