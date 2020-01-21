package com.codelab.accounts.service.webhook;

import com.cl.accounts.entity.App;
import com.codelab.accounts.domain.request.WebHookDto;

public interface WebHookService {
    void createWebHook(WebHookDto dto, App app);

    void updateWebHook(WebHookDto dto, App app);
}
