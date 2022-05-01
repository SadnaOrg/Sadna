package ServiceLayer;

import java.util.function.Function;
import java.util.function.Supplier;

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

    public static <T> Response<T> makeResponse(T res, String msg){
        return res!=null? new Response<>(res) : new Response<>(msg);
    }

    public static  <T> Response<T> tryMakeResponse(Supplier<T> res, String msg){
        try { return makeResponse(res.get(),msg);}
        catch (Exception e){return new Response<>(e.getMessage());}
    }

    public <K> Response<K> safe(Function<T,K> f){
        return this.isOk() ? new Response<K>(f.apply(this.element)) : new Response<K>(this.getMsg());
    }
    public <K> Response<K> safe(Function<T,K> f,String msg) {
        return this.isOk() ? tryMakeResponse(() -> f.apply(this.element), msg) : new Response<K>(this.getMsg());
    }
}
