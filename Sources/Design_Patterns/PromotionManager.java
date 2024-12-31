package Design_Patterns;

import Design_Patterns.PromotionStrategy;
import java.util.ArrayList;
import java.util.List;

import Components.Pizza;

public class PromotionManager {
    private List<PromotionStrategy> promotions;

    public PromotionManager() {
        this.promotions = new ArrayList<>();
    }

    public void addPromotion(PromotionStrategy promotion) {
        promotions.add(promotion);
    }

    public List<PromotionStrategy> getActivePromotions() {
        return promotions;
    }

    public List<Pizza> getPizzasWithPromotions(List<Pizza> allPizzas) {
        List<Pizza> pizzasWithPromotions = new ArrayList<>();
        for (Pizza pizza : allPizzas) {
            for (PromotionStrategy promotion : promotions) {
                if (promotion.isEligible(pizza)) {
                    pizzasWithPromotions.add(pizza);
                    break;
                }
            }
        }
        return pizzasWithPromotions;
    }
}
