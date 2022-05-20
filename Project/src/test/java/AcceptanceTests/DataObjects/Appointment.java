package AcceptanceTests.DataObjects;

import ServiceLayer.Objects.Administrator;

public class Appointment {
    public Role role;
    public String appointer;

    public Appointment(Role role,String appointer){
        this.role = role;
        this.appointer = appointer;
    }

    public Appointment(Administrator administrator){
        this.appointer = administrator.getAppointer();
        this.role = convert(administrator.getType());
    }
    public Role getRole() {
        return role;
    }

    public String getAppointer() {
        return appointer;
    }

    public enum Role{
        MANAGER,
        OWNER,
        FOUNDER,;
    }

    private Appointment.Role convert(ServiceLayer.Objects.Administrator.Type type){
        switch (type){
            case MANAGER -> {
                return Appointment.Role.MANAGER;
            }
            case OWNER -> {
                return Appointment.Role.OWNER;
            }
            default -> {
                return Appointment.Role.FOUNDER;
            }
        }
    }
}
