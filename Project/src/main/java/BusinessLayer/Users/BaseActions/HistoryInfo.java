package main.java.BusinessLayer.Users.BaseActions;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.BusinessLayer.Shops.PurchaseHistoryController;
import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Shops.ShopController;
import main.java.BusinessLayer.System.System;
import main.java.BusinessLayer.Users.BaseActions.BaseAction;

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
