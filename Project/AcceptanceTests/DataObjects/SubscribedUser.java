package DataObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribedUser extends User{
    public String username;
    public String password;
    public Map<Integer,Appointment> appointments;
    public Map<Integer,List<String>> shopPermissions;

    public SubscribedUser(int ID,String username,String password){
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.appointments = new HashMap<>();
        this.shopPermissions = new HashMap<>();
    }


    public List<String> getPermissions(int shopID) {
        return shopPermissions.getOrDefault(shopID,null);
    }

    public Appointment getRole(int shopID) {
        return appointments.getOrDefault(shopID,null);
    }

    public void addRole(int shopID, Appointment appointment){
        appointments.put(shopID, appointment);
    }
}
