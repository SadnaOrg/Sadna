package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import ORM.DAOs.DBImpl;
import BusinessLayer.Users.SubscribedUser;
import ORM.DAOs.Users.SubscribedUserDAO;
import ORM.Shops.Shop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class SubscribedUserMapper implements DBImpl<SubscribedUser, String>, CastEntity<ORM.Users.SubscribedUser, SubscribedUser> {

    private SubscribedUserDAO dao = new SubscribedUserDAO();
    private Func<PaymentMethodMapper> paymentMethodMapper = () -> PaymentMethodMapper.getInstance();
    private Func<ShopAdministratorMapper> shopAdministratorMapper = () -> ShopAdministratorMapper.getInstance();

    // create the object without the admin matching shop
    public SubscribedUser fromEntityNoAdmin(ORM.Users.SubscribedUser user, Shop shop) {
        return null;
    }

    public ORM.Users.SubscribedUser toEntityNoAdmin(SubscribedUser user, BusinessLayer.Shops.Shop shop) {
        return null;
    }

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

        ORM.Users.SubscribedUser user = new ORM.Users.SubscribedUser(entity.getUserName(), entity.getHashedPassword(), entity.isLoggedIn(),
                !entity.isRemoved(), paymentMethodMapper.run().toEntity(entity.getMethod()),
                entity.getAdministrators().stream().map(admin -> shopAdministratorMapper.run().toEntity(admin)).toList());

        if (user.getPaymentMethod() != null)
            user.getPaymentMethod().setUser(user);
        return user;
    }

    @Override
    public SubscribedUser fromEntity(ORM.Users.SubscribedUser entity) {
        if (entity == null)
            return null;
        SubscribedUser u = new SubscribedUser(entity.getUsername(), entity.isNotRemoved(), entity.getPassword(),
                new ArrayList<>(entity.getAdministrators().stream().map(admin -> shopAdministratorMapper.run().fromEntity(admin)).collect(Collectors.toList())), entity.isIs_login());
        u.updatePaymentMethod(paymentMethodMapper.run().fromEntity(entity.getPaymentMethod()));
        return u;
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

    @Override
    public Collection<SubscribedUser> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }
}
