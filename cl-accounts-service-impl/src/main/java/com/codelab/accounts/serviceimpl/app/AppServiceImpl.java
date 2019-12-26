package com.codelab.accounts.serviceimpl.app;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.QMembership;
import com.cl.accounts.entity.QPortalAccount;
import com.cl.accounts.enumeration.AppModeConstant;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalUserAuthenticationTypeConstant;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.dao.MembershipDao;
import com.codelab.accounts.dao.PortalAccountDao;
import com.codelab.accounts.domain.qualifier.AppCodeSequence;
import com.codelab.accounts.domain.response.AppStatisticsResponse;
import com.codelab.accounts.service.app.AppService;
import com.codelab.accounts.service.sequence.SequenceGenerator;
import com.querydsl.core.types.Predicate;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;

@Named
public class AppServiceImpl implements AppService {

    @Inject
    private AppDao appDao;

    @Inject
    private PortalAccountDao portalAccountDao;

    @Inject
    private MembershipDao membershipDao;

    @Inject
    @AppCodeSequence
    private SequenceGenerator appCodeGenerator;

    @Override
    public App createApp(String name, String description) {
        return appDao.findByNameIgnoreCaseAndStatus(name, EntityStatusConstant.ACTIVE).orElseGet(() -> {
            App app = new App();
            app.setName(name);
            app.setCode(appCodeGenerator.getNext());
            app.setDescription(description);
            app.setMode(AppModeConstant.TEST);
            app.setStatus(EntityStatusConstant.ACTIVE);
            app.setDateCreated(Timestamp.from(Instant.now()));
            appDao.save(app);
            return app;
        });
    }

    @Override
    public AppStatisticsResponse getStatistics(App app) {
        QPortalAccount qPortalAccount = QPortalAccount.portalAccount;

        Predicate totalAccountPredicate = qPortalAccount.app.eq(app)
                .and(qPortalAccount.status.eq(EntityStatusConstant.ACTIVE));

        AppStatisticsResponse appStat = new AppStatisticsResponse();
        appStat.setTotalActiveAccounts(portalAccountDao.count(totalAccountPredicate));

        QMembership qMembership = QMembership.membership;
        Predicate totalUserPredicate = qMembership.portalAccount.app.eq(app)
                .and(qMembership.status.eq(EntityStatusConstant.ACTIVE)
                .and(qMembership.portalUser.authenticationType.eq(PortalUserAuthenticationTypeConstant.IDENTIFIER_PASSWORD_CREDENTIALS)));
        appStat.setTotalActiveUsers(membershipDao.count(totalUserPredicate));
        return appStat;
    }
}
