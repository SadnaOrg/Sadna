package com.SadnaORM.ShopRepositoriesImpl;

import com.SadnaORM.ShopRepositories.ShopRepository;
import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.Shop;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShopRepositoryTest {
    @Autowired
    private ShopRepository shopRepository;
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

    private Shop createRandomEmptyShop() {
        int id = rand.nextInt();
        String name = "name" + rand.nextInt();
        String description = "desc" + rand.nextInt();
        return new Shop(id, name, description);
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
            ShopAdministrator shopAdministrator = new ShopManager(new ArrayList<>(), user, s1, administrators);
            shopAdministrators.put(user, shopAdministrator);
        }
        s1.setShopAdministrators(shopAdministrators);
        return s1;
    }

    private ShopAdministrator createRandomShopManager(Shop s1) {
        return new ShopManager(new ArrayList<>(), createRandomUser(), s1, new ArrayList<>());
    }

    private ShopAdministrator createRandomShopOwner(Shop s1) {
        return new ShopOwner(new ArrayList<>(), createRandomUser(), s1, new ArrayList<>(), false);
    }

    private SubscribedUser createRandomUser() {
        return new SubscribedUser("user" + rand.nextInt(), "pass" + rand.nextInt());
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