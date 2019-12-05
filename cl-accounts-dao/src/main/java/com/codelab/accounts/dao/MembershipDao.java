package com.codelab.accounts.dao;

import com.cl.accounts.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author lordUhuru 04/12/2019
 */
public interface MembershipDao extends JpaRepository<Membership, Long>, QuerydslPredicateExecutor<Membership> {
}
