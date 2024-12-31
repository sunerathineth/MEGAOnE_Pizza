
package Design_Patterns;

import Components.Pizza;

public class ExtraSausageDecorator extends PizzaDecorator {
    private static final double extraSausage = 300.0;
    
    public ExtraSausageDecorator(Pizza pizza) {
        super(pizza);
    }
    
    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Extra Sausage";
    }
    
    @Override
    public double calculatePrice() {
        return pizza.calculatePrice() + extraSausage;
    }
}
