package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;

import java.util.Collection;

public class HistoryInfo extends BaseAction {
      public HistoryInfo(Shop shop)
      {
          super(shop);
      }


      public Collection<PurchaseHistory> act()
      {
          return shop.getPurchaseHistory();
      }


}
