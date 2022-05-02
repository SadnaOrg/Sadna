package ServiceLayer;

import java.util.function.Supplier;

public class Result {
    String msg;

    public Result(){
        msg= null;
    }
    public Result(String msg) {
        this.msg = msg;
    }

    public boolean isOk(){
        return msg == null;
    }

    public String getMsg() {
        return msg;
    }

    public static Result makeResult(boolean res, String msg){
        return res? new Result() : new Result(msg);
    }

    public static Result tryMakeResult(MySupplier<Boolean> res, String msg){
       try {
           if (res.get())
               return new Result();
           Log.getInstance().error(msg);
           return new Result(msg);
       }
       catch (Exception e){
           Log.getInstance().error(e.toString());
           return new Result(e.getMessage());
       }
    }

}

