package com.codelab.accounts.domain.response;

import java.util.List;

/**
 * @author lordUhuru 04/12/2019
 */
public class TokenResponse {
    private NameCodeResponse account;
    private UserResponse user;
    private List<String> roles;
    private List<String> permissions;

    public NameCodeResponse getAccount() {
        return account;
    }

    public void setAccount(NameCodeResponse account) {
        this.account = account;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "account=" + account +
                ", user=" + user +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}
