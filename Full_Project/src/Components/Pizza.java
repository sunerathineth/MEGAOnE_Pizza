
package Components;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    
    // Local variables
    
    private static int idCounter = 1;
    private final int pizzaID;
    private String name;
    private String description;
    private String crustType;
    private String sauceType;
    private String size;
    private List<String> toppings;
    private double basePrice;
    private double rating;
    
    public Pizza(String name,
        String description,
        String crustType,
        String sauceType,
        String size,
        List<String> toppings,
        double basePrice,
        double rating)
    {
        this.pizzaID = idCounter++;
        this.name = name;
        this.description = description;
        this.crustType = crustType;
        this.sauceType = sauceType;
        this.size = size;        
        this.toppings = new ArrayList<>(toppings);
        this.basePrice = basePrice;
        this.rating = rating;
    }
    
    
    // Getters and setters
    
    public int getPizzaID() { return pizzaID; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCrustType() { return crustType; }
    public void setCrustType(String crustType) { this.crustType = crustType; }
    
    public String getSauceType() { return sauceType; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public List<String> getToppings() { return toppings; }
    public void setToppings(List<String> toppings) { this.toppings = toppings; }
    
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    
    // Functions
    
    public double calculatePrice() {
        return basePrice;
    }
    
    @Override
    public String toString() {
        return "Pizza (" + pizzaID + "): " + name + "\n" + "Description: " + description + "\n"
                + "Crust Type: " + crustType + "\n"
                + "Size: " + size + "\n"
                + "Toppings: " + toppings + "\n"
                + "Price: Rs." + basePrice + "\n"
                + "Rating: " + rating + "\n";
    }
}
