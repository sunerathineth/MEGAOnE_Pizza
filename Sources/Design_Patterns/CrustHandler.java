package Design_Patterns;

import Components.*;

public class CrustHandler extends CustomizationHandler {
    @Override
    public void handleRequest(CustomizedPizza pizza, String customization) {
        if (customization.startsWith("Crust:")) {
             String crustType = customization.split(":")[1];
             
              if (crustType.equals("Thick")) {
                   pizza.setCrustType("Thick");
                    pizza.addCustomization("Thick Crust", 200.0);
                } else if (crustType.equals("Whole Wheat")) {
                    pizza.setCrustType("Whole Wheat");
                     pizza.addCustomization("Whole Wheat Crust", 300.0);
                 }
            System.out.println("Crust set to: " + crustType);
        } else if (nextHandler != null) {
            nextHandler.handleRequest(pizza, customization);
        }
    }
}