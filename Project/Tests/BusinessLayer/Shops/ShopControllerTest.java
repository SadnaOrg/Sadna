package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ShopControllerTest {

    @Mock
    private Shop s1;

    @Mock
    private Basket basket;

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp(){
    }

    @Test
    public void addShop() {
        addShopHandler();
        Assert.assertEquals(ShopController.getInstance().getShops().get(1), s1);
    }

    @Test
    public void addBasket() {
        addShopHandler();
        when(s1.addBasket("Yuval", basket)).thenReturn(true);
        Assert.assertTrue(ShopController.getInstance().AddBasket(s1.getId(), "Yuval", basket));

    }

    @Test
    public void searchProducts() {
    }

    @Test
    public void purchaseBasket() {
    }

    @Test
    public void addToPurchaseHistory() {
    }

    @Test
    public void checkIfUserHasBasket() {
    }

    @Test
    public void getShops() {
    }

    @Test
    public void reciveInformation() {
    }

    @Test
    public void openShop() {
    }

    private void addShopHandler() {
        when(s1.getId()).thenReturn(1);
        ShopController.getInstance().addShop(s1);
    }
}