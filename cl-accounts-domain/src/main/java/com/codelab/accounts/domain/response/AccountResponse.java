package com.codelab.accounts.domain.response;

/**
 * @author lordUhuru 05/12/2019
 */
public class AccountResponse {
    private String name;
    private String code;
    private String dateCreated;
    private String displayName;

    public AccountResponse(String name, String code, String dateCreated, String displayName) {
        this.name = name;
        this.code = code;
        this.dateCreated = dateCreated;
        this.displayName = displayName;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
