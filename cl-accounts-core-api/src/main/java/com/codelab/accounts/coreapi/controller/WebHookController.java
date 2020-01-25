package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.QApp;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.dao.EntityDao;
import com.codelab.accounts.domain.request.WebHookDto;
import com.codelab.accounts.service.webhook.WebHookService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
@RequestMapping("/apps/{code}")
public class WebHookController {
    @Inject
    private AppDao appDao;

    @Inject
    private EntityDao entityDao;

    @Inject
    private WebHookService webHookService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/webhook")
    public ResponseEntity<App> addWebHook(@PathVariable("code") String code, @RequestBody @Valid WebHookDto dto) {
        QApp qApp = QApp.app;
        JPAQuery<App> appJPAQuery = entityDao.startJPAQueryFrom(QApp.app);
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        appJPAQuery.leftJoin(qApp.webHook).fetchJoin();

        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));
        if(app.getWebHook() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
         webHookService.createWebHook(dto, app);

        return ResponseEntity.ok(app);
    }

    @PutMapping("/webhook")
    public ResponseEntity<App> updateWebHook(@PathVariable("code") String code, @RequestBody @Valid WebHookDto dto) {
        QApp qApp = QApp.app;
        JPAQuery<App> appJPAQuery = entityDao.startJPAQueryFrom(QApp.app);
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        appJPAQuery.leftJoin(qApp.webHook).fetchJoin();

        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));
        webHookService.updateWebHook(dto, app);

        return ResponseEntity.ok(app);
    }
}
