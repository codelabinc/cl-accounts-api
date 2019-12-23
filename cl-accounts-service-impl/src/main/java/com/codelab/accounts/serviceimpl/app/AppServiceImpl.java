package com.codelab.accounts.serviceimpl.app;

import com.cl.accounts.entity.App;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.domain.qualifier.AppCodeSequence;
import com.codelab.accounts.service.app.AppService;
import com.codelab.accounts.service.sequence.SequenceGenerator;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;

@Named
public class AppServiceImpl implements AppService {

    @Inject
    private AppDao appDao;

    @Inject
    @AppCodeSequence
    private SequenceGenerator appCodeGenerator;

    @Override
    public App createApp(String name) {
        return appDao.findByNameIgnoreCaseAndStatus(name, EntityStatusConstant.ACTIVE).orElseGet(() -> {
            App app = new App();
            app.setName(name);
            app.setCode(appCodeGenerator.getNext());
            app.setStatus(EntityStatusConstant.ACTIVE);
            app.setDateCreated(Timestamp.from(Instant.now()));
            appDao.save(app);
            return app;
        });
    }
}
