package com.codelab.accounts.service.account;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.PortalAccount;
import com.codelab.accounts.domain.request.AccountCreationDto;
import com.codelab.accounts.domain.request.AccountUpdateDto;
import com.codelab.accounts.domain.response.AccountResponse;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface AccountService {
    PortalAccount createPortalAccount(AccountCreationDto dto, App app);
    AccountResponse getAccountDetails(PortalAccount portalAccount);
    PortalAccount updatePortalAccount(PortalAccount portalAccount, AccountUpdateDto dto);

    @Transactional
    PortalAccount deactivatePortalAccount(PortalAccount portalAccount);
}
