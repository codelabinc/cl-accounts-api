package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.Permission;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.Role;

import java.util.Collection;

/**
 * @author lordUhuru 04/12/2019
 */
public interface PermissionService {
    Collection<Permission> getPermissionsByRoleAndPortalAccount(Role role, PortalAccount portalAccount);
}
