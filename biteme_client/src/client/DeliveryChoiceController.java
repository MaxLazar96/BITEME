package client;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.Dish;
import logic.Message;
import logic.Order;
import logic.Restaurant;

import java.io.IOException;
import java.util.List;
/**
 * The DeliveryChoiceController class handles the delivery and takeaway options for the customer's order.
 * It manages the user's input for delivery details, calculates the total price, and sends the order to the server.
 */
public class DeliveryChoiceController {

    @FXML
    private VBox orderSummaryBox;

    @FXML
    private ToggleGroup deliveryMethodGroup;

    @FXML
    private RadioButton deliveryRadioButton, takeAwayRadioButton;

    @FXML
    private TextField phoneNumberField, emailField, deliveryAddressField;

    @FXML
    private Label deliveryAddressLabel,deliveryTypeLabel, peopleNumberLabel, deliveryDateLabel, deliveryTimeLabel;
    
    @FXML
    private CheckBox useCreditsCheckBox;

    private List<Dish> cartItems;
    private Client client;
	private User currentUser;
    private Restaurant selectedRestaurant;
    /**
     * Sets the client instance for communication with the server.
     *
     * @param client the Client instance to be set.
     */
    public void setClient(Client client) {
		this.client = client;
	}

    /**
     * Sets the current user.
     *
     * @param currentUser the User instance representing the current user.
     */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
     * Sets the selected restaurant for the order.
     *
     * @param selectedRestaurant the Restaurant instance representing the selected restaurant.
     */
	public void setSelectedRestaurant(Restaurant selectedRestaurant) {
		this.selectedRestaurant = selectedRestaurant;
	}

    
    @FXML
    private ComboBox<String> deliveryTypeComboBox, peopleNumberComboBox, deliveryTimeComboBox;

    @FXML
    private DatePicker deliveryDatePicker;
    
    
    /**
     * Initializes the DeliveryChoiceController. This method sets up the initial state of the form,
     * including the delivery method selection and visibility of fields.
     */
    @FXML
    public void initialize() {
    	deliveryMethodGroup = new ToggleGroup();
        // Link radio buttons to the ToggleGroup
        deliveryRadioButton.setToggleGroup(deliveryMethodGroup);
        takeAwayRadioButton.setToggleGroup(deliveryMethodGroup);

        // Initialize ComboBox options
        deliveryTypeComboBox.getItems().setAll("Regular", "Fast", "Business");
        peopleNumberComboBox.getItems().setAll("1", "2", "3+");
        deliveryTimeComboBox.getItems().setAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00");

        // Toggle visibility of fields based on delivery method and type selection
        deliveryMethodGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            boolean isDelivery = deliveryRadioButton.isSelected();
            deliveryAddressLabel.setVisible(isDelivery);
            deliveryAddressField.setVisible(isDelivery);
            deliveryTypeLabel.setVisible(isDelivery);
            deliveryTypeComboBox.setVisible(isDelivery);
            deliveryDateLabel.setVisible(isDelivery);
            deliveryDatePicker.setVisible(isDelivery);
            deliveryTimeLabel.setVisible(isDelivery);
            deliveryTimeComboBox.setVisible(isDelivery);
        });

        // Show people number ComboBox if "Business" delivery type is selected
        deliveryTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isBusiness = "Business".equals(newValue);
            peopleNumberLabel.setVisible(isBusiness);
            peopleNumberComboBox.setVisible(isBusiness);
        });
    }
    
    /**
     * Sets the items in the cart and populates the order summary.
     *
     * @param cartItems the list of Dish objects representing the items in the cart.
     */
    public void setCartItems(List<Dish> cartItems) {
        this.cartItems = cartItems;
        System.out.println("CartItems Set");
        populateOrderSummary();
    }
    /**
     * Populates the order summary with the items in the cart.
     * It creates a visual representation of each item and adds it to the order summary VBox.
     */
    public void populateOrderSummary() {
    	System.out.println("populateOrderSummary Called");
        if (orderSummaryBox == null || cartItems == null) {
            throw new IllegalStateException("Order summary box or cart items are not initialized.");
        }

        orderSummaryBox.getChildren().clear();

        for (Dish dish : cartItems) {
            HBox itemBox = new HBox(10);
            itemBox.setPadding(new Insets(5, 0, 5, 0));

            Label dishNameLabel = new Label(dish.getName());
            dishNameLabel.setStyle("-fx-font-weight: bold;");

            Label quantityLabel = new Label("Quantity: 1"); // Modify this if you track quantity
            Label priceLabel = new Label("Price: $" + dish.getPrice());

            String remarkText = dish.getRemark() != null ? dish.getRemark() : "No special requests";
            Label remarkLabel = new Label("Remarks: " + remarkText);

            String roastLevelText = dish.getRoastLevel() != null ? dish.getRoastLevel() : "N/A";
            Label roastLevelLabel = new Label("Roast Level: " + roastLevelText);

            itemBox.getChildren().addAll(dishNameLabel, quantityLabel, priceLabel, remarkLabel, roastLevelLabel);

            orderSummaryBox.getChildren().add(itemBox);
        }
    }
    /**
     * Handles the confirmation of the order. It validates the user's input, calculates the total price,
     * and shows an order summary dialog. If the user confirms, the order is sent to the server.
     */
    @FXML
    private void handleConfirmOrder() {
        String deliveryMethod = deliveryRadioButton.isSelected() ? "Delivery" : "TakeAway";
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String deliveryAddress = deliveryAddressField.getText();
        String deliveryType = deliveryTypeComboBox.getValue();
        if (deliveryMethod == "TakeAway"){
        	deliveryType = "Takeaway";
        }
        String numberOfPeople = peopleNumberComboBox.getValue();
        String deliveryDate = deliveryDatePicker.getValue() != null ? deliveryDatePicker.getValue().toString() : "N/A";
        String deliveryTime = deliveryTimeComboBox.getValue();
        int peopleNum = 0;

        // Perform validation if necessary
        if (phoneNumber.isEmpty() || email.isEmpty()) {
            showAlert("Validation Error", "Please fill in the required fields.");
            return;
        }
        
        if (deliveryRadioButton.isSelected() && deliveryAddress.isEmpty()) {
            showAlert("Validation Error", "Please provide a delivery address.");
            return;
        }

        // Process the order based on the provided information
        System.out.println("Order confirmed with the following details:");
        System.out.println("Delivery Method: " + deliveryMethod);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);

        if (deliveryMethod.equals("Delivery")) {
            System.out.println("Delivery Address: " + deliveryAddress);
            System.out.println("Delivery Type: " + deliveryType);
            if (deliveryType.equals("Business")) {
                System.out.println("Number of People: " + numberOfPeople);
            }
        }

        System.out.println("Delivery Date: " + deliveryDate);
        System.out.println("Delivery Time: " + deliveryTime);
        
     // Calculate the total price
        double totalPrice = cartItems.stream().mapToDouble(Dish::getPrice).sum();
        double deliveryCost = 25.0;
        double discount = 0.0;
        
        if (deliveryMethod.equals("Delivery")) {
            //String deliveryType = deliveryTypeComboBox.getValue();
            switch (deliveryType) {
                case "Fast":
                    discount = totalPrice * 0.10; // 10% discount
                    break;
                case "Business":
                    //int numberOfPeople = peopleNumberComboBox.getValue();
                    if (numberOfPeople == "1") {
                    	peopleNum = 1;
                    }
                    if(numberOfPeople == "2") {
                    	peopleNum = 2;
                    }
                    if(numberOfPeople == "3+") {
                    	peopleNum = 3;
                    }                  
                    discount = 5.0 * peopleNum; // 5 reduction per person
                    break;
                default:
                    // Regular delivery, no additional discount
                    break;
            }
            totalPrice = totalPrice + deliveryCost - discount;
        }
        
    	double remainingCredits = currentUser.getCredits();
    	 if (useCreditsCheckBox.isSelected()) {
    		 remainingCredits -= totalPrice;
    		 if (remainingCredits < 0) {
    			 remainingCredits = 0;
             }
    	 }
        
        
        // Apply credits if "Use Credits" checkbox is selected
        if (useCreditsCheckBox.isSelected()) {
            double credits = currentUser.getCredits();
            totalPrice -= credits;
            // Ensure the total price is not negative
            if (totalPrice < 0) {
                totalPrice = 0;
            }
        }
        
        // Show the order summary window
        showOrderSummaryWindow(totalPrice, discount, deliveryMethod, phoneNumber, email, deliveryAddress, deliveryType, numberOfPeople, deliveryDate, deliveryTime,deliveryCost,remainingCredits);
        
        // TODO: Send order details to the server or save them accordingly
	}
    /**
     * Displays the order summary and confirms the order with the user.
     * If confirmed, it creates the Order object and sends it to the server.
     *
     * @param totalPrice       the total price of the order
     * @param discount         the discount applied to the order
     * @param deliveryMethod   the chosen delivery method
     * @param phoneNumber      the user's phone number
     * @param email            the user's email address
     * @param deliveryAddress  the delivery address
     * @param deliveryType     the type of delivery
     * @param numberOfPeople   the number of people for business delivery
     * @param deliveryDate     the preferred delivery date
     * @param deliveryTime     the preferred delivery time
     * @param deliveryPrice    the cost of delivery
     * @param remainingCredits the remaining credits after the order
     */
    private void showOrderSummaryWindow(double totalPrice, double discount, String deliveryMethod, String phoneNumber, String email, String deliveryAddress, String deliveryType, String numberOfPeople, String deliveryDate, String deliveryTime,double deliveryprice, double remainingCredits) {
        // Create a summary message
        StringBuilder summary = new StringBuilder();
        summary.append("Your Order Summary:\n\n");

        for (Dish dish : cartItems) {
            summary.append(dish.getName())
                   .append(" - $")
                   .append(dish.getPrice())
                   .append("\n");
        }

        summary.append("\nDelivery Method: ").append(deliveryMethod);
        summary.append("\nDelivery Type: ").append(deliveryType);
        if (deliveryMethod.equals("Delivery")) {
            summary.append("\nDelivery Address: ").append(deliveryAddress);
            summary.append("\nNumber of People: ").append(numberOfPeople);
        }
        summary.append("\nPreferred Delivery Time: ").append(deliveryDate).append(" ").append(deliveryTime);
        summary.append("\nDelivery Price: $").append(deliveryprice);
        summary.append("\n\nTotal Price: $").append(totalPrice)
               .append(" (Discount Applied: $").append(discount).append(")");

        // Show the confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Order Summary");
        alert.setHeaderText("Please review your order:");
        alert.setContentText(summary.toString());

        // Only send the order to the server if the user clicks "OK"
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Create the Order object only after confirmation
                Order order = new Order();
                order.setDishes(cartItems);
                order.setUserId(currentUser.getId());
                order.setAddress(deliveryAddress);
                order.setPhoneNumber(phoneNumber);
                order.setEmail(email);
                order.setTotalPrice(totalPrice);
                order.setStatus("New");
                order.setRestaurantId(selectedRestaurant.getId());
                order.setStartTime("1000-01-01 00:00:00");  // Set default start time 
                order.setEndTime("1000-01-01 00:00:00");  // Set default end time
                order.setDeliveryType(deliveryType);
                order.setRequiredTime(deliveryDate + " " + deliveryTime);

                //send data to server
                System.out.println("sending New Order to server...");
				Message placeNewOrder = new Message("PlaceOrder", order);
				
				client.sendToServer(placeNewOrder);
				Object[] obj = new Object[]{currentUser.getId(), remainingCredits};
				client.sendToServer(new Message("updateCredits", obj));
				this.currentUser.setCredits(remainingCredits);
				
                showAlert("Success", "Order placed successfully!");
            } else {
                showAlert("Cancelled", "Order was not placed.");
            }
        });
    }
   
    /**
     * Shows an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the message content of the alert
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
