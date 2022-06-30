package ORM.Users;

import javax.persistence.Embeddable;

@Embeddable
public class BidApprovalStatus {

    private String approver;
    private int shopApprovalID;
    private boolean status;

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public int getShopID() {
        return shopApprovalID;
    }

    public void setShopID(int shopID) {
        this.shopApprovalID = shopID;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public BidApprovalStatus() {
    }

    public BidApprovalStatus(String approver, int shopID, boolean status) {
        this.approver = approver;
        this.shopApprovalID = shopID;
        this.status = status;
    }

}
