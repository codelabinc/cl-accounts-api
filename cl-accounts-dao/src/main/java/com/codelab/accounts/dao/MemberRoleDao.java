package com.codelab.accounts.dao;

import com.cl.accounts.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author lordUhuru 04/12/2019
 */
public interface MemberRoleDao extends JpaRepository<MemberRole, Long>, QuerydslPredicateExecutor<MemberRole> {
}
