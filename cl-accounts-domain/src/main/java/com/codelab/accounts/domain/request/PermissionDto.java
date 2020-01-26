package com.codelab.accounts.domain.request;

import javax.validation.constraints.NotBlank;

public class PermissionDto {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
