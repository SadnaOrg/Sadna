package BusinessLayer.Products;

public class ProductInfo {

    private int productid;
    private String productname;
    private String productdescription;
    private double productprice;
    private int productquantity;
    private String manufacturer;
    private String category;


    public ProductInfo(Product p)
    {
        this.productid = p.getID();
        this.productname = p.getName();
        this.productdescription = p.getDescription();
        this.productprice = p.getPrice();
        this.productquantity = p.getQuantity();
        this.manufacturer = p.getManufacturer();
        this.category= p.getCategory();

    }

    public ProductInfo(int productid, int productquantity, double productprice) {
        this.productid = productid;
        this.productprice = productprice;
        this.productquantity = productquantity;
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

    public String getManufacturer(){return this.manufacturer;}

    public String getCategory() {
        return category;
    }
}
