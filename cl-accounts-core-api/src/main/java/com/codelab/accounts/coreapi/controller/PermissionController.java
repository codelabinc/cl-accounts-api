package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.Permission;
import com.cl.accounts.entity.QApp;
import com.cl.accounts.entity.Role;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.dao.PermissionDao;
import com.codelab.accounts.dao.RoleDao;
import com.codelab.accounts.domain.request.PermissionDto;
import com.codelab.accounts.service.membership.PermissionService;
import com.querydsl.core.types.Predicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
@RequestMapping("/apps/{code}")
public class PermissionController {

    @Inject
    private AppDao appDao;

    @Inject
    private PermissionDao permissionDao;

    @Inject
    private PermissionService permissionService;

    @Inject
    private RoleDao roleDao;

    @PostMapping("/roles/{roleName}/permissions")
    public ResponseEntity<Permission> addPermission(@PathVariable("code") String code,
                                                    @PathVariable("roleName") String roleName,
                                                    @RequestBody @Valid PermissionDto dto) {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));

        Role role = roleDao.findByNameAndAppAndStatus(roleName, app, EntityStatusConstant.ACTIVE)
                .orElseThrow(() -> new NotFoundException(String.format("Role with name %s not found", roleName)));
        permissionDao.findByNameAndRoleAndAppAndStatus(dto.getName().trim(), role, app, EntityStatusConstant.ACTIVE)
                .ifPresent(permission -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                });
        Permission permission = permissionService
                .createPermission(app, role, dto);
        return ResponseEntity.ok(permission);
    }

    @DeleteMapping("/roles/{roleName}/permissions/{permissionName}")
    public ResponseEntity<Permission> deletePermission(@PathVariable("code") String code,
                                                    @PathVariable("roleName") String roleName,
                                                    @PathVariable("permissionName") String permissionName) {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));
        Role role = roleDao.findByNameAndAppAndStatus(roleName, app, EntityStatusConstant.ACTIVE)
                .orElseThrow(() -> new NotFoundException(String.format("Role with name %s not found", roleName)));

        Permission permission = permissionDao.findByNameAndRoleAndAppAndStatus(permissionName, role, app, EntityStatusConstant.ACTIVE)
                .orElseThrow(() -> new NotFoundException(String.format("Permission with name %s not found", permissionName)));

        permission = permissionService.deletePermission(permission);
        return ResponseEntity.ok(permission);

    }
}
