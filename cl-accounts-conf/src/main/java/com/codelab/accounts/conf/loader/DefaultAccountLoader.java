package com.codelab.accounts.conf.loader;

import com.cl.accounts.entity.PortalAccount;
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
import com.codelab.accounts.service.user.UserService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class DefaultAccountLoader {

    private final AccountService accountService;

    private final UserService userService;

    private final RolePermissionLoader rolePermissionLoader;

    private final RoleDao roleDao;

    public DefaultAccountLoader(AccountService accountService, UserService userService, RolePermissionLoader rolePermissionLoader, RoleDao roleDao) {
        this.accountService = accountService;
        this.userService = userService;
        this.rolePermissionLoader = rolePermissionLoader;
        this.roleDao = roleDao;
    }


    @Transactional
    public void createDefaultAccount(){
        AccountCreationDto accountCreationDto = new AccountCreationDto();
        accountCreationDto.setName("Codelab Technology Solutions Ltd.");
        accountCreationDto.setAccountType(PortalAccountTypeConstant.CODELAB.getValue());
        accountCreationDto.setAddress(addressDto());
        accountCreationDto.setAdminUser(userCreationDto());
        PortalAccount portalAccount = accountService.createPortalAccount(accountCreationDto);
        Role role = roleDao.findByNameAndStatus(SystemRoleTypeConstant.ADMIN.getValue(), EntityStatusConstant.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Role not found"));
        rolePermissionLoader.loadPermissionsForRole(role, portalAccount, SystemPermissionTypeConstant.values() );
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
