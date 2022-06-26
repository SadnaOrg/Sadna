package BusinessLayer.Caches;

public class Cacheable <K,T>{
    private T element;
    private K id;
    private boolean dirty;

    public Cacheable(K id, T element){
        this.element = element;
        this.id = id;
        this.dirty = false;
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

    public void mark(){
        this.dirty = true;
    }

    public void unMark(){
        this.dirty = false;
    }

    public boolean isDirty(){
        return this.dirty;
    }

}
