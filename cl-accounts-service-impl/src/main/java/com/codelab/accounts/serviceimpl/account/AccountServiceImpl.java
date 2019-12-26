package com.codelab.accounts.serviceimpl.account;

import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.codelab.accounts.dao.EntityRepository;
import com.codelab.accounts.domain.qualifier.AccountCodeSequence;
import com.codelab.accounts.domain.request.AccountCreationDto;
import com.codelab.accounts.domain.request.AccountUpdateDto;
import com.codelab.accounts.domain.response.AccountResponse;
import com.codelab.accounts.service.account.AccountService;
import com.codelab.accounts.service.app.AppService;
import com.codelab.accounts.service.sequence.SequenceGenerator;
import com.codelab.accounts.service.user.UserService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class AccountServiceImpl implements AccountService {

    private final SequenceGenerator accountCodeGenerator;

    private final EntityRepository entityRepository;

    private final UserService userService;

    private final AppService appService;

    public AccountServiceImpl(@AccountCodeSequence SequenceGenerator accountCodeGenerator,
                              EntityRepository entityRepository, UserService userService, AppService appService) {
        this.accountCodeGenerator = accountCodeGenerator;
        this.entityRepository = entityRepository;
        this.userService = userService;
        this.appService = appService;
    }

    @Override
    @Transactional
    public PortalAccount createPortalAccount(AccountCreationDto dto) {
        PortalAccount portalAccount = new PortalAccount();
        portalAccount.setName(dto.getName().trim());
        portalAccount.setCode(accountCodeGenerator.getNext());
        portalAccount.setDateCreated(Timestamp.from(Instant.now()));
        portalAccount.setApp(appService.createApp(dto.getName(), dto.getDescription()));
        portalAccount.setStatus(EntityStatusConstant.ACTIVE);
        portalAccount.setType(PortalAccountTypeConstant.valueOf(dto.getAccountType()));
        portalAccount = entityRepository.persist(portalAccount);

        userService.createPortalUser(portalAccount, dto.getAdminUser());
        userService.createSystemPortalUser(portalAccount);
        return portalAccount;

    }

    @Override
    public AccountResponse getAccountDetails(PortalAccount portalAccount) {
        return new AccountResponse(portalAccount.getName(),
                portalAccount.getCode(),
                portalAccount.getDateCreated().toString(),
                portalAccount.getDisplayName());
    }

    @Override
    @Transactional
    public PortalAccount updatePortalAccount(PortalAccount portalAccount, AccountUpdateDto dto) {
        portalAccount.setName(dto.getName());
        entityRepository.merge(portalAccount);
        return portalAccount;
    }

    @Override
    @Transactional
    public PortalAccount deactivatePortalAccount(PortalAccount portalAccount) {
        portalAccount.setStatus(EntityStatusConstant.DEACTIVATED);
        entityRepository.merge(portalAccount);
        return portalAccount;
    }
}
