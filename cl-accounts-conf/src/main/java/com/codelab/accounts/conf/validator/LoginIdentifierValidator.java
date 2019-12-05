package com.codelab.accounts.conf.validator;

import com.cl.accounts.entity.QPortalUser;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.PortalUserDao;
import com.codelab.accounts.domain.constraint.LoginIdentifierConstraint;
import com.querydsl.core.types.Predicate;

import javax.inject.Inject;
import javax.validation.ConstraintValidatorContext;

/**
 * @author lordUhuru 04/12/2019
 */
public class LoginIdentifierValidator implements LoginIdentifierConstraint.Validator {

    @Inject
    private PortalUserDao portalUserDao;

    @Override
    public boolean isValid(String identifier, ConstraintValidatorContext context) {
        boolean identifierIsEmail = identifier.contains("@");
        QPortalUser qPortalUser = QPortalUser.portalUser;
        Predicate emailPredicate = qPortalUser.email.equalsIgnoreCase(identifier.trim());
        Predicate usernamePredicate = qPortalUser.username.equalsIgnoreCase(identifier.trim());
        Predicate predicate = qPortalUser.status.eq(EntityStatusConstant.ACTIVE)
                .and(identifierIsEmail ? emailPredicate: usernamePredicate);
        return portalUserDao.findOne(predicate).isPresent();
    }

}
