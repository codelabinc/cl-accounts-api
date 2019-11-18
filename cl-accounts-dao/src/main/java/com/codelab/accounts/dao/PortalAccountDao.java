package com.codelab.accounts.dao;

import com.cl.accounts.entity.PortalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author lordUhuru 16/11/2019
 */
public interface PortalAccountDao extends JpaRepository<PortalAccount, Long>, QuerydslPredicateExecutor<PortalAccount>{
}
