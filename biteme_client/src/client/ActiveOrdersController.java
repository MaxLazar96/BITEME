package client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.Message;
import logic.Order;
/**
 * The {@code ActiveOrdersController} class is responsible for managing the user's active orders.
 * It interacts with the server to fetch, display, and update the status of orders in the client-side application.
 *
 * <p>This class provides functionalities such as:</p>
 * <ul>
 *   <li>Loading active orders for the current user.</li>
 *   <li>Accepting delivery for an order and handling potential refunds for late deliveries.</li>
 *   <li>Refreshing the list of active orders.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * ActiveOrdersController controller = new ActiveOrdersController();
 * controller.setClient(clientInstance);
 * controller.setUser(currentUser);
 * }
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
public class ActiveOrdersController {

    @FXML
    private TableView<Order> activeOrdersTable;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, String> statusColumn;
    @FXML
    private TableColumn<Order, String> detailsColumn;
    @FXML
    private TableColumn<Order, String> deliveryTypeColumn;

    private Client client; // Reference to the existing Client instance
    private User currentUser; // Reference to the current user
    private int timeDifference;
    /**
     * Sets the client instance used for server communication.
     *
     * @param client the client instance to be used by this controller
     */
    public void setClient(Client client) {
        this.client = client;
    }
    /**
     * Sets the current user and loads their active orders.
     *
     * @param user the user currently logged in
     */
    public void setUser(User user) {
        this.currentUser = user;
        loadActiveOrders();
        client.setActiveOrdersController(this);
    }

    /**
     * Loads the active orders for the current user by sending a request to the server.
     */
    private void loadActiveOrders() {
        if (client != null && currentUser != null) {
            client.sendToServer(new Message("GetActiveOrders", currentUser.getId()));
        }
    }
    
    /**
     * Handles the response from the server containing the list of active orders and displays them in the table.
     *
     * @param orders the list of active orders received from the server
     */
    public void handleActiveOrdersResponse(List<Order> orders) {
        activeOrdersTable.getItems().setAll(orders);
    }
    /**
     * Refreshes the list of active orders by reloading them from the server.
     */
    @FXML
    private void handleRefreshBtn() {
    	loadActiveOrders();
    }

    /**
     * Handles the action when the user accepts the delivery of an order.
     * Updates the order status and credits if the delivery was late, based on the delivery type.
     *
     * @param event the action event triggered by the accept delivery button
     */
    @FXML
    private void handleAcceptDeliveryButton(ActionEvent event) {
        Order selectedOrder = activeOrdersTable.getSelectionModel().getSelectedItem();
        double credits = currentUser.getCredits();

        if (selectedOrder == null) {
            showAlert("Selection Error", "Please select an order to accept delivery.");
            return;
        }
        System.out.println("\n<ActiveOrdersController> total price "+selectedOrder.getTotalPrice()+"\n");

        int orderId = selectedOrder.getId();
        String status = selectedOrder.getStatus();
        String deliverytype = selectedOrder.getDeliveryType();
        System.out.println("<ActiveOrdersController> Selected Order ID: " + orderId);
        System.out.println("<ActiveOrdersController> Selected Order status: " + status);
        System.out.println("<ActiveOrdersController> Selectef Delivery type: " + deliverytype + "\n");
        
        
        if(!(status.equals( "In Delivery"))) {
        	showAlert("Update fail!","Can't accept an order that is not in delivery yet!");
        	return;
        }

        // Send order ID to the server to update the status
        client.sendToServer(new Message("AcceptDelivery", orderId));
        
        client.sendToServer(new Message("Times", orderId));
        
        System.out.println("\n-----WAITING-----\n");
        try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("<ActiveOrdersController> Time difference is: " + timeDifference);
        
        
        if(deliverytype.equals("Regular") && timeDifference > 60){
        	System.out.println("delivery type" +deliverytype + "detected");
        	 credits += selectedOrder.getTotalPrice() * 0.5;
        	showAlert("We Are Sorry!", "sorry for arriving late :( , a 50% refund has been added to your account");
        	client.sendToServer(new Message("updateLate",orderId));
        }else if(deliverytype.equals("Business") && timeDifference > 60) {
        	System.out.println("delivery type" +deliverytype + "detected");
        	 credits += selectedOrder.getTotalPrice() * 0.5;
        	showAlert("We Are Sorry!", "sorry for arriving late :( , a 50% refund has been added to your account");
        	client.sendToServer(new Message("updateLate",orderId));
        }else if(deliverytype.equals("Fast") && timeDifference > 20) {
        	System.out.println("delivery type" +deliverytype + "detected");
        	 credits += selectedOrder.getTotalPrice() * 0.5;
        	showAlert("We Are Sorry!", "sorry for arriving late :( , a 50% refund has been added to your account");
        	client.sendToServer(new Message("updateLate",orderId));
        }else {
        	showAlert("","Bon Appétit");
        }
        
        currentUser.setCredits(credits);
        Object[] obj = new Object[]{currentUser.getId(), credits};
        client.sendToServer(new Message("updateCredits",obj));
        

        
        //send credits to DB
        System.out.println("\n<ActiveOrdersController> credits are: " + credits);
        
        loadActiveOrders();
    }

    

    /**
     * Initializes the columns of the active orders table.
     */
    @FXML
    private void initialize() {
        orderIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        detailsColumn.setCellValueFactory(data -> {
            String detailsInfo = data.getValue().getDishes().stream()
                .map(dish -> dish.getName() + " (Remark: " + (dish.getRemark() != null ? dish.getRemark() : "No Remark") + ")")
                .collect(Collectors.joining("\n"));
            return new SimpleStringProperty(detailsInfo);
        });
        deliveryTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDeliveryType()));
    }
    /**
     * Displays an alert with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the message to display in the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Sets the time difference (in minutes) between the order's start and end times.
     *
     * @param timeDifference the time difference to set
     */
	public void setTimeDifference(int timeDifference) {
		this.timeDifference = timeDifference;
	}
}
