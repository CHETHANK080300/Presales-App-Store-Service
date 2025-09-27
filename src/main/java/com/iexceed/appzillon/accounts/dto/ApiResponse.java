package com.iexceed.appzillon.accounts.dto;

public class ApiResponse<T> {
    private int serviceStatusCode; // 0-success, 1-failed
    private ResponseHeader responseHeader;
    private T responseBody;

    public ApiResponse() {}

    public ApiResponse(int serviceStatusCode, ResponseHeader responseHeader, T responseBody) {
        this.serviceStatusCode = serviceStatusCode;
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
    }

    // getters and setters
    public int getServiceStatusCode() { return serviceStatusCode; }
    public void setServiceStatusCode(int serviceStatusCode) { this.serviceStatusCode = serviceStatusCode; }
    public ResponseHeader getResponseHeader() { return responseHeader; }
    public void setResponseHeader(ResponseHeader responseHeader) { this.responseHeader = responseHeader; }
    public T getResponseBody() { return responseBody; }
    public void setResponseBody(T responseBody) { this.responseBody = responseBody; }
}

