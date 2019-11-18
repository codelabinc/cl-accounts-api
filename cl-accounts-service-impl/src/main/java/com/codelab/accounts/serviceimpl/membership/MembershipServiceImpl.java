package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.service.membership.MemberRoleService;
import com.codelab.accounts.service.membership.MembershipService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class MembershipServiceImpl implements MembershipService {

    @Inject
    private AppRepository appRepository;

    @Transactional
    @Override
    public Membership grantMembership(PortalAccount portalAccount, PortalUser portalUser) {
        Membership membership = new Membership();
        membership.setPortalAccount(portalAccount);
        membership.setDateCreated(Timestamp.from(Instant.now()));
        membership.setHasEverLoggedIn(false);
        membership.setStatus(EntityStatusConstant.ACTIVE);
        membership.setPortalUser(portalUser);
        appRepository.persist(membership);
        return membership;
    }
}
