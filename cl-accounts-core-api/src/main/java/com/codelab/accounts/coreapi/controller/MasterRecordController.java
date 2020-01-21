package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.*;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.EventNotificationTypeConstant;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.codelab.accounts.dao.CountryDao;
import com.codelab.accounts.dao.EventNotificationDao;
import com.codelab.accounts.dao.StateDao;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lordUhuru 07/12/2019
 */
@RestController
@RequestMapping("/master-records")
public class MasterRecordController {

    @Inject
    private CountryDao countryDao;

    @Inject
    private StateDao stateDao;

    @Inject
    private EventNotificationDao eventNotificationDao;

    @GetMapping("/account-types")
    public ResponseEntity<?> getAccountTypes() {
       return ResponseEntity.ok(PortalAccountTypeConstant.names());
    }

    @GetMapping("/countries")
    public ResponseEntity<?> getCountries(@QuerydslPredicate(root = Country.class) Predicate predicate) {
        if (predicate == null) {
            predicate = QCountry.country.id.gt(0);
        }
        return ResponseEntity.ok(countryDao.findAll(predicate));
    }

    @GetMapping("/states")
    public ResponseEntity<?> getStates(@QuerydslPredicate(root = State.class) Predicate predicate) {
        if (predicate == null) {
            predicate = QState.state.id.gt(0);
        }
        return ResponseEntity.ok(stateDao.findAll(predicate));
    }

    @GetMapping("/event-notification-types")
    public ResponseEntity<List<EventNotification>> getEventNotificationTyes() {
        QEventNotification qEventNotification = QEventNotification.eventNotification;
        Predicate predicate = qEventNotification.status.eq(EntityStatusConstant.ACTIVE);
        List<EventNotification> eventNotifications = StreamSupport.stream(eventNotificationDao
                .findAll(predicate).spliterator(), false).collect(Collectors.toList());
        return ResponseEntity.ok(eventNotifications);
    }
}
