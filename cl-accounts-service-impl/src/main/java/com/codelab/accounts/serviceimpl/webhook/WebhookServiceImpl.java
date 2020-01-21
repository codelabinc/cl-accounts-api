package com.codelab.accounts.serviceimpl.webhook;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.EventNotification;
import com.cl.accounts.entity.EventSubscription;
import com.cl.accounts.entity.WebHook;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.EventNotificationTypeConstant;
import com.codelab.accounts.dao.AppDao;
import com.codelab.accounts.dao.EventNotificationDao;
import com.codelab.accounts.dao.EventSubscriptionDao;
import com.codelab.accounts.dao.WebHookDao;
import com.codelab.accounts.domain.request.WebHookDto;
import com.codelab.accounts.service.webhook.WebHookService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class WebhookServiceImpl implements WebHookService {

    @Inject
    private WebHookDao webHookDao;

    @Inject
    private EventNotificationDao eventNotificationDao;

    @Inject
    private EventSubscriptionDao eventSubscriptionDao;

    @Inject
    private AppDao appDao;

    @Transactional
    @Override
    public void createWebHook(WebHookDto dto, App app) {
        dto.getEvents().forEach(event -> {
            EventNotification eventNotification = eventNotificationDao
                    .findByTypeAndStatus(EventNotificationTypeConstant.fromString(event), EntityStatusConstant.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Event notification %s not found", event)));
            EventSubscription eventSubscription = new EventSubscription();
            eventSubscription.setEventNotification(eventNotification);
            eventSubscription.setApp(app);
            eventSubscription.setStatus(EntityStatusConstant.ACTIVE);
            eventSubscriptionDao.save(eventSubscription);
        });

        WebHook webHook = new WebHook();
        webHook.setLiveUrl(dto.getLiveUrl());
        webHook.setTestUrl(dto.getTestUrl());
        webHookDao.save(webHook);
        app.setWebHook(webHook);
        appDao.save(app);
    }

    @Transactional
    @Override
    public void updateWebHook(WebHookDto dto, App app) {
        eventSubscriptionDao.findAllByAppAndStatus(app, EntityStatusConstant.ACTIVE).forEach(eventSubscription -> {
            eventSubscription.setStatus(EntityStatusConstant.DEACTIVATED);
            eventSubscriptionDao.save(eventSubscription);
        });
        dto.getEvents().forEach(event -> {
            EventNotification eventNotification = eventNotificationDao
                    .findByTypeAndStatus(EventNotificationTypeConstant.fromString(event), EntityStatusConstant.ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Event notification %s not found", event)));
            EventSubscription eventSubscription = new EventSubscription();
            eventSubscription.setEventNotification(eventNotification);
            eventSubscription.setApp(app);
            eventSubscription.setStatus(EntityStatusConstant.ACTIVE);
            eventSubscriptionDao.save(eventSubscription);
        });
        webHookDao.findById(app.getWebHook().getId()).ifPresent(webHook -> {
            webHook.setTestUrl(dto.getTestUrl());
            webHook.setLiveUrl(dto.getLiveUrl());
            webHookDao.save(webHook);
        });
    }
}
