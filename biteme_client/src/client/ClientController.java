package client;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
/**
 * The ClientController class manages the main client-side functionality,
 * including handling user authentication, connecting to the server, and
 * redirecting users based on their roles.
 */
public class ClientController {

    @FXML
    private TextField hostField, portField, usernameField;
    @FXML
    private PasswordField passwordField;
    private ClientController clientController;
    private Client client;
    private User currentUser; // Store the User object for the session
    private SelectRestaurantController selectRestaurantController;
    private MenuController menuController;
    private RestaurantWorkerController restaurantWorkerController;
    /**
     * Returns the RestaurantWorkerController instance.
     *
     * @return the RestaurantWorkerController instance.
     */
    public RestaurantWorkerController getRestaurantWorkerController() {
		return restaurantWorkerController;
	}
    /**
     * Sets the MenuController instance.
     *
     * @param menuController the MenuController instance to be set.
     */
	public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }
	/**
     * Returns the MenuController instance.
     *
     * @return the MenuController instance.
     */
    public MenuController getMenuController() {
        return this.menuController;
    }
    /**
     * Returns the Client instance.
     *
     * @return the Client instance.
     */
    public Client getClient() {
        return client;
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
     * Sets the SelectRestaurantController instance.
     *
     * @param controller the SelectRestaurantController instance to be set.
     */
    public void setSelectRestaurantController(SelectRestaurantController controller) {
        this.selectRestaurantController = controller;
    }
    /**
     * Returns the SelectRestaurantController instance.
     *
     * @return the SelectRestaurantController instance.
     */
    public SelectRestaurantController getSelectRestaurantController() {
        return this.selectRestaurantController;
    }
    /**
     * Sets the ClientController instance.
     *
     * @param clientController the ClientController instance to be set.
     */
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
    /**
     * Sets the current User object for the session.
     *
     * @param user the User object to be set.
     */
    public void setUser(User user) {
        this.currentUser = user; // Store the User object received from the server
    }
    /**
     * Returns the current User object.
     *
     * @return the current User object.
     */
    public User getUser() {
        return currentUser; // Provide access to the User object
    }
    /**
     * Handles the Exit button action to close the application.
     *
     * @param event the ActionEvent triggered by the Exit button.
     */
    @FXML
    public void handleExitButton(ActionEvent event) {
        System.exit(0); // Exit the application
    }
    /**
     * Handles the Connect button action to connect to the server.
     *
     * @param event the ActionEvent triggered by the Connect button.
     */
    @FXML
    public void handleConnectButton(ActionEvent event) {
        System.out.println("handleConnectButton called");

        if (clientController == null) {
            showAlert("Initialization Error", "ClientController is not initialized.");
            return;
        }

        String host = hostField.getText();
        String portText = portField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        if (host.isEmpty() || portText.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        try {
            int port = Integer.parseInt(portText);
            boolean connected = clientController.connectClientToServer(host, port, username, password);
            if (connected) {
                System.out.println("Connected successfully");
                // Successful connection handling (e.g., waiting for user type from server)
            } else {
                showAlert("Connection Error", "Failed to connect to server.");
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid port number.");
        }
    }
    /**
     * Connects the client to the server with the specified host, port, username, and password.
     *
     * @param host the server host.
     * @param port the server port.
     * @param username the username for authentication.
     * @param password the password for authentication.
     * @return true if connected successfully, false otherwise.
     */
    public boolean connectClientToServer(String host, int port, String username, String password) {
        try {
            System.out.println("connecting to server...");
            client = new Client(host, port, this);
            Message loginRequest = new Message("login", new String[]{username, password});
            client.sendToServer(loginRequest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    /**
     * Processes messages received from the server.
     *
     * @param message the message received from the server.
     */
    public void processServerMessage(String message) {
        if (isValidUserType(message)) {
            System.out.println("sending to redirecting handler");
            handleUserTypeRedirect(message);
        } else {
            // Handle other types of string messages or show an alert
            showAlert("Server Message", message);
        }
    }
    /**
     * Checks if the provided string is a valid user type.
     *
     * @param userType the user type string to check.
     * @return true if valid, false otherwise.
     */
    private boolean isValidUserType(String userType) {
        System.out.println("Cheking if type is valid");
        // Check if the received string is a valid user type
        switch (userType.toLowerCase()) {
            case "customer":
            case "ceo":
            case "branchmanager":
            case "restaurantworker":
            case "certifiedworker":
                return true;
            default:
                return false;
        }
    }
    /**
     * Redirects the user to the appropriate screen based on their user type.
     *
     * @param userType the user type to determine the redirection.
     */
    private void handleUserTypeRedirect(String userType) {
        Platform.runLater(() -> {
            try {
                System.out.println("Redirecting...");
                Stage stage = (Stage) hostField.getScene().getWindow(); // Get current window
                Parent root = null;
                FXMLLoader loader = null;

                switch (userType.toLowerCase()) {
                
                    case "customer":
                        loader = new FXMLLoader(getClass().getResource("/gui/CustomerScreen.fxml"));
                        root = loader.load();
                        // Pass the client instance to the CustomerController
                        CustomerController customercontroller = loader.getController();
                        customercontroller.setClient(client);
                        customercontroller.setUser(currentUser); // Pass the User object
                        customercontroller.setClientController(this); // Pass the ClientController instance

                        break;
                        
                    case "ceo":
                        loader = new FXMLLoader(getClass().getResource("/gui/CEOScreen.fxml"));
                        root = loader.load();
                        // Pass the client instance to the CEOController
                        CEOController ceocontroller = loader.getController();
                        ceocontroller.setClient(client);
                        ceocontroller.setUser(currentUser); // Pass the User object
                        break;
                        
                    case "branchmanager":
                        loader = new FXMLLoader(getClass().getResource("/gui/BranchManagerScreen.fxml"));
                        root = loader.load();
                        // Pass the client instance to the BranchManagerController
                        BranchManagerController branchManagerController = loader.getController();
                        branchManagerController.setClient(client); // Pass the current client instance
                        branchManagerController.setUser(currentUser); // Pass the User object
                        break;
                        
                    case "restaurantworker":
                        loader = new FXMLLoader(getClass().getResource("/gui/RestaurantWorkerScreen.fxml"));
                        root = loader.load();
                        // Pass the client instance to the RestaurantWorkerController
                        RestaurantWorkerController restaurantworkercontroller = loader.getController();
                        restaurantworkercontroller.setClient(client); // Pass the current client instance
                        restaurantworkercontroller.setUser(currentUser); // Pass the User object
                        this.restaurantWorkerController = restaurantworkercontroller; // Store the instance
                        //------------------------------------------------------------------
                        /////////client.setRestaurantWorkerController(restaurantWorkerController); //| Set it in the client as well
                        //-------------------------------------------------------------------
                        break;
                        
                    case "certifiedworker":
                        loader = new FXMLLoader(getClass().getResource("/gui/CertifiedWorkerScreen.fxml"));
                        root = loader.load();
                        // Pass the client instance to the CertifiedWorkerController
                        CertifiedWorkerController certifiedworkercontroller = loader.getController();
                        certifiedworkercontroller.setClient(client);
                        certifiedworkercontroller.setUser(currentUser); // Pass the User object
                        break;
                        
                    default:
                        showAlert("Login Error", "Unknown user type.");
                        return;
                }

                System.out.println("FXML loaded: " + (root == null ? "Failed to load FXML" : "FXML loaded successfully"));

                if (root != null) {
                    System.out.println("Changing scene...");
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    System.out.println("FXML root is null, scene change aborted.");
                }
            } catch (IOException e) {
                System.out.println("IOException occurred: " + e.getMessage());
                e.printStackTrace();
                showAlert("Redirection Error", "Failed to load the appropriate screen.");
            } catch (Exception e) {
                System.out.println("Unexpected exception: " + e.getMessage());
                e.printStackTrace();
                showAlert("Redirection Error", "An unexpected error occurred.");
            }
        });
    }
    /**
     * Displays an information alert with the specified title and message.
     *
     * @param title   the title of the alert window.
     * @param message the content message to be displayed in the alert.
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Sends an exit message to the server to properly disconnect the client.
     */
    public void sendExitMessage() {
        if (client != null) {
            client.sendToServer(new Message("exit", null));
        }
    }
}
