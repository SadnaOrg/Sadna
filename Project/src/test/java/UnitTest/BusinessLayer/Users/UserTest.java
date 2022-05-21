package UnitTest.BusinessLayer.Users;


import BusinessLayer.Products.Users.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

    User u;
    private String s = "u_";
    private int i =0;

    @Before
    public void setUp() throws Exception {
        u = getUser();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveProductsNegAmount() {
        getUser().saveProducts(1,1,-1,12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveProductsNegAmountAfterPosAmount() {
        getUser().saveProducts(1,1,1,12);
        getUser().saveProducts(1,1,-1,12);
    }

    @Test
    public void testSaveProductsGoodSenario() {
        testSaveProductsOK(u,1,1,1);
        testSaveProductsFail(u,1,1,3);
        testSaveProductsOK(u,1,2,1);
        testSaveProductsOK(u,2,1,1);
        testSaveProductsOK(u,2,2,1);
        testSaveProductsFail(u,2,2,5);

    }

    public void testSaveProductsOK(User u,int shopId,int productId,int quantity) {
        u.saveProducts(shopId,productId,quantity,15);
        Assert.assertTrue("has the product after adding the product",u.getBasket(shopId)!=null && u.getBasket(shopId).getProducts().containsKey(productId));
        Assert.assertEquals("product dont add up to previous amount", (int) u.getBasket(shopId).getProducts().get(productId), quantity);
    }

    public void testSaveProductsFail(User u,int shopId,int productId,int quantity) {
        try {
            u.saveProducts(shopId, productId, quantity,15);
            Assert.fail("should fail adding product");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void removeproduct() {
        testSaveProductsOK(u,1,1,4);
        u.removeProduct(1,1);
        Assert.assertFalse("product dont removed",u.getBasket(1)!=null&& u.getBasket(1).getProducts().containsKey(1));
    }

    @Test
    public void editProductQuantity() {
        testSaveProductsOK(u,1,1,4);
        u.editProductQuantity(1,1,2);
        Assert.assertTrue("product dont have the new quantity",u.getBasket(1)!=null&& u.getBasket(1).getProducts().getOrDefault(1,null)==2);
    }

    @Test
    public void editProductQuantityFailOnNonExistItem() {
        Assert.assertFalse(u.editProductQuantity(14,1,2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void editProductQuantityFailOnNegNumber() {
        testSaveProductsOK(u,15,1,4);
        u.editProductQuantity(15,1,-2);
    }

    private User getUser(){
        return new User(s+(++i)) {};
    }
}