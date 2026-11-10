package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import logic.Message;
/**
 * The {@code CEOController} class handles the actions available to the CEO in the application,
 * including navigating to the Quarterly Reports screen and exiting the application.
 */
public class CEOController {
	
	private Client client; // Reference to the existing Client instance
    private User currentUser;


    /**
     * Sets the {@code Client} instance for this controller.
     *
     * @param client the client instance to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    /**
     * Sets the {@code User} instance for this controller.
     *
     * @param user the user instance to set
     */
    public void setUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Handles the "Quarterly Reports" button click event. Loads the Quarterly Reports screen
     * and passes the current client and user instances to the {@code QuarterlyReportsController}.
     */
    @FXML
    private void handleQuarterlyReportsButtonClick() {
        try {
            // Load the Quarterly Reports screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuarterlyReportsScreen.fxml"));
            Parent root = loader.load();

            // Get the controller of the QuarterlyReportsScreen and pass the current client instance to it
            QuarterlyReportsController quarterlyReportsController = loader.getController();
            quarterlyReportsController.setClient(client); // Pass the existing client
            quarterlyReportsController.setUser(currentUser);

            // Set up the new scene in a new stage
            Stage stage = new Stage();
            stage.setTitle("Quarterly Reports");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Show an error message if the screen fails to load
            showAlert("Error", "Failed to load the Quarterly Reports screen");
        }
    }
    /**
     * Handles the "Exit" button click event. Disconnects the client from the server
     * and closes the CEO window.
     *
     * @param event the ActionEvent triggered by clicking the Exit button
     */
    @FXML
    private void handleExitButton(ActionEvent event) {
        try {
            // Disconnect the client from the server
            if (client != null) {
                client.sendToServer(new Message("exit", null));
                client.closeConnection();
            }

            // Close the ceo window
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
     * Initializes the controller. Displays a welcome message using the current user's first name.
     */
    @FXML
    public void initialize() {
        // Initialization logic here
    	if (currentUser != null) {
            System.out.println("Welcome, " + currentUser.getFirstName() + "!");
            // Other initialization code using the user data
        }
    }
    
    /**
     * Shows an informational alert with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the content of the alert
     */
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Add methods to handle user interactions here
}
