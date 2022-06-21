package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class PurchaseAndPolicyTest {


    private PurchaseAndPolicy purchaseAndPolicy;
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
        purchaseAndPolicy = new PurchaseAndPolicy(validateProductPurchase);
        Assert.assertTrue(purchaseAndPolicy.isValid(u,basket));
    }

    @Test
    public void onePredFail()
    {
        validateProductPurchase= new ValidateProductPurchase(1,5,false);
        purchaseAndPolicy = new PurchaseAndPolicy(validateProductPurchase);
        Assert.assertTrue(purchaseAndPolicy.isValid(u,basket));
    }

    @Test
    public void twoPredBothFail()
    {
        validateUserPurchase = new ValidateUserPurchase(50);
        validateProductPurchase= new ValidateProductPurchase(1,11, false);
        purchaseAndPolicy = new PurchaseAndPolicy(validateProductPurchase);
        purchaseAndPolicy.add(validateUserPurchase);
        Assert.assertFalse(purchaseAndPolicy.isValid(u,basket));
    }

    @Test
    public void twoPredOneFailOneGood()
    {
        validateUserPurchase = new ValidateUserPurchase(10);
        validateProductPurchase= new ValidateProductPurchase(1,11, false);
        purchaseAndPolicy = new PurchaseAndPolicy(validateProductPurchase);
        purchaseAndPolicy.add(validateUserPurchase);
        Assert.assertFalse(purchaseAndPolicy.isValid(u,basket));
    }

    @Test
    public void twoPredBothGood()
    {
        validateUserPurchase = new ValidateUserPurchase(10);
        validateProductPurchase= new ValidateProductPurchase(1,11, true);
        purchaseAndPolicy = new PurchaseAndPolicy(validateProductPurchase);
        purchaseAndPolicy.add(validateUserPurchase);
        Assert.assertTrue(purchaseAndPolicy.isValid(u,basket));
    }

}