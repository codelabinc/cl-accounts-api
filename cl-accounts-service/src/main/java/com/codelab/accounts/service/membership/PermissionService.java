package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.Permission;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.Role;
import com.codelab.accounts.domain.request.PermissionDto;

import java.util.Collection;

/**
 * @author lordUhuru 04/12/2019
 */
public interface PermissionService {
    Collection<Permission> getPermissionsByRoleAndApp(Role role, App app);

    Permission createPermission(App app, Role role, PermissionDto dto);

    Permission deletePermission(Permission permission);
}
