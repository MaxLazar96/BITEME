package client;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import logic.Message;
/**
 * The {@code AddCustomerController} class is responsible for handling the user interface
 * and interactions for adding a new customer in the client application. It collects
 * customer details from the user, displays the entered information, and sends it to the server.
 *
 * <p>This class provides functionalities such as:</p>
 * <ul>
 *   <li>Initializing the home branch options in a ComboBox.</li>
 *   <li>Collecting and validating customer data from the form fields.</li>
 *   <li>Displaying the entered customer information in a popup.</li>
 *   <li>Sending the collected customer data to the server.</li>
 *   <li>Handling the close button to exit the form.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * AddCustomerController controller = new AddCustomerController();
 * controller.setClient(clientInstance);
 * }
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
public class AddCustomerController {

    @FXML
    private TextField idField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField creditsField;
    @FXML
    private ComboBox<String> homeBranchComboBox;

    private Client client; // Reference to the client object to send messages to the server
    /**
     * Sets the client instance used for server communication.
     *
     * @param client the client instance to be used by this controller
     */
    public void setClient(Client client) {
        this.client = client;
    }
    /**
     * Initializes the controller, populating the home branch ComboBox with available options.
     */
    @FXML
    public void initialize() {
        // Populate the ComboBox with the home branch options
        homeBranchComboBox.setItems(FXCollections.observableArrayList("North", "Center", "South"));
    }
    /**
     * Handles the submit button action by collecting the customer details,
     * displaying the entered information in a popup, and sending the data to the server.
     */
    @FXML
    private void handleSubmitButton() {
        // Collecting form data
        String id = idField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String phoneNumber = phoneNumberField.getText();
        String credits = creditsField.getText();
        String homeBranch = homeBranchComboBox.getValue();
        String userType = "customer"; // Automatically setting the usertype as 'customer'

        // Show the popup with the entered information
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("First Name: ").append(firstName).append("\n");
        sb.append("Last Name: ").append(lastName).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Address: ").append(address).append("\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Password: ").append(password).append("\n");
        sb.append("Phone Number: ").append(phoneNumber).append("\n");
        sb.append("Credits: ").append(credits).append("\n");
        sb.append("Home Branch: ").append(homeBranch).append("\n");
        sb.append("User Type: ").append(userType);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Customer Information");
        alert.setHeaderText(null);
        alert.setContentText(sb.toString());
        alert.showAndWait();

        // Send the data to the server
        String[] customerData = {id, firstName, lastName, email, address, username, password, phoneNumber, credits, homeBranch, userType};
        Message addCustomerMessage = new Message("addCustomer", customerData);
        client.sendToServer(addCustomerMessage);
    }
    /**
     * Handles the close button action to close the current window.
     *
     * @param event the action event triggered by the close button
     */
    @FXML
    private void handleCloseButton(ActionEvent event) {
        // Close the current window
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
