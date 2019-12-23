package com.codelab.accounts.conf.loader;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.Role;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.codelab.accounts.conf.exception.NotFoundException;
import com.codelab.accounts.dao.RoleDao;
import com.codelab.accounts.domain.enumeration.SystemPermissionTypeConstant;
import com.codelab.accounts.domain.enumeration.SystemRoleTypeConstant;
import com.codelab.accounts.domain.request.AccountCreationDto;
import com.codelab.accounts.domain.request.AddressDto;
import com.codelab.accounts.domain.request.UserCreationDto;
import com.codelab.accounts.service.account.AccountService;
import com.codelab.accounts.service.app.AppService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class DefaultAccountLoader {

    @Inject
    private AccountService accountService;

    @Inject
    private RolePermissionLoader rolePermissionLoader;

    @Inject
    private RoleDao roleDao;

    @Inject
    private AppService appService;

    @Transactional
    public void createDefaultAccount(){
        App app = appService.createApp("Codelab Technology Solutions Ltd.");
        rolePermissionLoader.loadRoles(app);
        AccountCreationDto accountCreationDto = new AccountCreationDto();
        accountCreationDto.setName(app.getName());
        accountCreationDto.setAccountType(PortalAccountTypeConstant.SUPER_SYSTEM.getValue());
        accountCreationDto.setAddress(addressDto());
        accountCreationDto.setAdminUser(userCreationDto());
        accountService.createPortalAccount(accountCreationDto);
        Role role = roleDao.findByNameAndStatus(SystemRoleTypeConstant.ADMIN.getValue(), EntityStatusConstant.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Role not found"));
        rolePermissionLoader.loadPermissions(role, app);
        rolePermissionLoader.loadPermissionsForRole(role, app, SystemPermissionTypeConstant.values() );
    }

    private AddressDto addressDto() {
        AddressDto dto = new AddressDto();
        dto.setCountryCode("NGA");
        dto.setStateCode("NG-FC");
        dto.setHouseNumber("13");
        dto.setStreetAddress("Adbulrham Mora Street");
        dto.setTown("Jabi");
        dto.setLatitude(9.084433);
        dto.setLongitude(7.41482);
        return dto;
    }

    private UserCreationDto userCreationDto(){
        UserCreationDto dto = new UserCreationDto();
        dto.setFirstName("Nathaniel");
        dto.setLastName("Edeki");
        dto.setPassword("abercrombie");
        dto.setUsername("nedeki");
        dto.setPhoneNumber("+2348028584732");
        dto.setEmail("nathanieledeki@gmail.com");
        return dto;
    }

}
