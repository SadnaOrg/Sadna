package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Shops.Polices.Discount.ShopDiscount;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.Guest;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class PurchaseOrPolicyTest {

    private PurchaseOrPolicy purchaseOrPolicy;
    private ValidateProductPurchase validateProductPurchase;
    private ValidateUserPurchase validateUserPurchase;
    private User u;
    private Basket basket;

    @Before
    public void setUp() throws Exception {
        u = new SubscribedUser("Meower","meow", new Date(2001, Calendar.DECEMBER,1));
        basket = new Basket(1);
        basket.saveProducts(1,10,5,"meow");
        basket.saveProducts(2,100,15,"meow");
    }

    @Test
    public void onePredSuccess()
    {
        validateProductPurchase= new ValidateProductPurchase(1,20,true);
        purchaseOrPolicy = new PurchaseOrPolicy(validateProductPurchase);
        Assert.assertTrue(purchaseOrPolicy.isValid(u,basket));
    }

    @Test
    public void onePredFail()
    {
        validateProductPurchase= new ValidateProductPurchase(1,5,false);
        purchaseOrPolicy = new PurchaseOrPolicy(validateProductPurchase);
        Assert.assertTrue(purchaseOrPolicy.isValid(u,basket));
    }

    @Test
    public void twoPredBothFail()
    {
        validateUserPurchase = new ValidateUserPurchase(50);
        validateProductPurchase= new ValidateProductPurchase(1,11, false);
        purchaseOrPolicy = new PurchaseOrPolicy(validateProductPurchase);
        purchaseOrPolicy.add(validateUserPurchase);
        Assert.assertFalse(purchaseOrPolicy.isValid(u,basket));
    }

    @Test
    public void twoPredOneFailOneGood()
    {
        validateUserPurchase = new ValidateUserPurchase(10);
        validateProductPurchase= new ValidateProductPurchase(1,11, false);
        purchaseOrPolicy = new PurchaseOrPolicy(validateProductPurchase);
        purchaseOrPolicy.add(validateUserPurchase);
        Assert.assertTrue(purchaseOrPolicy.isValid(u,basket));
    }

    @Test
    public void twoPredBothGood()
    {
        validateUserPurchase = new ValidateUserPurchase(10);
        validateProductPurchase= new ValidateProductPurchase(1,11, true);
        purchaseOrPolicy = new PurchaseOrPolicy(validateProductPurchase);
        purchaseOrPolicy.add(validateUserPurchase);
        Assert.assertTrue(purchaseOrPolicy.isValid(u,basket));
    }
}