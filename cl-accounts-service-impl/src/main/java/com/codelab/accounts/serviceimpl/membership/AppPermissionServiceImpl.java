package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.AppPermission;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.AppPermissionDao;
import com.codelab.accounts.domain.request.PermissionDto;
import com.codelab.accounts.service.membership.AppPermissionService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AppPermissionServiceImpl implements AppPermissionService {
    @Inject
    private AppPermissionDao appPermissionDao;

    @Override
    public AppPermission createAppPermission(App app, PermissionDto dto) {
        return appPermissionDao.findByAppAndNameAndStatus(app, dto.getName().trim(), EntityStatusConstant.ACTIVE)
                .orElseGet(() -> {
                    AppPermission appPermission = new AppPermission();
                    appPermission.setApp(app);
                    appPermission.setName(dto.getName().trim());
                    appPermission.setStatus(EntityStatusConstant.ACTIVE);
                    appPermissionDao.save(appPermission);
                    return appPermission;
                });
    }

    @Override
    public AppPermission delete(AppPermission appPermission) {
        appPermission.setStatus(EntityStatusConstant.DEACTIVATED);
        appPermissionDao.save(appPermission);
        return appPermission;
    }
}
