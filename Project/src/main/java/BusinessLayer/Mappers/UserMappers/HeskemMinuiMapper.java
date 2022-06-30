package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.HeskemMinui;
import ORM.DAOs.Shops.HeskemMinuiDAO;
import ORM.Users.AppointApprovalStatus;

import java.util.ArrayList;

public class HeskemMinuiMapper implements CastEntity<ORM.Shops.HeskemMinui, HeskemMinui> {
    private HeskemMinuiDAO dao = new HeskemMinuiDAO();
    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();
    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();

    static private class HeskemMinuiMapperHolder {

        static final HeskemMinuiMapper mapper = new HeskemMinuiMapper();

    }

    public static HeskemMinuiMapper getInstance() {
        return HeskemMinuiMapper.HeskemMinuiMapperHolder.mapper;
    }

    @Override
    public ORM.Shops.HeskemMinui toEntity(HeskemMinui entity) {
        ORM.Shops.HeskemMinui ormHeskemMinui = findORMById(entity.getShopId(), entity.getAdminToAssign());
        if (ormHeskemMinui == null) {
            ORM.Shops.HeskemMinui heskemMinui = new ORM.Shops.HeskemMinui(shopMapper.run().findORMById(entity.getShopId()),
                    subscribedUserMapper.run().findORMById(entity.getAdminToAssign()),
                    subscribedUserMapper.run().findORMById(entity.getAppointer()), new ArrayList<>());
            heskemMinui.getApprovals().clear();
            heskemMinui.getApprovals().clear();
            for (String user : entity.getApprovals().keySet()) {
                heskemMinui.getApprovals().add(new AppointApprovalStatus(user, entity.getApprovals().get(user)));
            }
            return heskemMinui;
        }
        else {
            ormHeskemMinui.getApprovals().clear();
            for (String user : entity.getApprovals().keySet()) {
                ormHeskemMinui.getApprovals().add(new AppointApprovalStatus(user, entity.getApprovals().get(user)));
            }
        }
        return ormHeskemMinui;
    }

    @Override
    public HeskemMinui fromEntity(ORM.Shops.HeskemMinui entity) {
        HeskemMinui heskem = new HeskemMinui(entity.getShop().getId(), entity.getAdminToAssign().getUsername(),
                entity.getAppointer().getUsername(), new ArrayList<>());
        heskem.getApprovals().clear();
        for (AppointApprovalStatus approval : entity.getApprovals()) {
            heskem.getApprovals().put(approval.getAdmin(), approval.isStatus());
        }
        return heskem;
    }

    public ORM.Shops.HeskemMinui findORMById(int shopID, String adminToAssign) {
        return dao.findById(new ORM.Shops.HeskemMinui.HeskemMinuiPK(shopMapper.run().findORMById(shopID),
                subscribedUserMapper.run().findORMById(adminToAssign)));
    }
}
