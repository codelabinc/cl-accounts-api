package com.codelab.accounts.dao;

import com.cl.accounts.entity.State;
import com.cl.accounts.enumeration.EntityStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface StateDao extends JpaRepository<State, Long> {
    Optional<State> findByCodeIgnoreCaseAndStatus(String code, EntityStatusConstant status);
}
