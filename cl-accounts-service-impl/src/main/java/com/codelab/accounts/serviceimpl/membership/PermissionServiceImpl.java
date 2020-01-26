package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.EntityDao;
import com.codelab.accounts.dao.PermissionDao;
import com.codelab.accounts.domain.request.PermissionDto;
import com.codelab.accounts.service.membership.PermissionService;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.security.core.parameters.P;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author lordUhuru 04/12/2019
 */
@Named
public class PermissionServiceImpl implements PermissionService {

    @Inject
    private EntityDao entityDao;

    @Inject
    private PermissionDao permissionDao;

    @Override
    public Collection<Permission> getPermissionsByRoleAndApp(Role role, App app) {
        QPermission qPermission = QPermission.permission;
        JPAQuery<Permission> permissionJPAQuery = entityDao.startJPAQueryFrom(QPermission.permission);
        permissionJPAQuery.where(qPermission.app.eq(app)
                .and(qPermission.role.eq(role))
                .and(qPermission.status.eq(EntityStatusConstant.ACTIVE))
        );
        return new ArrayList<>(entityDao.fetchResultList(permissionJPAQuery));
    }

    @Override
    public Permission createPermission(App app, Role role, PermissionDto dto) {
        return permissionDao.findByNameAndRoleAndAppAndStatus(dto.getName().trim(), role, app, EntityStatusConstant.ACTIVE).orElseGet(() -> {
            Permission permission = new Permission();
            permission.setName(dto.getName().trim());
            permission.setStatus(EntityStatusConstant.ACTIVE);
            permission.setApp(app);
            permission.setRole(role);
            permissionDao.save(permission);
            return permission;
        });
    }

    @Override
    public Permission deletePermission(Permission permission) {
        permission.setStatus(EntityStatusConstant.DEACTIVATED);
        permissionDao.save(permission);
        return permission;
    }


}
