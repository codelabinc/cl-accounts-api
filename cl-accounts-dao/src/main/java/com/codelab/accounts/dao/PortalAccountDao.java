package com.codelab.accounts.dao;

import com.cl.accounts.entity.PortalAccount;
import com.cl.accounts.entity.QPortalAccount;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * @author lordUhuru 16/11/2019
 */
public interface PortalAccountDao extends JpaRepository<PortalAccount, Long>, QuerydslPredicateExecutor<PortalAccount>,
        QuerydslBinderCustomizer<QPortalAccount> {
    @Override
    default void customize(QuerydslBindings bindings, QPortalAccount root) {
        bindings.bind(root.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.code).first(StringExpression::containsIgnoreCase);
    }
}
