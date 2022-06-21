package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Shops.Polices.Discount.*;
import BusinessLayer.Shops.Polices.Purchase.*;
import com.SadnaORM.ShopImpl.ShopObjects.Discounts.*;
import com.SadnaORM.ShopImpl.ShopObjects.Policies.*;

import java.util.Collection;
import java.util.stream.Collectors;

// This class converts the policies and discounts to dto for shop mapper.
// The shop id has to be filled by shop.
public class ConverterImpl implements Converter {
    @Override
    public DiscountPolicyDTO convert(DiscountAndPolicy p) {
        Collection<DiscountPredDTO> myPreds = toPredDTOs(p.getDiscountPreds());
        return new AndPolicyDTO(p.getID(),-1,myPreds, p.getDiscountPolicy().conversion(this));
    }

    @Override
    public DiscountPolicyDTO convert(DiscountMaxPolicy p) {
        Collection<DiscountPolicyDTO> myPolicies = toDiscountPolicyDTOs(p.getDiscountPolicies());
        return new MaxDiscountPolicyDTO(p.getID(),-1,myPolicies);
    }

    @Override
    public DiscountPolicyDTO convert(DiscountOrPolicy p) {
        Collection<DiscountPredDTO> myPreds = toPredDTOs(p.getDiscountPreds());
        return new OrDiscountDTO(p.getID(),-1,p.getDiscountPolicy().conversion(this),myPreds);
    }

    @Override
    public DiscountPolicyDTO convert(DiscountPlusPolicy p) {
        Collection<DiscountPolicyDTO> myPolicies = toDiscountPolicyDTOs(p.getDiscountPolicies());
        return new DiscountPlusPolicyDTO(p.getID(),-1,myPolicies);
    }

    @Override
    public DiscountPolicyDTO convert(DiscountXorPolicy p) {
        DiscountPolicyDTO p1 = p.getDiscountRules1().conversion(this);
        DiscountPolicyDTO p2 = p.getDiscountRules2().conversion(this);
        Collection<DiscountPredDTO> tieBreakers = toPredDTOs(p.getTieBreakers());
        return new XorDiscountDTO(p.getID(),-1,p1, p2, tieBreakers);
    }

    @Override
    public DiscountPolicyDTO convert(ProductByQuantityDiscount p) {
        return new ProductByQuantityDiscountDTO(p.getID(),-1,p.getProductId(),p.getProductQuantity(),p.getDiscount());
    }

    @Override
    public DiscountPolicyDTO convert(ProductDiscount p) {
        return new ProductDiscountDTO(p.getID(),-1,p.getProductId(),p.getDiscount());
    }

    @Override
    public DiscountPolicyDTO convert(ProductQuantityInPriceDiscount p) {
        return new ProductQuantityInPriceDiscountDTO(p.getID(),-1,p.getProductID(),p.getQuantity(),p.getPriceForQuantity());
    }

    @Override
    public DiscountPolicyDTO convert(RelatedGroupDiscount p) {
        return new RelatedGroupDiscountDTO(p.getID(),-1,p.getCategory(),p.getDiscount());
    }

    @Override
    public DiscountPolicyDTO convert(ShopDiscount p) {
        return new ShopDiscountDTO(p.getID(),-1,p.getBasketQuantity(),p.getDiscount());
    }

    @Override
    public DiscountPredDTO convert(ValidateBasketQuantityDiscount p) {
        return new ValidateBasketQuantityPredDTO(p.getID(),-1,p.getBasketquantity(),p.isCantBeMore());
    }

    @Override
    public DiscountPredDTO convert(ValidateBasketValueDiscount p) {
        return new ValidateBasketValueDiscountDTO(p.getID(),-1, p.getBasketvalue(),p.isCantBeMore());
    }

    @Override
    public DiscountPredDTO convert(ValidateProductQuantityDiscount p) {
        return new ValidateProductQuantityDiscountDTO(p.getID(),-1,p.getProductId(), p.getProductQuantity());
    }

    @Override
    public PurchasePolicyDTO convert(PurchaseAndPolicy p) {
        return new PurchaseAndPolicyDTO(p.getID(),-1,toPurchasePoliciesDTOs(p.getPurchasePolicies()));
    }

    @Override
    public PurchasePolicyDTO convert(PurchaseGriraPolicy p) {
        PurchasePolicyDTO allow = p.getPurchasePolicyAllow().conversion(this);
        PurchasePolicyDTO validate =  p.getValidatePurchase().conversion(this);
        return new PurchaseGriraPolicyDTO(p.getID(),-1,allow,validate);
    }

    @Override
    public PurchasePolicyDTO convert(PurchaseOrPolicy p) {
        Collection<PurchasePolicyDTO> policyDTOS = toPurchasePoliciesDTOs(p.getPurchasePolicies());
        return new PurchaseOrPolicyDTO(p.getID(),-1,policyDTOS);
    }

    @Override
    public PurchasePolicyDTO convert(ValidateCategoryPurchase v) {
        return new ValidateCategoryPurchaseDTO(v.getID(),-1,v.getCategory(),v.getProductQuantity(),v.isCantbemore());
    }

    @Override
    public PurchasePolicyDTO convert(ValidateProductPurchase v) {
        return new ValidateProductPurchaseDTO(v.getID(),-1,v.getProductId(),v.getProductQuantity(), v.isCantbemore());
    }

    @Override
    public PurchasePolicyDTO convert(ValidateTImeStampPurchase v) {
        return new ValidateTImeStampPurchaseDTO(v.getID(),-1,v.getLocalTime(),v.getDate(), v.isBuybefore());
    }

    @Override
    public PurchasePolicyDTO convert(ValidateUserPurchase v) {
        return new ValidateUserPurchaseDTO(v.getID(),-1, v.getAge());
    }

    private Collection<DiscountPolicyDTO> toDiscountPolicyDTOs(Collection<DiscountRules> policies){
        return policies.stream().map(myPolicy -> myPolicy.conversion(this)).toList();
    }

    private Collection<DiscountPredDTO> toPredDTOs(Collection<DiscountPred> preds){
        return preds.stream().map(myPred -> myPred.conversion(this)).toList();
    }

    private Collection<PurchasePolicyDTO> toPurchasePoliciesDTOs(Collection<PurchasePolicy> policies){
        return policies.stream().map(policy -> policy.conversion(this)).collect(Collectors.toList());
    }
}
