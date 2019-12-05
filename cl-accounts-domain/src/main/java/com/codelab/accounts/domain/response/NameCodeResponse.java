package com.codelab.accounts.domain.response;

/**
 * @author lordUhuru 04/12/2019
 */
public class NameCodeResponse {
    private String name;
    private String code;

    public NameCodeResponse(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "NameCodeResponse{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
