package com.codelab.accounts.domain.enumeration;

import com.cl.accounts.enumeration.PermissionConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;

/**
 * @author lordUhuru 16/11/2019
 */
public enum CodelabRolePermission implements RolePermissionHolder {
    ADMIN(RoleTypeConstant.ADMIN, PermissionConstant.CREATE_CODELAB_USER);

    CodelabRolePermission(RoleTypeConstant displayName, PermissionConstant... permissions) {
        this.displayName = displayName;
        this.permissions = permissions;
    }

    private RoleTypeConstant displayName;
    private PermissionConstant[] permissions;

    @Override
    public RoleTypeConstant roleName() {
        return displayName;
    }

    @Override
    public PermissionConstant[] permissions() {
        return permissions;
    }
}
