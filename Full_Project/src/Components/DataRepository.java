package Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRepository {

    // Local variables
    private final Map<Integer, Pizza> pizzas;
    private final Map<Integer, Order> orders;
    private final Map<Integer, User> users;
    private final Map<Integer, Feedback> feedbacks;
    private final Map<Integer, Promotion> promotions;
    private final Map<Integer, Payment> payments;

    // Constructor
    public DataRepository() {
        this.pizzas = new HashMap<>();
        this.orders = new HashMap<>();
        this.users = new HashMap<>();
        this.feedbacks = new HashMap<>();
        this.promotions = new HashMap<>();
        this.payments = new HashMap<>();
    }

    // --- Pizza Management ---
    
    public void addPizza(Pizza pizza) {
        pizzas.put(pizza.getPizzaID(), pizza);
    }
    
    public void updatePizza(Pizza pizza) {
        pizzas.put(pizza.getPizzaID(), pizza);
    }

    public Pizza getPizza(int pizzaID) {
        return pizzas.get(pizzaID);
    }

    public List<Pizza> getAllPizzas() {
        return new ArrayList<>(pizzas.values());
    }

    public void removePizza(int pizzaID) {
        pizzas.remove(pizzaID);
    }

    // --- Order Management ---
    public void addOrder(Order order) {
        orders.put(order.getOrderID(), order);
    }

    public Order getOrder(int orderID) {
        return orders.get(orderID);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public void removeOrder(int orderID) {
        orders.remove(orderID);
    }

    // --- User Management ---
    public void addUser(User user) {
        users.put(user.getUserID(), user);
    }

    public User getUser(int userID) {
        return users.get(userID);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void removeUser(int userID) {
        users.remove(userID);
    }

    public void addFeedback(Feedback feedback) {
        feedbacks.put(feedback.getFeedbackID(), feedback);
    }

    public Feedback getFeedback(int feedbackID) {
        return feedbacks.get(feedbackID);
    }

    public List<Feedback> getAllFeedbacks() {
        return new ArrayList<>(feedbacks.values());
    }

    public void removeFeedback(int feedbackID) {
        feedbacks.remove(feedbackID);
    }

    public List<Feedback> getFeedbackByOrderID(int orderID) {
        List<Feedback> feedbackList = new ArrayList<>();
        for (Feedback feedback : feedbacks.values()) {
            if (feedback.getOrderID() == orderID) {
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }

    // --- Promotion Management ---
    public void addPromotion(Promotion promotion) {
        promotions.put(promotion.getPromotionID(), promotion);
    }

    public Promotion getPromotion(int promotionID) {
        return promotions.get(promotionID);
    }

    public List<Promotion> getAllPromotions() {
        return new ArrayList<>(promotions.values());
    }

    public void removePromotion(int promotionID) {
        promotions.remove(promotionID);
    }

    public List<Promotion> getActivePromotions() {
        List<Promotion> activePromotions = new ArrayList<>();
        for (Promotion promotion : promotions.values()) {
            if (promotion.isPromotionActive()) {
                activePromotions.add(promotion);
            }
        }
        return activePromotions;
    }

    // --- Payment Management ---
    public void addPayment(Payment payment) {
        payments.put(payment.getPaymentID(), payment);
    }

    public Payment getPayment(int paymentID) {
        return payments.get(paymentID);
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments.values());
    }

    public void removePayment(int paymentID) {
        payments.remove(paymentID);
    }
}
