package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.*;
import AcceptanceTests.DataObjects.*;

import java.util.List;

// TODO: NOTIFICATION TEST

// This class is used for setting up data for tests
public abstract class ProjectTests {

    protected static UserBridge userBridge;

    protected static ShopFilter [] shopFilters = null;
    public static final int NAME_FILTER = 0, DESC_FILTER = 1;

    protected static ShopFilter [] shopFailFilters = null;
    public static final int NAMEF_FILTER = 0, DESCF_FILTER = 1;

    protected static ProductFilter [] productFilters = null;
    public static final int MANUFACTURER_FILTER = 0,PRODUCT_DESC_FILTER = 1;

    protected static ProductFilter [] productFailFilters = null;
    public static final int MANUFACTURERF_FILTER = 0,PRODUCT_DESCF_FILTER = 1;

    public static final Shop[] shops = {null,null,null};
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
    private static boolean init = true;

    public static void setUpTests(){
        // setUpSystem();

        Guest ace_guest =userBridge.visit();
        Guest castro_guest = userBridge.visit();
        Guest megasport_guest = userBridge.visit();

        RegistrationInfo ace = new RegistrationInfo("ACEFounder","ACE_rocks");
        RegistrationInfo castro = new RegistrationInfo("castroFounder","castro_rocks");
        RegistrationInfo megaSport = new RegistrationInfo("MegaSportFounder","MegaSport_rocks");

        userBridge.register(ace_guest.name,ace);
        userBridge.register(castro_guest.name,castro);
        userBridge.register(megasport_guest.name, megaSport);

        ACEFounder = userBridge.login(ace_guest.getName(),ace);
        castroFounder = userBridge.login(castro_guest.getName(), castro);
        MegaSportFounder = userBridge.login(megasport_guest.getName(), megaSport);

        // not initialized by one of the test classes
        if(init){
            SubscribedUserProxy proxy = new SubscribedUserProxy((UserProxy) userBridge);
            Shop aceShop =proxy.openShop(ACEFounder.name,"ACE", "home renovation");
            Shop castroShop = proxy.openShop(castroFounder.name, "castro", "fashion");
            Shop megaSportShop = proxy.openShop(MegaSportFounder.name, "MegaSport", "sports");
            shops[ACE_ID] = aceShop;
            shops[castro_ID] = castroShop;
            shops[MegaSport_ID] = megaSportShop;

            ACEProducts = setUpACEProducts(proxy);
            castroProducts = setUpCastroProducts(proxy);
            MegaSportProducts = setUpMegaSportProducts(proxy);

            proxy.appointManager(castroShop.ID, castroFounder.name,MegaSportFounder.name);
            proxy.appointOwner(castroShop.ID, castroFounder.name,ACEFounder.name);
            proxy.appointOwner(aceShop.ID, ACEFounder.name,MegaSportFounder.name);

            shops[castro_ID] = shopSearch(shops[castro_ID].ID);
            shops[MegaSport_ID] = shopSearch(shops[MegaSport_ID].ID);
            shops[ACE_ID] = shopSearch(shops[ACE_ID].ID);
            init = false;
        }

        userBridge.exit(ACEFounder.name);
        userBridge.exit(castroFounder.name);
        userBridge.exit(MegaSportFounder.name);

        shopFilters = setUpShopFilters();
        shopFailFilters = setUpFailShopFilters();
        productFilters = setUpProductFilters();
        productFailFilters = setUpFailProductFilters();

    }

    private static Product [] setUpACEProducts(SubscribedUserBridge b){
        Product p1 = new Product("lamp","good","israel");
        Product p2 = new Product("office chair","good","china");
        Product p3 = new Product("desk","newest edition","china");

        b.addProductToShop(ACEFounder.name, shops[ACE_ID].ID, p1, 0, 30, 20);
        b.addProductToShop(ACEFounder.name, shops[ACE_ID].ID, p2, 1, 100, 25);
        b.addProductToShop(ACEFounder.name, shops[ACE_ID].ID, p3, 2, 40, 40);

        b.setCategory(ACEFounder.name,0,"not expensive",shops[ACE_ID].ID);
        b.setCategory(ACEFounder.name,1,"not expensive",shops[ACE_ID].ID);
        b.setCategory(ACEFounder.name,2,"expensive",shops[ACE_ID].ID);
        return new Product[]{p1, p2, p3};
    }

    private static Product[] setUpCastroProducts(SubscribedUserBridge b){
        Product p1 = new Product("T-shirt","recommended","china");
        Product p2 = new Product("jeans","recommended","china");
        Product p3 = new Product("shoes","recommended","china");

        b.addProductToShop(castroFounder.name, shops[castro_ID].ID, p1, 2, 30, 30);
        b.addProductToShop(castroFounder.name, shops[castro_ID].ID, p2, 345, 100, 35);
        b.addProductToShop(castroFounder.name, shops[castro_ID].ID, p3, 45, 40, 50);

        return new Product[]{p1, p2, p3};
    }

    private static Product[] setUpMegaSportProducts(SubscribedUserBridge b){
        Product p1 = new Product("running shoes","recommended","china");
        Product p2 = new Product("dumbbell" ,"recommended","china");
        Product p3 = new Product("jump rope","new product!", "china");

        b.addProductToShop(MegaSportFounder.name, shops[MegaSport_ID].ID, p1, 13, 30, 40);
        b.addProductToShop(MegaSportFounder.name, shops[MegaSport_ID].ID, p2, 31, 100, 55);
        b.addProductToShop(MegaSportFounder.name, shops[MegaSport_ID].ID, p3, 4, 40, 70);


        return new Product[]{p1, p2, p3};
    }

    private static ShopFilter [] setUpShopFilters(){
        // ShopFilter shopFilterRating = (s) -> s.rating >= 3.5;
        ShopFilter shopFilterName = (s) -> s.name.contains("AC");
        ShopFilter shopFilterDesc = (s) -> s.desc.equals("fashion");
        return new ShopFilter[]{shopFilterName,shopFilterDesc};
    }

    private static ShopFilter [] setUpFailShopFilters(){
        // ShopFilter shopFailFilterRating = (s) -> s.rating >= 5.2;
        ShopFilter shopFailFilterName = (s) -> s.name.contains("ad");
        ShopFilter shopFailFilterDesc = (s) -> s.desc.equals("food");
        return new ShopFilter[]{shopFailFilterName,shopFailFilterDesc};
    }

    private static ProductFilter[] setUpProductFilters() {
        // ProductFilter productFailFilterRating = (p) -> p.rating >= 3.5;
        ProductFilter productFilterManufacturer = (p) -> p.getManufacturer().equals("israel");
        ProductFilter productFilterDesc = (p) -> p.desc.equals("newest edition");
        return new ProductFilter[]{productFilterManufacturer,productFilterDesc};
    }

    private static ProductFilter[] setUpFailProductFilters() {
        // ProductFilter productFilterRating = (p) -> p.rating >= 5.2;
        ProductFilter productFailFilterManufacturer = (p) -> p.getManufacturer().equals("turkey");
        ProductFilter productFailFilterDesc = (p) -> p.desc.equals("very bad");
        return new ProductFilter[]{productFailFilterManufacturer, productFailFilterDesc};
    }

    private static Shop shopSearch(int id){
        List<Shop> shopsSearch = userBridge.getShopsInfo(castroFounder.name, new ShopFilter() {
            @Override
            public boolean filter(Shop shop) {
                return shop.ID == id;
            }
        });
        return shopsSearch.get(0);
    }
}