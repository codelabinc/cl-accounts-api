package com.codelab.accounts.domain.request;



import com.codelab.accounts.domain.constraint.LoginIdentifierConstraint;

import javax.validation.constraints.NotBlank;

/**
 * @author lordUhuru 04/12/2019
 */
public class LoginDto {
    @NotBlank
    private String identifier;
    @NotBlank
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
