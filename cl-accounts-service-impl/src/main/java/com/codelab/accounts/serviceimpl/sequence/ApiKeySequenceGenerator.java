package com.codelab.accounts.serviceimpl.sequence;

import com.codelab.accounts.domain.qualifier.ApiKeySequence;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.Locale;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
@ApiKeySequence
public class ApiKeySequenceGenerator extends SequenceGeneratorImpl {

    public ApiKeySequenceGenerator(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        super(entityManager, transactionTemplate, "api_key");
    }

    @Override
    public String getNext() {
        return String.format(Locale.ENGLISH, "%010d", getNextLong());
    }
}
