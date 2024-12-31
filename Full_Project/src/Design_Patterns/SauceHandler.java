package Design_Patterns;

import Components.*;

public class SauceHandler extends CustomizationHandler {
    @Override
    public void handleRequest(CustomizedPizza pizza, String customization) {
        if (customization.startsWith("Sauce:")) {
            String sauceType = customization.split(":")[1];
            if (sauceType.equals("Pesto")) {
                pizza.addCustomization("Pesto Sauce", 100.0);
            } else if (sauceType.equals("White Garlic")) {
                pizza.addCustomization("White Garlic Sauce", 150.0);
            }
            System.out.println("Sauce added: " + sauceType);
        } else if(nextHandler != null) {
            nextHandler.handleRequest(pizza, customization);
        }
    }
}