
package Design_Patterns;

import Components.User;

public class LoyaltyPointsPayment implements PaymentStrategy {
    private User user;
    
    public LoyaltyPointsPayment(User user) {
        this.user = user;
    }
    
    @Override
    public void pay(double amount) {
        if (user.getLoyaltyPoints() >= amount) {
            user.redeemLoyaltyPoints(amount);
            System.out.println("Paid Rs." + amount + " using Loyalty Points.");
        } else {
            System.out.println("Insufficient loyalty points for this payment.");
        }
    }
}
