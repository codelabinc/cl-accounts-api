package com.codelab.accounts.domain.response;

/**
 * @author lordUhuru 04/12/2019
 */
public class UserResponse {
    private long id;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private boolean hasEverLoggedIn;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isHasEverLoggedIn() {
        return hasEverLoggedIn;
    }

    public void setHasEverLoggedIn(boolean hasEverLoggedIn) {
        this.hasEverLoggedIn = hasEverLoggedIn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", hasEverLoggedIn=" + hasEverLoggedIn +
                '}';
    }
}
