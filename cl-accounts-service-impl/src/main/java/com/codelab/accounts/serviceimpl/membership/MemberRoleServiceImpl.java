package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.MemberRole;
import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.QMemberRole;
import com.cl.accounts.entity.Role;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.dao.MemberRoleDao;
import com.codelab.accounts.dao.RoleDao;
import com.codelab.accounts.service.membership.MemberRoleService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class MemberRoleServiceImpl implements MemberRoleService {
    private final AppRepository appRepository;

    private final RoleDao roleDao;

    private final MemberRoleDao memberRoleDao;

    public MemberRoleServiceImpl(AppRepository appRepository, RoleDao roleDao, MemberRoleDao memberRoleDao) {
        this.appRepository = appRepository;
        this.roleDao = roleDao;
        this.memberRoleDao = memberRoleDao;
    }

    @Override
    public Membership grantRole(Membership membership, Collection<RoleTypeConstant> roleTypeConstants) {
        roleTypeConstants.forEach(roleTypeConstant -> {
            MemberRole memberRole = new MemberRole();
            memberRole.setMembership(membership);
            memberRole.setStatus(EntityStatusConstant.ACTIVE);
            memberRole.setDateCreated(Timestamp.from(Instant.now()));
            memberRole.setRole(roleDao.findByNameAndStatus(roleTypeConstant, EntityStatusConstant.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Role with name %s not found", roleTypeConstant.getValue()))));
            appRepository.persist(memberRole);
        });
        return membership;
    }

    @Override
    public Collection<Role> getRolesByMembership(Membership membership) {
        QMemberRole qMemberRole = QMemberRole.memberRole;
        JPAQuery<MemberRole> memberRoleJPAQuery = appRepository.startJPAQueryFrom(QMemberRole.memberRole);
        memberRoleJPAQuery.innerJoin(qMemberRole.role)
                .where(qMemberRole.role.status.eq(EntityStatusConstant.ACTIVE)).fetchJoin();
        Predicate predicate = qMemberRole.status.eq(EntityStatusConstant.ACTIVE)
                .and(qMemberRole.membership.eq(membership));
        memberRoleJPAQuery.where(predicate);
       return appRepository.fetchResultList(memberRoleJPAQuery)
               .stream()
               .map(MemberRole::getRole).collect(Collectors.toList());
    }
}
