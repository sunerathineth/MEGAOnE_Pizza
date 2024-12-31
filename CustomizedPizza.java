package Components;

import java.util.ArrayList;
import java.util.List;

public class CustomizedPizza extends Pizza {
    private double extraCost;
    private List<String> customizations;

    public CustomizedPizza(Pizza basePizza) {
        super(basePizza.getName(), basePizza.getDescription(), basePizza.getCrustType(),
              basePizza.getSauceType(), basePizza.getSize(), basePizza.getToppings(),
              basePizza.getBasePrice(), basePizza.getRating());
        this.extraCost = 0.0;
        this.customizations = new ArrayList<>();
    }

    public void addCustomization(String customization, double cost) {
        this.customizations.add(customization);
        this.extraCost += cost;
    }

    @Override
    public double calculatePrice() {
        return super.calculatePrice() + extraCost;
    }

    @Override
    public String toString() {
        return super.toString() +
               "\nCustomizations: " + (customizations.isEmpty() ? "None" : String.join(", ", customizations)) +
               "\nExtra Cost: Rs." + extraCost;
    }
}
