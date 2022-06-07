package com.SadnaORM.RepositoriesImpl;

public interface RepositoryImpl <T, K> {
    public void save(T entity);
    public void delete(T entity);
    public T findById(K key);
}
