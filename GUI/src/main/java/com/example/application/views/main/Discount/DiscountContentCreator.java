package com.example.application.views.main.Discount;

import ServiceLayer.Objects.Policies.Discount.*;
import ServiceLayer.Objects.Policies.Discount.ProductByQuantityDiscount;
import ServiceLayer.Objects.Policies.Discount.ProductDiscount;
import ServiceLayer.Objects.Policies.Discount.ProductQuantityInPriceDiscount;
import ServiceLayer.Objects.Policies.Discount.ShopDiscount;
import ServiceLayer.Objects.Product;
import ServiceLayer.Response;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Discount.DiscountPolicies.*;
import com.example.application.views.main.Discount.DiscountPred.DiscountPred;
import com.example.application.views.main.Discount.DiscountPred.DiscountPredCreator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;

public class DiscountContentCreator {
    private SubscribedUserService service;
    private int shopId;

    public DiscountContentCreator(SubscribedUserService service, int shopId) {
        this.service = service;
        this.shopId = shopId;
    }

    public Component createPlusDiscount(int parentId) {
        Button button = new Button("Create Plus Discount");
        button.addClickListener(e -> {
            var res = service.createDiscountPlusPolicy(parentId, shopId);
            validateResult(res, "Added Plus Discount Successfully!");
        });
        return button;
    }

    public Component createMaxDiscount(int parentId) {
        Button button = new Button("Create Max Discount");
        button.addClickListener(e -> {
            var res = service.createDiscountMaxPolicy(parentId, shopId);
            validateResult(res, "Added Max Discount Successfully!");
        });
        return button;
    }

    public Component createAndDiscount(int parentId) {
        Button button = new Button("Create And Discount");
        button.addClickListener(e -> {
            var res = service.createDiscountAndPolicy(null, parentId, shopId);
            validateResult(res, "Added And Discount Successfully!");
        });
        return button;
    }

    public Component createOrDiscount(int parentId) {
        Button button = new Button("Create Or Discount");
        button.addClickListener(e -> {
            var res = service.createDiscountOrPolicy(null, parentId, shopId);
            validateResult(res, "Added Or Discount Successfully!");
        });
        return button;
    }

    public Component createXorDiscount(int parentId) {
        Button button = new Button("Create Xor Discount");
        button.addClickListener(e -> {
            var res = service.createDiscountXorPolicy(null, null, parentId, shopId);
            validateResult(res, "Added Xor Discount Successfully!");
        });
        return button;
    }

    public Component createShopDiscount(int parentId) {
        VerticalLayout layout = new VerticalLayout();
        NumberField amount = createNumberField("Enter a discount between 0-100", 100);
        IntegerField basketCount = createIntegerField("Enter amount of products in basket");
        Button button = new Button("Create Shop Discount");
        button.addClickListener(e -> {
            if(amount.getValue() != null && basketCount.getValue() != null) {
                var res = service.createShopDiscount(basketCount.getValue(), amount.getValue()/100, parentId, shopId);
                validateResult(res, "Added Shop Discount Successfully!");
            }
        });
        layout.add(amount, basketCount, button);
        return layout;
    }

    public Component createProductByQuantityDiscount(int parentId) {
        VerticalLayout layout = new VerticalLayout();
        NumberField priceField = createNumberField("Enter price", -1);
        IntegerField quantityField = createIntegerField("Enter product quantity");
        Select<Integer> selectProduct = createShopSelect();
        Button button = new Button("Create Product by Quantity Discount");
        priceField.addValueChangeListener(e -> button.setEnabled(priceField.getValue() != null && selectProduct.getValue() != null && quantityField.getValue() != null));
        quantityField.addValueChangeListener(e -> button.setEnabled(priceField.getValue() != null && selectProduct.getValue() != null && quantityField.getValue() != null));
        selectProduct.addValueChangeListener(e -> button.setEnabled(priceField.getValue() != null && selectProduct.getValue() != null && quantityField.getValue() != null));
        button.addClickListener(e -> {
            if(priceField.getValue() != null && quantityField.getValue() != null && selectProduct.getValue() != null) {
                var res = service.createProductByQuantityDiscount(selectProduct.getValue(), quantityField.getValue(), priceField.getValue(), parentId, shopId);
                validateResult(res, "Added Product By Discount Successfully!");
            }
        });
        layout.add(priceField, quantityField, selectProduct, button);
        return layout;
    }

    public Component createProductQuantityInPriceDiscount(int parentId) {
        VerticalLayout layout = new VerticalLayout();
        NumberField priceField = createNumberField("Enter price", -1);
        IntegerField quantityField = createIntegerField("Enter product quantity");
        Select<Integer> selectProduct = createShopSelect();
        Button button = new Button("Create Product Quantity in Price Discount");
        priceField.addValueChangeListener(e -> button.setEnabled(priceField.getValue() != null && selectProduct.getValue() != null && quantityField.getValue() != null));
        quantityField.addValueChangeListener(e -> button.setEnabled(priceField.getValue() != null && selectProduct.getValue() != null && quantityField.getValue() != null));
        selectProduct.addValueChangeListener(e -> button.setEnabled(priceField.getValue() != null && selectProduct.getValue() != null && quantityField.getValue() != null));
        button.addClickListener(e -> {
            if(priceField.getValue() != null && quantityField.getValue() != null && selectProduct.getValue() != null) {
                var res = service.createProductQuantityInPriceDiscount(selectProduct.getValue(), quantityField.getValue(), priceField.getValue(), parentId, shopId);
                validateResult(res, "Added Shop Discount Successfully!");
            }
        });
        layout.add(priceField, quantityField, selectProduct, button);
        return layout;
    }

    public Component createProductDiscount(int parentId) {
        VerticalLayout layout = new VerticalLayout();
        Select<Integer> selectProduct = createShopSelect();
        NumberField priceField = createNumberField("Enter an amount between 0-100", 100);
        Button button = new Button("Create Product Discount");
        priceField.addValueChangeListener(e -> button.setEnabled(priceField.getValue() != null && selectProduct.getValue() != null));
        selectProduct.addValueChangeListener(e -> button.setEnabled(priceField.getValue() != null && selectProduct.getValue() != null));
        button.addClickListener(e -> {
            if(priceField.getValue() != null && selectProduct.getValue() != null) {
                var res = service.createProductDiscount(selectProduct.getValue(), priceField.getValue()/100, parentId, shopId);
                validateResult(res, "Added Product Discount Successfully!");
            }
        });
        layout.add(priceField, selectProduct, button);
        return layout;
    }

    private void validateResult(Response<Integer> res, String successMessage) {
        if(res.isOk()){
            notifySuccess(successMessage);
            UI.getCurrent().getPage().reload();
        }
        else{
            notifyError(res.getMsg());
        }
    }

    public DiscountPolicy getDiscounts(){
        var res = service.getDiscount(shopId);
        int id = findParentId(res.getElement());
        if(res.isOk() && id >= 0){
            return getDiscounts(res.getElement(), findParentId(res.getElement()));
        }
        return null;
    }

    public int findParentId(DiscountRules discountRules){
        if(discountRules instanceof ProductByQuantityDiscount discount) {
            return discount.discountId();
        }

        else if(discountRules instanceof ProductDiscount discount) {
            return discount.discountId();
        }

        else if(discountRules instanceof ProductQuantityInPriceDiscount discount) {
            return discount.discountId();
        }

        else if(discountRules instanceof ShopDiscount discount) {
            return discount.discountId();
        }

        else if(discountRules instanceof DiscountAndPolicy discount) {
            return discount.connectId();
        }

        else if(discountRules instanceof DiscountOrPolicy discount) {
            return discount.connectId();
        }

        else if(discountRules instanceof DiscountXorPolicy discount) {
            return discount.connectId();
        }

        else if(discountRules instanceof DiscountMaxPolicy discount) {
            return discount.connectId();
        }

        else if(discountRules instanceof DiscountPlusPolicy discount) {
            return discount.connectId();
        }
        return -1;
    }

    public DiscountPolicy getDiscounts(DiscountRules discountRules, int parentId){
        if(discountRules instanceof ProductByQuantityDiscount discount) {
            return new com.example.application.views.main.Discount.DiscountPolicies.ProductByQuantityDiscount(service, shopId, parentId, discount.productId(), discount.productQuantity(), discount.discount());
        }

        else if(discountRules instanceof ProductDiscount discount) {
            return new com.example.application.views.main.Discount.DiscountPolicies.ProductDiscount(service, shopId, parentId, discount.productId(), discount.discount());
        }

        else if(discountRules instanceof ProductQuantityInPriceDiscount discount) {
            return new com.example.application.views.main.Discount.DiscountPolicies.ProductQuantityInPriceDiscount(service, shopId, parentId, discount.productID(), discount.quantity(), discount.priceForQuantity());
        }

        else if(discountRules instanceof ShopDiscount discount) {
            return new com.example.application.views.main.Discount.DiscountPolicies.ShopDiscount(service, shopId, parentId, discount.discount(), discount.basketQuantity());
        }

        else if(discountRules instanceof DiscountAndPolicy discount) {
            List<DiscountPred> preds = new ArrayList<>();
            for(ServiceLayer.Objects.Policies.Discount.DiscountPred pred : discount.discountPreds()) {
                preds.add(getPreds(pred, discount.connectId()));
            }
            return new AndDiscount(service, shopId, parentId, preds, getDiscounts(discount.discountPolicy()));
        }

        else if(discountRules instanceof DiscountOrPolicy discount) {
            List<DiscountPred> preds = new ArrayList<>();
            for(ServiceLayer.Objects.Policies.Discount.DiscountPred pred : discount.discountPreds()) {
                preds.add(getPreds(pred, discount.connectId()));
            }
            return new OrDiscount(service, shopId, parentId, preds, getDiscounts(discount.discountPolicy()));
        }

        else if(discountRules instanceof DiscountXorPolicy discount) {
            List<DiscountPred> preds = new ArrayList<>();
            for(ServiceLayer.Objects.Policies.Discount.DiscountPred pred : discount.tieBreakers()) {
                preds.add(getPreds(pred, discount.connectId()));
            }
            return new XorDiscount(service, shopId, parentId, preds, getDiscounts(discount.discountRules1()), getDiscounts(discount.discountRules2()));
        }

        else if(discountRules instanceof DiscountMaxPolicy discount) {
            List<DiscountPolicy> rules = new ArrayList<>();
            for(DiscountRules rule : discount.discountPolicies()){
                rules.add(getDiscounts(rule));
            }
            return new MaxDiscount(service, shopId, parentId, rules);
        }

        else if(discountRules instanceof DiscountPlusPolicy discount) {
            List<DiscountPolicy> rules = new ArrayList<>();
            for(DiscountRules rule : discount.discountPolicies()){
                rules.add(getDiscounts(rule));
            }
            return new PlusDiscount(service, shopId, parentId, rules);
        }
        return null;
    }


    private DiscountPolicy getDiscounts(DiscountRules rule) {
        return getDiscounts(rule, findParentId(rule));
    }

    private DiscountPred getPreds(ServiceLayer.Objects.Policies.Discount.DiscountPred discountPred, int parentId) {
        if(discountPred instanceof ValidateBasketQuantityDiscount pred){
            return new com.example.application.views.main.Discount.DiscountPred.ValidateBasketQuantityDiscount(service, shopId, parentId, pred.basketquantity(), pred.cantBeMore(), pred.ruleId());
        }
        else if(discountPred instanceof ValidateBasketValueDiscount pred){
            return new com.example.application.views.main.Discount.DiscountPred.ValidateBasketValueDiscount(service, shopId, parentId, pred.basketvalue(), pred.cantBeMore(), pred.ruleId());
        }
        else if(discountPred instanceof ValidateProductQuantityDiscount pred){
            return new com.example.application.views.main.Discount.DiscountPred.ValidateProductQuantityDiscount(service, shopId, parentId, pred.productId(), pred.productQuantity(), pred.cantbemore(), pred.ruleId());
        }
        return null;
    }

    private NumberField createNumberField(String label, int maxValue) {
        NumberField amount = new NumberField(label);
        amount.setAutofocus(true);
        amount.setWidthFull();
        amount.setClearButtonVisible(true);
        amount.setMin(0);
        if(maxValue != -1)
            amount.setMax(maxValue);
        return amount;
    }

    private IntegerField createIntegerField(String label) {
        IntegerField basketField = new IntegerField(label);
        basketField.setWidthFull();
        basketField.setClearButtonVisible(true);
        basketField.setMin(0);
        basketField.setValue(1);
        basketField.setHasControls(true);
        return basketField;
    }

    private Select<Integer> createShopSelect() {
        Select<Integer> selectShop = new Select<>();
        selectShop.setLabel("Select Product");
        selectShop.setItems(filterShopIds());
        return selectShop;
    }

    private List<Integer> filterShopIds(){
        Predicate<Product> productPredicate = p -> true;
        var res = service.searchProducts(s -> s.shopId() == shopId, productPredicate);
        if(!res.isOk())
            return null;
        var myShop = res.getElement().shops().stream().filter(s -> s.shopId()==shopId).findFirst();
        if(myShop.isEmpty())
            return null;
        return myShop.get().shopProducts().stream().map(Product::productID).toList();
    }
}
