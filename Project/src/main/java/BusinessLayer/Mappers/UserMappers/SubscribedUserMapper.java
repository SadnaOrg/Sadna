package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.MapperInterface;
import BusinessLayer.Users.SubscribedUser;
import com.SadnaORM.UserImpl.UserObjects.PaymentMethodDTO;
import com.SadnaORM.UserImpl.UserObjects.SubscribedUserDTO;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class SubscribedUserMapper implements MapperInterface<com.SadnaORM.Users.SubscribedUser, SubscribedUserDTO, SubscribedUser, String> {
    RestTemplate restTemplate = new RestTemplate();
    private String baseURL = "http://localhost:8081/subscribedUser";
    Gson gson = new Gson();
    static private class SubscribedUserMapperHolder {
        static final SubscribedUserMapper mapper = new SubscribedUserMapper();
    }

    public static SubscribedUserMapper getInstance(){
        return SubscribedUserMapperHolder.mapper;
    }

    private SubscribedUserMapper() {

    }

    @Override
    public void save(SubscribedUser entity) {
        String dalEntity = gson.toJson(toEntity(entity));
        HttpEntity<String> httpEntity = new HttpEntity<>(dalEntity);
        restTemplate.postForEntity(baseURL, httpEntity, Void.class);
    }

    @Override
    public void delete(SubscribedUser entity) {
        String dalEntity = gson.toJson(toEntity(entity));
        HttpEntity<String> httpEntity = new HttpEntity<>(dalEntity);
        restTemplate.delete(baseURL + entity.getUserName(), httpEntity);
    }

    @Override
    public SubscribedUserDTO toEntity(SubscribedUser entity) {
        return new SubscribedUserDTO(entity.getUserName(), entity.getHashedPassword(), entity.isLoggedIn(), !entity.isRemoved(), new PaymentMethodDTO());
    }

    @Override
    public SubscribedUser FromEntity(SubscribedUserDTO entity) {
        if (entity == null)
            return null;
        return new SubscribedUser(entity.getUsername(), entity.isNotRemoved(), entity.getPassword(), new ArrayList<>(), entity.isIs_login());
    }

    @Override
    public SubscribedUser findByID(String key) {
        String dalRes = restTemplate.getForObject("http://localhost:8081/subscribedUser/" + key, String.class);
        SubscribedUserDTO user = gson.fromJson(dalRes, SubscribedUserDTO.class);
        return FromEntity(user);
    }
}
