package com.codelab.accounts.conf.interceptors;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.PortalUser;
import com.cl.accounts.entity.QPortalAccount;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PortalUserAuthenticationTypeConstant;
import com.codelab.accounts.dao.PortalAccountDao;
import com.codelab.accounts.dao.PortalUserDao;
import com.codelab.accounts.domain.enumeration.TokenClaimsConstant;
import com.codelab.accounts.domain.response.ApiResponse;
import com.codelab.accounts.jwt.TokenService;
import com.codelab.accounts.service.membership.MembershipService;
import com.codelab.accounts.service.principal.RequestPrincipal;
import com.codelab.accounts.serviceimpl.principal.RequestPrincipalImpl;
import com.google.gson.Gson;
import com.nimbusds.jwt.JWTClaimsSet;
import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author lordUhuru 30/11/2019
 */
@Named
public class RequestPrincipalInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PortalUserDao portalUserDao;

    private final Gson gson;

    private final TokenService tokenService;

    private final ApplicationContext applicationContext;

    private final PortalAccountDao portalAccountDao;

    private final MembershipService membershipService;

    public RequestPrincipalInterceptor(PortalUserDao portalUserDao, Gson gson, TokenService tokenService, ApplicationContext applicationContext, PortalAccountDao portalAccountDao, MembershipService membershipService) {
        this.portalUserDao = portalUserDao;
        this.gson = gson;
        this.tokenService = tokenService;
        this.applicationContext = applicationContext;
        this.portalAccountDao = portalAccountDao;
        this.membershipService = membershipService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String targetPath = new UrlPathHelper().getPathWithinApplication(request);
        logger.debug(">>> targetPath: {}", targetPath);
        if (targetPath.contains("/login")
                || targetPath.contains("/forgot-password")
                || targetPath.contains("/refresh-token")
                || targetPath.contains("/swagger-ui")
                || targetPath.contains("/logout")
                || request.getServletPath().startsWith("/error")
                || request.getServletPath().startsWith("/swagger-ui.html")
                || request.getServletPath().startsWith("/webjars")
                || request.getServletPath().startsWith("/swagger-resources")
                || request.getServletPath().equals("/v2/api-docs")
        ) {
            return true;
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.debug(">>> Authorization: {}", authHeader);
        if (StringUtils.isBlank(authHeader)) {
            response.getWriter().append(gson.toJson(new ApiResponse(HttpStatus.UNAUTHORIZED.value(),
                    "Missing Authorization Header")));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        JWTClaimsSet claimsSet = tokenService.decodeToken(authHeader.replace("Bearer ", ""));
        if (StringUtils.isNotBlank(authHeader)) {
            RequestAttributes currentRequestAttributes = RequestContextHolder.currentRequestAttributes();

            if (claimsSet.getExpirationTime()
                    .before(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))) {
                response.getWriter().append(gson.toJson(new ApiResponse(HttpStatus.UNAUTHORIZED.value(),
                        "Token Expired. Call Refresh Token to get new Token")));
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            QPortalAccount qPortalAccount = QPortalAccount.portalAccount;
            Predicate portalAccountPredicate = qPortalAccount
                    .code.equalsIgnoreCase(claimsSet.getClaim(TokenClaimsConstant.ACCOUNT_CODE.getValue()).toString())
                    .and(qPortalAccount.status.eq(EntityStatusConstant.ACTIVE));
            PortalAccount portalAccount = portalAccountDao.findOne(portalAccountPredicate).orElse(null);
            if (portalAccount == null) {
                response.getWriter().append(gson.toJson(new ApiResponse(HttpStatus.UNAUTHORIZED.value(),
                        String.format("Account with code %s not found", claimsSet.getClaim(TokenClaimsConstant.ACCOUNT_CODE.getValue()).toString()))));
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            PortalUser portalUser = portalUserDao.findById(Long.valueOf(claimsSet.getSubject())).orElse(null);
            if (portalUser == null) {
                response.getWriter().append(gson.toJson(new ApiResponse(HttpStatus.UNAUTHORIZED.value(),
                        String.format("User with id %s not found", claimsSet.getSubject()))));
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            Membership membership = membershipService.getMembershipByPortalUserAndPortalAccount(portalUser, portalAccount)
                    .orElse(null);
            if(membership == null){
                response.getWriter().append(gson.toJson(new ApiResponse(HttpStatus.UNAUTHORIZED.value(),
                        "No Membership for user")));
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            if (membership.getRequestTokenRefresh() != null && membership.getRequestTokenRefresh().equals(Boolean.TRUE)) {
                response.getWriter().append(gson.toJson(new ApiResponse(HttpStatus.UNAUTHORIZED.value(),
                        "Token Invalidated. Call Refresh Token API")));
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            RequestPrincipalImpl requestPrincipal = new RequestPrincipalImpl(
                    portalUser,
                    portalAccount,
                    claimsSet.getStringListClaim(TokenClaimsConstant.ROLES.getValue()),
                    claimsSet.getStringListClaim(TokenClaimsConstant.PERMISSIONS.getValue()),
                    StringUtils.defaultIfBlank(request.getHeader("X-FORWARDED-FOR"), request.getRemoteAddr())
            ) {

            };
            applicationContext.getAutowireCapableBeanFactory().autowireBean(requestPrincipal);
            currentRequestAttributes.setAttribute(RequestPrincipal.class.getName(),
                    requestPrincipal,
                    RequestAttributes.SCOPE_REQUEST);
        }
        return true;
    }

    public static FactoryBean<RequestPrincipal> requestPrincipal() {
        return new FactoryBean<RequestPrincipal>() {

            @Override
            public RequestPrincipal getObject() {
                return (RequestPrincipal) RequestContextHolder.currentRequestAttributes().getAttribute(RequestPrincipal.class.getName(),
                        RequestAttributes.SCOPE_REQUEST);
            }

            @Override
            public Class<?> getObjectType() {
                return RequestPrincipal.class;
            }

            @Override
            public boolean isSingleton() {
                return false;
            }
        };
    }
}
