package Mocks;

import Mocks.User;

public class Guest extends User {

    public Guest(int ID){
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
