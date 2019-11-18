package com.codelab.accounts.dao;

import com.cl.accounts.entity.Permission;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.cl.accounts.enumeration.PermissionConstant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface PermissionDao extends JpaRepository<Permission, Long> {
    Optional<Permission> findByNameAndStatus(PermissionConstant permissionConstant, EntityStatusConstant status);
}
