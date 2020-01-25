package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.Role;

import java.util.Collection;

/**
 * @author lordUhuru 16/11/2019
 */
public interface MemberRoleService {
    Membership grantRole(Membership membership, App app, Collection<String> roles);

    Collection<Role> getRolesByMembership(Membership membership);
}
