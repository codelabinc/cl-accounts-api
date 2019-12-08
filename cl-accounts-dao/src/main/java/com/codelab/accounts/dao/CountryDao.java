package com.codelab.accounts.dao;

import com.cl.accounts.entity.Country;
import com.cl.accounts.entity.QCountry;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface CountryDao extends JpaRepository<Country, Long>, QuerydslPredicateExecutor<Country>,
        QuerydslBinderCustomizer<QCountry> {
    Optional<Country> findByAlpha3IgnoreCase(String alpha3);

    @Override
    default void customize(QuerydslBindings bindings, QCountry root) {
        bindings.bind(root.name).first(StringExpression::containsIgnoreCase);

    }
}
