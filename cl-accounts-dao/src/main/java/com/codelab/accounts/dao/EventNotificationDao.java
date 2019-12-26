package com.codelab.accounts.dao;

import com.cl.accounts.entity.EventNotification;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.EventNotificationTypeConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface EventNotificationDao extends JpaRepository<EventNotification, Long>, QuerydslPredicateExecutor<EventNotification> {
    Optional<EventNotification> findByTypeAndStatus(EventNotificationTypeConstant type, EntityStatusConstant status);
}
