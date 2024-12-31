
package Design_Patterns;
import Components.Order;

public class PlacedState implements OrderState {
    @Override
    public void handleState(Order order) {
        System.out.println("Order is Placed.");
        order.setState(new InPreparationState());
    }
    
    @Override
    public String getStateName() { return "Placed"; }
}
