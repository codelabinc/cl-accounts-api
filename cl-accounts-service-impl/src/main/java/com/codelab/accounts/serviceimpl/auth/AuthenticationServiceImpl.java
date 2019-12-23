package com.codelab.accounts.serviceimpl.auth;

import com.cl.accounts.entity.*;
import com.codelab.accounts.dao.EntityRepository;
import com.codelab.accounts.domain.response.LoginResponse;
import com.codelab.accounts.domain.response.NameCodeResponse;
import com.codelab.accounts.domain.response.TokenResponse;
import com.codelab.accounts.domain.response.UserResponse;
import com.codelab.accounts.jwt.TokenService;
import com.codelab.accounts.service.auth.AuthenticationService;
import com.codelab.accounts.service.membership.MemberRoleService;
import com.codelab.accounts.service.membership.MembershipService;
import com.codelab.accounts.service.membership.PermissionService;
import com.nimbusds.jose.JOSEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lordUhuru 04/12/2019
 */
@Named
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MembershipService membershipService;

    private final MemberRoleService memberRoleService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PermissionService permissionService;

    private final TokenService tokenService;

    private final EntityRepository entityRepository;

    public AuthenticationServiceImpl(MembershipService membershipService, MemberRoleService memberRoleService, PermissionService permissionService, TokenService tokenService, EntityRepository entityRepository) {
        this.membershipService = membershipService;
        this.memberRoleService = memberRoleService;
        this.permissionService = permissionService;
        this.tokenService = tokenService;
        this.entityRepository = entityRepository;
    }

    @Override
    @Transactional
    public LoginResponse generateLoginResponse(PortalUser portalUser, PortalAccount portalAccount){
        List<String> permissions = new ArrayList<>();
        TokenResponse tokenResponse = new TokenResponse();
        App app = entityRepository.findById(App.class, portalAccount.getApp().getId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("App with id %s not found", portalAccount.getApp().getId())));
        tokenResponse.setAccount(new NameCodeResponse(portalAccount.getName(), portalAccount.getCode()));
        Membership membership = membershipService.getMembershipByPortalUserAndPortalAccount(portalUser, portalAccount)
                .orElseThrow(() -> new IllegalArgumentException("No Membership for User"));
        if(membership.getRequestTokenRefresh()!= null && membership.getRequestTokenRefresh().equals(Boolean.TRUE)) {
            membership.setRequestTokenRefresh(Boolean.FALSE);
            entityRepository.merge(membership);
        }
        tokenResponse.setUser(toUserResponse(portalUser, membership));
        tokenResponse.setRoles(memberRoleService.getRolesByMembership(membership)
                .stream()
                .map(role -> {
                    permissions.addAll(permissionService.getPermissionsByRoleAndApp(role, app)
                            .stream()
                            .map(Permission::getName).collect(Collectors.toList()));
                    return role.getName();
                }).collect(Collectors.toList()));
        tokenResponse.setPermissions(permissions);
        try {
            return new LoginResponse(tokenService.createToken(tokenResponse));
        } catch (JOSEException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not generate User Token");
        }
    }

    private UserResponse toUserResponse(PortalUser portalUser, Membership membership) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(portalUser.getId());
        userResponse.setLastName(portalUser.getLastName());
        userResponse.setFirstName(portalUser.getFirstName());
        userResponse.setEmail(portalUser.getEmail());
        userResponse.setPhoneNumber(portalUser.getPhoneNumber());
        userResponse.setHasEverLoggedIn(membership.isHasEverLoggedIn());
        return userResponse;
    }
}
