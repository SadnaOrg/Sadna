package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Users.ShopAdministrator;
import ORM.DAOs.DBImpl;
import BusinessLayer.Users.SubscribedUser;
import ORM.DAOs.Users.SubscribedUserDAO;

import java.util.Collection;
import java.util.Objects;

public class SubscribedUserMapper implements DBImpl<SubscribedUser, String>, CastEntity<ORM.Users.SubscribedUser, SubscribedUser> {

    private SubscribedUserDAO dao = new SubscribedUserDAO();
    private Func<PaymentMethodMapper> paymentMethodMapper = () -> PaymentMethodMapper.getInstance();
    private Func<ShopAdministratorMapper> shopAdministratorMapper = () -> ShopAdministratorMapper.getInstance();
    static private class SubscribedUserMapperHolder {
        static final SubscribedUserMapper mapper = new SubscribedUserMapper();
    }

    public static SubscribedUserMapper getInstance(){
        return SubscribedUserMapperHolder.mapper;
    }

    private SubscribedUserMapper() {

    }

    @Override
    public ORM.Users.SubscribedUser toEntity(SubscribedUser entity) {
        if (entity == null)
            return null;

        ORM.Users.SubscribedUser user = findORMById(entity.getUserName());

        for (ShopAdministrator admin : entity.getAdministrators()) {
            if (admin.getUserName() != entity.getUserName()) {
                ORM.Users.ShopAdministrator ormAdmin = shopAdministratorMapper.run().toEntity(admin);
                ormAdmin.setUser(findORMById(admin.getUserName()));
                user.getAdministrators().add(ormAdmin);
            }
        }
        if (user.getPaymentMethod() != null)
            user.getPaymentMethod().setUser(user);
        return user;
    }

    @Override
    public SubscribedUser fromEntity(ORM.Users.SubscribedUser entity) {
        if (entity == null)
            return null;
        return new SubscribedUser(entity.getUsername(), entity.isNotRemoved(), entity.getPassword(),
                entity.getAdministrators().stream().map(admin -> shopAdministratorMapper.run().fromEntity(admin)).toList(), entity.isIs_login());
    }

    @Override
    public void save(SubscribedUser entity) {
        dao.save(toEntity(entity));
    }

    @Override
    public void update(SubscribedUser entity) {
        dao.update(toEntity(entity));
    }

    @Override
    public void delete(String key) {
        dao.delete(key);
    }

    @Override
    public SubscribedUser findById(String key) {
        return fromEntity(dao.findById(key));
    }

    public ORM.Users.SubscribedUser findORMById(String key) {
        return dao.findById(key);
    }

    @Override
    public Collection<SubscribedUser> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }
}
