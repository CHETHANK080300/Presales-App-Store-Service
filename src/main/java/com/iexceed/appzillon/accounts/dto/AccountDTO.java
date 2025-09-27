package com.iexceed.appzillon.accounts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDTO {

    @JsonProperty("ACCOUNT_NO")
    private String accountNo;

    @JsonProperty("CUSTOMER_ID")
    private String customerId;

    @JsonProperty("ACC_HOLDER_NAME")
    private String accHolderName;

    @JsonProperty("ACC_STATUS")
    private String accStatus;

    @JsonProperty("ACCOUNT_TYPE")
    private String accountType;

    @JsonProperty("ACCOUNT_NAME")
    private String accountName;

    @JsonProperty("ALLOW_OVERDRAFT")
    private String allowOverdraft;

    @JsonProperty("AVAILABLE_BALACE")
    private String availableBalance;

    @JsonProperty("BRANCH_ADDRESS")
    private String branchAddress;

    @JsonProperty("BANK_NAME")
    private String bankName;

    @JsonProperty("BRANCH_CODE")
    private String branchCode;

    @JsonProperty("BRANCH_NAME")
    private String branchName;

    @JsonProperty("CREDIT_ALLOWED")
    private String creditAllowed;

    @JsonProperty("CURRENCY")
    private String currency;

    @JsonProperty("CURRENT_BALANCE")
    private String currentBalance;

    @JsonProperty("DEBIT_ALLOWED")
    private String debitAllowed;

    @JsonProperty("EMAIL_FREQUENCY")
    private String emailFrequency;

    @JsonProperty("IFSC_CODE")
    private String ifscCode;

    @JsonProperty("INTEREST_RATE")
    private String interestRate;

    @JsonProperty("JOINT_HOLDER_NAME")
    private String jointHolderName;

    @JsonProperty("LIEN_AMOUNT")
    private String lienAmount;

    @JsonProperty("NICK_NAME")
    private String nickName;

    @JsonProperty("ACC_OPEN_DATE")
    private String accOpenDate;

    @JsonProperty("OVERDRAFT_LIMIT")
    private String overdraftLimit;

    @JsonProperty("PROD_CODE")
    private String prodCode;

    @JsonProperty("PRODUCTNAME")
    private String productName;

    @JsonProperty("RELATIONSHIP")
    private String relationship;

    @JsonProperty("WITHDRAW_BALANCE")
    private String withdrawBalance;

    // getters and setters
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAccHolderName() { return accHolderName; }
    public void setAccHolderName(String accHolderName) { this.accHolderName = accHolderName; }

    public String getAccStatus() { return accStatus; }
    public void setAccStatus(String accStatus) { this.accStatus = accStatus; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getAllowOverdraft() { return allowOverdraft; }
    public void setAllowOverdraft(String allowOverdraft) { this.allowOverdraft = allowOverdraft; }

    public String getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(String availableBalance) { this.availableBalance = availableBalance; }

    public String getBranchAddress() { return branchAddress; }
    public void setBranchAddress(String branchAddress) { this.branchAddress = branchAddress; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getCreditAllowed() { return creditAllowed; }
    public void setCreditAllowed(String creditAllowed) { this.creditAllowed = creditAllowed; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(String currentBalance) { this.currentBalance = currentBalance; }

    public String getDebitAllowed() { return debitAllowed; }
    public void setDebitAllowed(String debitAllowed) { this.debitAllowed = debitAllowed; }

    public String getEmailFrequency() { return emailFrequency; }
    public void setEmailFrequency(String emailFrequency) { this.emailFrequency = emailFrequency; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getInterestRate() { return interestRate; }
    public void setInterestRate(String interestRate) { this.interestRate = interestRate; }

    public String getJointHolderName() { return jointHolderName; }
    public void setJointHolderName(String jointHolderName) { this.jointHolderName = jointHolderName; }

    public String getLienAmount() { return lienAmount; }
    public void setLienAmount(String lienAmount) { this.lienAmount = lienAmount; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getAccOpenDate() { return accOpenDate; }
    public void setAccOpenDate(String accOpenDate) { this.accOpenDate = accOpenDate; }

    public String getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(String overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    public String getProdCode() { return prodCode; }
    public void setProdCode(String prodCode) { this.prodCode = prodCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getWithdrawBalance() { return withdrawBalance; }
    public void setWithdrawBalance(String withdrawBalance) { this.withdrawBalance = withdrawBalance; }
}
