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

    public static Result tryMakeResult(MySupplier<Boolean> res, String eventName,String errMsg){
       try {
           if (res.get()) {
               Log.getInstance().event(eventName + " Successes");
               return new Result();
           }
           Log.getInstance().error(eventName + " : " + errMsg);
           return new Result(errMsg);
       }
       catch (Exception e){
           Log.getInstance().error(e.toString());
           return new Result(e.getMessage());
       }
    }

}

