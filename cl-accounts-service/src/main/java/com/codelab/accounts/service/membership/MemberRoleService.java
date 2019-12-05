package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.Role;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;

import java.util.Collection;

/**
 * @author lordUhuru 16/11/2019
 */
public interface MemberRoleService {
    Membership grantRole(Membership membership, Collection<RoleTypeConstant> roleTypeConstants);

    Collection<Role> getRolesByMembership(Membership membership);
}
