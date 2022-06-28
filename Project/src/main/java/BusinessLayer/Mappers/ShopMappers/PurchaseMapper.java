package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Products.ProductInfo;
import BusinessLayer.Shops.Purchase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class PurchaseMapper  implements CastEntity <ORM.Shops.Purchase, Purchase>{


    private Func<ProductInfoMapper> productInfoMapper = () -> ProductInfoMapper.getInstance();

    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();

    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();


    static private class PurchaseMapperHolder {
        static final PurchaseMapper mapper = new PurchaseMapper();
    }

    public static PurchaseMapper getInstance(){
        return PurchaseMapper.PurchaseMapperHolder.mapper;
    }

    private PurchaseMapper() {

    }

    @Override
    public ORM.Shops.Purchase toEntity(Purchase entity) {
        Collection<ProductInfo> infos= new ArrayList<>();
        for (int pid: entity.getInfoProducts().keySet())
        {
            infos.add(new ProductInfo(pid,entity.getInfoProducts().get(pid),entity.getProductPrices().get(pid)));
        }
        String pattern = "yyyy-MM-dd";
        DateFormat format = new SimpleDateFormat(pattern);
        ORM.Shops.Purchase purchase = new ORM.Shops.Purchase(entity.getTransectionid(),
                infos.stream().map(productInfo -> productInfoMapper.run().toEntity(productInfo)).collect(Collectors.toList()),
                format.format(entity.getDateOfPurchase()));
        purchase.setUser(subscribedUserMapper.run().findORMById(entity.getUser()));
        purchase.setShop(shopMapper.run().findORMById(entity.getShopid()));
        purchase.getProductInfos().stream().peek(productInfo -> productInfo.setPurchase(purchase));
        return purchase;
    }

    @Override
    public Purchase fromEntity(ORM.Shops.Purchase entity) {
        try {
            return new Purchase(entity.getTransactionid(),
                    entity.getProductInfos().stream().map(productInfo -> productInfoMapper.run().fromEntity(productInfo)).collect(Collectors.toList()),
                    new SimpleDateFormat("yyyy-MM-dd").parse(entity.getDateOfPurchase()),
                    entity.getShop().getId(),
                    entity.getUser().getUsername());
        }
        catch (ParseException ignored)
        {

        }
        return null;
    }
}
