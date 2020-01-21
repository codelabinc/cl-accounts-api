package com.codelab.accounts.dao;

import com.cl.accounts.entity.WebHook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WebHookDao extends JpaRepository<WebHook, Long>, QuerydslPredicateExecutor<WebHook> {
}
