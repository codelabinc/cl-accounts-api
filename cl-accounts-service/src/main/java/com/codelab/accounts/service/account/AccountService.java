package com.codelab.accounts.service.account;

import com.cl.accounts.entity.PortalAccount;
import com.codelab.accounts.domain.requests.AccountCreationDto;

/**
 * @author lordUhuru 16/11/2019
 */
public interface AccountService {
    PortalAccount createPortalAccount(AccountCreationDto dto);
}
