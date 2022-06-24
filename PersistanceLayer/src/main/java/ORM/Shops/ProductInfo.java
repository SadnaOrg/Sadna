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

    public class ProductInfoPKID implements Serializable {
        private int productID;
        private Purchase purchase;
    }
}
