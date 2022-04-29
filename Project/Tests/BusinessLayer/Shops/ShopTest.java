package BusinessLayer.Shops;

import BusinessLayer.Products.ProductImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class ShopTest {
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

    @Test
    public void removeProduct() {
        Shop s1 = createShop();
        ProductImpl p1 = createProduct();
        s1.addProduct(p1);
        s1.removeProduct(p1);
        Assert.assertEquals(0, s1.getProducts().size());
    }

    @Test
    public void searchProducts() {
        Shop s = createShopWithTwoProducts();

    }

    @Test
    public void purchaseBasket() {
        purchaseBasketSuccess();
        purchaseBasketFail1();
        purchaseBasketFail12();
    }

    private void purchaseBasketSuccess() {
        Shop s1 = createShopWithTwoProducts();
        ConcurrentHashMap<Integer, Integer> basket = BasketWithTwoProductsSuccess();
        Assert.assertNotEquals(0.0, s1.purchaseBasket(basket),0.0);
    }

    private void purchaseBasketFail1() {
        Shop s1 = createShopWithTwoProducts();
        ConcurrentHashMap<Integer, Integer> basket = BasketWithTwoProductsFail1();
        Assert.assertEquals(0.0, s1.purchaseBasket(basket),0.0);
    }

    private void purchaseBasketFail12() {
        Shop s1 = createShopWithTwoProducts();
        ConcurrentHashMap<Integer, Integer> basket = BasketWithTwoProductsFail2();
        Assert.assertEquals(0.0, s1.purchaseBasket(basket),0.0);
    }

    private void changeProductFail() {
        Shop s1 = createShopWithProduct();
        ProductImpl p2 = createDifferentProduct();
        s1.changeProduct(p2);
        Assert.assertNotEquals(s1.getProducts().get(p2.getID()), p2);
    }

    private void changeProductSuccess() {
        Shop s1 = createShopWithProduct();
        ProductImpl p2 = createChangedProduct();
        s1.changeProduct(p2);
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getName(), p2.getName());
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getPrice(), p2.getPrice(), 0.0);
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getQuantity(), p2.getQuantity());
    }

    private Shop createShopWithProduct() {
        Shop s1 = createShop();
        ProductImpl p1 = createProduct();
        s1.addProduct(p1);
        return s1;
    }

    private Shop createShopWithTwoProducts() {
        Shop s1 = createShop();
        ProductImpl p1 = createProduct();
        ProductImpl p2 = createDifferentProduct();
        s1.addProduct(p1);
        s1.addProduct(p2);
        return s1;
    }

    private Shop createShop() {
        return new Shop(100, "shop");
    }

    private ProductImpl createProduct() {
        return new ProductImpl(1, "a", 5, 100);
    }

    private ProductImpl createChangedProduct() {
        return new ProductImpl(1, "b", 10, 10);
    }

    private ProductImpl createDifferentProduct() {
        return new ProductImpl(2, "c", 15, 500);
    }

    private ProductImpl getProductFromShop(Shop s1, int id) {
        return s1.getProducts().get(id);
    }

    private ConcurrentHashMap<Integer, Integer> BasketWithTwoProductsSuccess() {
        ConcurrentHashMap<Integer, Integer> basket = new ConcurrentHashMap<>();
        basket.put(1, 10);
        basket.put(2, 100);
        return basket;
    }

    private ConcurrentHashMap<Integer, Integer> BasketWithTwoProductsFail1() {
        ConcurrentHashMap<Integer, Integer> basket = new ConcurrentHashMap<>();
        basket.put(1, 200);
        basket.put(2, 100);
        return basket;
    }

    private ConcurrentHashMap<Integer, Integer> BasketWithTwoProductsFail2() {
        ConcurrentHashMap<Integer, Integer> basket = new ConcurrentHashMap<>();
        basket.put(1, 10);
        basket.put(2, 1000);
        return basket;
    }
}
