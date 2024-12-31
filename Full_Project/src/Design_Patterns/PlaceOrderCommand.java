
package Design_Patterns;

import Components.*;

public class PlaceOrderCommand implements Command {
    private Order order;
    private DataRepository repository;
    
    public PlaceOrderCommand(Order order, DataRepository repository) {
        this.order = order;
        this.repository = repository;
    }
    
    @Override
    public void execute() {
        System.out.println("Placing order...");
        repository.addOrder(order);
    }
    
    @Override
    public void undo() {
        System.out.println("Undoing order placement...");
        repository.removeOrder(order.getOrderID());
    }

    @Override
    public String getCommandLog() {
        return "Place Order Command: OrderID - " + order.getOrderID();
    }
}