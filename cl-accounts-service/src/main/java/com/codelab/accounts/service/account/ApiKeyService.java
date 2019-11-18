package com.codelab.accounts.service.account;

import com.cl.accounts.entity.ApiKey;
import com.cl.accounts.entity.Membership;

/**
 * @author lordUhuru 16/11/2019
 */
public interface ApiKeyService {
    ApiKey createApiKey(Membership membership);
}
