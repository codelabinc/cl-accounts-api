package com.codelab.accounts.serviceimpl.principal;


import com.cl.accounts.entity.PortalUser;
import com.codelab.accounts.service.principal.RequestPrincipal;

public class RequestPrincipalImpl implements RequestPrincipal {
    private PortalUser loggedInUser;
    private String ipAddress;

    public RequestPrincipalImpl(PortalUser loggedInUser, String ipAddress) {
        this.loggedInUser = loggedInUser;
        this.ipAddress = ipAddress;
    }

    @Override
    public PortalUser getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

}
