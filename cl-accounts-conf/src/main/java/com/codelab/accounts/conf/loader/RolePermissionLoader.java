package com.codelab.accounts.conf.loader;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.*;
import com.codelab.accounts.domain.enumeration.SystemPermissionTypeConstant;
import com.codelab.accounts.domain.enumeration.SystemRoleTypeConstant;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class RolePermissionLoader {

    @Inject
    private RoleDao roleDao;

    @Inject
    private PermissionDao permissionDao;

    public void loadRoles(App app) {
        for (SystemRoleTypeConstant roleTypeConstant : SystemRoleTypeConstant.values()) {
            roleDao.findByNameAndAppAndStatus(roleTypeConstant.getValue(), app, EntityStatusConstant.ACTIVE).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleTypeConstant.getValue());
                role.setStatus(EntityStatusConstant.ACTIVE);
                role.setApp(app);
                roleDao.save(role);
                return role;
            });
        }
    }

    public void loadPermissions(Role role, App app) {
        for (SystemPermissionTypeConstant permissionConstant : SystemPermissionTypeConstant.values()) {
            permissionDao.findByNameAndStatus(permissionConstant.getValue(), EntityStatusConstant.ACTIVE).orElseGet(() -> {
                Permission permission = new Permission();
                permission.setName(permissionConstant.getValue());
                permission.setStatus(EntityStatusConstant.ACTIVE);
                permission.setApp(app);
                permission.setRole(role);
                permissionDao.save(permission);
                return permission;
            });
        }
    }

    public void loadPermissionsForRole(Role role, App app, SystemPermissionTypeConstant... permissions) {
        for (SystemPermissionTypeConstant permissionConstant: permissions) {
            Permission permission = permissionDao.findByNameAndStatus(permissionConstant.getValue(), EntityStatusConstant.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Permission %s not found", permissionConstant)));
            permissionDao.findByNameAndRoleAndAppAndStatus(permissionConstant.getValue(), role, app, EntityStatusConstant.ACTIVE)
                    .orElseGet(() -> {
                permission.setRole(role);
                permission.setApp(app);
                permission.setName(permissionConstant.getValue());
                permission.setStatus(EntityStatusConstant.ACTIVE);
                permissionDao.save(permission);
                return permission;
            });
        }
    }
}
