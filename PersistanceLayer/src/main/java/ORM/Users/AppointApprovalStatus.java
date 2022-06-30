package ORM.Users;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Embeddable
public class AppointApprovalStatus {
    private String admin;
    private boolean status;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AppointApprovalStatus() {
    }

    public AppointApprovalStatus(String admin, boolean status) {
        this.admin = admin;
        this.status = status;
    }
}
