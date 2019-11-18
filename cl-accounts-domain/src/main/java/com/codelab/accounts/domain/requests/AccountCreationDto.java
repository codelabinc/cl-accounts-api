package com.codelab.accounts.domain.requests;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lordUhuru 16/11/2019
 */
public class AccountCreationDto {
    @NotBlank
    private String name;
    @Valid
    @NotNull
    private AddressDto address;
    @Valid
    @NotNull
    private UserCreationDto adminUser;
    @NotBlank
    private String accountType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public UserCreationDto getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(UserCreationDto adminUser) {
        this.adminUser = adminUser;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
