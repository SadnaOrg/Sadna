package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.System.PaymentMethod;
import ORM.Users.SubscribedUser;

public class PaymentMethodMapper implements CastEntity<ORM.Users.PaymentMethod, PaymentMethod> {
    static private class PaymentMethodMapperHolder {
        static final PaymentMethodMapper mapper = new PaymentMethodMapper();
    }

    public static PaymentMethodMapper getInstance(){
        return PaymentMethodMapper.PaymentMethodMapperHolder.mapper;
    }

    private PaymentMethodMapper() {

    }

    @Override
    public ORM.Users.PaymentMethod toEntity(PaymentMethod entity) {
        if (entity == null)
            return null;
        return new ORM.Users.PaymentMethod(entity.getCreditCardNumber(), entity.getCVV().get(), entity.getExpiryYear().get(),
                entity.getExpiryMonth().get());
    }

    @Override
    public PaymentMethod fromEntity(ORM.Users.PaymentMethod entity) {
        if (entity == null)
            return null;
        return new PaymentMethod(entity.getCreditCard(), entity.getCVV(), entity.getExpirationYear(), entity.getExpirationDay());
    }
}
