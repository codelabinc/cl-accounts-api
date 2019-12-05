package com.codelab.accounts.conf.listener;


import com.codelab.accounts.conf.loader.DefaultAccountLoader;
import com.codelab.accounts.conf.loader.LocationLoader;
import com.codelab.accounts.conf.loader.RolePermissionLoader;
import com.codelab.accounts.dao.CountryDao;
import com.codelab.accounts.dao.PortalAccountDao;
import com.codelab.accounts.dao.StateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Named
public class ApplicationListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final CountryDao countryDao;

    private final StateDao stateDao;

    private final LocationLoader locationLoader;

    private final RolePermissionLoader rolePermissionLoader;

    private final DefaultAccountLoader defaultAccountLoader;

    private final PortalAccountDao portalAccountDao;

    public ApplicationListener(CountryDao countryDao,
                               StateDao stateDao,
                               LocationLoader locationLoader,
                               RolePermissionLoader rolePermissionLoader, DefaultAccountLoader defaultAccountLoader, PortalAccountDao portalAccountDao) {
        this.countryDao = countryDao;
        this.stateDao = stateDao;
        this.locationLoader = locationLoader;
        this.rolePermissionLoader = rolePermissionLoader;
        this.defaultAccountLoader = defaultAccountLoader;
        this.portalAccountDao = portalAccountDao;
    }

    @PostConstruct
    public void init() {

        if (countryDao.count() == 0) {
            locationLoader.loadCountries();
        }
        if (stateDao.count() == 0) {
            locationLoader.loadStates();
        }
        rolePermissionLoader.loadRoles();
        rolePermissionLoader.loadPermissions();
        if(portalAccountDao.count() == 0) {
            defaultAccountLoader.createDefaultAccount();
        }
        rolePermissionLoader.loadCodelabRolePermissions();
        logger.info("=====> Countries: {}", countryDao.count());
        logger.info("=====> States: {}", stateDao.count());
        logger.info("=====> Portal Accounts : {}", portalAccountDao.count());

    }
}
