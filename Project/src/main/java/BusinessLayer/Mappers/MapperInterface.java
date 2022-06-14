package BusinessLayer.Mappers;

import BusinessLayer.Users.SubscribedUser;

public interface MapperInterface<S, M, T, K> {

    public void save(T entity);
    public void delete(T entity);
    public M toEntity(T entity);
    public T FromEntity(M entity);

    public T findByID(K key);
}
