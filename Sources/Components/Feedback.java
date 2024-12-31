
package Components;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Feedback {
    private static int idCount = 1;
    private final int feedbackID;
    private final int userID;
    private final int orderID;
    private double rating;
    private String comments;
    private final LocalDateTime submissionDate;
    
    public Feedback(int userID,
            int orderID,
            double rating,
            String comments)
    {
        this.feedbackID = idCount++;
        this.userID = userID;
        this.orderID = orderID;
        this.rating = rating;
        this.comments = comments;
        this.submissionDate = LocalDateTime.now();
    }
    
    
    // Getters and setters
    
    public int getFeedbackID() { return feedbackID; }
    
    public int getUserID() { return userID; }
    
    public int getOrderID() { return orderID; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 1 and 5;");
        }
    }
    
    public String getComments() { return comments; }
    public void setComment(String comments) { this.comments = comments; }
    
    public LocalDateTime getSubmissionDate() { return submissionDate; }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackID +
                ", userId=" + userID +
                ", orderId=" + orderID +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                ", submissionDate=" + submissionDate;
    }
}
