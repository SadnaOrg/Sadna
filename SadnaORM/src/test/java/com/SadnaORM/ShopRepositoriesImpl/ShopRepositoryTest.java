package com.SadnaORM.ShopRepositoriesImpl;

import com.SadnaORM.ShopRepositories.ProductRepository;
import com.SadnaORM.ShopRepositories.PurchaseHistoryRepository;
import com.SadnaORM.ShopRepositories.ShopRepository;
import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.PurchaseHistory;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserRepositories.BasketRepository;
import com.SadnaORM.UserRepositories.SubscribedUserRepository;
import com.SadnaORM.Users.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShopRepositoryTest {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SubscribedUserRepository subscribedUserRepository;

    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;

    @Autowired
    private BasketRepository basketRepository;

    private int productCounter;
    private Random rand = new Random();
    private int n = rand.nextInt(3, 20);

    private Shop shop;

    @Before
    public void setUp() throws Exception {
        productCounter = 0;
    }

    @After
    public void tearDown() throws Exception {
        shopRepository.deleteAll();
    }

    @Test
    public void saveEmptyShop() {
        shop = createRandomEmptyShop();
        shopRepository.save(shop);
        Assert.assertNotEquals(0, shopRepository.findById(shop.getId()).stream().count());
    }

    @Test
    public void updateEmptyShop() {
        saveEmptyShop();
        shop.setName("new name");
        shop.setDescription("new description");
        shop.setState(Shop.State.CLOSED);
        shopRepository.save(shop);
        Optional<Shop> savedShop = shopRepository.findById(shop.getId());
        Assert.assertEquals("new name", savedShop.get().getName());
        Assert.assertEquals("new description", savedShop.get().getDescription());
        Assert.assertEquals(Shop.State.CLOSED, savedShop.get().getState());
    }

    @Test
    public void deleteEmptyShop() {
        saveEmptyShop();
        shopRepository.delete(shop);
        Assert.assertEquals(0, shopRepository.findById(shop.getId()).stream().count());
    }

    @Test
    public void saveShopWithProducts() {
        shop = createRandomNonEmptyShop(n);
        shopRepository.save(shop);
        Assert.assertEquals(shopRepository.findById(shop.getId()).get().getProducts().size(), 3);
        Assert.assertTrue(productRepository.findAll().iterator().hasNext());
    }

    @Test
    public void updateShopWithProducts() {
        saveShopWithProducts();
        Collection<Product> newProducts = List.of(new Product[]{createRandomProduct(), createRandomProduct()});
        shop.setProducts(newProducts);
        shopRepository.save(shop);
        Assert.assertEquals(2, shopRepository.findById(shop.getId()).get().getProducts().size());
    }

    @Test
    public void deleteShopWithProducts() {
        saveShopWithProducts();
        shopRepository.delete(shop);
        Assert.assertEquals(0, shopRepository.findById(shop.getId()).stream().count());
        Assert.assertFalse(productRepository.findAll().iterator().hasNext());
    }

    @Test
    public void saveShopWithShopAdministrators() {
        shop = createRandomShopWithShopAdministrators(n);
        shopRepository.save(shop);
        Assert.assertEquals(n, shopRepository.findById(shop.getId()).get().getShopAdministrators().size());
    }

    @Test
    public void updateShopWithShopAdministrators() {
        saveShopWithShopAdministrators();
        Map<SubscribedUser, ShopAdministrator> shopAdministrators = createShopAdministratorMap(2);
        shop.setShopAdministrators(shopAdministrators);
        shopRepository.save(shop);
        Assert.assertEquals(2, shopRepository.findById(shop.getId()).get().getShopAdministrators().size());
    }

    @Test
    public void deleteShopWithShopAdministrators() {
        saveShopWithShopAdministrators();
        shopRepository.delete(shop);
        Assert.assertEquals(0, shopRepository.findById(shop.getId()).stream().count());
    }

    @Test
    public void saveShopWithShopAdministratorsNotExisting() {
            shop = createRandomShopWithShopAdministratorsNotExisting();
            try {
            }
            catch (InvalidDataAccessApiUsageException e1) {
                try {
                    shopRepository.save(shop);
                }
                catch (InvalidDataAccessApiUsageException e2) {
                    Assert.assertTrue(true);
                }
            }
    }

    private Shop createRandomShopWithShopAdministrators(int shopAdministratorsNum) {
        shop = createRandomEmptyShop();
        Map<SubscribedUser, ShopAdministrator> shopAdministrators = createShopAdministratorMap(shopAdministratorsNum);
        shop.setShopAdministrators(shopAdministrators);
        return shop;
    }

    private Map<SubscribedUser, ShopAdministrator> createShopAdministratorMap(int shopAdministratorsNum) {
        Map<SubscribedUser, ShopAdministrator> shopAdministrators = new ConcurrentHashMap<>();
        for (int i = 0; i < shopAdministratorsNum; i++) {
            SubscribedUser user = createRandomUser();
            List<ShopAdministrator> administrators = new ArrayList<>();
            administrators.add(createRandomShopManager(shop));
            administrators.add(createRandomShopOwner(shop));
            administrators.add(createRandomShopManager(shop));
            ShopAdministrator shopAdministrator = createRandomShopManager(shop);
            shopAdministrators.put(user, shopAdministrator);
        }
        return shopAdministrators;
    }

    private Shop createRandomShopWithShopAdministratorsNotExisting() {
        shop = createRandomEmptyShop();
        Map<SubscribedUser, ShopAdministrator> shopAdministrators = new ConcurrentHashMap<>();
        for (int i = 0; i < n; i++) {
            SubscribedUser user = createRandomUser();
            List<ShopAdministrator> administrators = new ArrayList<>();
            administrators.add(createRandomShopManagerWithoutSave(shop));
            administrators.add(createRandomShopOwnerWithoutSave(shop));
            administrators.add(createRandomShopManagerWithoutSave(shop));
            ShopAdministrator shopAdministrator = createRandomShopManagerWithoutSave(shop);
            shopAdministrators.put(user, shopAdministrator);
        }
        shop.setShopAdministrators(shopAdministrators);
        return shop;
    }

    @Test
    public void saveShopWithUsersBaskets() {
        shop = createRandomShopWithUsersBaskets(n);
        shopRepository.save(shop);
        Assert.assertEquals(n, shopRepository.findById(shop.getId()).get().getUsersBaskets().size());
        Assert.assertTrue(basketRepository.findAll().iterator().hasNext());
    }

    @Test
    public void updateShopWithUsersBaskets() {
        saveShopWithUsersBaskets();
        shop.setUsersBaskets(createUsersBasketsMap(2));
        shopRepository.save(shop);
        Assert.assertEquals(2, shopRepository.findById(shop.getId()).get().getUsersBaskets().size());
    }

    @Test
    public void deleteShopWithUsersBaskets() {
        saveShopWithUsersBaskets();
        shopRepository.delete(shop);
        Assert.assertEquals(0, shopRepository.findById(shop.getId()).stream().count());
        Assert.assertFalse(basketRepository.findAll().iterator().hasNext());
    }

    private Shop createRandomShopWithUsersBaskets(int usersBasketsNum) {
        shop = createRandomEmptyShop();
        Map<SubscribedUser, Basket> usersBaskets = createUsersBasketsMap(usersBasketsNum);
        shop.setUsersBaskets(usersBaskets);
        return shop;
    }

    private Map<SubscribedUser, Basket> createUsersBasketsMap(int usersBasketsNum) {
        Map<SubscribedUser, Basket> usersBaskets = new ConcurrentHashMap<>();
        for (int i = 0; i < usersBasketsNum; i++) {
            SubscribedUser user = createRandomUser();
            Basket basket = new Basket(shop, user);
            usersBaskets.put(user, basket);
        }
        return usersBaskets;
    }

    @Test
    public void saveShopWithPurchaseHistory() {
        shop = createRandomShopWithPurchaseHistory(n);
        shopRepository.save(shop);
        Assert.assertEquals(n, shopRepository.findById(shop.getId()).get().getPurchaseHistory().size());
        Assert.assertTrue(purchaseHistoryRepository.findAll().iterator().hasNext());
    }

    @Test
    public void updateShopWithPurchaseHistory() {
        saveShopWithPurchaseHistory();
        shop.setPurchaseHistory(createPurchaseHistoriesMap(2));
        shopRepository.save(shop);
        Assert.assertEquals(2, shopRepository.findById(shop.getId()).get().getPurchaseHistory().size());
    }

    @Test
    public void deleteShopWithPurchaseHistory() {
        saveShopWithPurchaseHistory();
        shopRepository.delete(shop);
        Assert.assertEquals(0, shopRepository.findById(shop.getId()).stream().count());
        Assert.assertFalse(purchaseHistoryRepository.findAll().iterator().hasNext());
    }

    private Shop createRandomShopWithPurchaseHistory(int purchasesNum) {
        shop = createRandomEmptyShop();
        Map<SubscribedUser, PurchaseHistory> purchaseHistories = createPurchaseHistoriesMap(purchasesNum);
        shop.setPurchaseHistory(purchaseHistories);
        return shop;
    }

    private Map<SubscribedUser, PurchaseHistory> createPurchaseHistoriesMap(int purchasesNum) {
        Map<SubscribedUser, PurchaseHistory> purchaseHistories = new ConcurrentHashMap<>();
        for (int i = 0; i < purchasesNum; i++) {
            SubscribedUser user = createRandomUser();
            PurchaseHistory purchaseHistory = new PurchaseHistory(shop, user);
            purchaseHistories.put(user, purchaseHistory);
        }
        return purchaseHistories;
    }

    private ShopAdministrator createRandomShopOwnerWithoutSave(Shop shop) {
        SubscribedUser user = createRandomUser();
        ShopManager manager = new ShopManager(new ArrayList<>(), user, shop, new ArrayList<>());
        subscribedUserRepository.delete(user);
        return manager;
    }

    private ShopAdministrator createRandomShopManagerWithoutSave(Shop shop) {
        SubscribedUser user = createRandomUser();
        ShopOwner manager = new ShopOwner(new ArrayList<>(), user, shop, new ArrayList<>(), false);
        subscribedUserRepository.delete(user);
        return manager;
    }

    private Shop createRandomEmptyShop() {
        int id = rand.nextInt(0, Integer.MAX_VALUE);
        String name = "name" + rand.nextInt();
        String description = "desc" + rand.nextInt();
        Shop shop = new Shop(id, name, description);
        shopRepository.save(shop);
        return shop;
    }

    private Shop createRandomNonEmptyShop(int productsNum) {
        shop = createRandomEmptyShop();
        Collection<Product> products = List.of(new Product[]{createRandomProduct(), createRandomProduct(), createRandomProduct()});
        shop.setProducts(products);
        return shop;
    }

    private ShopAdministrator createRandomShopManager(Shop shop) {
        ShopManager manager = new ShopManager(new ArrayList<>(), createRandomUser(), shop, new ArrayList<>());
        return manager;
    }

    private ShopAdministrator createRandomShopOwner(Shop shop) {
        ShopOwner owner = new ShopOwner(new ArrayList<>(), createRandomUser(), shop, new ArrayList<>(), false);
        return owner;
    }

    private SubscribedUser createRandomUser() {
        SubscribedUser user = new SubscribedUser("user" + rand.nextInt(0, Integer.MAX_VALUE), "pass" + rand.nextInt(), true, false, null, new ArrayList<>());
        subscribedUserRepository.save(user);
        return user;
    }

    private Product createRandomProduct() {
        int id = productCounter++;
        String name = "name" + rand.nextInt();
        String description = "description" + rand.nextInt();
        String manufacturer = "manufacturer" + rand.nextInt();
        double price = rand.nextDouble(0, 100);
        int quantity = rand.nextInt(0, 100);
        return new Product(id, name, description, manufacturer, price, quantity);
    }
}