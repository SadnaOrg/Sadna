package AcceptanceTests.DataObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribedUser extends User{
    public Map<Integer,Appointment> appointments;
    public Map<Integer,List<String>> shopPermissions;

    public SubscribedUser(String username){
        this.name = username;
        this.appointments = new HashMap<>();
        this.shopPermissions = new HashMap<>();
    }

    public SubscribedUser(ServiceLayer.Objects.SubscribedUser user){

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
