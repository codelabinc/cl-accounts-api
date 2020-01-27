package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.dao.EntityDao;
import com.codelab.accounts.dao.MembershipDao;
import com.codelab.accounts.domain.response.AppResponse;
import com.codelab.accounts.domain.response.AppStatisticsResponse;
import com.codelab.accounts.service.app.AppService;
import com.codelab.accounts.service.principal.RequestPrincipal;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apps")
public class AppController {

    @Inject
    private EntityDao entityDao;

    @Inject
    private AppDao appDao;

    @Inject
    private MembershipDao membershipDao;

    @Inject
    private Provider<RequestPrincipal> requestPrincipalProvider;

    @Inject
    private AppService appService;


    @GetMapping
    public Page<App> getApps(@QuerydslPredicate(root = App.class) Predicate predicate,
                             Pageable pageable) {
        PortalUser portalUser = requestPrincipalProvider.get().getLoggedInUser();
        QMembership qMembership = QMembership.membership;
        JPAQuery<Membership> qMembershipJPAQuery = entityDao.startJPAQueryFrom(QMembership.membership);
        qMembershipJPAQuery.innerJoin(qMembership.portalAccount).fetchJoin();
        qMembershipJPAQuery.where(qMembership.portalUser.eq(portalUser)
                .and(qMembership.status.eq(EntityStatusConstant.ACTIVE))
        );
        List<Membership> memberships = entityDao.fetchResultList(qMembershipJPAQuery);
        QApp qApp = QApp.app;
        JPAQuery<App> appJPAQuery = entityDao.startJPAQueryFrom(QApp.app);
        qApp.id.in(memberships.stream().map(membership -> membership.getPortalAccount().getApp().getId()).collect(Collectors.toList()))
                .and(qApp.status.eq(EntityStatusConstant.ACTIVE));


        appJPAQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());
        if (predicate == null) {
            predicate = qApp.id.gt(0);
        }
        appJPAQuery.where(predicate);

        return entityDao.fetchPagedResults(appJPAQuery, app -> app);
    }

    @GetMapping("/{code}")
    public ResponseEntity<AppResponse> getApp(@PathVariable("code") String code) {
        QApp qApp = QApp.app;
        JPAQuery<App> appJPAQuery = entityDao.startJPAQueryFrom(QApp.app);
        Predicate predicate = qApp.code.equalsIgnoreCase(code);
        appJPAQuery.leftJoin(qApp.webHook).fetchJoin();
        appJPAQuery.where(predicate);
        App app = entityDao.fetchOne(appJPAQuery)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));
        AppResponse appResponse = appService.toResponse(app);

        JPAQuery<EventSubscription> eventSubscriptionJPAQuery = entityDao.startJPAQueryFrom(QEventSubscription.eventSubscription);
        QEventSubscription qEventSubscription = QEventSubscription.eventSubscription;
        qEventSubscription.app.eq(app)
                .and(qEventSubscription.status.eq(EntityStatusConstant.ACTIVE));
        eventSubscriptionJPAQuery.join(qEventSubscription.eventNotification)
                .where(qEventSubscription.status.eq(EntityStatusConstant.ACTIVE)).fetchJoin();

        QPermission qPermission = QPermission.permission;
        JPAQuery<Permission> permissionJPAQuery = entityDao.startJPAQueryFrom(QPermission.permission);
        permissionJPAQuery.leftJoin(qPermission.role).fetchJoin();
        permissionJPAQuery.leftJoin(qPermission.app).fetchJoin();
        Predicate permissionPredicate = qPermission.app.eq(app)
                .and(qPermission.status.eq(EntityStatusConstant.ACTIVE))
                .and(qPermission.role.app.eq(app))
                .and(qPermission.role.status.eq(EntityStatusConstant.ACTIVE));

        permissionJPAQuery.where(permissionPredicate);

        List<Permission> permissions = entityDao.fetchResultList(permissionJPAQuery);
        appResponse.setPermissions(permissions);

        JPAQuery<Role> roleJPAQuery = entityDao.startJPAQueryFrom(QRole.role);
        QRole qRole = QRole.role;
        Predicate rolePredicate = qRole.app.eq(app)
                .and(qRole.status.eq(EntityStatusConstant.ACTIVE));
        roleJPAQuery.where(rolePredicate);

        List<Role> roles = entityDao.fetchResultList(roleJPAQuery);
        appResponse.setRoles(roles);

        JPAQuery<AppPermission> appPermissionJPAQuery = entityDao.startJPAQueryFrom(QAppPermission.appPermission);
        QAppPermission qAppPermission = QAppPermission.appPermission;
        Predicate appPermissionPredicate = qAppPermission.app.eq(app)
                .and(qAppPermission.status.eq(EntityStatusConstant.ACTIVE));
        appPermissionJPAQuery.where(appPermissionPredicate);
        appResponse.setAppPermissions(entityDao.fetchResultList(appPermissionJPAQuery));

        List<EventSubscription> events = entityDao.fetchResultList(eventSubscriptionJPAQuery);
        appResponse.setEvents(events.stream()
                .map(EventSubscription::getEventNotification).collect(Collectors.toList()));

        return ResponseEntity.ok(appResponse);
    }

    @GetMapping("/{code}/statistics")
    public ResponseEntity<AppStatisticsResponse> appStatistics(@PathVariable("code") String code) {
        App app = appDao.findByCodeIgnoreCaseAndStatus(code, EntityStatusConstant.ACTIVE)
                .orElseThrow(() -> new NotFoundException(String.format("App with code %s not found", code)));
        return ResponseEntity.ok(appService.getStatistics(app));
    }
}
