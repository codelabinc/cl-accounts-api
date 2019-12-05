package com.codelab.accounts.service.user;

import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.codelab.accounts.domain.request.UserCreationDto;

/**
 * @author lordUhuru 16/11/2019
 */
public interface UserService {
    PortalUser createPortalUser(PortalAccount portalAccount, UserCreationDto dto);
}
