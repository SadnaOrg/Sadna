package com.SadnaORM.ShopRepositoriesImpl;

import com.SadnaORM.ShopRepositories.ShopRepository;
import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserRepositories.ShopManagerRepository;
import com.SadnaORM.UserRepositories.ShopOwnerRepository;
import com.SadnaORM.UserRepositories.SubscribedUserRepository;
import com.SadnaORM.Users.ShopAdministrator;
import com.SadnaORM.Users.ShopManager;
import com.SadnaORM.Users.ShopOwner;
import com.SadnaORM.Users.SubscribedUser;
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
    private SubscribedUserRepository subscribedUserRepository;

    @Autowired
    private ShopOwnerRepository shopOwnerRepository;

    @Autowired
    private ShopManagerRepository shopManagerRepository;

    private int productCounter;
    private Random rand = new Random();

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
        Shop shop = createRandomEmptyShop();
        shopRepository.save(shop);
        Assert.assertNotNull(shopRepository.findAll());
    }

    @Test
    public void saveShopWithProducts() {
        Shop shop = createRandomNonEmptyShop();
        shopRepository.save(shop);
        Assert.assertEquals(shopRepository.findById(shop.getId()).get().getProducts().size(), 3);
    }

    @Test
    public void saveShopWithShopAdministrators() {
        Shop shop = createRandomShopWithShopAdministrators();
        shopRepository.save(shop);
        Assert.assertEquals(shopRepository.findById(shop.getId()).get().getShopAdministrators().size(), 3);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveShopWithShopAdministratorsNotExisting() {
        Shop shop = createRandomShopWithShopAdministratorsNotExisting();
        shopRepository.save(shop);
    }

    private Shop createRandomShopWithShopAdministratorsNotExisting() {
        Shop s1 = createRandomEmptyShop();
        Map<SubscribedUser, ShopAdministrator> shopAdministrators = new ConcurrentHashMap<>();
        for (int i = 0; i < 3; i++) {
            SubscribedUser user = createRandomUser();
            List<ShopAdministrator> administrators = new ArrayList<>();
            administrators.add(createRandomShopManagerWithoutSave(s1));
            administrators.add(createRandomShopOwnerWithoutSave(s1));
            administrators.add(createRandomShopManagerWithoutSave(s1));
            ShopAdministrator shopAdministrator = createRandomShopManagerWithoutSave(s1);
            shopAdministrators.put(user, shopAdministrator);
        }
        s1.setShopAdministrators(shopAdministrators);
        return s1;
    }

    private ShopAdministrator createRandomShopOwnerWithoutSave(Shop s1) {
        SubscribedUser user = createRandomUser();
        ShopManager manager = new ShopManager(new ArrayList<>(), user, s1, new ArrayList<>());
        subscribedUserRepository.delete(user);
        return manager;
    }

    private ShopAdministrator createRandomShopManagerWithoutSave(Shop s1) {
        SubscribedUser user = createRandomUser();
        ShopOwner manager = new ShopOwner(new ArrayList<>(), user, s1, new ArrayList<>(), false);
        subscribedUserRepository.delete(user);
        return manager;
    }

    private Shop createRandomEmptyShop() {
        int id = rand.nextInt(0, Integer.MAX_VALUE);
        String name = "name" + rand.nextInt();
        String description = "desc" + rand.nextInt();
        Shop s1 = new Shop(id, name, description);
        shopRepository.save(s1);
        return s1;
    }

    private Shop createRandomNonEmptyShop() {
        Shop s1 = createRandomEmptyShop();
        Collection<Product> products = List.of(new Product[]{createRandomProduct(), createRandomProduct(), createRandomProduct()});
        s1.setProducts(products);
        return s1;
    }

    private Shop createRandomShopWithShopAdministrators() {
        Shop s1 = createRandomEmptyShop();
        Map<SubscribedUser, ShopAdministrator> shopAdministrators = new ConcurrentHashMap<>();
        for (int i = 0; i < 3; i++) {
            SubscribedUser user = createRandomUser();
            List<ShopAdministrator> administrators = new ArrayList<>();
            administrators.add(createRandomShopManager(s1));
            administrators.add(createRandomShopOwner(s1));
            administrators.add(createRandomShopManager(s1));
            ShopAdministrator shopAdministrator = createRandomShopManager(s1);
            shopAdministrators.put(user, shopAdministrator);
        }
        s1.setShopAdministrators(shopAdministrators);
        return s1;
    }

    private ShopAdministrator createRandomShopManager(Shop s1) {
        ShopManager manager = new ShopManager(new ArrayList<>(), createRandomUser(), s1, new ArrayList<>());
        return manager;
    }

    private ShopAdministrator createRandomShopOwner(Shop s1) {
        ShopOwner owner = new ShopOwner(new ArrayList<>(), createRandomUser(), s1, new ArrayList<>(), false);
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