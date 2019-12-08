package com.codelab.accounts.service.user;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.codelab.accounts.domain.request.UserCreationDto;
import com.codelab.accounts.domain.response.PortalUserResponse;

/**
 * @author lordUhuru 16/11/2019
 */
public interface UserService {
    PortalUser createPortalUser(PortalAccount portalAccount, UserCreationDto dto);
    PortalUser createSystemPortalUser(PortalAccount portalAccount);

    PortalUserResponse toUserResponse(PortalUser portalUser, Membership membership);
}
