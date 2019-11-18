package com.codelab.accounts.dao;

import com.cl.accounts.entity.AssignableRoleTypesToAccount;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface AssignableRoleTypesToAccountDao extends JpaRepository<AssignableRoleTypesToAccount, Long> {
    Optional<AssignableRoleTypesToAccount> findByRole_NameAndPortalAccountTypeAndStatus(RoleTypeConstant roleTypeConstant, PortalAccountTypeConstant type, EntityStatusConstant status);
}
