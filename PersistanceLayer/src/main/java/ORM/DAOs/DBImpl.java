package ORM.DAOs;

import java.util.Collection;

public interface DBImpl<T, K> {
    int save(T entity);
    void update(T entity);
    void delete(K key);
    T findById(K key);
    Collection<T> findAll();
}
