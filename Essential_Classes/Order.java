package Components;

import Design_Patterns.Observer;
import Design_Patterns.OrderState;
import Design_Patterns.PlacedState;
import Design_Patterns.Subject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Subject {
    private static int orderCounter = 1;
    private final int orderID;
    private String status;
    private String deliveryType;
    private String deliveryAddress;
    private String estimatedDeliveryTime;
    private List<Observer> observers;
    private OrderState currentState;
    
    public Order(int orderID) {
        this.orderID = orderCounter++;
        this.status = "Placed";
        this.observers = new ArrayList<>();
        this.currentState = new PlacedState();
        this.deliveryType = "Pickup";
    }
    
    public Order(String deliveryAddress, String deliveryTime) {
        this.orderID = orderCounter++;
        this.status = "Placed";
        this.deliveryType = "Delivery";
        this.deliveryAddress = deliveryAddress;
        this.estimatedDeliveryTime = deliveryTime;
        this.observers = new ArrayList<>();
        this.currentState = new PlacedState();
    }
    
    // Getters and setters
    
    public int getOrderID() { return orderID; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }
    
    public String getDeliveryType() {
        return deliveryType;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public String getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }
    
    // Observer management
    
    @Override
    public void registerObserver(Observer observer) { observers.add(observer); }
    
    @Override
    public void removeObserver(Observer observer) { observers.remove(observer); }
    
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(status);
        }
    }
    
    // Related to order state management
    
    public OrderState getCurrentState() { return currentState; }
    public void setState(OrderState state) { this.currentState = state; }
    
    public void nextState() {
        if (currentState != null) {
            currentState.handleState(this);
            System.out.println("Order status updated to: " + getStatus());
            notifyObservers();
        } else {
            System.out.println("No current state set. Cannot transition.");
        }
    }
    public String getStateName() { return currentState.getStateName(); }
    
    @Override
    public String toString() {
        if (deliveryType.equals("Delivery")) {
            return "Order," +
                "orderId=" + orderID +
                ", status='" + status + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", estimatedDeliveryTime=" + estimatedDeliveryTime;
        } else {
            return "Order," +
                "orderId=" + orderID +
                ", status='" + status + '\'' +
                ", deliveryType='" + deliveryType + '\'';
        }
    }
}
