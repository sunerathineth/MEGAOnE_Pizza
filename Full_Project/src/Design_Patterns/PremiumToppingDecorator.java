
package Design_Patterns;

import Components.*;

public class PremiumToppingDecorator extends PizzaDecorator {
    private String toppingName;
    private double toppingCost;
    
    public PremiumToppingDecorator(Pizza pizza, String toppingName, double toppingCost) {
        super(pizza);
        this.toppingName = toppingName;
        this.toppingCost = toppingCost;
    }
    
    @Override
    public String getDescription() {
        return pizza.getDescription() + ", " + toppingName;
    }
    
    @Override
    public double calculatePrice() {
        return pizza.calculatePrice() + toppingCost;
    }
}
