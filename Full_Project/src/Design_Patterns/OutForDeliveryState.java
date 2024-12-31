
package Design_Patterns;

import Components.Order;

public class OutForDeliveryState implements OrderState {
    @Override
    public void handleState(Order order) {
        System.out.println("Order is Out for Delivery.");
        order.setState(new DeliveredState());
    }
    
    @Override
    public String getStateName() { return "Out for Delivery"; };
}
