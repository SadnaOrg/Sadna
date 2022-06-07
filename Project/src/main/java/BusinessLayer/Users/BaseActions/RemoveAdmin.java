package BusinessLayer.Users.BaseActions;

import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopManager;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Shops.Shop;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RemoveAdmin extends BaseAction{
    private SubscribedUser u;
    private Shop shop;

    public RemoveAdmin(SubscribedUser u, Shop shop){
        this.u= u;
        this.shop = shop;
    }

    public boolean act(SubscribedUser toRemove){
        ShopAdministrator admin = shop.getShopAdministrator(toRemove.getUserName());
        ShopAdministrator adminUser = shop.getShopAdministrator(u.getUserName());
        if(adminUser == null) // only owners can remove appointments
            throw  new IllegalStateException("you aren't an admin of that shop!");

        if(admin == null)
            throw new IllegalStateException("this user isn't an admin of that shop!");

        String appointer = admin.getAppointer();

        if(!Objects.equals(appointer, u.getUserName())) // you have to be the appointer
            throw new IllegalStateException("you didn't appoint this admin!");

        if(!(adminUser instanceof ShopOwner)) // only owners can remove
            throw new IllegalStateException("you aren't an owner!");

        if(admin instanceof ShopManager){
            removeAdminAppointment(toRemove);
            return true;
        }
        else {
            if(admin instanceof ShopOwner){
                if(((ShopOwner) admin).isFounder())
                    throw new IllegalStateException("cant remove the founder!");
                removeAdminAppointment(toRemove);
                removeOwnerAppointments(admin);
                return true;
            }
            else throw new IllegalStateException("have to remove manager or owner!");
        }
    }

    private void removeAdminAppointment(SubscribedUser toRemove) {
        shop.removeAdmin(toRemove.getUserName()); // remove from shop
        toRemove.removeMyRole(shop.getId()); // remove from user
    }

    private void removeOwnerAppointments(ShopAdministrator owner) {
        Collection<ShopAdministrator> appoints = owner.getAppoints();
        for (ShopAdministrator admin: appoints) {
            removeAdminAppointment(admin.getSubscribed()); // simply remove
            removeOwnerAppointments(admin); // remove everyone he has appointed
        }
    }

}
