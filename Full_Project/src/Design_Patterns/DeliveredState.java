
package Design_Patterns;

import Components.Order;

public class DeliveredState implements OrderState {
    @Override
    public void handleState(Order order) {
        System.out.println("The order has been delivered. Enjoy...");
    }
    
    @Override
    public String getStateName() { return "Delivered"; };
}
