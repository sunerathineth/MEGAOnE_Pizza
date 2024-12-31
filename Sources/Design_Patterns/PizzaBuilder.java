
package Design_Patterns;

import Components.Pizza;
import Components.Promotion;
import java.util.ArrayList;
import java.util.List;

public class PizzaBuilder {
    private String name = "Custom Pizza";
    private String description = "Customized Pizza";
    private String crustType = "Thin Crust";
    private String sauceType = "Tomato Basil";
    private String size = "Medium";
    private List<String> toppings = new ArrayList<>();
    private double basePrice;
    private double rating;
    
    public PizzaBuilder setName(String name) {
        this.name = name;
        return this;
    }
    
    public PizzaBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public PizzaBuilder setSauceType(String sauceType) {
        this.sauceType = sauceType;
        return this;
    }
    
    public PizzaBuilder setCrustType(String crustType) {
        this.crustType = crustType;
        return this;
    }
    
    public PizzaBuilder setsize(String size) {
        this.size = size;
        return this;
    }
    
    public PizzaBuilder addTopping(List<String> newTopping) {
        this.toppings.addAll(newTopping);
        return this;
    }
    
    public PizzaBuilder setBasePrice(double basePrice) {
        this.basePrice = basePrice;
        return this;
    }
    
    public PizzaBuilder setRating(double rating) {
        this.rating = rating;
        return this;
    }
    
    public PizzaBuilder applyPromotion(Promotion promotion) {
        if (promotion != null && promotion.isPromotionActive()) {
            this.basePrice -= promotion.getDiscount();
        }
        return this;
    }
    
    public Pizza build() {
        return new Pizza(name, description, crustType, sauceType, size, toppings, basePrice, rating);
    }
}
