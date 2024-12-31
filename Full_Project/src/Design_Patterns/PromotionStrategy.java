
package Design_Patterns;

import Components.Pizza;

public interface PromotionStrategy {
    boolean isEligible(Pizza pizza);
    double applyDiscount(Pizza pizza);
    String getPromotionDetails();
}
