package com.codelab.accounts.conf.listener;


import com.codelab.accounts.conf.loader.DefaultAccountLoader;
import com.codelab.accounts.conf.loader.LocationLoader;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.dao.CountryDao;
import com.codelab.accounts.dao.PortalAccountDao;
import com.codelab.accounts.dao.StateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ApplicationListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    private CountryDao countryDao;

    @Inject
    private StateDao stateDao;

    @Inject
    private LocationLoader locationLoader;

    @Inject
    private DefaultAccountLoader defaultAccountLoader;

    @Inject
    private AppDao appDao;

    @Inject
    private PortalAccountDao portalAccountDao;

    @PostConstruct
    public void init() {

        if (countryDao.count() == 0) {
            locationLoader.loadCountries();
        }
        if (stateDao.count() == 0) {
            locationLoader.loadStates();
        }
        if(portalAccountDao.count() == 0) {
            defaultAccountLoader.createDefaultAccount();
        }
        logger.info("=====> Apps: {}", appDao.count());
        logger.info("=====> Countries: {}", countryDao.count());
        logger.info("=====> States: {}", stateDao.count());
        logger.info("=====> Portal Accounts : {}", portalAccountDao.count());

    }
}
