package BusinessLayer.Products;

public class ProductInfo {

    private int productid;
    private String productname;
    private String productdescription;
    private double productprice;
    private int productquantity;


    public ProductInfo(Product p)
    {
        this.productid = p.getID();
        this.productname = p.getName();
        this.productdescription = p.getDescription();
        this.productprice = p.getPrice();
        this.productquantity = p.getQuantity();
    }

    public ProductInfo getProductInfo()
    {
        return this;
    }


    public int getProductid() {
        return productid;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public double getProductprice() {
        return productprice;
    }

    public int getProductquantity() {
        return productquantity;
    }
}