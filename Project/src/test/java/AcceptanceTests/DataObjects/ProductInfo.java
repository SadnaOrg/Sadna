package AcceptanceTests.DataObjects;

public class ProductInfo {
    public int shopId;
    public int Id;
    public int quantity;
    public double price;

    public ProductInfo(ServiceLayer.Objects.ProductInfo productInfo){
        this.Id = productInfo.Id();
        this.price = productInfo.price();
        this.quantity = productInfo.quantity();
        this.price = productInfo.price();
    }
}
