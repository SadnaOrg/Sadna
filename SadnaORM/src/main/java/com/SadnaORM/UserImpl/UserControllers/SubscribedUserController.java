package com.SadnaORM.UserImpl.UserControllers;

import com.SadnaORM.ShopImpl.ShopObjects.ShopDTO;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserImpl.UserObjects.BasketDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopAdministratorDTO;
import com.SadnaORM.UserImpl.UserObjects.SubscribedUserDTO;
import com.SadnaORM.UserImpl.UserServices.SubscribedUserService;
import com.SadnaORM.Users.Basket;
import com.SadnaORM.Users.ShopAdministrator;
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
@RequestMapping("/subscribedUser")
public class SubscribedUserController {
    @Autowired
    private SubscribedUserService subscribedUserService;
    Gson gson = new Gson();

    @PostMapping
    public void save(@RequestBody String entity) {
        //String id = entity.getUsername();
        SubscribedUser dalEntity = fromEntity(gson.fromJson(entity, SubscribedUserDTO.class));
        subscribedUserService.save(dalEntity);
    }

    @PutMapping
    public void update(@RequestBody String entity) {
        SubscribedUser dalEntity = fromEntity(gson.fromJson(entity, SubscribedUserDTO.class));
        subscribedUserService.save(dalEntity);
    }

    @DeleteMapping
    public void delete(@RequestBody String entity) {
        SubscribedUser dalEntity = fromEntity(gson.fromJson(entity, SubscribedUserDTO.class));
        subscribedUserService.delete(dalEntity);
    }

    @GetMapping("/{key}")
    public String findById(@PathVariable String key) {
        SubscribedUserDTO dalRes = toEntity(subscribedUserService.findById(key));
        String dalGson = gson.toJson(dalRes);
        return dalGson;
    }

    @GetMapping
    public Collection<SubscribedUser> findAll() {
        return subscribedUserService.findAll();
    }

    private SubscribedUser fromEntity(SubscribedUserDTO entityDTO) {
        return new SubscribedUser(entityDTO.getUsername(), entityDTO.getPassword(), false, entityDTO.isNotRemoved(),
                null, null, null);
    }

    private SubscribedUserDTO toEntity(SubscribedUser entity) {
        if (entity == null)
                return null;
        List<ShopAdministratorDTO> shopAdministrators = new ArrayList<>();
        Map<Integer, BasketDTO> userBaskets = new ConcurrentHashMap<>();
        return new SubscribedUserDTO(entity.getUsername(), entity.getPassword(), false, entity.isNotRemoved(),
                null, shopAdministrators, userBaskets);
    }
}
