package com.SadnaORM.ShopImpl.ShopControllers;

import com.SadnaORM.ShopImpl.ShopObjects.ProductDTO;
import com.SadnaORM.ShopImpl.ShopObjects.PurchaseHistoryDTO;
import com.SadnaORM.ShopImpl.ShopObjects.ShopDTO;
import com.SadnaORM.ShopImpl.ShopServices.ShopService;
import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.PurchaseHistory;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserImpl.UserControllers.SubscribedUserController;
import com.SadnaORM.UserImpl.UserObjects.BasketDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopAdministratorDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopOwnerDTO;
import com.SadnaORM.UserImpl.UserObjects.SubscribedUserDTO;
import com.SadnaORM.UserImpl.UserServices.SubscribedUserService;
import com.SadnaORM.Users.Basket;
import com.SadnaORM.Users.ShopAdministrator;
import com.SadnaORM.Users.ShopOwner;
import com.SadnaORM.Users.SubscribedUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService ShopService;
    private Gson gson = new Gson();
    @Autowired
    private SubscribedUserService subscribedUserService = new SubscribedUserService();

    @PostMapping
    public void save(@RequestBody String entity) {
        Shop dalEntity = fromEntity(gson.fromJson(entity, ShopDTO.class));
        ShopService.save(dalEntity);
    }

    @PutMapping
    public void update(@RequestBody String entity) {
        Shop dalEntity = fromEntity(gson.fromJson(entity, ShopDTO.class));
        ShopService.save(dalEntity);
    }

    @DeleteMapping
    public void delete(@RequestBody String entity) {
        Shop dalEntity = fromEntity(gson.fromJson(entity, ShopDTO.class));
        ShopService.delete(dalEntity);
    }

    @GetMapping("/{key}")
    public String findById(@PathVariable Integer key) {
        ShopDTO dalRes = toEntity(ShopService.findById(key));
        return gson.toJson(dalRes);
    }

    @GetMapping
    public String findAll() {
        return gson.toJson(ShopService.findAll().stream().map(e -> toEntity(e)).toList());
    }

    private Shop fromEntity(ShopDTO shopDTO) {
        //TODO: connect initializers
        Collection<Product> products = new ArrayList<>();
        java.util.Map<SubscribedUser, Basket> usersBaskets = new ConcurrentHashMap<>();
        Map<SubscribedUser, PurchaseHistory > purchaseHistory = new ConcurrentHashMap<>();
        Map<SubscribedUser, ShopAdministrator > shopAdministrators = new ConcurrentHashMap<>();
        SubscribedUser subscribedUser = subscribedUserService.findById(shopDTO.getFounder().getUserId());
        Shop.State state = Shop.State.valueOf(shopDTO.getState().toString());
        return new Shop(shopDTO.getId(), shopDTO.getName(), shopDTO.getDescription(), subscribedUser, true,
                state, products, usersBaskets, purchaseHistory, shopAdministrators);
    }

    private ShopDTO toEntity(Shop shop) {
        Collection<ProductDTO> products = new ArrayList<>();
        Map<String, BasketDTO> usersBaskets = new ConcurrentHashMap<>();
        Map<String, PurchaseHistoryDTO> purchaseHistory = new ConcurrentHashMap<>();
        Map<String, ShopAdministratorDTO> shopAdministrators = new ConcurrentHashMap<>();
        ShopOwner owner = shop.getFounder();
        List<ShopAdministratorDTO.BaseActionType> actions = owner.getAction().stream().map(e ->ShopAdministratorDTO.BaseActionType.valueOf(e.name())).toList();
        ShopOwnerDTO founder = new ShopOwnerDTO(actions, owner.getUser().getUsername(), owner.getShop().getId(), true);
        ShopDTO.State state = ShopDTO.State.valueOf(shop.getState().toString());
        return new ShopDTO(shop.getId(), shop.getName(), shop.getDescription(), founder, state,
                products, usersBaskets, purchaseHistory, shopAdministrators);
    }
}
