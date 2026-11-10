package client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import logic.Dish;
import logic.Message;
import logic.Order;
import logic.Restaurant;
/**
 * The RestaurantWorkerController class is responsible for managing the restaurant worker's view in the application.
 * It handles the display and management of orders for a specific restaurant, allowing the worker to update the status
 * of orders and view the details of each order.
 */
public class RestaurantWorkerController {

    private Client client; // Reference to the existing Client instance
    private User currentUser;
    private Restaurant restaurant;
    private ClientController clientcontroller;

    @FXML
    private TableView<Order> ordersTableView;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, String> dishesColumn;
    @FXML
    private TableColumn<Order, String> orderStatusColumn;

    /**
     * Sets the client instance to be used by this controller.
     * 
     * @param client the client instance
     */
    public void setClient(Client client) {
        this.client = client;
    }
    /**
     * Sets the current user for this controller.
     * 
     * @param user the current user
     */
    public void setUser(User user) {
        this.currentUser = user;
    }
    /**
     * Sets the restaurant associated with this controller and loads the orders for that restaurant.
     * 
     * @param restaurant the restaurant associated with this controller
     */
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        loadOrders();
    }
    /**
     * Handles the exit button click event, disconnecting the client from the server and closing the window.
     * 
     * @param event the action event triggered by clicking the exit button
     */
    @FXML
    private void handleExitButton(ActionEvent event) {
        try {
            // Disconnect the client from the server
            if (client != null) {
                client.sendToServer(new Message("exit", null));
                client.closeConnection();
            }

            // Close the window
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to disconnect and close");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    /**
     * Loads the orders for the restaurant associated with the current user.
     * If either the client or the restaurant is null, it prints a message to the console.
     */
    private void loadOrders() {
        System.out.println("loadOrders initiated");
        if (client != null && restaurant != null) {
            client.sendToServer(new Message("GetOrdersForRestaurant", currentUser.getId()));
        } else if (client == null) {
            System.out.println("client is null");
        } else {
            System.out.println("restaurant is null");
        }
    }
    /**
     * Handles the refresh button click event, reloading the orders for the restaurant.
     */
    @FXML
    private void handleRefreshOrders() {
        System.out.println("Refresh button clicked");
        loadOrders();
    }
    /**
     * Handles the response from the server containing the list of orders for the restaurant.
     * 
     * @param orders the list of orders for the restaurant
     */
    public void handleOrdersForRestaurantResponse(List<Order> orders) {
        System.out.println("handleOrdersForRestaurantResponse called with orders: " + orders);
        
        // Print each order to confirm data
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId());
            System.out.println("Order Status: " + order.getStatus());
            System.out.println("Dishes: " + order.getDishes().stream().map(Dish::getName).collect(Collectors.joining(",")));    
        }

        ordersTableView.getItems().setAll(orders);
    }
    /**
     * Handles the event when the "In Preparation" button is clicked, marking the selected order as "In Preparation".
     * 
     * @param event the action event triggered by clicking the "In Preparation" button
     */
    @FXML
    private void handleInPreparationButton(ActionEvent event){
    	
        // Get the selected order from the TableView
        Order selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
        
        if (selectedOrder == null) {
            showAlert("Selection Error", "Please select an order to mark as 'In Preparation'.");
            return;
        }

        // Extract the order ID from the selected order
        int orderId = selectedOrder.getId(); 
        System.out.println("Selected Order ID: " + orderId); // Print the Order ID for debugging

        // You can now use this order ID for further processing
        // For example, send it to the server to update the order status
        client.sendToServer(new Message("UpdateInPreperation", orderId));
        
        loadOrders();
        
    }
    /**
     * Handles the event when the "In Delivery" button is clicked, marking the selected order as "In Delivery".
     * 
     * @param event the action event triggered by clicking the "In Delivery" button
     */
    @FXML
    private void handleInDeliveryButton(ActionEvent event) {
Order selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
        
        if (selectedOrder == null) {
            showAlert("Selection Error", "Please select an order to mark as 'In Preparation'.");
            return;
        }

        // Extract the order ID from the selected order
        int orderId = selectedOrder.getId(); 
        System.out.println("Selected Order ID: " + orderId); // Print the Order ID for debugging
        client.sendToServer(new Message("UpdateInDelivery", orderId));
        
        loadOrders();
    }


    /**
     * Initializes the controller and sets up the TableView columns.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("\ninitialize method called\n");

        orderIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());

        // Modify to use "\n" to ensure each dish and its remark are on a new line
        dishesColumn.setCellValueFactory(data -> {
            String dishesInfo = data.getValue().getDishes().stream()
                .map(dish -> dish.getName() + " (Remark: " + (dish.getRemark() != null ? dish.getRemark() : "No Remark") + ")")
                .collect(Collectors.joining("\n"));  // Use "\n" to separate each dish and remark
            return new SimpleStringProperty(dishesInfo);
        });

        orderStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
    }
    /**
     * Displays an alert with the specified title and message.
     * 
     * @param title   the title of the alert
     * @param message the message to be displayed in the alert
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Sets the client controller associated with this controller.
     * 
     * @param clientcontroller the client controller to be set
     */
	public void setClientcontroller(ClientController clientcontroller) {
		this.clientcontroller = clientcontroller;
	}


//    @FXML
//    private void initialize() {
//        orderIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
//
//        // Use the getDishesAsString method to set the dishes column
//        dishesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDishesAsString()));
//
//        orderStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
//    }

    // Add methods to handle user interactions here
}
