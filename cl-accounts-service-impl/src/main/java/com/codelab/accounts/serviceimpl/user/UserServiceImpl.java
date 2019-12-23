package com.codelab.accounts.serviceimpl.user;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalUserAuthenticationTypeConstant;
import com.cl.accounts.enumeration.PortalUserTypeConstant;
import com.codelab.accounts.dao.EntityRepository;
import com.codelab.accounts.domain.enumeration.SystemRoleTypeConstant;
import com.codelab.accounts.domain.request.UserCreationDto;
import com.codelab.accounts.domain.response.PortalUserResponse;
import com.codelab.accounts.service.membership.MemberRoleService;
import com.codelab.accounts.service.membership.MembershipService;
import com.codelab.accounts.service.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class UserServiceImpl implements UserService {
    private final EntityRepository entityRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final MembershipService membershipService;

    private final MemberRoleService memberRoleService;

    public UserServiceImpl(EntityRepository entityRepository, BCryptPasswordEncoder passwordEncoder, MembershipService membershipService, MemberRoleService memberRoleService) {
        this.entityRepository = entityRepository;
        this.passwordEncoder = passwordEncoder;
        this.membershipService = membershipService;
        this.memberRoleService = memberRoleService;
    }

    @Override
    public PortalUser createPortalUser(PortalAccount portalAccount, UserCreationDto dto) {
        PortalUser portalUser = new PortalUser();
        portalUser.setLastName(dto.getLastName().trim());
        portalUser.setFirstName(dto.getFirstName().trim());
        portalUser.setEmail(dto.getEmail().trim());
        portalUser.setPhoneNumber(dto.getPhoneNumber().trim());
        portalUser.setPassword(passwordEncoder.encode(dto.getPassword().trim()));
        portalUser.setUsername(dto.getUsername().trim());
        portalUser.setStatus(EntityStatusConstant.ACTIVE);
        portalUser.setDateCreated(Timestamp.from(Instant.now()));
        portalUser.setAuthenticationType(PortalUserAuthenticationTypeConstant.IDENTIFIER_PASSWORD_CREDENTIALS);
        portalUser.setType(PortalUserTypeConstant.PORTAL_USER);
        entityRepository.persist(portalUser);
        Membership membership = membershipService.grantMembership(portalAccount, portalUser);
        memberRoleService.grantRole(membership, Collections.singleton(SystemRoleTypeConstant.ADMIN.getValue()));
        return portalUser;
    }

    @Override
    public PortalUser createSystemPortalUser(PortalAccount portalAccount) {
        PortalUser portalUser = new PortalUser();
        portalUser.setLastName(portalAccount.getName());
        portalUser.setFirstName("SYSTEM USER");
        portalUser.setStatus(EntityStatusConstant.ACTIVE);
        portalUser.setDateCreated(Timestamp.from(Instant.now()));
        portalUser.setAuthenticationType(PortalUserAuthenticationTypeConstant.API_CREDENTIALS);
        portalUser.setType(PortalUserTypeConstant.EXTERNAL_SYSTEM_USER);
        entityRepository.persist(portalUser);
        Membership membership = membershipService.grantMembership(portalAccount, portalUser);
        memberRoleService.grantRole(membership, Collections.singleton(SystemRoleTypeConstant.ADMIN.getValue()));
        return portalUser;
    }

    @Override
    public PortalUserResponse toUserResponse(PortalUser portalUser, Membership membership) {
        PortalUserResponse response = new PortalUserResponse();
        response.setUsername(portalUser.getUsername());
        response.setId(portalUser.getId());
        response.setEmail(portalUser.getEmail());
        response.setPhoneNumber(portalUser.getPhoneNumber());
        response.setDateCreated(portalUser.getDateCreated().toString());
        response.setHasEverLoggedIn(membership.isHasEverLoggedIn());
        response.setLastName(portalUser.getLastName());
        response.setFirstName(portalUser.getFirstName());
        response.setStatus(portalUser.getStatus());
        return response;
    }
}
