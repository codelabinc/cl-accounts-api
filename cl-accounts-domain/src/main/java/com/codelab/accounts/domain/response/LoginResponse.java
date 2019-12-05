package com.codelab.accounts.domain.response;

/**
 * @author lordUhuru 05/12/2019
 */
public class LoginResponse {
    private String token;


    public LoginResponse(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
