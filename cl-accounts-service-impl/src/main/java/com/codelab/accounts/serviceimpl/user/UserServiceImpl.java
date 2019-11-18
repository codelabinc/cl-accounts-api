package com.codelab.accounts.serviceimpl.user;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.cl.accounts.entity.PortalUserFactory;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.RoleTypeConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.domain.requests.UserCreationDto;
import com.codelab.accounts.service.membership.MemberRoleService;
import com.codelab.accounts.service.membership.MembershipService;
import com.codelab.accounts.service.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;

/**
 * @author lordUhuru 16/11/2019
 */
@Named
public class UserServiceImpl implements UserService {
    @Inject
    private AppRepository appRepository;

    @Inject
    private BCryptPasswordEncoder passwordEncoder;

    @Inject
    private MembershipService membershipService;

    @Inject
    private MemberRoleService memberRoleService;

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
        appRepository.persist(portalUser);
        Membership membership = membershipService.grantMembership(portalAccount, portalUser);
        memberRoleService.grantRole(membership, Collections.singleton(RoleTypeConstant.ADMIN));
        return portalUser;
    }
}
