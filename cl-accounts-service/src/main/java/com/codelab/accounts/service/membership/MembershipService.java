package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;

/**
 * @author lordUhuru 16/11/2019
 */
public interface MembershipService {
    Membership grantMembership(PortalAccount portalAccount, PortalUser portalUser);
}
