package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Message;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
/**
 * The {@code BranchManagerController} class is responsible for handling the user interface
 * and interactions for the Branch Manager screen in the client application. It allows the
 * Branch Manager to add new customers, watch reports, and exit the application.
 *
 * <p>This class provides functionalities such as:</p>
 * <ul>
 *   <li>Loading the Add Customer form and passing the existing client instance to it.</li>
 *   <li>Loading the Watch Reports screen and passing the current user and client instance to it.</li>
 *   <li>Handling the exit operation, including disconnecting the client from the server.</li>
 *   <li>Initializing the controller with user-specific data.</li>
 *   <li>Displaying alerts to the user.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * BranchManagerController controller = new BranchManagerController();
 * controller.setClient(clientInstance);
 * controller.setUser(currentUser);
 * }
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
public class BranchManagerController {

    private Client client; // Reference to the existing Client instance
    private User currentUser;


    /**
     * Sets the existing client instance for communication with the server.
     *
     * @param client the client instance to be used by this controller
     */
    public void setClient(Client client) {
        this.client = client;
    }
    /**
     * Sets the current user for this session.
     *
     * @param user the user instance representing the current user
     */
    public void setUser(User user) {
        this.currentUser = user;
    }
    /**
     * Handles the action when the "Add Customer" button is clicked.
     * This method loads the Add Customer form and passes the current client instance to it.
     *
     * @param event the action event triggered by the "Add Customer" button
     */
    @FXML
    private void handleAddCustomerButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AddCustomerForm.fxml"));
            Parent root = loader.load();

            // Get the controller of the AddCustomerForm and pass the current client instance to it
            AddCustomerController addCustomerController = loader.getController();
            addCustomerController.setClient(client);  // Pass the existing client

            Stage stage = new Stage();
            stage.setTitle("Add a Customer");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the Add Customer form");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    /**
     * Handles the action when the "Watch Reports" button is clicked.
     * This method loads the Watch Reports screen and passes the current user and client instance to it.
     *
     * @param event the action event triggered by the "Watch Reports" button
     */
    @FXML
    private void handleWatchReportsButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/WatchReportsScreen.fxml"));
            Parent root = loader.load();

            // Get the controller of the WatchReportsScreen and pass the current client instance to it
            WatchReportsController watchReportsController = loader.getController();
            watchReportsController.setClient(client);  // Pass the existing client
            watchReportsController.setUser(currentUser);  // Pass the current user

            Stage stage = new Stage();
            stage.setTitle("Watch Reports");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the Watch Reports screen");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Handles the action when the "Exit" button is clicked.
     * This method disconnects the client from the server and closes the Branch Manager window.
     *
     * @param event the action event triggered by the "Exit" button
     */
    @FXML
    private void handleExitButton(ActionEvent event) {
        try {
            // Disconnect the client from the server
            if (client != null) {
                client.sendToServer(new Message("exit", null));
                client.closeConnection();
            }

            // Close the Branch Manager window
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
     * Initializes the controller with user-specific data, such as displaying a welcome message.
     */
    @FXML
    public void initialize() {
        // Initialization logic, if needed
    	if (currentUser != null) {
            System.out.println("Welcome, " + currentUser.getFirstName() + "!");
            // Other initialization code using the user data
        }
    }
    /**
     * Displays an informational alert to the user.
     *
     * @param title   the title of the alert
     * @param message the message content of the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
