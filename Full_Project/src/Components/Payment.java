
package Components;

import java.time.LocalDateTime;

public class Payment {
    private static int counter;
    private final int paymentID;
    private final int orderID;
    private String paymentMethod;
    private double amount;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    
    public Payment(int orderID, String paymentMethod, double amount)
    {
        this.paymentID = counter++;
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = "Pending";
        this.paymentDate = LocalDateTime.now();
    }
    
    
    // Getters and setters
    
    public int getPaymentID() { return paymentID; }
    
    public int getOrderID() { return orderID; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    
    
    // Functions
    
    public void processPayment() {
        if (amount > 0) {
            this.paymentStatus = "Completed";
        } else {
            this.paymentStatus = "Failed";
        }
    }
    
    public boolean isPaymentSuccessful() {
        return "Completed".equals(paymentStatus);
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentID +
                ", orderId=" + orderID +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
