package Design_Patterns;

import Components.Pizza;

public class SizeDiscountPromotion implements PromotionStrategy {
    private String eligibleSize;
    private double discount;

    public SizeDiscountPromotion(String eligibleSize, double discount) {
        this.eligibleSize = eligibleSize;
        this.discount = discount;
    }

    @Override
    public boolean isEligible(Pizza pizza) {
        return pizza.getSize().equalsIgnoreCase(eligibleSize);
    }

    @Override
    public double applyDiscount(Pizza pizza) {
        return pizza.getBasePrice() - discount;
    }

    @Override
    public String getPromotionDetails() {
        return "Discount of Rs." + discount + " on " + eligibleSize + " pizzas.";
    }
}
