package com.codelab.accounts.dao;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.AppPermission;
import com.cl.accounts.enumeration.EntityStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface AppPermissionDao extends JpaRepository<AppPermission, Long>, QuerydslPredicateExecutor<AppPermission> {
    Optional<AppPermission> findByAppAndNameAndStatus(App app, String name, EntityStatusConstant status);
}
