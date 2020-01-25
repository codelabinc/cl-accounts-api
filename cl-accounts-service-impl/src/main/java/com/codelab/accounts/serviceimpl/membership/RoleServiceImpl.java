package com.codelab.accounts.serviceimpl.membership;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.Role;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.PermissionDao;
import com.codelab.accounts.dao.RoleDao;
import com.codelab.accounts.domain.request.RoleDto;
import com.codelab.accounts.service.membership.RoleService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RoleServiceImpl implements RoleService {

    @Inject
    private RoleDao roleDao;

    @Inject
    private PermissionDao permissionDao;

    @Override
    public Role createRole(App app, RoleDto dto) {
        return roleDao.findByNameAndAppAndStatus(dto.getName().trim(), app, EntityStatusConstant.ACTIVE)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setApp(app);
                    role.setName(dto.getName().trim());
                    role.setStatus(EntityStatusConstant.ACTIVE);
                    roleDao.save(role);
                    return role;
                });
    }

    @Override
    public Role deleteRole(Role role) {
        if(permissionDao.countAllByRoleAndStatus(role, EntityStatusConstant.ACTIVE) > 0) {
            throw new IllegalArgumentException("Role has entities dependent on it");
        }
        role.setStatus(EntityStatusConstant.DEACTIVATED);
        roleDao.save(role);
        return role;
    }
}
