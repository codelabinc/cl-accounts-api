package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.QApp;
import com.cl.accounts.entity.QRole;
import com.cl.accounts.entity.Role;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.dao.EntityDao;
import com.codelab.accounts.dao.RoleDao;
import com.codelab.accounts.domain.request.RoleDto;
import com.codelab.accounts.service.membership.RoleService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
@RequestMapping("/apps/{code}")
public class RoleController {

    @Inject
    private AppDao appDao;

    @Inject
    private EntityDao entityDao;

    @Inject
    private RoleDao roleDao;

    @Inject
    private RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity<Role> addRole(@PathVariable("code") String code, @RequestBody @Valid RoleDto dto) {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));

        roleDao.findByNameAndAppAndStatus(dto.getName().trim(), app, EntityStatusConstant.ACTIVE).ifPresent(role -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        });
        Role role = roleService.createRole(app, dto);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/roles/{name}")
    public ResponseEntity<Role> deleteRole(@PathVariable("code") String code, @PathVariable("name") String roleName) {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));

        Role role = roleDao.findByNameAndAppAndStatus(roleName, app, EntityStatusConstant.ACTIVE)
                .orElseThrow(() -> new NotFoundException(String.format("Role %s not found", roleName)));

        role = roleService.deleteRole(role);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/roles")
    public Page<Role> getRoles(@PathVariable("code") String code, Pageable pageable) {
        QApp qApp = QApp.app;
        Predicate predicate = qApp.code.equalsIgnoreCase(code)
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));
        App app = appDao.findOne(predicate)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));
        QRole qRole = QRole.role;
        JPAQuery<Role> roleJPAQuery = entityDao.startJPAQueryFrom(qRole);
        roleJPAQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        roleJPAQuery.where(qRole.app.eq(app)
                .and(qRole.status.eq(EntityStatusConstant.ACTIVE)));
        return entityDao.fetchPagedResults(roleJPAQuery, role -> role);
    }

}
