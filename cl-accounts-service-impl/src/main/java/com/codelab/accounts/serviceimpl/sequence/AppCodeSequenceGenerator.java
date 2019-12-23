package com.codelab.accounts.serviceimpl.sequence;

import com.codelab.accounts.domain.qualifier.AppCodeSequence;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Named;
import javax.persistence.EntityManager;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
@AppCodeSequence
public class AppCodeSequenceGenerator extends SequenceGeneratorImpl {

    public AppCodeSequenceGenerator(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        super(entityManager, transactionTemplate, "app_code");
    }

    @Override
    public String getNext() {
        return String.format("APP%05d", getNextLong());
    }
}
