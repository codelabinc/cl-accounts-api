package com.codelab.accounts.serviceimpl.sequence;

import com.codelab.accounts.domain.qualifier.AccountCodeSequence;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
@AccountCodeSequence
public class AccountCodeSequenceGenerator extends SequenceGeneratorImpl {

    public AccountCodeSequenceGenerator(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        super(entityManager, transactionTemplate, "account_code");
    }

    @Override
    public String getNext() {
        return String.format("CL%05d", getNextLong());
    }
}
