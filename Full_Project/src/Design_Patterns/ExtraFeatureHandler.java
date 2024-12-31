package Design_Patterns;

import Components.*;

public class ExtraFeatureHandler extends CustomizationHandler {
    @Override
    public void handleRequest(CustomizedPizza pizza, String customization) {
        if (customization.startsWith("Extra:")) {
            String extraFeature = customization.split(":")[1];
             switch (extraFeature) {
                case "Cheese":
                    pizza.addCustomization("Extra Cheese", 100.0);
                    break;
                case "Pepperoni":
                     pizza.addCustomization("Extra Pepperoni", 150.0);
                    break;
                case "Packaging":
                     pizza.addCustomization("Special Packaging", 50.0);
                     break;
                default:
                    break;
            }
            System.out.println("Extra added: " + extraFeature);
        } else if(nextHandler != null) {
             nextHandler.handleRequest(pizza, customization);
        }
    }
}