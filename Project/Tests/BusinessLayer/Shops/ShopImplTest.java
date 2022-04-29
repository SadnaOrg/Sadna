package BusinessLayer.Shops;

import org.junit.Assert;
import org.junit.Test;

public class ShopImplTest {
    @Test
    public void addProduct() {
        Shop s1 = createShopWithProduct();
        Assert.assertEquals(1, s1.getProducts().size());
    }

    @Test
    public void changeProduct() {
        changeProductSuccess();
        changeProductFail();
    }

    private void changeProductFail() {
        Shop s1 = createShopWithProduct();
        Product p2 = createDifferentProduct();
        s1.changeProduct(p2);
        Assert.assertNotEquals(s1.getProducts().get(p2.getID()), p2);
    }

    private void changeProductSuccess() {
        Shop s1 = createShopWithProduct();
        Product p2 = createChangedProduct();
        s1.changeProduct(p2);
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getName(), p2.getName());
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getPrice(), p2.getPrice(), 0.0);
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getQuantity(), p2.getQuantity());
    }

    @Test
    public void removeProduct() {
        Shop s1 = createShop();
        Product p1 = createProduct();
        s1.addProduct(p1);
        s1.removeProduct(p1);
        Assert.assertEquals(0, s1.getProducts().size());
    }

    private Shop createShopWithProduct() {
        Shop s1 = createShop();
        Product p1 = createProduct();
        s1.addProduct(p1);
        return s1;
    }

    private Shop createShop() {
        return new ShopImpl(100, "shop");
    }

    private Product createProduct() {
        return new ProductImpl(1, "a", 5, 100);
    }

    private Product createChangedProduct() {
        return new ProductImpl(1, "b", 10, 10);
    }

    private Product createDifferentProduct() {
        return new ProductImpl(2, "c", 15, 500);
    }

    private Product getProductFromShop(Shop s1, int id) {
        return s1.getProducts().get(id);
    }
}
