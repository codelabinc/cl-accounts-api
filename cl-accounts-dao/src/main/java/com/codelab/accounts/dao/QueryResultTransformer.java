package com.codelab.accounts.dao;

public interface QueryResultTransformer<E, T> {

    T transform(E e);
}
