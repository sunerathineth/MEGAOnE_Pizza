
package Design_Patterns;

import Components.*;

public class SpecialPackagingDecorator extends PizzaDecorator {
    private static final double packagingCost = 100.0;
    
    public SpecialPackagingDecorator(Pizza pizza) {
        super(pizza);
    }
    
    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Special Packaging";
    }
    
    @Override
    public double calculatePrice() {
        return pizza.calculatePrice() + packagingCost;
    }
}
