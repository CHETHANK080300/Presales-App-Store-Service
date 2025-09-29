package com.iexceed.appzillon.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Fund Transfer request body for NAPAS/CITAD")
public class FundTransferRequestBody {
    @Schema(example = "NAPAS", description = "Type of transfer: NAPAS or CITAD")
    private String transferType;
    @Schema(example = "FT", description = "Operation type")
    private String operation;
    @Schema(description = "Transfer Info")
    private XferInfo xferInfo;
    @Schema(example = "1234567890", description = "Beneficiary Account")
    private String beneficiaryAct;
    @Schema(description = "Customer Info")
    private Customer customer;
    @Schema(description = "Fee Info")
    private FeeInfo feeInfo;
    @Schema(description = "T24 Info")
    private T24 t24;
    @Schema(example = "Y", description = "CITAD Indirect VCB")
    private String CITADIndirVCB;
    @Schema(example = "001", description = "Officer Account Branch")
    private String officerAcctBranch;

    public String getTransferType() { return transferType; }
    public void setTransferType(String transferType) { this.transferType = transferType; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public XferInfo getXferInfo() { return xferInfo; }
    public void setXferInfo(XferInfo xferInfo) { this.xferInfo = xferInfo; }
    public String getBeneficiaryAct() { return beneficiaryAct; }
    public void setBeneficiaryAct(String beneficiaryAct) { this.beneficiaryAct = beneficiaryAct; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public FeeInfo getFeeInfo() { return feeInfo; }
    public void setFeeInfo(FeeInfo feeInfo) { this.feeInfo = feeInfo; }
    public T24 getT24() { return t24; }
    public void setT24(T24 t24) { this.t24 = t24; }
    public String getCITADIndirVCB() { return CITADIndirVCB; }
    public void setCITADIndirVCB(String CITADIndirVCB) { this.CITADIndirVCB = CITADIndirVCB; }
    public String getOfficerAcctBranch() { return officerAcctBranch; }
    public void setOfficerAcctBranch(String officerAcctBranch) { this.officerAcctBranch = officerAcctBranch; }

    // Nested class getters and setters
    public static class XferInfo {
        @Schema(example = "1000.00", description = "Amount to transfer")
        private String amount;
        @Schema(description = "Credit Branch Info")
        private CreditBranch creditBranch;
        @Schema(description = "Debit Account Info")
        private DebitAccount debitAccount;
        @Schema(example = "Payment for invoice", description = "Remark")
        private String remark;
        @Schema(example = "12:00:00", description = "Transaction Time")
        private String transTime;
        @Schema(example = "2025-09-29", description = "Transaction Date")
        private String transDate;
        @Schema(example = "MOBILE", description = "Channel")
        private String channel;

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
        public CreditBranch getCreditBranch() { return creditBranch; }
        public void setCreditBranch(CreditBranch creditBranch) { this.creditBranch = creditBranch; }
        public DebitAccount getDebitAccount() { return debitAccount; }
        public void setDebitAccount(DebitAccount debitAccount) { this.debitAccount = debitAccount; }
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
        public String getTransTime() { return transTime; }
        public void setTransTime(String transTime) { this.transTime = transTime; }
        public String getTransDate() { return transDate; }
        public void setTransDate(String transDate) { this.transDate = transDate; }
        public String getChannel() { return channel; }
        public void setChannel(String channel) { this.channel = channel; }
    }
    public static class CreditBranch {
        @Schema(example = "001", description = "Bank Number")
        private String bankNo;
        public String getBankNo() { return bankNo; }
        public void setBankNo(String bankNo) { this.bankNo = bankNo; }
    }
    public static class DebitAccount {
        @Schema(example = "9876543210", description = "Account Number")
        private String acctNo;
        @Schema(example = "VND", description = "Currency")
        private String currency;
        public String getAcctNo() { return acctNo; }
        public void setAcctNo(String acctNo) { this.acctNo = acctNo; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }
    public static class Customer {
        @Schema(example = "John Doe", description = "Beneficiary Customer Name")
        private String benCustomer;
        public String getBenCustomer() { return benCustomer; }
        public void setBenCustomer(String benCustomer) { this.benCustomer = benCustomer; }
    }
    public static class FeeInfo {
        @Schema(example = "FEE01", description = "Fee Code")
        private String code;
        @Schema(example = "10.00", description = "Fee Amount")
        private String amount;
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
    }
    public static class T24 {
        @Schema(example = "Hanoi", description = "Receiving Address")
        private String receivingAddr;
        public String getReceivingAddr() { return receivingAddr; }
        public void setReceivingAddr(String receivingAddr) { this.receivingAddr = receivingAddr; }
    }
}
