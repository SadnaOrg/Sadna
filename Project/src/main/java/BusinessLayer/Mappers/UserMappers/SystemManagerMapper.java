package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import ORM.DAOs.DBImpl;
import BusinessLayer.Users.SystemManager;
import ORM.DAOs.Users.SystemManagerDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class SystemManagerMapper implements DBImpl<SystemManager, String>, CastEntity<ORM.Users.SystemManager, SystemManager> {

    private SystemManagerDAO dao = new SystemManagerDAO();
    private Func<PaymentMethodMapper> paymentMethodMapper = () -> PaymentMethodMapper.getInstance();
    private Func<ShopAdministratorMapper> shopAdministratorMapper = () -> ShopAdministratorMapper.getInstance();
    static private class SystemManagerMapperHolder {
        static final SystemManagerMapper mapper = new SystemManagerMapper();
    }

    public static SystemManagerMapper getInstance(){
        return SystemManagerMapperHolder.mapper;
    }

    private SystemManagerMapper() {

    }

    @Override
    public ORM.Users.SystemManager toEntity(SystemManager entity) {
        if (entity == null)
            return null;

        String pattern = "yyyy-MM-dd";
        DateFormat format = new SimpleDateFormat(pattern);
        ORM.Users.SystemManager user = new ORM.Users.SystemManager(entity.getUserName(), entity.getHashedPassword(),
                format.format(entity.getBirthDate()), entity.isLoggedIn(), !entity.isRemoved(),
                paymentMethodMapper.run().toEntity(entity.getMethod()));

        if (user.getPaymentMethod() != null)
            user.getPaymentMethod().setUser(user);
        return user;
    }

    @Override
    public SystemManager fromEntity(ORM.Users.SystemManager entity) {
        if (entity == null)
            return null;
        return new SystemManager(entity.getUsername(), entity.isNotRemoved(), entity.getPassword(),
                new ArrayList<>(), entity.isIs_login(), entity.getDate());
    }

    @Override
    public void save(SystemManager entity) {
        dao.save(toEntity(entity));
    }

    @Override
    public void update(SystemManager entity) {
        dao.update(toEntity(entity));
    }

    @Override
    public void delete(String key) {
        dao.delete(key);
    }

    @Override
    public SystemManager findById(String key) {
        return fromEntity(dao.findById(key));
    }

    @Override
    public Collection<SystemManager> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }
}
