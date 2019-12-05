package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.cl.accounts.entity.QMembership;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.dao.MembershipDao;
import com.codelab.accounts.service.membership.MembershipService;
import com.querydsl.core.types.Predicate;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class MembershipServiceImpl implements MembershipService {

    private final AppRepository appRepository;

    private final MembershipDao membershipDao;

    public MembershipServiceImpl(AppRepository appRepository, MembershipDao membershipDao) {
        this.appRepository = appRepository;
        this.membershipDao = membershipDao;
    }

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

    @Override
    public Collection<Membership> getAllMembershipsByPortalUser(PortalUser portalUser) {
        QMembership qMembership = QMembership.membership;
        Predicate predicate = qMembership.portalUser.eq(portalUser)
                .and(qMembership.status.eq(EntityStatusConstant.ACTIVE));
        return (Collection<Membership>) membershipDao.findAll(predicate);
    }

    @Override
    public Optional<Membership> getMembershipByPortalUserAndPortalAccount(PortalUser portalUser, PortalAccount portalAccount) {
        QMembership qMembership = QMembership.membership;
        Predicate predicate = qMembership.portalUser.eq(portalUser)
                .and(qMembership.portalAccount.eq(portalAccount))
                .and(qMembership.status.eq(EntityStatusConstant.ACTIVE));
        return membershipDao.findOne(predicate);
    }
}
