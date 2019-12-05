package com.codelab.accounts.service.principal;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.Permission;
import com.cl.accounts.entity.PortalUser;
import com.cl.accounts.entity.Role;

import java.util.Collections;
import java.util.List;

/**
 * @author lordUhuru 30/11/2019
 */
public interface RequestPrincipal {
    PortalUser getLoggedInUser();

    String getIpAddress();

    default List<Role> getRoles() {
        return Collections.emptyList();
    }

    default List<Permission> getPermissions() {
        return Collections.emptyList();
    }

    default List<Membership> getMemberships() {
        return Collections.emptyList();
    }
}
