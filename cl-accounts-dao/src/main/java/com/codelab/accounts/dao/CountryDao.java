package com.codelab.accounts.dao;

import com.cl.accounts.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface CountryDao extends JpaRepository<Country, Long> {
    Optional<Country> findByAlpha3IgnoreCase(String alpha3);
}
