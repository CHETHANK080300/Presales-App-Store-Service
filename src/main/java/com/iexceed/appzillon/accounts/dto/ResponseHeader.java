package com.iexceed.appzillon.accounts.dto;

public class ResponseHeader {
	private String statusCode;
    private String statusMessage;

    public ResponseHeader() {}
    public ResponseHeader(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    // getters and setters
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
}
