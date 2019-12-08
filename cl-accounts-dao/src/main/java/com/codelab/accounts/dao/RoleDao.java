package com.codelab.accounts.dao;

import com.cl.accounts.entity.Role;
import com.cl.accounts.enumeration.EntityStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface RoleDao extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {
    Optional<Role> findByNameAndStatus(String name, EntityStatusConstant status);
}
