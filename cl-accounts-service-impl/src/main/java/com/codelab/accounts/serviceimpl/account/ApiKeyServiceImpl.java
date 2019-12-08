package com.codelab.accounts.serviceimpl.account;


import com.cl.accounts.entity.ApiKey;
import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.codelab.accounts.dao.AppRepository;
import com.codelab.accounts.domain.qualifier.ApiKeySequence;
import com.codelab.accounts.service.account.ApiKeyService;
import com.codelab.accounts.service.sequence.SequenceGenerator;

import javax.inject.Inject;
import javax.inject.Named;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

/**
 * @author lordUhuru 11/11/2019
 */
@Named
public class ApiKeyServiceImpl implements ApiKeyService {

    final
    SequenceGenerator apiKeySequenceService;

    private final AppRepository appRepository;

    public ApiKeyServiceImpl(@ApiKeySequence SequenceGenerator apiKeySequenceService, AppRepository appRepository) {
        this.apiKeySequenceService = apiKeySequenceService;
        this.appRepository = appRepository;
    }

    @Override
    public ApiKey createApiKey(PortalAccount portalAccount) {
        ApiKey apiKey = new ApiKey();
        apiKey.setDateCreated(Timestamp.from(Instant.now()));
        apiKey.setKey(generateApiKeyFrom(apiKeySequenceService.getNextLong()));
        apiKey.setStatus(EntityStatusConstant.ACTIVE);
        apiKey.setPortalAccount(portalAccount);
        appRepository.persist(apiKey);
        return apiKey;
    }

    private String generateApiKeyFrom(Long number) {
        byte[] bytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(number);
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }
}
