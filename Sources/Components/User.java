

package Components;

import Design_Patterns.Observer;
import java.util.ArrayList;
import java.util.List;

public class User implements Observer {
    private static int counter = 1;
    private final int userID;
    private String name;
    private String email;
    private String phoneNumber;
    private double loyaltyPoints;
    private List<Pizza> favorites;
    
    public User(String name, String email, String phoneNumber) {
        this.userID = counter++;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.loyaltyPoints = 0;
        this.favorites = new ArrayList<>();
    }
    
    // Observer method
    
    @Override
    public void update(String orderStatus) {
        System.out.println("User " + name + " notified: Order status updated to " + orderStatus);
    }
    
    // Getters and setters
    
    public int getUserID() { return userID; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public List<Pizza> getFavorites() { return favorites; }
    
    public double getLoyaltyPoints() { return loyaltyPoints; }
    public void addLoyaltyPoiints(double points) { this.loyaltyPoints += points; }
    
    // Functions
    
    public boolean redeemLoyaltyPoints(double points) {
        if (this.loyaltyPoints >= points) {
            this.loyaltyPoints -= points;
            return true;
        } else {
            System.out.println("Not enough loyalty points to redeem.");
            return false;
        }
    }
    
    public void addFavoritePizza(Pizza pizza) {
        favorites.add(pizza);
    }
    
    public void removeFavoritePizza(int pizzaID) {
        favorites.removeIf(p -> p.getPizzaID() == pizzaID);
    }
    
    @Override
    public String toString() {
        return "User (" + userID + "): " + name + "\n"
                + "Email: " + email + "\n"
                + "Phone Number: " + phoneNumber + "\n"
                + "Loyalty Points: " + loyaltyPoints + "\n"
                + "Favorites: \n\n" + favorites + "\n";
    }
    
}
