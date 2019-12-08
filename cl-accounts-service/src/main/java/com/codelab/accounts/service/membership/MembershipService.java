package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;

import java.util.Collection;
import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface MembershipService {
    Membership  grantMembership(PortalAccount portalAccount, PortalUser portalUser);
    Collection<Membership> getAllMembershipsByPortalUser(PortalUser portalUser);

    Optional<Membership> getMembershipByPortalUserAndPortalAccount(PortalUser portalUser, PortalAccount portalAccount);
}
