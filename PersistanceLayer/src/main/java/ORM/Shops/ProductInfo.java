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
    private Purchase purchase;
    private int quantity;
    private int price;
    public ProductInfo(int productID, Purchase purchase, int quantity, int price) {
        this.productID = productID;
        this.purchase = purchase;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductInfo() {

    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public class ProductInfoPKID implements Serializable {
        private int productID;
        private Purchase purchase;
    }
}
