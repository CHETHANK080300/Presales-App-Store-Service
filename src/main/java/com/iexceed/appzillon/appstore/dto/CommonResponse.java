package com.iexceed.appzillon.appstore.dto;

public class CommonResponse {
    private String status; // SUCCESS or FAILURE
    private String message;
    private Object responseBody;

    public CommonResponse() {}

    public CommonResponse(String status, String message, Object responseBody) {
        this.status = status;
        this.message = message;
        this.responseBody = responseBody;
    }

    public static CommonResponse success(String message, Object body) {
        return new CommonResponse("SUCCESS", message, body);
    }

    public static CommonResponse failure(String message) {
        return new CommonResponse("FAILURE", message, null);
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getResponseBody() { return responseBody; }
    public void setResponseBody(Object responseBody) { this.responseBody = responseBody; }
}
