package com.codelab.accounts.dao;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.EventSubscription;
import com.cl.accounts.enumeration.EntityStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import java.util.List;

public interface EventSubscriptionDao extends JpaRepository<EventSubscription, Long>, QuerydslPredicateExecutor<EventSubscription> {
    List<EventSubscription> findAllByAppAndStatus(App app, EntityStatusConstant status);
}
