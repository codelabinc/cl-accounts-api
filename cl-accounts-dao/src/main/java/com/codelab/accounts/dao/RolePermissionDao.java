package com.codelab.accounts.dao;

import com.cl.accounts.entity.Permission;
import com.cl.accounts.entity.Role;
import com.cl.accounts.entity.RolePermission;
import com.cl.accounts.enumeration.EntityStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface RolePermissionDao extends JpaRepository<RolePermission, Long>, QuerydslPredicateExecutor<RolePermission> {
    Optional<RolePermission> findByRoleAndPermissionAndStatus(Role role, Permission permission, EntityStatusConstant status);
}
