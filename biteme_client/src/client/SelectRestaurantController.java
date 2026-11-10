package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Dish;
import logic.Message;
import logic.Restaurant;

import java.io.IOException;
import java.util.List;
/**
 * The SelectRestaurantController class is responsible for managing the view where the user can select a restaurant.
 * It handles the display of available restaurants in a TableView and allows the user to select a restaurant to view its menu.
 */
public class SelectRestaurantController {

    @FXML
    private TableView<Restaurant> restaurantTableView;
    
    @FXML
    private TableColumn<Restaurant, String> nameColumn;
    @FXML
    private TableColumn<Restaurant, String> addressColumn;
    @FXML
    private TableColumn<Restaurant, String> branchColumn;

    private Client client;
    private User currentUser;
    /**
     * Initializes the controller and sets up the columns in the TableView.
     * This method is automatically called after the FXML file has been loaded.
     */
    public void initialize() {
        // Set up the columns in the table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        branchColumn.setCellValueFactory(new PropertyValueFactory<>("branch"));
    }
    /**
     * Sets the client instance to be used by this controller.
     *
     * @param client the client instance
     */
    public void setClient(Client client) {
        this.client = client;
    }
    /**
     * Sets the current user for this controller.
     *
     * @param user the current user
     */
    public void setUser(User user) {
        this.currentUser = user;
    }
    /**
     * Loads the list of restaurants into the TableView.
     *
     * @param restaurants the list of restaurants to be displayed
     */
    public void loadRestaurants(List<Restaurant> restaurants) {
        ObservableList<Restaurant> restaurantData = FXCollections.observableArrayList(restaurants);
        restaurantTableView.setItems(restaurantData);
    }
    /**
     * Handles the event when the "Select Restaurant" button is clicked.
     * Sends a request to the server to get the dishes for the selected restaurant and opens the menu screen.
     */
    @FXML
    private void handleSelectRestaurantButtonClick() {
        Restaurant selectedRestaurant = restaurantTableView.getSelectionModel().getSelectedItem();
        if (selectedRestaurant != null) {
            // Set the callback to open the menu screen when dishes are received
            client.setOnDishListReceived(dishes -> openMenuScreen(selectedRestaurant, dishes));
            
            // Send a request to the server to get the dishes for the selected restaurant
            client.sendToServer(new Message("getDishes", selectedRestaurant.getId()));
            
            // Close the current window
            Stage stage = (Stage) restaurantTableView.getScene().getWindow();
            stage.close();
        } else {
            showAlert("Selection Error", "Please select a restaurant.");
        }
    }
    /**
     * Opens the menu screen for the selected restaurant and displays the dishes.
     *
     * @param selectedRestaurant the restaurant selected by the user
     * @param dishes the list of dishes to be displayed on the menu screen
     */
    private void openMenuScreen(Restaurant selectedRestaurant, List<Dish> dishes) {
    	System.out.println(dishes.toString());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MenuScreen.fxml"));
            Parent root = loader.load();

            MenuController menuController = loader.getController();
            menuController.setClient(client);
            menuController.setUser(currentUser);
            menuController.setSelectedRestaurant(selectedRestaurant);
            menuController.loadDishes(dishes);

            Stage stage = new Stage();
            stage.setTitle("Menu - " + selectedRestaurant.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the menu screen.");
        }
    }
    /**
     * Displays an alert with the specified title and message.
     *
     * @param title the title of the alert
     * @param message the message to be displayed in the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
