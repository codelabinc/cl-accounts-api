package com.codelab.accounts.serviceimpl.principal;


import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.codelab.accounts.service.principal.RequestPrincipal;

import java.util.List;

public class RequestPrincipalImpl implements RequestPrincipal {
    private PortalUser loggedInUser;
    private String ipAddress;
    private PortalAccount portalAccount;
    private List<String> roles;
    private List<String> permissions;

    public RequestPrincipalImpl(PortalUser loggedInUser, PortalAccount portalAccount,
                                List<String> roles,
                                List<String> permissions,
                                String ipAddress) {
        this.loggedInUser = loggedInUser;
        this.ipAddress = ipAddress;
        this.portalAccount = portalAccount;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public PortalUser getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public PortalAccount getPortalAccount() {
        return portalAccount;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public List<String> getPermissions() {
        return permissions;
    }
}
