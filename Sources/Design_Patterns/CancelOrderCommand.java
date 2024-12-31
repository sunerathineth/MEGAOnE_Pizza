
package Design_Patterns;

import Components.Order;

public class CancelOrderCommand implements Command {
    private Order order;

    public CancelOrderCommand(Order order) {
        this.order = order;
    }

    @Override
    public void execute() {
        System.out.println("Canceling order...");
        order.setStatus("Canceled");
    }

    @Override
    public void undo() {
        System.out.println("Undoing order cancellation...");
        order.setStatus("Placed");
    }

    @Override
    public String getCommandLog() {
        return "Cancel Order Command: OrderID - " + order.getOrderID();
    }
}
