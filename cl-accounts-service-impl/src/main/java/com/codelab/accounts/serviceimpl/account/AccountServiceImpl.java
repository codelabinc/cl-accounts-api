package com.codelab.accounts.serviceimpl.account;

import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.domain.qualifier.AccountCodeSequence;
import com.codelab.accounts.domain.request.AccountCreationDto;
import com.codelab.accounts.domain.request.AccountUpdateDto;
import com.codelab.accounts.domain.response.AccountResponse;
import com.codelab.accounts.service.account.AccountService;
import com.codelab.accounts.service.sequence.SequenceGenerator;
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

    private final AppRepository appRepository;

    public AccountServiceImpl(@AccountCodeSequence SequenceGenerator accountCodeGenerator,
                              AppRepository appRepository) {
        this.accountCodeGenerator = accountCodeGenerator;
        this.appRepository = appRepository;
    }

    @Override
    public PortalAccount createPortalAccount(AccountCreationDto dto) {
        PortalAccount portalAccount = new PortalAccount();
        portalAccount.setName(dto.getName().trim());
        portalAccount.setCode(accountCodeGenerator.getNext());
        portalAccount.setDateCreated(Timestamp.from(Instant.now()));
        portalAccount.setStatus(EntityStatusConstant.ACTIVE);
        portalAccount.setType(PortalAccountTypeConstant.valueOf(dto.getAccountType()));
        return appRepository.persist(portalAccount);
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
        appRepository.merge(portalAccount);
        return portalAccount;
    }

    @Override
    @Transactional
    public PortalAccount deactivatePortalAccount(PortalAccount portalAccount) {
        portalAccount.setStatus(EntityStatusConstant.DEACTIVATED);
        appRepository.merge(portalAccount);
        return portalAccount;
    }
}
