package com.codelab.accounts.dao;

import com.cl.accounts.entity.Membership;
import com.cl.accounts.entity.QMembership;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * @author lordUhuru 04/12/2019
 */
public interface MembershipDao extends JpaRepository<Membership, Long>, QuerydslPredicateExecutor<Membership>,
        QuerydslBinderCustomizer<QMembership> {

    @Override
    default void customize(QuerydslBindings bindings, QMembership root) {
        bindings.bind(root.portalAccount.code).first(StringExpression::containsIgnoreCase);

    }
}
