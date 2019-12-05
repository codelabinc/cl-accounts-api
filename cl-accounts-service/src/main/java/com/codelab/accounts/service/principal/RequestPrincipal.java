package com.codelab.accounts.service.principal;

import com.cl.accounts.entity.*;

import java.util.Collections;
import java.util.List;

/**
 * @author lordUhuru 30/11/2019
 */
public interface RequestPrincipal {
    PortalUser getLoggedInUser();

    PortalAccount getPortalAccount();

    String getIpAddress();

    default List<String> getRoles() {
        return Collections.emptyList();
    }

    default List<String> getPermissions() {
        return Collections.emptyList();
    }
}
