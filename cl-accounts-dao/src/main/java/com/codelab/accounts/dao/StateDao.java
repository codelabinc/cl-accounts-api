package com.codelab.accounts.dao;

import com.cl.accounts.entity.QState;
import com.cl.accounts.entity.State;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;

/**
 * @author lordUhuru 16/11/2019
 */
public interface StateDao extends JpaRepository<State, Long>, QuerydslPredicateExecutor<State>,
        QuerydslBinderCustomizer<QState> {
    Optional<State> findByCodeIgnoreCaseAndStatus(String code, EntityStatusConstant status);

    @Override
    default void customize(QuerydslBindings bindings, QState root) {
        bindings.bind(root.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.country.alpha3).as("countryCode").first(StringExpression::containsIgnoreCase);
    }
}
