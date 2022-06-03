package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public abstract class DiscountPolicy implements DiscountPolicyInterface{

    protected DiscountPolicyInterface discountPolicy;
    int discountId;

    public DiscountPolicy(DiscountPolicyInterface discountPolicy)
    {
        this.discountPolicy = discountPolicy;
        this.discountId = discountPolicy.getDiscountId()+1;
    }

    public double calculateDiscount(Basket basket){
        return discountPolicy.calculateDiscount(basket);
    }

    public int getDiscountId() {
        return discountId;
    }

    public DiscountPolicyInterface getDiscountPolicy() {
        return discountPolicy;
    }

    public DiscountPolicyInterface getDiscountById(int id)
    {
        if(this.discountId==id)
        {
            return this.getDiscountPolicy();
        }
        else
        {
            return this.discountPolicy.getDiscountById(id);
        }
    }


    /***
     *
     * @param id of the removed discount
     * @return the policy after the remove
     */
    public DiscountPolicyInterface removeDiscountById(int id)
    {
        if(id<=0||id>this.discountId)
            throw new IllegalStateException("can't delete the default or not exist discount");
        if(this.discountId==id)
        {
           return this.getDiscountPolicy();
        }
        else
            removeDiscountByIdReq(id);
        return this;
    }
    /***
     * reqursia
     * @param id of the removed discount
     * @return the state of the changes in the policies
     */
    public boolean removeDiscountByIdReq(int id)
    {
        if(this.discountId-1 ==id)
        {
            this.discountPolicy = this.discountPolicy.getDiscountPolicy();
            this.discountId--;
            return true;
        }
        else
        {
            if(this.discountPolicy.removeDiscountByIdReq(id))
            {
                this.discountId--;
                return true;
            }
            else
                return false;
        }
    }
}
