package com.codelab.accounts.conf.loader;

import com.cl.accounts.entity.Permission;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.Role;
import com.cl.accounts.entity.RolePermission;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.*;
import com.codelab.accounts.domain.enumeration.SystemPermissionTypeConstant;
import com.codelab.accounts.domain.enumeration.SystemRoleTypeConstant;

import javax.inject.Named;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class RolePermissionLoader {

    private final RoleDao roleDao;
    private final PermissionDao permissionDao;

    private final RolePermissionDao rolePermissionDao;

    public RolePermissionLoader(RoleDao roleDao, PermissionDao permissionDao,
                                RolePermissionDao rolePermissionDao) {
        this.roleDao = roleDao;
        this.permissionDao = permissionDao;
        this.rolePermissionDao = rolePermissionDao;
    }

    public void loadRoles() {
        for (SystemRoleTypeConstant roleTypeConstant : SystemRoleTypeConstant.values()) {
            roleDao.findByNameAndStatus(roleTypeConstant.getValue(), EntityStatusConstant.ACTIVE).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleTypeConstant.getValue());
                role.setStatus(EntityStatusConstant.ACTIVE);
                roleDao.save(role);
                return role;
            });
        }
    }

    public void loadPermissions() {
        for (SystemPermissionTypeConstant permissionConstant : SystemPermissionTypeConstant.values()) {
            permissionDao.findByNameAndStatus(permissionConstant.getValue(), EntityStatusConstant.ACTIVE).orElseGet(() -> {
                Permission permission = new Permission();
                permission.setName(permissionConstant.getValue());
                permission.setStatus(EntityStatusConstant.ACTIVE);
                permissionDao.save(permission);
                return permission;
            });
        }
    }

    public void loadPermissionsForRole(Role role, PortalAccount portalAccount, SystemPermissionTypeConstant... permissions) {
        for (SystemPermissionTypeConstant permissionConstant: permissions) {
            Permission permission = permissionDao.findByNameAndStatus(permissionConstant.getValue(), EntityStatusConstant.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Permission %s not found", permissionConstant)));
            rolePermissionDao.findByRoleAndPermissionAndStatus(role, permission, EntityStatusConstant.ACTIVE).orElseGet(() -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(role);
                rolePermission.setPermission(permission);
                rolePermission.setStatus(EntityStatusConstant.ACTIVE);
                rolePermission.setPortalAccount(portalAccount);
                rolePermissionDao.save(rolePermission);
                return rolePermission;
            });
        }
    }
}
