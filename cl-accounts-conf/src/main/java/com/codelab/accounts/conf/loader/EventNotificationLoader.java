package com.codelab.accounts.conf.loader;

import com.cl.accounts.entity.EventNotification;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.EventNotificationTypeConstant;
import com.codelab.accounts.dao.EventNotificationDao;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EventNotificationLoader {

    @Inject
    private EventNotificationDao eventNotificationDao;

    public void loadEvents() {
        for(EventNotificationTypeConstant event: EventNotificationTypeConstant.values()) {
            eventNotificationDao.findByTypeAndStatus(event, EntityStatusConstant.ACTIVE)
                    .orElseGet(() -> {
                        EventNotification eventNotification = new EventNotification();
                        eventNotification.setType(event);
                        eventNotification.setStatus(EntityStatusConstant.ACTIVE);
                        eventNotification.setPayload(getPayload(event));
                        eventNotificationDao.save(eventNotification);
                        return eventNotification;
                    });
        }
    }

    private String getPayload(EventNotificationTypeConstant event) {
        switch (event) {
            default:
                return "{\n" +
                        "    \"identifier\": \"shalxht38\",\n" +
                        "    \"password\": \"kapotoshinto\"\n" +
                        "}";

        }
    }
}
