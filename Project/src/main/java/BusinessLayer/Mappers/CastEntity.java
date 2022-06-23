package BusinessLayer.Mappers;

public interface CastEntity<S, T> {

    public S toEntity(T entity);
    public T fromEntity(S entity);
}
