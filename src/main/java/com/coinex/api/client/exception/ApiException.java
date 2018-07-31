package com.coinex.api.client.exception;

import org.apache.commons.lang3.StringUtils;

public class ApiException extends RuntimeException {

    private Integer errorCode;
    private String errorMsg;

    public ApiException(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private String describeError(int errorCode, String errorMsg) {
        StringBuffer stringBuffer = new StringBuffer("api error: ");
        stringBuffer.append(errorCode);
        if (StringUtils.isNotEmpty(errorMsg)) {
            stringBuffer.append(" - ").append(errorMsg);
        }
        return stringBuffer.toString();
    }

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String getMessage() {
        if (errorCode == null) {
            return super.getMessage();
        } else {
            return describeError(errorCode, errorMsg);
        }
    }
}
