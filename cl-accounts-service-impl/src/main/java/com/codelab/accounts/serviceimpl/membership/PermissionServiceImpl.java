package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.EntityRepository;
import com.codelab.accounts.service.membership.PermissionService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author lordUhuru 04/12/2019
 */
@Named
public class PermissionServiceImpl implements PermissionService {

    @Inject
    private EntityRepository entityRepository;

    @Override
    public Collection<Permission> getPermissionsByRoleAndApp(Role role, App app) {
        QPermission qPermission = QPermission.permission;
        JPAQuery<Permission> permissionJPAQuery = entityRepository.startJPAQueryFrom(QPermission.permission);
        permissionJPAQuery.where(qPermission.app.eq(app)
                .and(qPermission.role.eq(role))
                .and(qPermission.status.eq(EntityStatusConstant.ACTIVE))
        );
        return new ArrayList<>(entityRepository.fetchResultList(permissionJPAQuery));
    }
}
