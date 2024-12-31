
package Design_Patterns;
import Components.Order;

public class InPreparationState implements OrderState {
    @Override
    public void handleState(Order order) {
        System.out.println("Order is In Preparation.");
        order.setState(new OutForDeliveryState());
    }
    
    @Override
    public String getStateName() { return "In Preparation"; };
}
