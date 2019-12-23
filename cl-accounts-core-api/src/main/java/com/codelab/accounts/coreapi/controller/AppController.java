package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.EntityRepository;
import com.codelab.accounts.dao.QueryResultTransformer;
import com.codelab.accounts.service.membership.MembershipService;
import com.codelab.accounts.service.principal.RequestPrincipal;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collection;

@RestController
@RequestMapping("/apps")
public class AppController {

    @Inject
    private EntityRepository entityRepository;

    @Inject
    private MembershipService membershipService;

    @Inject
    private Provider<RequestPrincipal> requestPrincipalProvider;


    @GetMapping
    public Page<App> getApps(@QuerydslPredicate(root = App.class) Predicate predicate,
                                         Pageable pageable) {
        PortalUser portalUser = requestPrincipalProvider.get().getLoggedInUser();
        QMembership qMembership = QMembership.membership;
        JPAQuery<Membership> qMembershipJPAQuery = entityRepository.startJPAQueryFrom(QMembership.membership);

        qMembershipJPAQuery.where(qMembership.portalUser.eq(portalUser)
            .and(qMembership.status.eq(EntityStatusConstant.ACTIVE))
        );
        qMembershipJPAQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());
        if (predicate == null) {
            predicate = qMembership.id.gt(0);
        }
        qMembershipJPAQuery.where(predicate);

        return entityRepository.fetchPagedResults(qMembershipJPAQuery.select(qMembership.portalAccount.app),app -> app);
    }
}
