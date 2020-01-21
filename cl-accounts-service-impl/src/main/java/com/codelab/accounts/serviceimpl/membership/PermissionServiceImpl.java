package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.EntityDao;
import com.codelab.accounts.service.membership.PermissionService;
import com.querydsl.jpa.impl.JPAQuery;

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
}
