package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.service.membership.PermissionService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author lordUhuru 04/12/2019
 */
@Named
public class PermissionServiceImpl implements PermissionService {

    @Inject
    private AppRepository appRepository;

    @Override
    public Collection<Permission> getPermissionsByRoleAndPortalAccount(Role role, PortalAccount portalAccount) {
        QRolePermission qRolePermission = QRolePermission.rolePermission;
        JPAQuery<RolePermission> rolePermissionJPAQuery = appRepository.startJPAQueryFrom(QRolePermission.rolePermission);
        rolePermissionJPAQuery.innerJoin(qRolePermission.permission)
                .where(qRolePermission.permission.status.eq(EntityStatusConstant.ACTIVE)).fetchJoin();
        Predicate predicate = qRolePermission.role.eq(role)
                .and(qRolePermission.portalAccount.eq(portalAccount))
                .and(qRolePermission.status.eq(EntityStatusConstant.ACTIVE));
        rolePermissionJPAQuery.where(predicate);
        return appRepository.fetchResultList(rolePermissionJPAQuery).stream()
                .map(RolePermission::getPermission).collect(Collectors.toList());
    }
}
