package Mocks;

public class Appointment {
    public String role;
    public int appointer;

    public Appointment(String role,int appointer){
        this.role = role;
        this.appointer = appointer;
    }
    public String getRole() {
        return role;
    }

    public int getAppointer() {
        return appointer;
    }
}
