package com.codelab.accounts.dao;

import com.cl.accounts.entity.App;
import com.cl.accounts.entity.QApp;
import com.cl.accounts.enumeration.EntityStatusConstant;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;

public interface AppDao extends JpaRepository<App, Long>, QuerydslPredicateExecutor<App>,
        QuerydslBinderCustomizer<QApp> {
    Optional<App> findByNameIgnoreCaseAndStatus(String name, EntityStatusConstant status);

    Optional<App> findByCodeIgnoreCaseAndStatus(String code, EntityStatusConstant status);

    @Override
    default void customize(QuerydslBindings bindings, QApp root){
        bindings.bind(root.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.code).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.status).first((path, value) -> path.eq(value));

    }
}
