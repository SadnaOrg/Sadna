package ServiceLayer;

public class Response<T> extends Result {

    T element;

    public Response(String msg) {
        super(msg);
        element = null;
    }

    public Response(T element) {
        this.element = element;
    }

    public T getElement() {
        return element;
    }
}
