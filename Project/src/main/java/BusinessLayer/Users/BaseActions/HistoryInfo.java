package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;

import java.util.Collection;

public class HistoryInfo extends BaseAction {
      private Shop shop;
      public HistoryInfo(Shop shop)
      {
          this.shop= shop;
      }


      public Collection<PurchaseHistory> act()
      {
          return shop.getPurchaseHistory();
      }


}
