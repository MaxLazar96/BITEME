package server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
//import ocsf.server.ConnectionToClient;
import ocsf.server.ConnectionToClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * The {@code ServerClientViewController} class is the controller for the server-side
 * graphical user interface (GUI). It manages the interaction between the server and 
 * its client connections, displaying connected clients in a table and providing 
 * functionality to start the server and connect to the database.
 * 
 * <p>This class is responsible for handling user actions within the GUI, such as 
 * starting the server, connecting to the database, and exiting the application.</p>
 * 
 * @version 1.0
 * @since 1.0
 */
public class ServerClientViewController {

    @FXML
    private TextField dbNameField, dbPasswordField, ipHost, portField;

    @FXML
    private Button connectButton, exitID;

    @FXML
    private TableView<ClientInfo> clientTable;

    @FXML
    private TableColumn<ClientInfo, String> ipColumn, hostColumn, statusColumn;

    private Server server;
    private ObservableList<ClientInfo> clientList;
    /**
     * Initializes the controller class. This method is automatically called after the 
     * FXML file has been loaded. It sets up the columns in the client table and initializes 
     * the observable list that will hold the client information.
     */
    @FXML
    public void initialize() {
        // Initialize the TableView columns
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        hostColumn.setCellValueFactory(new PropertyValueFactory<>("hostName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Initialize the observable list for the TableView
        clientList = FXCollections.observableArrayList();
        clientTable.setItems(clientList);
    }
    /**
     * Sets the server instance for this controller.
     * 
     * @param server the server instance to be associated with this controller
     */
    public void setServer(Server server) {
        this.server = server;
    }
    /**
     * Handles the action event for the "Connect" button. This method retrieves the 
     * database name, database password, and port number from the corresponding text fields, 
     * initializes the server, connects to the database, and starts listening for client connections.
     */
    @FXML
    private void handleConnectButtonAction() {
        String dbName = dbNameField.getText();
        String dbPassword = dbPasswordField.getText();
        String portText = portField.getText();

        if (portText == null || portText.isEmpty()) {
            System.out.println("Port number is required.");
            return;
        }

        int port = Integer.parseInt(portText);

        server = new Server(port); // Initialize the server with the chosen port
        server.setController(this); // Set the controller for the server
        server.setClientList(clientList); // Pass the client list to the server

        server.connectToDB(dbName, dbPassword);
        try {
            server.listen(); // Start listening for connections
            System.out.println("Server is now listening for connections on port: " + port);
            //connectButton.setDisable(true); // Disable the button after successful connection
            displayServerIPAddress(); // Display the server IP address
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to start the server.");
        }
    }
    /**
     * Displays the server's IP address in the IP host text field.
     */
    private void displayServerIPAddress() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            if (ipHost != null) {
                ipHost.setText(ip.getHostAddress());
            } else {
                System.out.println("ipHost TextField is null.");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            if (ipHost != null) {
                ipHost.setText("Unknown IP Address");
            } else {
                System.out.println("ipHost TextField is null.");
            }
        }
    }
    /**
     * Handles the action event for the "Exit" button. This method exits the application.
     */
    @FXML
    private void handleExitButton() {
        System.exit(0); // Exit the application
    }

    /**
     * Updates the client list table with the information of a newly connected or disconnected client.
     * 
     * @param client the client connection to update in the list
     * @param status the connection status of the client ("Connected" or "Disconnected")
     */
    public void updateClientList(ConnectionToClient client, String status) {
        String ipAddress = client.getInetAddress().getHostAddress();
        String hostName = client.getInetAddress().getHostName();
        ClientInfo clientInfo = new ClientInfo(ipAddress, hostName, status);
        clientList.add(clientInfo);
    }
}
