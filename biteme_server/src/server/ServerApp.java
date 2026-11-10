package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * The {@code ServerApp} class is the entry point for the server-side application.
 * It extends the JavaFX {@link Application} class and is responsible for initializing
 * and displaying the server's graphical user interface (GUI).
 * 
 * <p>This class loads the FXML layout for the server's client view, sets up the primary stage,
 * and displays the server GUI.</p>
 * 
 * <p>To launch the application, the {@link #main(String[])} method should be invoked.</p>
 * 
 * @version 1.0
 * @since 1.0
 */
public class ServerApp extends Application {
	/**
     * Starts the JavaFX application by setting up the primary stage and displaying the server GUI.
     * 
     * <p>This method loads the FXML file that defines the layout of the server's client view,
     * retrieves the corresponding controller, and sets the scene for the primary stage.</p>
     * 
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     * @throws Exception if an error occurs during the loading of the FXML file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ServerClientView.fxml"));
        Parent root = loader.load();

        ServerClientViewController controller = loader.getController();

        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    /**
     * The main method that launches the JavaFX application.
     * 
     * <p>This method calls the {@link #launch(String...)} method, which starts the JavaFX runtime and 
     * calls the {@link #start(Stage)} method.</p>
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
