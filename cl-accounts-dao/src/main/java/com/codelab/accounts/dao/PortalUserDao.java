package com.codelab.accounts.dao;

import com.cl.accounts.entity.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface PortalUserDao extends JpaRepository<PortalUser, Long>, QuerydslPredicateExecutor<PortalUser> {
}
