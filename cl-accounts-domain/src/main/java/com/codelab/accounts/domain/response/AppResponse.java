package com.codelab.accounts.domain.response;

import com.cl.accounts.entity.EventNotification;
import com.cl.accounts.entity.WebHook;
import com.cl.accounts.enumeration.AppModeConstant;
import com.cl.accounts.enumeration.EntityStatusConstant;

import java.sql.Timestamp;
import java.util.List;

public class AppResponse {
    private String name;
    private String code;
    private EntityStatusConstant status;
    private Timestamp dateCreated;
    private AppModeConstant mode;
    private String description;
    private WebHook webHook;
    private List<EventNotification> events;

    public AppResponse(String name, String code, EntityStatusConstant status, Timestamp dateCreated,
                       AppModeConstant mode, String description,
                       WebHook webHook) {
        this.name = name;
        this.code = code;
        this.status = status;
        this.dateCreated = dateCreated;
        this.mode = mode;
        this.description = description;
        this.webHook = webHook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EntityStatusConstant getStatus() {
        return status;
    }

    public void setStatus(EntityStatusConstant status) {
        this.status = status;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public AppModeConstant getMode() {
        return mode;
    }

    public void setMode(AppModeConstant mode) {
        this.mode = mode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WebHook getWebHook() {
        return webHook;
    }

    public void setWebHook(WebHook webHook) {
        this.webHook = webHook;
    }

    public List<EventNotification> getEvents() {
        return events;
    }

    public void setEvents(List<EventNotification> events) {
        this.events = events;
    }
}
