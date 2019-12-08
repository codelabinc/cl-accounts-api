package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.QPortalAccount;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.codelab.accounts.conf.exception.ApiException;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.PortalAccountDao;
import com.codelab.accounts.domain.request.AccountCreationDto;
import com.codelab.accounts.domain.request.AccountUpdateDto;
import com.codelab.accounts.domain.response.AccountResponse;
import com.codelab.accounts.service.account.AccountService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author lordUhuru 16/11/2019
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final PortalAccountDao portalAccountDao;

    @Inject
    private AccountService accountService;

    public AccountController(PortalAccountDao portalAccountDao) {
        this.portalAccountDao = portalAccountDao;
    }

    @GetMapping
    public ResponseEntity<?> getAccounts(@QuerydslPredicate(root = PortalAccount.class) Predicate predicate,
                                         Pageable pageable) {
        if (predicate == null) {
            predicate = QPortalAccount.portalAccount.id.gt(0);
        }
        Page<PortalAccount> page = portalAccountDao.findAll(predicate, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAccount(@RequestBody @Valid AccountCreationDto dto) {
        QPortalAccount qPortalAccount = QPortalAccount.portalAccount;

       PortalAccount portalAccount = portalAccountDao.findOne(qPortalAccount.name.equalsIgnoreCase(dto.getName())
        .and(qPortalAccount.type.eq(PortalAccountTypeConstant.valueOf(dto.getAccountType()))
                .and(qPortalAccount.status.eq(EntityStatusConstant.ACTIVE)))).orElse(null);
       if(portalAccount != null) {
           throw new NotFoundException(String.format("Account with name %s Already Exists", dto.getName()));
       }
        accountService.createPortalAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{code}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("code") String code) {
        PortalAccount portalAccount = portalAccountDao.findOne(QPortalAccount.portalAccount.code.equalsIgnoreCase(code))
                .orElseThrow(() -> new NotFoundException(String.format("Account with code %s not found", code)));
        return ResponseEntity.ok(accountService.getAccountDetails(portalAccount));
    }

    @PutMapping("/{code}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable("code")String code, @RequestBody @Valid AccountUpdateDto dto) {
        PortalAccount portalAccount = portalAccountDao.findOne(QPortalAccount.portalAccount.code.equalsIgnoreCase(code))
                .orElseThrow(() -> new NotFoundException(String.format("Account with code %s not found", code)));
        portalAccount = accountService.updatePortalAccount(portalAccount, dto);
        return ResponseEntity.ok(accountService.getAccountDetails(portalAccount));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<HttpStatus> deactivateAccount(@PathVariable("code")String code) {
        PortalAccount portalAccount = portalAccountDao.findOne(QPortalAccount.portalAccount.code.equalsIgnoreCase(code))
                .orElseThrow(() -> new NotFoundException(String.format("Account with code %s not found", code)));
        if(portalAccount.getStatus().equals(EntityStatusConstant.DEACTIVATED)) {
            throw new ApiException("Account already Deactivated", HttpStatus.BAD_REQUEST);
        }
        accountService.deactivatePortalAccount(portalAccount);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
