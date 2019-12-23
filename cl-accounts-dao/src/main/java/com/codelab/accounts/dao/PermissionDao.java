package com.codelab.accounts.dao;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.Permission;
import com.cl.accounts.entity.Role;
import com.cl.accounts.enumeration.EntityStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface PermissionDao extends JpaRepository<Permission, Long> {
    Optional<Permission> findByNameAndStatus(String name, EntityStatusConstant status);
    Optional<Permission> findByNameAndRoleAndAppAndStatus(String name, Role role, App app, EntityStatusConstant status);
}
