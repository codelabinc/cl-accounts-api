package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.AppPermission;
import com.cl.accounts.entity.QApp;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.dao.AppPermissionDao;
import com.codelab.accounts.domain.request.PermissionDto;
import com.codelab.accounts.service.membership.AppPermissionService;
import com.querydsl.core.types.Predicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
@RequestMapping("/apps/{code}")
public class AppPermissionController {

    @Inject
    private AppDao appDao;

    @Inject
    private AppPermissionDao appPermissionDao;

    @Inject
    private AppPermissionService appPermissionService;

    @PostMapping("/permissions")
    public ResponseEntity<AppPermission> addAppPermission(@PathVariable("code") String code,
                                                          @RequestBody @Valid PermissionDto dto) {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));

        appPermissionDao.findByAppAndNameAndStatus(app, dto.getName(), EntityStatusConstant.ACTIVE)
                .ifPresent(appPermission -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                });
        AppPermission appPermission = appPermissionService.createAppPermission(app, dto);
        return ResponseEntity.ok(appPermission);
    }

    @DeleteMapping("/permissions/{permissionName}")
    public ResponseEntity<AppPermission> deletePermission(@PathVariable("code") String code, @PathVariable("permissionName") String permissionName) {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));
        AppPermission appPermission = appPermissionDao.findByAppAndNameAndStatus(app, permissionName, EntityStatusConstant.ACTIVE)
                .orElseThrow(() ->
                    new NotFoundException(String.format("Permission with name %s not found", permissionName))
                );

        appPermission = appPermissionService.delete(appPermission);
        return ResponseEntity.ok(appPermission);

    }
}
