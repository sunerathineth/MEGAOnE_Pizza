package Design_Patterns;

import Components.*;

public class ToppingHandler extends CustomizationHandler {
    @Override
    public void handleRequest(CustomizedPizza pizza, String customization) {
        if (customization.startsWith("Topping:")) {
            String topping = customization.split(":")[1];
           
            pizza.getToppings().add(topping);
           double cost = (topping.equals("Pepperoni")) ? 150.0 : 100.0;
           pizza.addCustomization(topping, cost);
            System.out.println("Topping added: " + topping);
        } else if(nextHandler != null) {
            nextHandler.handleRequest(pizza, customization);
        }
    }
}