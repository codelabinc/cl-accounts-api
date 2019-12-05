package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.cl.accounts.entity.QPortalAccount;
import com.cl.accounts.entity.QPortalUser;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.conf.exception.ApiException;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.PortalAccountDao;
import com.codelab.accounts.dao.PortalUserDao;
import com.codelab.accounts.domain.request.LoginDto;
import com.codelab.accounts.domain.response.HttpError;
import com.codelab.accounts.service.auth.AuthenticationService;
import com.querydsl.core.types.Predicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author lordUhuru 04/12/2019
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final PortalUserDao portalUserDao;

    private final AuthenticationService authenticationService;

    private final PortalAccountDao portalAccountDao;

    public AuthenticationController(PortalUserDao portalUserDao, AuthenticationService authenticationService, PortalAccountDao portalAccountDao) {
        this.portalUserDao = portalUserDao;
        this.authenticationService = authenticationService;
        this.portalAccountDao = portalAccountDao;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDto dto, @RequestHeader("X-ACCOUNT-CODE") String accountCode){
        QPortalAccount qPortalAccount = QPortalAccount.portalAccount;
        Predicate portalAccountPredicate = qPortalAccount.code.equalsIgnoreCase(accountCode.trim())
                .and(qPortalAccount.status.eq(EntityStatusConstant.ACTIVE));
        PortalAccount portalAccount = portalAccountDao.findOne(portalAccountPredicate)
                .orElseThrow(() -> new NotFoundException(String.format("Account with code %s not found", accountCode)));
        boolean identifierIsEmail = dto.getIdentifier().contains("@");
        QPortalUser qPortalUser = QPortalUser.portalUser;
        Predicate emailPredicate = qPortalUser.email.equalsIgnoreCase(dto.getIdentifier().trim());
        Predicate usernamePredicate = qPortalUser.username.equalsIgnoreCase(dto.getIdentifier().trim());
        Predicate predicate = qPortalUser.status.eq(EntityStatusConstant.ACTIVE)
                .and(identifierIsEmail ? emailPredicate: usernamePredicate);
        PortalUser portalUser = portalUserDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("User with %s %s not found", identifierIsEmail? "email": "username", dto.getIdentifier())));
        String token = authenticationService.doLogin(portalUser, portalAccount);
        return ResponseEntity.ok(token);
    }

}
