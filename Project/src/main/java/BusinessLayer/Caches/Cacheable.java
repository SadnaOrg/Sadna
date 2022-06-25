package BusinessLayer.Caches;

public class Cacheable <K,T>{
    private T element;
    private K id;

    public Cacheable(K id, T element){
        this.element = element;
        this.id = id;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public K getId() {
        return id;
    }


}
