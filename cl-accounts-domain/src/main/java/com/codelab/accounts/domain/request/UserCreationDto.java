package com.codelab.accounts.domain.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author lordUhuru 16/11/2019
 */
public class UserCreationDto {
    @NotBlank
    @Min(3)
    private String firstName;
    @NotBlank
    @Min(3)
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @Size(min = 4)
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 6)
    private String password;
    @NotBlank
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
