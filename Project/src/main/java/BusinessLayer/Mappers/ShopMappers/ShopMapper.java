package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.MapperInterface;
import BusinessLayer.Mappers.UserMappers.BasketMapper;
import BusinessLayer.Mappers.UserMappers.ShopOwnerMapper;
import BusinessLayer.Shops.Shop;
import com.SadnaORM.ShopImpl.ShopObjects.ProductDTO;
import com.SadnaORM.ShopImpl.ShopObjects.PurchaseHistoryDTO;
import com.SadnaORM.ShopImpl.ShopObjects.ShopDTO;
import com.SadnaORM.Shops.PurchaseHistory;
import com.SadnaORM.UserImpl.UserObjects.BasketDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopAdministratorDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopOwnerDTO;
import com.SadnaORM.Users.ShopAdministrator;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShopMapper implements MapperInterface<com.SadnaORM.Shops.Shop, ShopDTO ,Shop, Integer> {
    RestTemplate restTemplate = new RestTemplate();
    private String baseURL = "http://localhost:8081/shop";
    private BasketMapper basketMapper = BasketMapper.getInstance();

    private Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                static int shop_idx = 0;
                static int sa_idx = 0;
                public boolean shouldSkipClass(Class<?> clazz) {
                    return clazz == Shop.class || clazz == ShopAdministrator.class || clazz == PurchaseHistory.class;
                }

                /**
                 * Custom field exclusion goes here
                 */
                public boolean shouldSkipField(FieldAttributes f) {
                    return false;
                }

            }).serializeNulls().create();

    static private class ShopMapperHolder {
        static final ShopMapper mapper = new ShopMapper();
    }

    public static ShopMapper getInstance(){
        return ShopMapperHolder.mapper;
    }

    private ShopMapper() {

    }

    @Override
    public void save(Shop entity) {
        String dalEntity = gson.toJson(toEntity(entity));
        HttpEntity<String> httpEntity = new HttpEntity<>(dalEntity);
        restTemplate.postForEntity(baseURL, httpEntity, Void.class);
    }

    @Override
    public void delete(Shop entity) {
        String dalEntity = gson.toJson(toEntity(entity));
        HttpEntity<String> httpEntity = new HttpEntity<>(dalEntity);
        restTemplate.delete(baseURL + entity.getId(), httpEntity);
    }

    @Override
    public ShopDTO toEntity(Shop entity) {
        //TODO: initialize maps correctly
        ShopOwnerDTO founder = ShopOwnerMapper.getInstance().toEntity(entity.getFounder(), entity);
        Map<String, BasketDTO> usersBaskets =
                entity.getUsersBaskets().entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey(),
                                e -> basketMapper.toEntity(e.getValue(), e.getKey())));
        Map<String, PurchaseHistoryDTO> purchaseHistory = new ConcurrentHashMap<>();
        Map<String, ShopAdministratorDTO> shopAdministrators = new ConcurrentHashMap<>();
        Collection<ProductDTO> products = new ArrayList<>();
        return new ShopDTO(entity.getId(), entity.getName(), entity.getDescription(), founder,
                com.SadnaORM.ShopImpl.ShopObjects.ShopDTO.State.valueOf(entity.getState().toString()),
                products, usersBaskets, purchaseHistory, shopAdministrators);
    }

    @Override
    public Shop FromEntity(ShopDTO entity) {
        //TODO: initialize maps correctly
        ConcurrentHashMap<Integer, BusinessLayer.Products.Product> products = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, BusinessLayer.Users.Basket> usersBaskets = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, BusinessLayer.Shops.PurchaseHistory > purchaseHistory = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, BusinessLayer.Users.ShopAdministrator > shopAdministrators = new ConcurrentHashMap<>();
        return new Shop(entity.getId(), entity.getName(), entity.getDescription(),
                Shop.State.valueOf(entity.getState().toString()), ShopOwnerMapper.getInstance().FromEntity(entity.getFounder()),
                products, usersBaskets, purchaseHistory, shopAdministrators);
    }

    @Override
    public Shop findByID(Integer key) {
        String dalRes = restTemplate.getForObject("http://localhost:8081/shop/" + key, String.class);
        return FromEntity(gson.fromJson(dalRes, ShopDTO.class));
    }

    public List<Shop> findAll() {
        ShopDTO[] dalRes = gson.fromJson(restTemplate.getForObject("http://localhost:8081/shop", String.class), ShopDTO[].class);
        List<Shop> shops = Arrays.stream(dalRes).map(e -> FromEntity(e)).toList();
        return shops;
    }
}
