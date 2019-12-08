package com.codelab.accounts.domain.response;

import com.cl.accounts.enumeration.EntityStatusConstant;

/**
 * @author lordUhuru 08/12/2019
 */
public class PortalUserResponse {
    private String username;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private boolean hasEverLoggedIn;
    private EntityStatusConstant status;
    private String dateCreated;
    private long id;

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


    public EntityStatusConstant getStatus() {
        return status;
    }

    public void setStatus(EntityStatusConstant status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isHasEverLoggedIn() {
        return hasEverLoggedIn;
    }

    public void setHasEverLoggedIn(boolean hasEverLoggedIn) {
        this.hasEverLoggedIn = hasEverLoggedIn;
    }
}
