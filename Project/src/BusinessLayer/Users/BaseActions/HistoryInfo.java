package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.System.System;
import BusinessLayer.Users.BaseActions.BaseAction;

import java.util.Collection;

public class HistoryInfo extends BaseAction {
      private Shop shop;
      public HistoryInfo(Shop shop)
      {
          this.shop= shop;
      }


      public Collection<PurchaseHistory> act()
      {
          return PurchaseHistoryController.getInstance().getPurchaseInfo(shop.getId());
      }


}
