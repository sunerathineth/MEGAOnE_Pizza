
package Design_Patterns;

import Components.Order;

public interface OrderState {
    void handleState(Order order);
    String getStateName();
}
