package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.Role;
import com.codelab.accounts.domain.request.RoleDto;

public interface RoleService {
    Role createRole(App app, RoleDto dto);
    Role deleteRole(Role role);
}
