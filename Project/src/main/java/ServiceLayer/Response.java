package ServiceLayer;

import javax.naming.NoPermissionException;
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

    public static  <T> Response<T> tryMakeResponse(MySupplier<T> res,String eventName, String errMsg){
        try {
            Response<T> response = makeResponse(res.get(),errMsg);
            if (!response.isOk()) Log.getInstance().error(eventName+": "+ errMsg);
            else Log.getInstance().event(eventName+" Successes");
            return response;
        }
        catch (Exception e) {
            Log.getInstance().error(e.toString());
            return new Response<>(e.getMessage());
        }
    }

    public <K> Response<K> safe(Function<T,K> f){
        return this.isOk() ? new Response<>(f.apply(this.element)) : new Response<>(this.getMsg());
    }
    public <K> Response<K> safe(Function<T,K> f,String eventMSG,String msg) {
        return this.isOk() ? tryMakeResponse(() -> f.apply(this.element),eventMSG, msg) : new Response<>(this.getMsg());
    }
}
