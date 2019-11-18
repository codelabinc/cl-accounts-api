package com.codelab.accounts.conf.loader;

import com.cl.accounts.entity.AssignableRoleTypesToAccount;
import com.cl.accounts.entity.Permission;
import com.cl.accounts.entity.Role;
import com.cl.accounts.entity.RolePermission;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PermissionConstant;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;
import com.codelab.accounts.dao.AssignableRoleTypesToAccountDao;
import com.codelab.accounts.dao.PermissionDao;
import com.codelab.accounts.dao.RoleDao;
import com.codelab.accounts.dao.RolePermissionDao;
import com.codelab.accounts.domain.enumeration.CodelabRolePermission;

import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class RolePermissionLoader {

    private final RoleDao roleDao;
    private final PermissionDao permissionDao;

    private final RolePermissionDao rolePermissionDao;

    private final AssignableRoleTypesToAccountDao assignableRoleTypesToAccountDao;

    public RolePermissionLoader(RoleDao roleDao, PermissionDao permissionDao, RolePermissionDao rolePermissionDao, AssignableRoleTypesToAccountDao assignableRoleTypesToAccountDao) {
        this.roleDao = roleDao;
        this.permissionDao = permissionDao;
        this.rolePermissionDao = rolePermissionDao;
        this.assignableRoleTypesToAccountDao = assignableRoleTypesToAccountDao;
    }

    public void loadRoles() {
        for (RoleTypeConstant roleTypeConstant : RoleTypeConstant.values()) {
            roleDao.findByNameAndStatus(roleTypeConstant, EntityStatusConstant.ACTIVE).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleTypeConstant);
                role.setStatus(EntityStatusConstant.ACTIVE);
                roleDao.save(role);
                return role;
            });
        }
    }

    public void loadPermissions() {
        for (PermissionConstant permissionConstant : PermissionConstant.values()) {
            permissionDao.findByNameAndStatus(permissionConstant, EntityStatusConstant.ACTIVE).orElseGet(() -> {
                Permission permission = new Permission();
                permission.setName(permissionConstant);
                permission.setStatus(EntityStatusConstant.ACTIVE);
                permissionDao.save(permission);
                return permission;
            });
        }
    }

    public void loadCodelabRolePermissions() {
        for (CodelabRolePermission codelabRolePermission : CodelabRolePermission.values()) {
            assignableRoleTypesToAccountDao
                    .findByRole_NameAndPortalAccountTypeAndStatus(codelabRolePermission.roleName(),
                            PortalAccountTypeConstant.CODELAB, EntityStatusConstant.ACTIVE).orElseGet(() -> {
                                Role role = roleDao.findByNameAndStatus(codelabRolePermission.roleName(), EntityStatusConstant.ACTIVE)
                                        .orElseThrow(() -> new IllegalArgumentException(String.format("Role %s not found", codelabRolePermission.roleName())));
                AssignableRoleTypesToAccount assignableRoleTypesToAccount = new AssignableRoleTypesToAccount();
                assignableRoleTypesToAccount.setRole(role);
                assignableRoleTypesToAccount.setDateCreated(Timestamp.from(Instant.now()));
                assignableRoleTypesToAccount.setPortalAccountType(PortalAccountTypeConstant.CODELAB);
                assignableRoleTypesToAccount.setStatus(EntityStatusConstant.ACTIVE);
                assignableRoleTypesToAccountDao.save(assignableRoleTypesToAccount);

                loadPermissionsForRole(role, codelabRolePermission.permissions());

                return assignableRoleTypesToAccount;
            });
        }
    }

    private void loadPermissionsForRole(Role role, PermissionConstant... permissions) {
        for (PermissionConstant permissionConstant: permissions) {
            Permission permission = permissionDao.findByNameAndStatus(permissionConstant, EntityStatusConstant.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Permission %s not found", permissionConstant)));
            rolePermissionDao.findByRoleAndPermissionAndStatus(role, permission, EntityStatusConstant.ACTIVE).orElseGet(() -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(role);
                rolePermission.setPermission(permission);
                rolePermission.setStatus(EntityStatusConstant.ACTIVE);
                rolePermissionDao.save(rolePermission);
                return rolePermission;
            });
        }
    }
}
