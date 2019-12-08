package com.codelab.accounts.coreapi.controller;

import com.cl.accounts.entity.Country;
import com.cl.accounts.entity.QCountry;
import com.cl.accounts.entity.QState;
import com.cl.accounts.entity.State;
import com.cl.accounts.enumeration.PortalAccountTypeConstant;
import com.codelab.accounts.dao.CountryDao;
import com.codelab.accounts.dao.StateDao;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

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
}
