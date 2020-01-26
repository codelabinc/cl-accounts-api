package com.codelab.accounts.service.membership;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.AppPermission;
import com.codelab.accounts.domain.request.PermissionDto;

public interface AppPermissionService {
    AppPermission createAppPermission(App app, PermissionDto dto);

    AppPermission delete(AppPermission appPermission);
}
