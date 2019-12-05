package com.codelab.accounts.domain.response;

/**
 * @author lordUhuru 04/12/2019
 */
public class HttpError {
    private String error;
    private int code;
    private String message;

    public HttpError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
