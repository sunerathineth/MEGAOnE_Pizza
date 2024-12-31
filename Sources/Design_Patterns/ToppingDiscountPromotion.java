package Design_Patterns;

import Components.Pizza;

public class ToppingDiscountPromotion implements PromotionStrategy {
    private String eligibleTopping;
    private double discount;

    public ToppingDiscountPromotion(String eligibleTopping, double discount) {
        this.eligibleTopping = eligibleTopping;
        this.discount = discount;
    }

    @Override
    public boolean isEligible(Pizza pizza) {
        return pizza.getToppings().contains(eligibleTopping);
    }

    @Override
    public double applyDiscount(Pizza pizza) {
        return pizza.getBasePrice() - discount;
    }

    @Override
    public String getPromotionDetails() {
        return "Discount of Rs." + discount + " on pizzas with " + eligibleTopping + " topping.";
    }
}
