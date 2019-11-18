package com.codelab.accounts.serviceimpl.account;

import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.domain.qualifier.AccountCodeSequence;
import com.codelab.accounts.domain.requests.AccountCreationDto;
import com.codelab.accounts.service.account.AccountService;
import com.codelab.accounts.service.sequence.SequenceGenerator;

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
}
