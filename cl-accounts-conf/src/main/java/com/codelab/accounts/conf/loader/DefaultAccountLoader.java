package com.codelab.accounts.conf.loader;

import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.codelab.accounts.domain.request.AccountCreationDto;
import com.codelab.accounts.domain.request.AddressDto;
import com.codelab.accounts.domain.request.UserCreationDto;
import com.codelab.accounts.service.account.AccountService;
import com.codelab.accounts.service.user.UserService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class DefaultAccountLoader {

    private final AccountService accountService;

    private final UserService userService;

    public DefaultAccountLoader(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }


    @Transactional
    public void createDefaultAccount(){
        AccountCreationDto accountCreationDto = new AccountCreationDto();
        accountCreationDto.setName("Codelab Technology Solutions Ltd.");
        accountCreationDto.setAccountType(PortalAccountTypeConstant.CODELAB.getValue());
        accountCreationDto.setAddress(addressDto());
        accountCreationDto.setAdminUser(userCreationDto());
        PortalAccount portalAccount = accountService.createPortalAccount(accountCreationDto);
        userService.createPortalUser(portalAccount, accountCreationDto.getAdminUser());
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
        dto.setPassword("");
        dto.setUsername("nedeki");
        dto.setPhoneNumber("+2348028584732");
        dto.setEmail("nathanieledeki@gmail.com");
        return dto;
    }

}
