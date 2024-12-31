package mega_one_console_version;

import Components.CustomizedPizza;
import Components.DataRepository;
import Components.Feedback;
import Components.Order;
import Components.Payment;
import Components.Pizza;
import Design_Patterns.PizzaBuilder;
import Components.Promotion;
import Components.User;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Scanner;

import Design_Patterns.*;
import java.util.ArrayList;

import External.*;
import static External.DeliveryEstimator.calculateDeliveryTime;
import static External.DeliveryEstimator.getCoordinatesFromPlace;
import static External.DeliveryEstimator.getUserCountry;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MEGA_ONE_Console_Version {

    private Scanner scanner;
    private static String userInput;
    private static int userInputInt;
    private static MEGA_ONE_Console_Version megaOne = new MEGA_ONE_Console_Version();
    User activeUser;
    private String userDeliveryAddress;
    private PromotionManager promotionManager;
//    private Timer timer;
    
    // Topping limits
    
    private static final int minToppings = 1;
    private static final int maxToppings = 4;

    // Data center
    
    private static DataRepository repository;
    
    // Repositories
    

    public MEGA_ONE_Console_Version() {
        scanner = new Scanner(System.in);
        repository = new DataRepository();
//        timer = new Timer();
    }

    private String getUserInput(String question) {
        System.out.print(question);
        userInput = scanner.nextLine();
        System.out.println("");
        return userInput;
    }
    
    private int getUserInput_Int(String question) {
        System.out.print(question);
        userInputInt = scanner.nextInt();
        System.out.println("");
        return userInputInt;
    }

    private String returnUserInput() {
        return userInput;
    }

    private void displayUserInput() {
        System.out.println(userInput);
    }
    
    private void pizzaFromMEGAOnE() {
        repository.addPizza(new Pizza("Chicken Kottu Pizza", "A fusion pizza with shredded roti, chicken, vegetables, and spices", "Thin", "Spicy Tomato", "Medium", Arrays.asList("Chicken", "Roti", "Onions", "Chili"), 1800.00, 4.5));
        repository.addPizza(new Pizza("Seafood Lagoon Pizza", "Pizza with prawns, calamari, crab, and a touch of chili", "Thin", "Garlic Butter", "Large", Arrays.asList("Prawns", "Calamari", "Crab", "Chili"), 2500.00, 4.6));
        repository.addPizza(new Pizza("Spicy Chicken Tikka Masala Pizza", "Pizza with a creamy tikka masala sauce, chicken, and onions", "Thick", "Tikka Masala", "Medium", Arrays.asList("Chicken", "Tikka Masala Sauce", "Onions"), 2000.00, 4.3));
        repository.addPizza(new Pizza("Vegetable Curry Pizza", "A vegetarian pizza with mixed vegetable curry, coconut milk, and spices", "Thick", "Coconut Curry", "Small", Arrays.asList("Mixed Vegetables", "Coconut Milk", "Spices"), 1500.00, 4.0));
        repository.addPizza(new Pizza("Devilled Chicken Pizza", "A fiery pizza with devilled chicken, onions, and peppers", "Thin", "Devilled Sauce", "Large", Arrays.asList("Devilled Chicken", "Onions", "Bell Peppers"), 2200.00, 4.7));
    }
    
    private void userCreation() {
        activeUser = new User("Sunera Amaraguru", "suneraedu@gmail.com", "0719427485");
        
        // First time sign up points
        
        activeUser.addLoyaltyPoiints(200);
        
        repository.addUser(activeUser);
    }

    public void displayMainMenu() {
        System.out.println("\nWelcome to MEGA OnE Pizza");
        System.out.println("1. Place a new order");
        System.out.println("2. View pizza menu");
        System.out.println("3. My profile");
        System.out.println("4. My favorites");
        System.out.println("5. View order history");
        System.out.println("6. Track order");
        System.out.println("7. View promotions");
        System.out.println("8. Provide feedback");
        System.out.println("9. View all feedback");
        System.out.println("10. Top rated pizza");
        System.out.println("11. Exit\n");
    }
    
    private static String formatDeliveryTime(double timeInMinutes) {
        if (timeInMinutes < 60) {
            return String.format("%.1f minutes", timeInMinutes);
        } else if (timeInMinutes >= 60 && timeInMinutes < 1440) {
            double hours = timeInMinutes / 60;
            return String.format("%.1f hours", hours);
        } else {
            double days = timeInMinutes / 1440;
            return String.format("%.1f days", days);
        }
    }
    
    private void acceptPayment(double amount) {
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        boolean paymentSuccessful = false;

        while (!paymentSuccessful) {
            System.out.println("\nSelect Payment Method:");
            System.out.println("1. Credit Card");
            System.out.println("2. Wallet");
            System.out.println("3. Loyalty Points");
            System.out.print("Enter your choice: ");
            int paymentChoice = scanner.nextInt();
            scanner.nextLine();

            switch (paymentChoice) {
                case 1:
                    System.out.print("Enter Credit Card Number: ");
                    String cardNumber = scanner.nextLine();
                    System.out.print("Enter Card Holder Name: ");
                    String cardHolderName = scanner.nextLine();
                    paymentProcessor.setPaymentStrategy(new CreditCardPayment(cardNumber, cardHolderName));
                    paymentProcessor.processPayment(amount);
                    paymentSuccessful = true;
                    break;
                case 2:
                    System.out.print("Enter Wallet ID: ");
                    String walletID = scanner.nextLine();
                    paymentProcessor.setPaymentStrategy(new WalletPayment(walletID));
                    paymentProcessor.processPayment(amount);
                    paymentSuccessful = true;
                    break;
                case 3:
                    boolean isLoyaltyPointsSufficient = activeUser.redeemLoyaltyPoints(amount);
                    if (isLoyaltyPointsSufficient) {
                        paymentProcessor.setPaymentStrategy(new LoyaltyPointsPayment(activeUser));
                        paymentProcessor.processPayment(amount);
                        paymentSuccessful = true;
                    } else {
                        System.out.println("Please choose another payment method.");
                    }
                    break;
                default:
                    System.out.println("Invalid payment method. Please select a valid option.");
                    break;
            }
        }
        
        activeUser.addLoyaltyPoiints(100);
    }

    public String getDeliveryTimeWithFixedStart() {
        String userCountry = getUserCountry();
        if (userCountry == null) {
            System.out.println("Couldn't identify your country. Results will be not limited to your country.");
        }

        try {
            String fixedStartPlace = "Pizza Hut, Alvis Town Road, Wattala 11300, Sri Lanka";
            String fixedStartCoordinates = "6.9862699,79.8892128";

            System.out.println("From MEGA OnE: " + fixedStartPlace);

            String endPlace = null;
            List<String> endCoordinatesList = null;

            while (true) {
                System.out.print("Enter the delivery address: ");
                endPlace = scanner.nextLine();
                endCoordinatesList = getCoordinatesFromPlace(endPlace, userCountry);

                if (endCoordinatesList.isEmpty()) {
                    System.out.println("No places found in your country for the destination location. Please try again.");
                } else {
                    break;
                }
            }

            System.out.println("Select the correct destination place:");
            for (int i = 0; i < endCoordinatesList.size(); i++) {
                System.out.println((i + 1) + ". " + endCoordinatesList.get(i));
            }

            int endChoice = -1;
            while (endChoice < 1 || endChoice > endCoordinatesList.size()) {
                System.out.print("Enter the number of the correct destination place: ");

                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input. Please enter a valid number: ");
                    scanner.next();
                }

                endChoice = scanner.nextInt();
                scanner.nextLine();

                if (endChoice < 1 || endChoice > endCoordinatesList.size()) {
                    System.out.println("Invalid choice. Please enter a valid number between 1 and " + endCoordinatesList.size());
                }
            }

            String endCoordinates = endCoordinatesList.get(endChoice - 1).split(":")[1].trim();
            
            userDeliveryAddress = endCoordinatesList.get(endChoice - 1);

            if (endCoordinates == null) {
                System.out.println("Error retrieving coordinates. Please check the place name.");
                return null;
            }

            double deliveryTime = calculateDeliveryTime(fixedStartCoordinates, endCoordinates);

            if (deliveryTime != -1) {
                return formatDeliveryTime(deliveryTime);
            } else {
                return "Unable to calculate delivery time.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while calculating the delivery time.";
        }
    }
    
    public void getDeliveryTime() {
        String userCountry = getUserCountry();
        if (userCountry == null) {
            System.out.println("Couldn't identify your country. Results will be not limited to your country.");
        }



        try {
            System.out.print("Enter the starting place name: ");
            String startPlace = scanner.nextLine();
            List<String> startCoordinatesList = getCoordinatesFromPlace(startPlace, userCountry);

            if (startCoordinatesList.isEmpty()) {
                System.out.println("No places found in your country for the starting location.");
                return;
            }
            
            System.out.println("Select the correct starting place:");
            for (int i = 0; i < startCoordinatesList.size(); i++) {
                System.out.println((i + 1) + ". " + startCoordinatesList.get(i));
            }
            
            System.out.print("Enter the number of the correct starting place: ");
            int startChoice = scanner.nextInt();
            scanner.nextLine();

            
            System.out.print("Enter the destination place name: ");
            String endPlace = scanner.nextLine();
            List<String> endCoordinatesList = getCoordinatesFromPlace(endPlace, userCountry);
            
            if (endCoordinatesList.isEmpty()) {
                System.out.println("No places found in your country for the destination location.");
                return;
            }
            System.out.println("Select the correct destination place:");
            for (int i = 0; i < endCoordinatesList.size(); i++) {
                System.out.println((i + 1) + ". " + endCoordinatesList.get(i));
            }
            System.out.print("Enter the number of the correct destination place: ");
            int endChoice = scanner.nextInt();
            scanner.nextLine();

            String startCoordinates = startCoordinatesList.get(startChoice - 1).split(":")[1].trim();
            String endCoordinates = endCoordinatesList.get(endChoice - 1).split(":")[1].trim();

            if (startCoordinates == null || endCoordinates == null) {
                System.out.println("Error retrieving coordinates. Please check the place names.");
                return;
            }

            double deliveryTime = calculateDeliveryTime(startCoordinates, endCoordinates);

            if (deliveryTime != -1) {
                String finalEstimatedDeliveryTime = formatDeliveryTime(deliveryTime);
                System.out.println("Delivery time: " + finalEstimatedDeliveryTime);
            } else {
                System.out.println("Unable to calculate delivery time.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initiateSystem() {
        boolean exit = false;

        while (!exit) {
            displayMainMenu();
            System.out.print("Enter menu number: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    placeNewOrder();
                    break;
                case 2:
                    viewPizzaMenu();
                    break;
                case 3:
                    viewProfile();
                    break;
                case 4:
                    manageFavorites();
                    break;
                case 5:
                    viewOrderHistory();
                    break;
                case 6:
                    trackOrder();
                    break;
                case 7:
                    viewPromotions();
                    break;
                case 8:
                    provideFeedback();
                    break;
                case 9:
                    viewFeedback();
                    break;
                case 10:
                    displayTopRatedPizzasMenu();
                    break;
                case 11:
                    System.out.println("Thank you for visiting MEGA OnE Pizza! Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayTopRatedPizzasMenu() {
        System.out.println("\n--- Top Rated Pizzas ---\n");

        List<Pizza> allPizzas = repository.getAllPizzas();
        List<Pizza> ratedPizzas = new ArrayList<>();

        for (Pizza pizza : allPizzas) {
            ratedPizzas.add(pizza);
        }

        ratedPizzas.sort((p1, p2) -> Double.compare(p2.getRating(), p1.getRating()));

        System.out.println("Top Rated Pizzas:");
        for (int i = 0; i < ratedPizzas.size(); i++) {
            Pizza pizza = ratedPizzas.get(i);
            double rating = pizza.getRating();
            System.out.printf("%d. %s - Rating: %.2f\n", (i + 1), pizza.getName(), rating);
        }

        System.out.println("\nOptions:");
        System.out.println("1. Order a pizza from this list");
        System.out.println("2. Return to main menu");
        System.out.print("Enter your choice: ");

        int choice = getValidInput(2);

        switch (choice) {
            case 1:
                orderPromotedPizza(ratedPizzas);
                break;
            case 2:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    // New order placement
    
    private void placeNewOrder() {
        System.out.println("\n--- Place a New Order ---\n");
        
        System.out.println("Select Delivery Type:");
        System.out.println("1. Delivery");
        System.out.println("2. Pickup");
        System.out.print("Enter your choice: ");

        int deliveryTypeChoice = getValidInput(2);
        String deliveryType = "";
        switch (deliveryTypeChoice) {
            case 1:
                deliveryType = "Delivery";
                break;
            case 2:
                deliveryType = "Pickup";
                break;
            default:
                break;
        }

        String crust = getValidCrustChoice();
        String sauce = getValidSauceChoice();
        List<String> toppings = getValidToppingsChoice();
        String cheese = getValidCheeseChoice();
        String size = getValidSizeChoice();
        
        System.out.print("Enter a name for your custom pizza: ");
        String pizzaName = scanner.nextLine();

        
        double basePrice = 600;
        switch (size) {
            case "Small":
                basePrice += 200;
                break;
            case "Medium":
                basePrice += 1200;
                break;
            case "Large":
                basePrice += 4400;
                break;
            default:
                break;
        }

        basePrice += toppings.size() * 200;

        PizzaBuilder builder = new PizzaBuilder();
        Pizza customPizza = builder
                .setName(pizzaName)
                .setDescription("Custom Pizza")
                .setCrustType(crust)
                .setSauceType(sauce)
                .setsize(size)
                .addTopping(toppings)
                .setBasePrice(basePrice)
                .build();

        System.out.println("\nYour custom pizza has been created:");
        System.out.println(customPizza);

         System.out.print("Enter the quantity of this pizza you would like to order: ");
         int quantity = getValidInput(10);
         double totalPrice = customPizza.calculatePrice() * quantity;

        System.out.print("Would you like to save this pizza to your favorites? (yes/no): ");
        String saveToFavorites = scanner.nextLine();

        if (saveToFavorites.equalsIgnoreCase("yes")) {
            activeUser.addFavoritePizza(customPizza);
            System.out.println("Pizza saved to favorites.");
        }

        System.out.println("Would you like to confirm this order? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            String estimatedDeliveryTime = "";
            if (deliveryType.equals("Delivery")) {
                estimatedDeliveryTime = getDeliveryTimeWithFixedStart();
                if (userDeliveryAddress == null || userDeliveryAddress.isEmpty()) {
                    System.out.println("Error: Invalid delivery address.");
                    return;
                }
            } else {
                userDeliveryAddress = "Pickup";
                estimatedDeliveryTime = "0 minutes";
            }

            acceptPayment(totalPrice);

            Order newOrder = deliveryType.equals("Delivery") ? new Order(userDeliveryAddress, estimatedDeliveryTime) : new Order(1);
            
            if (newOrder == null) {
                System.out.println("Order could not be created.");
                return;
            }

            newOrder.registerObserver(activeUser);

            PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(newOrder, repository);

            CommandManager commnadManager = new CommandManager();
            commnadManager.addCommand(placeOrderCommand);
            commnadManager.executeCommands();

            System.out.println("Your order has been placed successfully!");
            System.out.println("\nPreparation time: 5 minutes");
            System.out.println("Estimated delivery time: " + estimatedDeliveryTime);
        
            activeUser.addLoyaltyPoiints(100);

            scheduleOrderTransitions(newOrder, estimatedDeliveryTime);

        } else {
            System.out.println("Order cancelled.");
        }
    }
    
    // Delivery tracking section
    
    private void scheduleOrderTransitions(Order order, String estimatedDeliveryTime) {
        Timer timer = new Timer();

        double deliveryTimeInMinutes = 0.0;
        try {
            String numericPart = estimatedDeliveryTime.split(" ")[0];
            deliveryTimeInMinutes = Double.parseDouble(numericPart);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error parsing delivery time. Defaulting to 30 minutes.");
            deliveryTimeInMinutes = 30.0;
        }

        TimerTask preparationTask = new TimerTask() {
            @Override
            public void run() {
                order.setStatus("In Preparation");
                order.nextState();
            }
        };

        TimerTask deliveryTask = new TimerTask() {
            @Override
            public void run() {
                order.setStatus("Out for Delivery");
                order.nextState();
            }
        };

        TimerTask deliveredTask = new TimerTask() {
            @Override
            public void run() {
                order.setStatus("Delivered");
                order.nextState();
                timer.cancel();
                
                requestFeedback(order);
            }
        };

        timer.schedule(preparationTask, 0);
        timer.schedule(deliveryTask, 300000);
        timer.schedule(deliveredTask, 300000 + (int) (deliveryTimeInMinutes * 60000));
    }

    private void requestFeedback(Order order) {
        System.out.println("\nYour order has been delivered! Please provide feedback.");

        System.out.print("Enter your rating (1 to 5): ");
        double rating = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter your comments: ");
        String comments = scanner.nextLine();

        Feedback feedback = new Feedback(activeUser.getUserID(), order.getOrderID(), rating, comments);
        ProvideFeedbackCommand feedbackCommand = new ProvideFeedbackCommand(feedback, repository);

        Pizza pizzaToUpdate = repository.getPizza(order.getOrderID());
        if (pizzaToUpdate != null) {
            pizzaToUpdate.setRating(feedback.getRating());
            repository.updatePizza(pizzaToUpdate);
        }

        CommandManager commandManager = new CommandManager();
        commandManager.addCommand(feedbackCommand);
        commandManager.executeCommands();
    }
    
    private void updateOrderInRepository(Order order) {
        int orderID = order.getOrderID();
        repository.removeOrder(orderID);
        repository.addOrder(order);
    }
    
    private long parseDeliveryTime(String time) {
        if (time == null || time.isEmpty()) {
            return 0;
        }

        Pattern minutesPattern = Pattern.compile("(\\d+\\.?\\d*)\\s+minutes");
        Pattern hoursPattern = Pattern.compile("(\\d+\\.?\\d*)\\s+hours");
        Pattern daysPattern = Pattern.compile("(\\d+\\.?\\d*)\\s+days");

        Matcher minutesMatcher = minutesPattern.matcher(time);
        Matcher hoursMatcher = hoursPattern.matcher(time);
        Matcher daysMatcher = daysPattern.matcher(time);

        if (minutesMatcher.find()) {
            return (long) (Double.parseDouble(minutesMatcher.group(1)) * 60 * 1000);
        } else if (hoursMatcher.find()) {
            return (long) (Double.parseDouble(hoursMatcher.group(1)) * 60 * 60 * 1000);
        } else if (daysMatcher.find()) {
            return (long) (Double.parseDouble(daysMatcher.group(1)) * 24 * 60 * 60 * 1000);
        } else {
            System.out.println("Cannot parse delivery time: " + time);
            return 0;
        }
    }

    private String getValidCrustChoice() {
        String crust = "";
        boolean valid = false;

        while (!valid) {
            System.out.println("\nSelect Crust:");
            System.out.println("1. Thin");
            System.out.println("2. Thick");
            System.out.println("3. Whole Wheat");
            System.out.print("Enter your choice: ");

            int crustChoice = getValidInput(3);
            switch (crustChoice) {
                case 1:
                    crust = "Thin";
                    valid = true;
                    break;
                case 2:
                    crust = "Thick";
                    valid = true;
                    break;
                case 3:
                    crust = "Whole Wheat";
                    valid = true;
                    break;
                default:
                    break;
            }
        }
        return crust;
    }

    private String getValidSauceChoice() {
        String sauce = "";
        boolean valid = false;

        while (!valid) {
            System.out.println("\nSelect Sauce:");
            System.out.println("1. Tomato Basil");
            System.out.println("2. Pesto");
            System.out.println("3. White Garlic");
            System.out.print("Enter your choice: ");

            int sauceChoice = getValidInput(3);
            switch (sauceChoice) {
                case 1:
                    sauce = "Tomato Basil";
                    valid = true;
                    break;
                case 2:
                    sauce = "Pesto";
                    valid = true;
                    break;
                case 3:
                    sauce = "White Garlic";
                    valid = true;
                    break;
                default:
                    break;
            }
        }
        return sauce;
    }

    private List<String> getValidToppingsChoice() {
        List<String> toppings = new ArrayList<>();
        boolean valid = false;

        while (!valid) {
            System.out.println("\nSelect Toppings (Enter numbers separated by commas, e.g., 1,2,3):");
            System.out.println("1. Mushrooms");
            System.out.println("2. Olives");
            System.out.println("3. Pepperoni");
            System.out.println("4. Spinach");
            System.out.print("Enter your choices: ");

            String[] toppingChoices = scanner.nextLine().split(",");
            valid = true;

            if (toppingChoices.length < minToppings || toppingChoices.length > maxToppings) {
                System.out.println("Please enter a valid number of toppings between " + minToppings + " and " + maxToppings);
                valid = false;
                continue;
            }

            List<String> tempToppings = new ArrayList<>();
            for (String choice : toppingChoices) {
                try {
                    int choiceNum = Integer.parseInt(choice.trim());
                    String toppingName = "";
                    switch (choiceNum) {
                        case 1:
                            toppingName = "Mushrooms";
                            break;
                        case 2:
                            toppingName = "Olives";
                            break;
                        case 3:
                            toppingName = "Pepperoni";
                            break;
                        case 4:
                            toppingName = "Spinach";
                            break;
                        default:
                            valid = false;
                            System.out.println("Invalid topping choice: " + choice);
                            break;
                    }

                    if (valid) {
                        if (tempToppings.contains(toppingName)) {
                            System.out.println("Topping " + toppingName + " is already added");
                            valid = false;
                            break;
                        } else {
                            tempToppings.add(toppingName);
                        }
                    } else {
                        break;
                    }

                } catch (NumberFormatException e) {
                    valid = false;
                    System.out.println("Invalid topping choice: " + choice);
                    break;
                }
            }

            if (valid) {
                toppings.addAll(tempToppings);
            } else {
                System.out.println("Please enter valid topping choices.");
            }
        }
        return toppings;
    }

    private String getValidCheeseChoice() {
        String cheese = "";
        boolean valid = false;

        while (!valid) {
            System.out.println("\nSelect Cheese:");
            System.out.println("1. Mozzarella");
            System.out.println("2. Cheddar");
            System.out.println("3. Vegan");
            System.out.print("Enter your choice: ");

            int cheeseChoice = getValidInput(3);
            switch (cheeseChoice) {
                case 1:
                    cheese = "Mozzarella";
                    valid = true;
                    break;
                case 2:
                    cheese = "Cheddar";
                    valid = true;
                    break;
                case 3:
                    cheese = "Vegan";
                    valid = true;
                    break;
                default:
                    break;
            }
        }
        return cheese;
    }

    private String getValidSizeChoice() {
        String size = "";
        boolean valid = false;

        while (!valid) {
            System.out.println("\nSelect Size:");
            System.out.println("1. Small");
            System.out.println("2. Medium");
            System.out.println("3. Large");
            System.out.print("Enter your choice: ");

            int sizeChoice = getValidInput(3);
            switch (sizeChoice) {
                case 1:
                    size = "Small";
                    valid = true;
                    break;
                case 2:
                    size = "Medium";
                    valid = true;
                    break;
                case 3:
                    size = "Large";
                    valid = true;
                    break;
                default:
                    break;
            }
        }
        return size;
    }

    private int getValidInput(int maxChoice) {
        int choice = -1;
        while (choice < 1 || choice > maxChoice) {
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a valid number: ");
                scanner.next();
            }
           try {
                choice = scanner.nextInt();
           } catch (Exception e) {
               choice = -1;
               scanner.next();
           }
           
           scanner.nextLine();
            if (choice < 1 || choice > maxChoice) {
                System.out.println("Invalid option. Please try again.");
            }
        }
        return choice;
    }

    private void viewPizzaMenu() {
        System.out.println("\n--- View Pizza Menu ---\n");

        List<Pizza> allPizzas = repository.getAllPizzas();

        if (allPizzas.isEmpty()) {
            System.out.println("No pizzas found in the menu.");
        } else {
            while (true) {
                for (Pizza pizza : allPizzas) {
                    System.out.println(pizza);
                    System.out.println("--------------------------");
                }

                System.out.println("Options:");
                System.out.println("1. Order a pizza");
                System.out.println("2. Add a pizza to favorites");
                System.out.println("3. Return to main menu");
                System.out.print("Enter your choice: ");

                int choice = getValidInput(3);

                switch (choice) {
                    case 1:
                        orderPizzaWithCustomization(allPizzas);
                        break;

                    case 2:
                        addPizzaToFavorites(allPizzas);
                        break;

                    case 3:
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private void orderPizzaWithCustomization(List<Pizza> allPizzas) {
        System.out.print("Enter the ID of the pizza you'd like to order: ");
        int pizzaID = getValidInput(allPizzas.size());

        Pizza selectedPizza = repository.getPizza(pizzaID);
        if (selectedPizza == null) {
            System.out.println("Invalid pizza ID. Please try again.");
            return;
        }

        System.out.println("\nSelected Pizza:");
        System.out.println(selectedPizza);

        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Order as-is");
        System.out.println("2. Customize and order");
        System.out.print("Enter your choice: ");
        int choice = getValidInput(2);

        switch (choice) {
            case 1:
                placeNewOrder(selectedPizza);
                break;

            case 2:
                customizePizza(selectedPizza);
                break;

            default:
                System.out.println("Invalid choice.");
                return;
        }
    }
    
    private void customizePizza(Pizza selectedPizza) {
        CustomizedPizza customizedPizza = new CustomizedPizza(selectedPizza);

        System.out.println("\nCustomize your pizza:");

        CustomizationHandler crustHandler = new CrustHandler();
        CustomizationHandler sauceHandler = new SauceHandler();
        CustomizationHandler toppingHandler = new ToppingHandler();
        CustomizationHandler extraFeatureHandler = new ExtraFeatureHandler();

        crustHandler.setNextHandler(sauceHandler);
        sauceHandler.setNextHandler(toppingHandler);
        toppingHandler.setNextHandler(extraFeatureHandler);

        System.out.println("\nSelect Crust:");
        System.out.println("1. Thin (Default)");
        System.out.println("2. Thick (+Rs.200)");
        System.out.println("3. Whole Wheat (+Rs.300)");
        System.out.print("Enter your choice: ");
        int crustChoice = getValidInput(3);

        if (crustChoice == 2) {
            crustHandler.handleRequest(customizedPizza, "Crust:Thick");
        } else if (crustChoice == 3) {
            crustHandler.handleRequest(customizedPizza, "Crust:Whole Wheat");
        }

        System.out.println("\nSelect Sauce:");
        System.out.println("1. Tomato Basil (Default)");
        System.out.println("2. Pesto (+Rs.100)");
        System.out.println("3. White Garlic (+Rs.150)");
        System.out.print("Enter your choice: ");
        int sauceChoice = getValidInput(3);

        if (sauceChoice == 2) {
            sauceHandler.handleRequest(customizedPizza, "Sauce:Pesto");
        } else if (sauceChoice == 3) {
            sauceHandler.handleRequest(customizedPizza, "Sauce:White Garlic");
        }

        System.out.println("\nAdd Toppings (Type 'done' to finish):");
        String[] availableToppings = {"Mushrooms", "Olives", "Pepperoni", "Spinach"};
        while (true) {
            System.out.println("1. Mushrooms (+Rs.100)");
            System.out.println("2. Olives (+Rs.100)");
            System.out.println("3. Pepperoni (+Rs.150)");
            System.out.println("4. Spinach (+Rs.100)");
            System.out.print("Enter your choice (or type 'done'): ");
            String toppingInput = scanner.nextLine();
            if (toppingInput.equalsIgnoreCase("done")) {
                break;
            }

            try {
                int toppingChoice = Integer.parseInt(toppingInput);
                if (toppingChoice >= 1 && toppingChoice <= availableToppings.length) {
                    toppingHandler.handleRequest(customizedPizza, "Topping:" + availableToppings[toppingChoice - 1]);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        
        System.out.println("\nAdd Special Features:");
        while (true) {
            System.out.println("1. Extra Cheese (+Rs.100)");
            System.out.println("2. Extra Pepperoni (+Rs.150)");
            System.out.println("3. Special Packaging (+Rs.50)");
            System.out.println("4. Done");
            System.out.print("Enter your choice: ");
            int featureChoice = getValidInput(4);

            switch (featureChoice) {
                case 1:
                    extraFeatureHandler.handleRequest(customizedPizza, "Extra:Cheese");
                    break;
                case 2:
                    extraFeatureHandler.handleRequest(customizedPizza, "Extra:Pepperoni");
                    break;
                case 3:
                    extraFeatureHandler.handleRequest(customizedPizza, "Extra:Packaging");
                    break;
                case 4:
                    System.out.println("\nFinal Customized Pizza:");
                    System.out.println(customizedPizza);
                    placeNewOrder(customizedPizza);
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addPizzaToFavorites(List<Pizza> allPizzas) {
        System.out.print("Enter the ID of the pizza you'd like to add to your favorites: ");
        int pizzaID = getValidInput(allPizzas.size());

        Pizza selectedPizza = repository.getPizza(pizzaID);
        if (selectedPizza == null) {
            System.out.println("Invalid pizza ID. Please try again.");
            return;
        }

        activeUser.addFavoritePizza(selectedPizza);
        System.out.println("Pizza added to favorites successfully.");
    }
    
    private void placeNewOrder(Pizza pizza) {
        System.out.println("\nPlacing your order...");

        System.out.print("Enter the quantity of this pizza you would like to order: ");
        int quantity = getValidInput(10);
        double totalPrice = pizza.calculatePrice() * quantity;

        String estimatedDeliveryTime = getDeliveryTimeWithFixedStart();
        if (userDeliveryAddress == null || userDeliveryAddress.isEmpty()) {
            System.out.println("Error: Invalid delivery address.");
            return;
        }

        acceptPayment(totalPrice);

        Order newOrder = new Order(userDeliveryAddress, estimatedDeliveryTime);
        newOrder.registerObserver(activeUser);

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(newOrder, repository);

        CommandManager commandManager = new CommandManager();
        commandManager.addCommand(placeOrderCommand);
        commandManager.executeCommands();

        System.out.println("Your order has been placed successfully!");
        System.out.println("Preparation time: 5 minutes");
        System.out.println("Estimated delivery time: " + estimatedDeliveryTime);
        
        activeUser.addLoyaltyPoiints(100);

        scheduleOrderTransitions(newOrder, estimatedDeliveryTime);
    }



    private void viewProfile() {
        System.out.println("\n--- My Profile ---\n");
        if (activeUser != null) {
            System.out.println("Your ID: " + activeUser.getUserID());
            System.out.println("Name: " + activeUser.getName());
            System.out.println("Email: " + activeUser.getEmail());
            System.out.println("Phone Number: " + activeUser.getPhoneNumber());
            System.out.println("Loyalty Points: " + activeUser.getLoyaltyPoints());
        } else {
            System.out.println("No user profile found.");
        }
    }

    
    // Favorites pizza management
    
    private void manageFavorites() {
        System.out.println("\n--- My Favorites ---\n");

        if (activeUser == null || activeUser.getFavorites().isEmpty()) {
            System.out.println("You have no favorite pizzas saved.");
            return;
        }

        System.out.println("Your favorite pizzas:");
        for (Pizza pizza : activeUser.getFavorites()) {
            System.out.println(pizza);
            System.out.println("--------------------------");
        }

        System.out.println("Options:");
        System.out.println("1. Order a favorite pizza");
        System.out.println("2. Remove a pizza from favorites");
        System.out.println("3. Return to main menu");
        System.out.print("Enter your choice: ");

        int choice = getValidInput(3);

        switch (choice) {
            case 1:
                orderFavoritePizza();
                break;

            case 2:
                removePizzaFromFavorites();
                break;

            case 3:
                return;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void orderFavoritePizza() {
        if (activeUser == null || activeUser.getFavorites().isEmpty()) {
            System.out.println("You have no favorite pizzas to order.");
            return;
        }

        System.out.println("Enter the ID of the favorite pizza you'd like to order: ");
        for (int i = 0; i < activeUser.getFavorites().size(); i++) {
            System.out.println((i + 1) + ". " + activeUser.getFavorites().get(i).getName());
        }

        int pizzaChoice = getValidInput(activeUser.getFavorites().size());
        Pizza selectedPizza = activeUser.getFavorites().get(pizzaChoice - 1);

        if (selectedPizza == null) {
            System.out.println("Invalid pizza ID. Please try again.");
            return;
        }
        
        placeNewOrder(selectedPizza);
    }
    
    private void removePizzaFromFavorites() {
        if (activeUser == null || activeUser.getFavorites().isEmpty()) {
            System.out.println("You have no favorite pizzas to remove.");
            return;
        }

        System.out.print("Enter the ID of the favorite pizza you'd like to remove: ");
        for (int i = 0; i < activeUser.getFavorites().size(); i++) {
            System.out.println((i + 1) + ". " + activeUser.getFavorites().get(i).getName());
        }

        int pizzaChoice = getValidInput(activeUser.getFavorites().size());
        Pizza selectedPizza = activeUser.getFavorites().get(pizzaChoice - 1);

        if (selectedPizza == null) {
            System.out.println("Invalid pizza ID. Please try again.");
            return;
        }

        activeUser.removeFavoritePizza(selectedPizza.getPizzaID());
        System.out.println("Pizza removed from favorites successfully.");
    }

    private void viewOrderHistory() {
        System.out.println("\n--- Order History ---\n");
        
        List<Order> allOrders = repository.getAllOrders();

        if (allOrders.isEmpty()) {
            System.out.println("No orders found in your order history.");
        } else {
            for (Order order : allOrders) {
                System.out.println(order);
            }
        }
    }

    private void trackOrder() {
        System.out.println("\n--- Track Order ---");
        
        List<Order> allOrders = repository.getAllOrders();

        if (allOrders.isEmpty()) {
            System.out.println("No orders found in your order history.");
        } else {
            for (Order order : allOrders) {
                System.out.println(order);
            }
            
            System.out.print("Enter the order ID to track: ");
            int orderIDToTrack = scanner.nextInt();
            scanner.nextLine();
            
            Order order = repository.getOrder(orderIDToTrack);
            
            if (order == null) {
                System.out.println("Order not found with ID: " + orderIDToTrack);
                return;
            }
            
            System.out.println("Tracking order: " + order.getOrderID());
            trackOrderHelper(order);
            System.out.println("Current order status: " + order.getStateName());
        }
    }
    
    private void trackOrderHelper(Order order) {
        System.out.println("Tracking the order...");
        
        if(order.getStateName().equals("Placed")) {
            System.out.println("\nOrder is " + order.getStateName());
        } else if (order.getStateName().equals("In Preparation")) {
            System.out.println("\nOrder is " + order.getStateName());
        } else if (order.getStateName().equals("Out for Delivery")) {
           System.out.println("\nOrder is " + order.getStateName());
        } else {
          System.out.println("\nOrder is already " + order.getStateName());
        }
    }

    
    // Promotions management
    
    private void viewPromotions() {
        System.out.println("\n--- Seasonal Specials and Promotions ---\n");

        List<Pizza> allPizzas = repository.getAllPizzas();
        List<Pizza> pizzasWithPromotions = promotionManager.getPizzasWithPromotions(allPizzas);

        if (pizzasWithPromotions.isEmpty()) {
            System.out.println("No promotions available at the moment.");
            return;
        }

        while (true) {
            System.out.println("\nPizzas with Promotions:");
            int index = 1;
            for (Pizza pizza : pizzasWithPromotions) {
                System.out.println(index + ". " + pizza);
                System.out.println("Promotions Applied:");
                for (PromotionStrategy promotion : promotionManager.getActivePromotions()) {
                    if (promotion.isEligible(pizza)) {
                        System.out.println(" - " + promotion.getPromotionDetails());
                    }
                }
                System.out.println("--------------------------");
                index++;
            }

            System.out.println("\nOptions:");
            System.out.println("1. Order a pizza from this list");
            System.out.println("2. Return to main menu");
            System.out.print("Enter your choice: ");

            int choice = getValidInput(2);

            switch (choice) {
                case 1:
                    orderPromotedPizza(pizzasWithPromotions);
                    return;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void orderPromotedPizza(List<Pizza> pizzasWithPromotions) {
        System.out.print("\nEnter the number of the pizza you'd like to order: ");
        int pizzaIndex = getValidInput(pizzasWithPromotions.size()) - 1;

        Pizza selectedPizza = pizzasWithPromotions.get(pizzaIndex);

        System.out.println("\nSelected Pizza:");
        System.out.println(selectedPizza);

        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Order as-is");
        System.out.println("2. Customize and order");
        System.out.print("Enter your choice: ");
        int choice = getValidInput(2);

        switch (choice) {
            case 1:
                placeNewOrderWithPromotion(selectedPizza);
                break;
            case 2:
                customizePizzaWithPromotion(selectedPizza);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void placeNewOrderWithPromotion(Pizza pizza) {
        System.out.println("\nPlacing your order...");
        
        System.out.print("Enter the quantity of this pizza you would like to order: ");
        int quantity = getValidInput(10);

        double finalPrice = pizza.getBasePrice();
        for (PromotionStrategy promotion : promotionManager.getActivePromotions()) {
            if (promotion.isEligible(pizza)) {
                finalPrice = promotion.applyDiscount(pizza);
                System.out.println("Promotion applied: " + promotion.getPromotionDetails());
            }
        }

        System.out.println("Final Price after Promotions: Rs." + finalPrice);
        double totalPrice = finalPrice * quantity;

        acceptPayment(totalPrice);

        String estimatedDeliveryTime = getDeliveryTimeWithFixedStart();
        Order newOrder = new Order(userDeliveryAddress, estimatedDeliveryTime);
        repository.addOrder(newOrder);
        
        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(newOrder, repository);

        CommandManager commandManager = new CommandManager();
        commandManager.addCommand(placeOrderCommand);
        commandManager.executeCommands();

        scheduleOrderTransitions(newOrder, estimatedDeliveryTime);
        System.out.println("Your order has been placed successfully!");
        return;
        
    }
    
    private void customizePizzaWithPromotion(Pizza pizza) {
        CustomizedPizza customizedPizza = new CustomizedPizza(pizza);
        customizePizza(customizedPizza);

        System.out.print("Enter the quantity of this pizza you would like to order: ");
        int quantity = getValidInput(10);

        double finalPrice = customizedPizza.calculatePrice();
        for (PromotionStrategy promotion : promotionManager.getActivePromotions()) {
            if (promotion.isEligible(customizedPizza)) {
                finalPrice = promotion.applyDiscount(customizedPizza);
                System.out.println("Promotion applied: " + promotion.getPromotionDetails());
            }
        }

        System.out.println("Final Price after Promotions and Customization: Rs." + finalPrice);
        double totalPrice = finalPrice * quantity;
        acceptPayment(totalPrice);

        String estimatedDeliveryTime = getDeliveryTimeWithFixedStart();
        Order newOrder = new Order(userDeliveryAddress, estimatedDeliveryTime);
        newOrder.registerObserver(activeUser);

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(newOrder, repository);

        CommandManager commandManager = new CommandManager();
        commandManager.addCommand(placeOrderCommand);
        commandManager.executeCommands();

        scheduleOrderTransitions(newOrder, estimatedDeliveryTime);
        System.out.println("Your order has been placed successfully!");
        return;
    }
    
    private void initializePromotions() {
        promotionManager = new PromotionManager();
        promotionManager.addPromotion(new ToppingDiscountPromotion("Mushrooms", 100));
        promotionManager.addPromotion(new SizeDiscountPromotion("Large", 400));
    }

    // Feedback management
    
    private void provideFeedback() {
        System.out.println("\n--- Provide Feedback ---");

        List<Order> allOrders = repository.getAllOrders();

        if (allOrders.isEmpty()) {
            System.out.println("No orders found. Feedback cannot be submitted.");
            return;
        }

        System.out.println("Select an order to provide feedback for:");
        for (Order order : allOrders) {
            System.out.println(order.getOrderID() + ". " + order);
        }

        System.out.print("Enter the order ID: ");
        int orderID = getValidInput(allOrders.size());

        Order selectedOrder = repository.getOrder(orderID);
        if (selectedOrder == null) {
            System.out.println("Invalid order ID. Feedback submission canceled.");
            return;
        }

        System.out.print("Enter your rating (1 to 5): ");
        double rating = scanner.nextDouble();
        
        // Clear the buffer as usual
        scanner.nextLine();

        System.out.print("Enter your comments: ");
        String comments = scanner.nextLine();

        Feedback feedback = new Feedback(activeUser.getUserID(), selectedOrder.getOrderID(), rating, comments);
        ProvideFeedbackCommand feedbackCommand = new ProvideFeedbackCommand(feedback, repository);
        
        Pizza pizzaToUpdate = repository.getPizza(selectedOrder.getOrderID());
        pizzaToUpdate.setRating(feedback.getRating());
        repository.updatePizza(pizzaToUpdate);

        CommandManager commandManager = new CommandManager();
        commandManager.addCommand(feedbackCommand);
        commandManager.executeCommands();
    }

    private void viewFeedback() {
        System.out.println("\n--- View Feedback ---");

        List<Feedback> allFeedbacks = repository.getAllFeedbacks();
        if (allFeedbacks.isEmpty()) {
            System.out.println("No feedback has been submitted yet.");
            return;
        }

        System.out.println("Feedback List:");
        for (Feedback feedback : allFeedbacks) {
            System.out.println(feedback);
        }
    }
    
    public static void main(String[] args) {
        megaOne.initializePromotions();
        megaOne.pizzaFromMEGAOnE();
        megaOne.userCreation();
        megaOne.initiateSystem();
    }
}

