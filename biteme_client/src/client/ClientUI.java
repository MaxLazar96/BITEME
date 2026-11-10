package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * The ClientUI class is the entry point for the client-side application.
 * It extends the JavaFX Application class and initializes the primary stage
 * with the Client Connection screen.
 */
public class ClientUI extends Application {
	 /**
     * The start method is the main entry point for all JavaFX applications.
     * It sets up the initial stage and scene for the client connection.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     * @throws Exception if an error occurs during loading the FXML resource.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ClientConnection.fxml"));
        Parent root = loader.load();

        ClientController clientController = loader.getController();  // Get the controller created by the FXML loader

        // At this point, clientController is already initialized with FXML fields
        clientController.setClientController(clientController);  // Set the controller reference if needed
        System.out.println("ClientController has been set in ClientConnectionController. - msg in clientUI");

        primaryStage.setTitle("Client Connection");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    /**
     * The main method launches the JavaFX application.
     *
     * @param args command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
