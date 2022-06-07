package BusinessLayer.Mappers;

public interface MapperInterface<S, T, K> {

    public void save(T entity);
    public void delete(T entity);
    public S toEntity(T entity);
    public T FromEntity(S entity);
    public T findByID(K key);
}
