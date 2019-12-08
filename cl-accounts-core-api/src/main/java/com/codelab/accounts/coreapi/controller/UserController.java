package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.QMembership;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.dao.MembershipDao;
import com.codelab.accounts.domain.response.PortalUserResponse;
import com.codelab.accounts.service.user.UserService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author lordUhuru 08/12/2019
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Inject
    private AppRepository appRepository;

    @Inject
    private UserService userService;

    @GetMapping
    public Page<PortalUserResponse> getUsers(@QuerydslPredicate(root = Membership.class) Predicate predicate,
                                 Pageable pageable) {
        QMembership qMembership = QMembership.membership;
        JPAQuery<Membership> membershipJPAQuery = appRepository.startJPAQueryFrom(qMembership);

        membershipJPAQuery.innerJoin(qMembership.portalUser)
                .where(qMembership.portalUser.status.eq(EntityStatusConstant.ACTIVE))
                .fetchJoin();

        membershipJPAQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());
        if (predicate == null) {
            predicate = qMembership.id.gt(0);
        }
        membershipJPAQuery.where(predicate);

        return appRepository.fetchPagedResults(membershipJPAQuery, membership ->
                userService.toUserResponse(membership.getPortalUser(), membership));

    }
}
