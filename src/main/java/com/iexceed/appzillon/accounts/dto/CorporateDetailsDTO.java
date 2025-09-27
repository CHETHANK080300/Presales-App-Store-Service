package com.iexceed.appzillon.accounts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CorporateDetailsDTO {

    @JsonProperty("CUSTOMER_ID")
    private String customerId;

    @JsonProperty("ADDRESS_LINE")
    private String addressLine;

    @JsonProperty("ADDRESS_LINE2")
    private String addressLine2;

    @JsonProperty("BUSINESS_PLACE")
    private String businessPlace;

    @JsonProperty("CLIENT_NAME")
    private String clientName;

    @JsonProperty("COMM_LIC_EXP_DATE")
    private String commLicExpDate;

    @JsonProperty("COMM_LIC_NUMBER")
    private String commLicNumber;

    @JsonProperty("CORRELATION_ID")
    private String correlationId;

    @JsonProperty("EMAIL")
    private String email;

    @JsonProperty("ENTITY_CATEGORY")
    private String entityCategory;

    @JsonProperty("ENTITY_TYPE")
    private String entityType;

    @JsonProperty("INCORPORATION_COUNTRY")
    private String incorporationCountry;

    @JsonProperty("INCORPORATION_DATE")
    private String incorporationDate;

    @JsonProperty("PHONE_NO")
    private String phoneNo;

    @JsonProperty("REG_BODY")
    private String regBody;

    @JsonProperty("REG_NUMBER")
    private String regNumber;

    // getters and setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getBusinessPlace() { return businessPlace; }
    public void setBusinessPlace(String businessPlace) { this.businessPlace = businessPlace; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getCommLicExpDate() { return commLicExpDate; }
    public void setCommLicExpDate(String commLicExpDate) { this.commLicExpDate = commLicExpDate; }

    public String getCommLicNumber() { return commLicNumber; }
    public void setCommLicNumber(String commLicNumber) { this.commLicNumber = commLicNumber; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEntityCategory() { return entityCategory; }
    public void setEntityCategory(String entityCategory) { this.entityCategory = entityCategory; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getIncorporationCountry() { return incorporationCountry; }
    public void setIncorporationCountry(String incorporationCountry) { this.incorporationCountry = incorporationCountry; }

    public String getIncorporationDate() { return incorporationDate; }
    public void setIncorporationDate(String incorporationDate) { this.incorporationDate = incorporationDate; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getRegBody() { return regBody; }
    public void setRegBody(String regBody) { this.regBody = regBody; }

    public String getRegNumber() { return regNumber; }
    public void setRegNumber(String regNumber) { this.regNumber = regNumber; }
}
