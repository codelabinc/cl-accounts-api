package com.codelab.accounts.domain.enumeration;

import com.cl.accounts.enumeration.PermissionConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;

/**
 * @author lordUhuru 16/11/2019
 */
public interface RolePermissionHolder {
    RoleTypeConstant roleName();
    PermissionConstant[] permissions();
}
