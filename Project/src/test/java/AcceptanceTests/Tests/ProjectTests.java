package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.ShopsBridge;
import AcceptanceTests.Bridge.SystemBridge;
import AcceptanceTests.Bridge.UserBridge;
import AcceptanceTests.DataObjects.*;

// This class is used for setting up data for tests
public abstract class ProjectTests {

    protected static UserBridge userBridge;
    protected static ShopsBridge shopsBridge;
    protected static SystemBridge systemBridge;

    protected static final ShopFilter [] shopFilters = setUpShopFilters();
    public static final int RATING_FILTER = 0, NAME_FILTER = 1, CATEGORY_FILTER = 2;

    protected static final ShopFilter [] shopFailFilters = setUpFailShopFilters();
    public static final int RATINGF_FILTER = 0, NAMEF_FILTER = 1, CATEGORYF_FILTER = 2;

    protected static final ProductFilter [] productFilters = setUpProductFilters();
    public static final int PRODUCT_RATING_FILTER = 0, MANUFACTURER_FILTER = 1;

    protected static final ProductFilter [] productFailFilters = setUpFailProductFilters();
    public static final int PRODUCT_RATINGF_FILTER = 0, MANUFACTURERF_FILTER = 1;

    public static final Shop[] shops = {createACE(),createCastro(),createMegaSport()};
    public static final int ACE_ID = 0, castro_ID = 1, MegaSport_ID = 2;

    public static SubscribedUser ACEFounder=null;
    public static SubscribedUser castroFounder = null;
    public static SubscribedUser MegaSportFounder =null;


    protected static Product[] ACEProducts;
    protected static Product[] castroProducts;
    protected static Product[] MegaSportProducts;

    public static void setUserBridge(UserBridge bridge) {
        userBridge = bridge;
    }
    public static UserBridge getUserBridge(){return userBridge;}

    public static void setUpTests(){
        setUpSystem();
        shopsBridge.addShop(shops[ACE_ID],ACEFounder);
        shopsBridge.addShop(shops[castro_ID],castroFounder);
        shopsBridge.addShop(shops[MegaSport_ID],MegaSportFounder);

        Guest ace_guest =userBridge.visit();
        Guest castro_guest = userBridge.visit();
        Guest megasport_guest = userBridge.visit();

        ACEFounder = userBridge.register(ace_guest.ID,new RegistrationInfo("ACEFounder","ACE_rocks"));
        castroFounder = userBridge.register(castro_guest.ID, new RegistrationInfo("castroFounder","castro_rocks"));
        MegaSportFounder = userBridge.register(megasport_guest.ID, new RegistrationInfo("MegaSportFounder","MegaSport_rocks"));

        Appointment ace_founder = new Appointment("Founder", -1);
        Appointment ace_owner = new Appointment("Owner", ACEFounder.ID);
        Appointment castro_founder = new Appointment("Founder", -1);
        Appointment megasport_founder = new Appointment("Founder", -1);
        Appointment megasport_manager = new Appointment("Manager", MegaSportFounder.ID);

        ACEFounder.addRole(shops[ACE_ID].ID,ace_founder);
        castroFounder.addRole(shops[castro_ID].ID,castro_founder);
        castroFounder.addRole(shops[MegaSport_ID].ID,megasport_manager);
        MegaSportFounder.addRole(shops[MegaSport_ID].ID,megasport_founder);
        MegaSportFounder.addRole(shops[ACE_ID].ID,ace_owner);

        shopsBridge.addManager(shops[MegaSport_ID].ID,castroFounder.ID);
        shopsBridge.appointOwner(shops[ACE_ID].ID,ACEFounder.ID,MegaSportFounder.ID);

        ACEProducts = setUpACEProducts();
        castroProducts = setUpCastroProducts();
        MegaSportProducts = setUpMegaSportProducts();
    }

    private static Shop createACE(){
        return new Shop(ACEFounder,0, "ACE", 3.8, "home renovation");
    }

    private static Shop createMegaSport(){
        return new Shop(MegaSportFounder,1, "MegaSport", 4.0, "sports");
    }

    private static Shop createCastro(){
        return new Shop(castroFounder,2, "castro",4.2, "fashion");
    }

    private static void setUpSystem(){
        // use system bridge here : external services , manager, other users
    }

    private static Product [] setUpACEProducts(){
        Product p1 = new Product("lamp","israel");
        Product p2 = new Product("office chair","china");
        Product p3 = new Product("desk","china");

        shopsBridge.addProductToShop(shops[ACE_ID].ID,p1,0,3,30,20);
        shopsBridge.addProductToShop(shops[ACE_ID].ID,p2,1,4.2,100,25);
        shopsBridge.addProductToShop(shops[ACE_ID].ID,p3,2,2.9,40,40);

        return new Product[]{p1, p2, p3};
    }

    private static Product[] setUpCastroProducts(){
        Product p1 = new Product("T-shirt","china");
        Product p2 = new Product("jeans","china");
        Product p3 = new Product("shoes","china");

        shopsBridge.addProductToShop(shops[castro_ID].ID,p1,2,3,30,30);
        shopsBridge.addProductToShop(shops[castro_ID].ID,p2,345,4.2,100,35);
        shopsBridge.addProductToShop(shops[castro_ID].ID,p3,45,2.9,40,50);

        return new Product[]{p1, p2, p3};
    }

    private static Product[] setUpMegaSportProducts(){
        Product p1 = new Product("running shoes","china");
        Product p2 = new Product("dumbbell" ,"china");
        Product p3 = new Product("jump rope","china");

        shopsBridge.addProductToShop(shops[MegaSport_ID].ID,p1,13,3,30,40);
        shopsBridge.addProductToShop(shops[MegaSport_ID].ID,p2,31,4.2,100,55);
        shopsBridge.addProductToShop(shops[MegaSport_ID].ID,p3,4,2.9,40,70);

        return new Product[]{p1, p2, p3};
    }

    private static ShopFilter [] setUpShopFilters(){
        ShopFilter shopFilterRating = (s) -> s.rating >= 3.5;
        ShopFilter shopFilterName = (s) -> s.name.contains("AC");
        ShopFilter shopFilterCategory = (s) -> s.category.equals("fashion");
        return new ShopFilter[]{shopFilterRating,shopFilterName,shopFilterCategory};
    }

    private static ShopFilter [] setUpFailShopFilters(){
        ShopFilter shopFailFilterRating = (s) -> s.rating >= 5.2;
        ShopFilter shopFailFilterName = (s) -> s.name.contains("ad");
        ShopFilter shopFailFilterCategory = (s) -> s.category.equals("food");
        return new ShopFilter[]{shopFailFilterRating,shopFailFilterName,shopFailFilterCategory};
    }

    private static ProductFilter[] setUpProductFilters() {
        ProductFilter productFailFilterRating = (p) -> p.rating >= 3.5;
        ProductFilter productFailFilterManufacturer = (p) -> p.getManufacturer().equals("israel");
        return new ProductFilter[]{productFailFilterRating,productFailFilterManufacturer};
    }

    private static ProductFilter[] setUpFailProductFilters() {
        ProductFilter productFilterRating = (p) -> p.rating >= 5.2;
        ProductFilter productFilterManufacturer = (p) -> p.getManufacturer().equals("gali");
        return new ProductFilter[]{productFilterRating,productFilterManufacturer};
    }
}