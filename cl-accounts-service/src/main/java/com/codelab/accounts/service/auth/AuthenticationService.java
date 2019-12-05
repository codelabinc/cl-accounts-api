package com.codelab.accounts.service.auth;


import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.codelab.accounts.domain.response.LoginResponse;

/**
 * @author lordUhuru 16/11/2019
 */
public interface AuthenticationService {
    LoginResponse generateLoginResponse(PortalUser portalUser, PortalAccount portalAccount);
}
