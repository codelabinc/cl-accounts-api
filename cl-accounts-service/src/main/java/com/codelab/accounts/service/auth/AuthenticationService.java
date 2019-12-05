package com.codelab.accounts.service.auth;


import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;

/**
 * @author lordUhuru 16/11/2019
 */
public interface AuthenticationService {
    String doLogin(PortalUser portalUser, PortalAccount portalAccount);
}
