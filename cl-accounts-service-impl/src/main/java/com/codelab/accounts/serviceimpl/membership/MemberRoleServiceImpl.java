package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.MemberRole;
import com.cl.accounts.entity.Membership;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.dao.RoleDao;
import com.codelab.accounts.service.membership.MemberRoleService;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class MemberRoleServiceImpl implements MemberRoleService {
    @Inject
    private AppRepository appRepository;

    @Inject
    private RoleDao roleDao;

    @Override
    public Membership grantRole(Membership membership, Collection<RoleTypeConstant> roleTypeConstants) {
        roleTypeConstants.forEach(roleTypeConstant -> {
            MemberRole memberRole = new MemberRole();
            memberRole.setMembership(membership);
            memberRole.setStatus(EntityStatusConstant.ACTIVE);
            memberRole.setDateCreated(Timestamp.from(Instant.now()));
            memberRole.setRole(roleDao.findByNameAndStatus(roleTypeConstant, EntityStatusConstant.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Role with name %s not found", roleTypeConstant.getValue()))));
            appRepository.persist(memberRole);
        });
        return membership;
    }
}
