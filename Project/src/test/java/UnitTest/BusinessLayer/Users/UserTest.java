package UnitTest.BusinessLayer.Users;

import BusinessLayer.Users.User;
import ServiceLayer.Objects.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UserTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSaveProductsNegAmount(User u,int shopId,int productId,int quantity) {
        u.saveProducts(shopId,productId,-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSaveProductsNegAmount(User u,int shopId,int productId,int quantity) {
        u.saveProducts(shopId,productId,-1);
    }


    @Test
    public void showCart() {
    }

    @Test
    public void removeproduct() {
    }

    @Test
    public void editProductQuantity() {
    }

    @Test
    public void getShoppingCart() {
    }

    @Test
    public void reciveInformation() {
    }

    @Test
    public void getBasket() {
    }

    @Test
    public void updatePaymentMethod() {
    }
}