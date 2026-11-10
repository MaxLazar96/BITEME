package client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import logic.Message;
/**
 * The CustomerController class handles the customer-related actions
 * and interactions in the application. It is responsible for managing
 * the customer's active orders, creating new orders, and handling 
 * the exit process for the customer.
 */
public class CustomerController {
	
	private Client client; // Reference to the existing Client instance
    private User currentUser;
    private ClientController clientController;
    /**
     * Sets the ClientController instance.
     *
     * @param clientController the ClientController to be set.
     */
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Sets the Client instance.
     *
     * @param client the Client instance to be set.
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    /**
     * Sets the User instance.
     *
     * @param user the User instance to be set.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }
    /**
     * Handles the action of clicking the "Active Orders" button.
     * It loads the Active Orders screen and passes the current client 
     * and user instances to the ActiveOrdersController.
     *
     * @param event the ActionEvent triggered by clicking the button.
     */
    @FXML
    private void handleActiveOrdersButton(ActionEvent event) {
        // Code to handle the Active Orders button click
        System.out.println("Active Orders button clicked");
        try {
            // Load the ActiveOrders screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ActiveOrdersScreen.fxml"));
            Parent root = loader.load();

            // Get the controller for the ActiveOrders screen
            ActiveOrdersController activeOrdersController = loader.getController();

            // Pass the client and user instances to the ActiveOrdersController
            activeOrdersController.setClient(client);
            activeOrdersController.setUser(currentUser);

            // Open the new screen in a new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Active Orders");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the Active Orders screen.");
        }
    }
    
    /**
     * Handles the action of clicking the "Exit" button.
     * It disconnects the client from the server and closes the customer window.
     *
     * @param event the ActionEvent triggered by clicking the button.
     */
    @FXML
    private void handleExitButton(ActionEvent event) {
        try {
            // Disconnect the client from the server
            if (client != null) {
            	System.out.println("bye bye " + currentUser.getFirstName());
                client.sendToServer(new Message("exit", null));
                client.closeConnection();
            }

            // Close the customer window
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
     * Handles the action of clicking the "New Order" button.
     * It loads the Select Restaurant screen and requests the list of restaurants 
     * from the server.
     *
     * @param event the ActionEvent triggered by clicking the button.
     */
    @FXML
    private void handleNewOrderButtonClick(ActionEvent event) {
        try {
        	System.out.println("handleNewOrderButtonClick Clicked");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SelectRestaurantScreen.fxml"));
            Parent root = loader.load();

            SelectRestaurantController controller = loader.getController();
            controller.setClient(client);
            controller.setUser(currentUser);
            
            clientController.setSelectRestaurantController(controller);

            // Request the list of restaurants from the server
            client.sendToServer(new Message("getRestaurants", null));

            Stage stage = new Stage();
            stage.setTitle("Select a Restaurant");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open restaurant selection screen.");
        }
    }
    /**
     * Displays an alert with the specified title and message.
     *
     * @param title the title of the alert.
     * @param message the content of the alert message.
     */
    private void showAlert(String title, String message) {
        // This ensures that the alert is displayed on the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    /**
     * Initializes the CustomerController. This method is called automatically 
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialization logic here
    	if (currentUser != null) {
            System.out.println("Welcome, " + currentUser.getFirstName() + "!");
            // Other initialization code using the user data
        }
    }

    // Add methods to handle user interactions here
}
