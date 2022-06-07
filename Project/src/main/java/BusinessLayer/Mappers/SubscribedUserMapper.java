package BusinessLayer.Mappers;

import BusinessLayer.Users.SubscribedUser;

public class SubscribedUserMapper implements MapperInterface<com.SadnaORM.Users.SubscribedUser, SubscribedUser, String>{
    private SubscribedUserMapper mapper = new SubscribedUserMapper();

    public SubscribedUserMapper getInstance(){
        return this.mapper;
    }

    @Override
    public void save(SubscribedUser entity) {

    }

    @Override
    public void delete(SubscribedUser entity) {

    }

    @Override
    public com.SadnaORM.Users.SubscribedUser toEntity(SubscribedUser entity) {
        return null;
    }

    @Override
    public SubscribedUser FromEntity(com.SadnaORM.Users.SubscribedUser entity) {
        return null;
    }

    @Override
    public SubscribedUser findByID(String key) {
        return null;
    }
}
