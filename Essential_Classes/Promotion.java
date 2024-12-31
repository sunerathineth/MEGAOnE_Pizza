
package Components;

import java.time.LocalDate;
import java.util.List;

public class Promotion {
    private static int counter = 1;
    private final int promotionID;
    private String name;
    private String description;
    private double discount;
    private boolean isPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> eligibleItems;
    
    public Promotion(String name,
        String description,
        double discount,
        boolean isPercentage,
        LocalDate startDate,
        LocalDate endDate,
        List<Integer> eligibleItems)
    {
        this.promotionID = counter++;
        this.name = name;
        this.description = description;
        this.discount = discount;
        this.isPercentage = isPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eligibleItems = eligibleItems;
    }

    
    // Getters and setters
    
    public int getPromotionID() {
        return promotionID;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    
    public boolean isPercentage() { return isPercentage; }
    public void setPercentage(boolean percentage) { this.isPercentage = percentage; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public List<Integer> getEligibleItems() { return eligibleItems; }
    public void setEligibleItems(List<Integer> eligibleItems) { this.eligibleItems = eligibleItems; }
    
    
    // Promotion related functions
    
    public boolean isPromotionActive() {
        LocalDate today = LocalDate.now();
        return (today.isEqual(startDate) || today.isAfter(startDate)) && today.isBefore(endDate.plusDays(1));
    }
    
    public double applyPromotion(double price) {
        if (isPercentage) {
            return price - (price * (discount / 100));
        } else {
            return price - discount;
        }
    }
    
    @Override
    public String toString() {
        return "Promotion (" + promotionID + ": " + name + ")" + "\n" +
                "Description: " + description + "\n" +
                "Discount: " + (isPercentage ? discount + "%" : "Rs." + discount) + "\n" +
                "Start Date: " + startDate + "\n" +
                "End Date: " + endDate + "\n" +
                "Eligible Items:  \n" +
                eligibleItems + "\n";
    }
}
