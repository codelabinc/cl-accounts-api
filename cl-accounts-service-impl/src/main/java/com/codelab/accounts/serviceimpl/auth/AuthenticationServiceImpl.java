package com.codelab.accounts.serviceimpl.auth;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.codelab.accounts.domain.response.HttpError;
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
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
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

    public AuthenticationServiceImpl(MembershipService membershipService, MemberRoleService memberRoleService, PermissionService permissionService, TokenService tokenService) {
        this.membershipService = membershipService;
        this.memberRoleService = memberRoleService;
        this.permissionService = permissionService;
        this.tokenService = tokenService;
    }

    @Override
    public String doLogin(PortalUser portalUser, PortalAccount portalAccount){
        List<String> permissions = new ArrayList<>();
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccount(new NameCodeResponse(portalAccount.getName(), portalAccount.getCode()));
        Membership membership = membershipService.getMembershipByPortalUserAndPortalAccount(portalUser, portalAccount)
                .orElseThrow(() -> new IllegalArgumentException("No Membership for User"));
        tokenResponse.setUser(toUserResponse(portalUser, membership));
        tokenResponse.setRoles(memberRoleService.getRolesByMembership(membership)
                .stream()
                .map(role -> {
                    permissions.addAll(permissionService.getPermissionsByRoleAndPortalAccount(role, portalAccount)
                            .stream()
                            .map(permission -> permission.getName().getValue()).collect(Collectors.toList()));
                    return role.getName().getValue();
                }).collect(Collectors.toList()));
        tokenResponse.setPermissions(permissions);
        logger.info(tokenResponse.toString());
        try {
            return tokenService.createToken(tokenResponse);
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
