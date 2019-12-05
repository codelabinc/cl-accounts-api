package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.conf.exception.ApiException;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.PortalAccountDao;
import com.codelab.accounts.dao.PortalUserDao;
import com.codelab.accounts.domain.enumeration.TokenClaimsConstant;
import com.codelab.accounts.domain.request.LoginDto;
import com.codelab.accounts.domain.response.LoginResponse;
import com.codelab.accounts.jwt.TokenService;
import com.codelab.accounts.service.auth.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

/**
 * @author lordUhuru 04/12/2019
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final PortalUserDao portalUserDao;

    private final AuthenticationService authenticationService;

    private final PortalAccountDao portalAccountDao;

    private final BCryptPasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    public AuthenticationController(PortalUserDao portalUserDao, AuthenticationService authenticationService, PortalAccountDao portalAccountDao, BCryptPasswordEncoder passwordEncoder, TokenService tokenService) {
        this.portalUserDao = portalUserDao;
        this.authenticationService = authenticationService;
        this.portalAccountDao = portalAccountDao;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginDto dto, @RequestHeader("X-ACCOUNT-CODE") String accountCode) {
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
                .and(identifierIsEmail ? emailPredicate : usernamePredicate);
        PortalUser portalUser = portalUserDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("User with %s %s not found", identifierIsEmail ? "email" : "username", dto.getIdentifier())));
        if (!passwordEncoder.matches(dto.getPassword().trim(), portalUser.getPassword())) {
            throw new ApiException("Invalid Login Credentials", HttpStatus.BAD_REQUEST);
        }
        LoginResponse loginResponse = authenticationService.generateLoginResponse(portalUser, portalAccount);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            JWTClaimsSet claimsSet = tokenService.decodeToken(token);

            if (StringUtils.isNotBlank(token)) {
                QPortalAccount qPortalAccount = QPortalAccount.portalAccount;
                Predicate portalAccountPredicate = qPortalAccount
                        .code.equalsIgnoreCase(claimsSet.getClaim(TokenClaimsConstant.ACCOUNT_CODE.getValue()).toString())
                        .and(qPortalAccount.status.eq(EntityStatusConstant.ACTIVE));
                PortalAccount portalAccount = portalAccountDao.findOne(portalAccountPredicate)
                        .orElseThrow(() -> new NotFoundException(String.format("Account with code %s not found",
                                TokenClaimsConstant.ACCOUNT_CODE.getValue())));

                PortalUser portalUser = portalUserDao.findById(Long.valueOf(claimsSet.getSubject())).orElseThrow(() -> new NotFoundException(String.format("User with id %s not found",
                        claimsSet.getSubject())));
                LoginResponse loginResponse = authenticationService.generateLoginResponse(portalUser, portalAccount);
                return ResponseEntity.ok(loginResponse);

            }
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            throw new ApiException("Invalid Token", HttpStatus.BAD_REQUEST);
        }
        throw new ApiException("Missing Authorization Header", HttpStatus.BAD_REQUEST);
    }

}
