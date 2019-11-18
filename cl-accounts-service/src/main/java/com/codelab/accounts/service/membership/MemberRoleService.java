package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.enumeration.RoleTypeConstant;

import java.util.Collection;

/**
 * @author lordUhuru 16/11/2019
 */
public interface MemberRoleService {
    Membership grantRole(Membership membership, Collection<RoleTypeConstant> roleTypeConstants);
}
