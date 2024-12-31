
package Design_Patterns;

import Components.*;

public abstract class PizzaDecorator extends Pizza {
    protected Pizza pizza;
    
    public PizzaDecorator(Pizza pizza) {
        super(pizza.getName(), pizza.getDescription(), pizza.getCrustType(), pizza.getSauceType(), pizza.getSize(), pizza.getToppings(), pizza.getBasePrice(), pizza.getRating());
        this.pizza = pizza;
    }
    
    @Override
    public abstract String getDescription();
    
    @Override
    public abstract double calculatePrice();
}
