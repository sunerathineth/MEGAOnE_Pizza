
package Design_Patterns;

import Components.Pizza;

public class ExtraPepperoniDecorator extends PizzaDecorator {
    private static final double extraPepperoni = 300.0;
    
    public ExtraPepperoniDecorator(Pizza pizza) {
        super(pizza);
    }
    
    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Extra Pepperoni";
    }
    
    @Override
    public double calculatePrice() {
        return pizza.calculatePrice() + extraPepperoni;
    }
}
