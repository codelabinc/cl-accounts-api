package com.codelab.accounts.domain.request;

import javax.validation.constraints.NotBlank;

/**
 * @author lordUhuru 05/12/2019
 */
public class AccountUpdateDto {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
