package com.iexceed.appzillon.accounts.dto;



public class ApiRequest<T> {
    private RequestHeader requestHeader;
    private T requestBody;

    // getters and setters
    public RequestHeader getRequestHeader() { return requestHeader; }
    public void setRequestHeader(RequestHeader requestHeader) { this.requestHeader = requestHeader; }
    public T getRequestBody() { return requestBody; }
    public void setRequestBody(T requestBody) { this.requestBody = requestBody; }
}

