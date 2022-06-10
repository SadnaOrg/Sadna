//package BusinessLayer.Shops.Polices.Discount;
//
//import BusinessLayer.Users.Basket;
//
//import java.util.Collection;
//
//public class DiscountGriraPolicy implements LogicDiscountRules{
//    private int connectId;
//
//    private Collection<DiscountPred> discountPreds;
//    private DiscountRules discountPolicy;
//
//    public DiscountGriraPolicy(Collection<DiscountPred> discountPreds, DiscountRules discountPolicy) {
//        this.discountPreds = discountPreds;
//        this.discountPolicy = discountPolicy;
//        this.connectId = atomicconnectId.incrementAndGet();
//    }
//
//    @Override
//    public double calculateDiscount(Basket basket) {
//        for (DiscountPred discountPred: discountPreds) {
//            if (!discountPred.validateDiscount(basket))
//                return 0;
//        }
//        return discountPolicy.calculateDiscount(basket);
//    }
//
//    @Override
//    public void add(DiscountPred discountPred) {
//        discountPreds.add(discountPred);
//    }
//    @Override
//    public boolean remove(DiscountPred discountPred) {
//        return discountPreds.remove(discountPred);
//    }
//
//
//    public NumericDiscountRules getNumericRule(int searchConnectId) {
//        return discountPolicy.getNumericRule(searchConnectId);
//    }
//
//    public LogicDiscountRules getLogicRule(int searchConnectId)
//    {
//        if(this.connectId == searchConnectId)
//            return this;
//        return discountPolicy.getLogicRule(searchConnectId);
//    }
//}
