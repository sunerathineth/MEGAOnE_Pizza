
package Design_Patterns;

import Components.*;

public class ExtraCheeseDecorator extends PizzaDecorator {
    private static final double extraCheeseCost = 300.0;
    
    public ExtraCheeseDecorator(Pizza pizza) {
        super(pizza);
    }
    
    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Extra Chesse";
    }
    
    @Override
    public double calculatePrice() {
        return pizza.calculatePrice() + extraCheeseCost;
    }
}
