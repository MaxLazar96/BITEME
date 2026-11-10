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
 * The {@code CertifiedWorkerController} class serves as the controller for the Certified Worker interface in the
 * client application. It handles user interactions for certified workers, such as editing the menu and exiting
 * the application.
 *
 * <p>This controller interacts with the server to manage data related to the certified worker, such as the menu
 * they are authorized to edit. It also manages the user's session and ensures proper disconnection when the user exits.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * CertifiedWorkerController controller = new CertifiedWorkerController();
 * controller.setClient(clientInstance);
 * controller.setUser(currentUser);
 * }
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
public class CertifiedWorkerController {

	private Client client; // Reference to the existing Client instance
	private User currentUser;

	/**
     * Sets the client instance that this controller will use to send and receive messages to and from the server.
     *
     * @param client the {@code Client} instance
     */
	public void setClient(Client client) {
		this.client = client;
	}
	 /**
     * Sets the current user for this controller.
     *
     * @param user the {@code User} instance representing the current user
     */
	public void setUser(User user) {
		this.currentUser = user;
	}
	 /**
     * Handles the click event for the "Edit Menu" button.
     * This method loads the Menu Editor screen and passes the client and user data to the Menu Editor controller.
     */
	@FXML
	private void handleEditMenuButtonClick() {
		System.out.println("Edit Menu button clicked");

		try {
			// Load the Menu Editor FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MenuEditor.fxml"));
			Parent root = loader.load();

			// Pass the client and user data to the controller of the Menu Editor screen
			MenuEditorController menueditorController = loader.getController();

			menueditorController.setClient(client);
			menueditorController.setUser(currentUser);
			client.sendToServer(new Message("restaurantForCertified", currentUser.getId()));

			// Display the Menu Editor screen
			Stage stage = new Stage();
			stage.setTitle("Menu Editor");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to load the Menu Editor screen.");
		}
	}
	 /**
     * Handles the click event for the "Exit" button.
     * This method disconnects the client from the server and closes the current window.
     *
     * @param event the {@code ActionEvent} triggered by the button click
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
     * Displays an alert with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the content of the alert
     */
	public void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	/**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     * It can be used to set up any necessary data or event listeners.
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
