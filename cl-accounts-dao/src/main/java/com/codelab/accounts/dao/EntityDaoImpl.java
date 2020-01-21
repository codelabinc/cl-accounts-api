package com.codelab.accounts.dao;

import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Named
class EntityDaoImpl implements EntityDao {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public <E> long count(Class<E> type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(type);
        criteriaQuery.select(cb.count(root));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public <E> Optional<E> findById(Class<E> type, Object id) {
        return Optional.ofNullable(entityManager.find(type, id));
    }

    @Override
    public <E> E persist(E e) {
        entityManager.persist(e);
        return e;
    }

    @Override
    public <E> E merge(E e) {
        return entityManager.merge(e);
    }

    @Override
    public void remove(Object e) {
        entityManager.remove(e);
    }

    @Override
    public <E> JPAQuery<E> startJPAQueryFrom(EntityPath<E> entityPath) {
        return new JPAQuery<E>(entityManager).from(entityPath);
    }

    @Transactional(readOnly = true)
    @Override
    public <E> QueryResults<E> fetchResults(JPAQuery<E> query) {
        return query.fetchResults();
    }


    public boolean isProxy(Object entity) {
        return !Persistence.getPersistenceUtil().isLoaded(entity);
    }

    @Override
    public <E> Optional<E> fetchOne(JPAQuery<E> query) {
        return Optional.ofNullable(query.fetchFirst());
    }

    @Transactional
    @Override
    public <E> List<E> fetchResultList(JPAQuery<E> query) {
        return query.fetch();
    }

    @Override
    public <E, T> QueryResults<T> fetchResults(JPAQuery<E> query, QueryResultTransformer<E, T> transformer) {
        QueryResults<E> results = query.fetchResults();
        return new QueryResults<T>(results.getResults().stream().map(transformer::transform)
                .collect(Collectors.toList()), new QueryModifiers(results.getLimit(), results.getOffset()), results.getTotal());
    }

    @Transactional
    @Override
    public <E, T> Page<T> fetchPagedResults(JPAQuery<E> query, QueryResultTransformer<E, T> transformer) {
        QueryResults<E> results = query.fetchResults();
        return new PageImpl<T>(results.getResults().stream().map(transformer::transform)
                .collect(Collectors.toList()), PageRequest.of((int)results.getOffset(), (int)results.getLimit()), results.getTotal());
    }
}
