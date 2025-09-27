package com.iexceed.appzillon.accounts.dto;

import java.util.Map;

public class ApiRequest<T> {
    private RequestHeader requestHeader;
    private T requestBody;

    // getters and setters
    public RequestHeader getRequestHeader() { return requestHeader; }
    public void setRequestHeader(RequestHeader requestHeader) { this.requestHeader = requestHeader; }
    public T getRequestBody() { return requestBody; }
    public void setRequestBody(T requestBody) { this.requestBody = requestBody; }
}

class RequestHeader {
    private String appID;
    private String interfaceName;
    private String corporateID;
    private String deviceID;
    private String channel;

    // getters and setters
    public String getAppID() { return appID; }
    public void setAppID(String appID) { this.appID = appID; }
    public String getInterfaceName() { return interfaceName; }
    public void setInterfaceName(String interfaceName) { this.interfaceName = interfaceName; }
    public String getCorporateID() { return corporateID; }
    public void setCorporateID(String corporateID) { this.corporateID = corporateID; }
    public String getDeviceID() { return deviceID; }
    public void setDeviceID(String deviceID) { this.deviceID = deviceID; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
}
