package AcceptanceTests.DataObjects;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class SubscribedUser extends User{

    public SubscribedUser(String username){
        this.name = username;
    }

    public SubscribedUser(ServiceLayer.Objects.SubscribedUser user){
        this.name = user.username;
    }

    public enum Permission{
        STOCK_MANAGEMENT(1),
        SET_PURCHASE_POLICY(2),
        ASSIGN_SHOP_OWNER(4),
        ASSIGN_SHOP_MANAGER(6),
        CHANGE_MANAGER_PERMISSION(7),
        CLOSE_SHOP(9),
        REOPEN_SHOP(10),
        ROLE_INFO(11),
        HISTORY_INFO(13),
        REMOVE_ADMIN(5),;

        private int code;

        Permission(int code){
            this.code = code;
        }

        public static Map<Integer,Permission> lookup = new HashMap<>();

        static {
            for(Permission p : EnumSet.allOf(Permission.class))
                lookup.put(p.getCode(), p);
        }

        public int getCode(){
            return this.code;
        }

        public static Permission lookup(int code){
            return lookup.get(code);
        }
    }
}
