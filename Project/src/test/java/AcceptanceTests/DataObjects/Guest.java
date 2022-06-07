package AcceptanceTests.DataObjects;

import java.util.LinkedList;

public class Guest extends User {

    public Guest(String name){
        this.name = name;
        this.notifications = new LinkedList<>();
    }

    public String getName() {
        return this.name;
    }
}
