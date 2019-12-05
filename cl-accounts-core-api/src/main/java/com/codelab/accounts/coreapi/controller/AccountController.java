package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.PortalAccount;
import com.codelab.accounts.dao.PortalAccountDao;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lordUhuru 16/11/2019
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final PortalAccountDao portalAccountDao;

    public AccountController(PortalAccountDao portalAccountDao) {
        this.portalAccountDao = portalAccountDao;
    }

    @GetMapping
    public ResponseEntity<?> getAccounts(@QuerydslPredicate(root = PortalAccount.class) Predicate predicate,
                                         Pageable pageable) {
        Page<PortalAccount> page = portalAccountDao.findAll(predicate, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
