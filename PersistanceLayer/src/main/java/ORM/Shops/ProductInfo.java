package ORM.Shops;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productInfo")
@IdClass(ProductInfo.ProductInfoPKID.class)
public class ProductInfo {
    @Id
    private int productID;
    @Id
    @ManyToOne
    @JoinColumn(name = "transactionID")
    private Purchase purchase;//TODO:check WTF is this
    private int quantity;
    private double price;
    public ProductInfo(int productID, Purchase purchase, int quantity, int price) {
        this.productID = productID;
        this.purchase = purchase;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductInfo(int productID,  int quantity, double price) {
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductInfo() {

    }

    public class ProductInfoPKID implements Serializable {
        private int productID;
        private Purchase purchase;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}
