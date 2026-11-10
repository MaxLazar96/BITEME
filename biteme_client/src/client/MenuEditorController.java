package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import logic.Dish;
import logic.Message;
import logic.Restaurant;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
/**
 * The MenuEditorController class handles the functionality for editing the menu of a restaurant.
 * It allows users to search for dishes, add new dishes, edit existing dishes, and delete dishes from the menu.
 */
public class MenuEditorController {

    @FXML
    private TableView<Dish> dishesTableView;
    @FXML
    private TableColumn<Dish, String> dishNameColumn;
    @FXML
    private TableColumn<Dish, String> dishTypeColumn;
    @FXML
    private TableColumn<Dish, String> dishSizeColumn;
    @FXML
    private TableColumn<Dish, String> dishPriceColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TextField newDishNameField;
    @FXML
    private TextField newDishTypeField;
    @FXML
    private TextField newDishSizeField;
    @FXML
    private TextField newDishPriceField;

    @FXML
    private TextField editDishNameField;
    @FXML
    private TextField editDishTypeField;
    @FXML
    private TextField editDishSizeField;
    @FXML
    private TextField editDishPriceField;
    
    @FXML
    private ComboBox<String> newDishTypeComboBox;
    @FXML
    private ComboBox<String> editDishTypeComboBox;
    
    private final ObservableList<String> dishTypes = FXCollections.observableArrayList(
            "entree", "starter", "side", "salad", "dessert", "drink"
        );
    

    private Client client;
    private User currentUser;
    private Restaurant currentrestaurant;
    private ObservableList<Dish> dishList = FXCollections.observableArrayList();
    /**
     * Sets the client instance for communication with the server.
     *
     * @param client the Client instance to be set.
     */
    public void setClient(Client client) {
    	System.out.println("<MenuEditorController> Setting Client");
        this.client = client;
        if (client != null) {
            client.setMenuEditorController(this); // Set this controller in the client
        }
        else {
        	System.out.println("<MenuEditorController> Client is null");
        }
    }
    /**
     * Sets the current user.
     *
     * @param user the User instance representing the current user.
     */
    public void setUser(User user) {
        this.currentUser = user;
        loadDishes();  // Load dishes when user is set
    }
    /**
     * Initializes the MenuEditorController. This method sets up the dish table columns and combo boxes.
     */
    @FXML
    private void initialize() {
    	// Initialize the combo boxes with predefined types
        newDishTypeComboBox.setItems(dishTypes);
        editDishTypeComboBox.setItems(dishTypes);
        
        // Set up the table columns
        dishNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        dishTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        dishSizeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSize()));
        dishPriceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPrice())));

        // Add listener to update the fields when a row is selected
        dishesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateEditFields(newSelection);
            }
        });
    }
    /**
     * Loads the dishes associated with the current user from the server.
     */
    private void loadDishes() {
    	System.out.println("\n<MenuEditorController> " + currentUser.getUsername()+ "is loading dishes");
        if (client != null && currentUser != null) {
            client.sendToServer(new Message("GetDishesForUser", currentUser.getId()));
        }
    }
    /**
     * Handles the response from the server with the list of dishes.
     *
     * @param dishes the list of dishes received from the server.
     */
    public void handleDishesResponse(List<Dish> dishes) {
        dishList.setAll(dishes);
        dishesTableView.setItems(dishList);
    }

//    @FXML
//    private void handleSearch(KeyEvent event) {
//        String searchText = searchField.getText().toLowerCase();
//        List<Dish> filteredDishes = dishList.stream()
//                .filter(dish -> dish.getName().toLowerCase().contains(searchText))
//                .collect(Collectors.toList());
//        dishesTableView.setItems(FXCollections.observableArrayList(filteredDishes));
//    }
    /**
     * Handles the search functionality by filtering the dishes based on the search text.
     *
     * @param event the KeyEvent triggered by typing in the search field.
     */
    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText().toLowerCase();
        
        // Find the first dish that matches the search text
        Dish foundDish = dishList.stream()
            .filter(dish -> dish.getName().toLowerCase().contains(searchText))
            .findFirst()
            .orElse(null);

        if (foundDish != null) {
            // Select the found dish in the TableView
            dishesTableView.getSelectionModel().select(foundDish);
            // Scroll to the found dish
            dishesTableView.scrollTo(foundDish);
        } else {
            // Clear selection if no match is found
            dishesTableView.getSelectionModel().clearSelection();
        }
    }


//    @FXML
//    private void handleAddNewDish() {
//        String name = newDishNameField.getText();
//        String type = newDishTypeField.getText();
//        String size = newDishSizeField.getText();
//        double price = Double.parseDouble(newDishPriceField.getText());
//
//        Dish newDish = new Dish(name, type, size, price);
//        
//        client.sendToServer(new Message("AddNewDish", newDish));
//        client.sendToServer(new Message("AddNewDishToMenu", currentrestaurant.getId()));
//
//        loadDishes();
//        clearNewDishFields();
//        
//    }
    
    /**
     * Handles adding a new dish to the menu. Validates input and sends the new dish to the server.
     */
    @FXML
    private void handleAddNewDish() {
    	System.out.println("\n<MenuEditorController.java> handleAddNewDish invoked");
        String name = newDishNameField.getText().trim();
        //String type = newDishTypeField.getText().trim();
        String type = newDishTypeComboBox.getValue();
        String size = newDishSizeField.getText().trim();
        String priceText = newDishPriceField.getText().trim();

        // Validate that not all fields are empty
        if (name.isEmpty() && type.isEmpty() && size.isEmpty() && priceText.isEmpty()) {
            showAlert("Input Error", "All the fields are empty!");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid number for the price.");
            return;
        }

        Dish newDish = new Dish(name, type, size, price);

        client.sendToServer(new Message("AddNewDish", newDish));
        
        
        client.sendToServer(new Message("AddNewDishToMenu", currentrestaurant.getId()));
        
        // Update the local list immediately
        dishList.add(newDish);
        dishesTableView.setItems(dishList); // Refresh the table

        loadDishes();
        clearNewDishFields();
    }
    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert dialog.
     * @param message the message content of the alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Handles editing an existing dish. Updates the selected dish and sends the changes to the server.
     */
    @FXML
    private void handleEditDish() {
        Dish selectedDish = dishesTableView.getSelectionModel().getSelectedItem();
        if (selectedDish != null) {
        	selectedDish.setId(selectedDish.getId());
            selectedDish.setName(editDishNameField.getText());
            selectedDish.setType(editDishTypeField.getText());
            selectedDish.setSize(editDishSizeField.getText());
            selectedDish.setPrice(Double.parseDouble(editDishPriceField.getText()));
            
            client.sendToServer(new Message("EditDish", selectedDish));

            clearEditDishFields();
            loadDishes();
        }
    }
    /**
     * Handles deleting a selected dish from the menu. Sends the delete request to the server.
     */
    @FXML
    private void handleDeleteDish() {
        Dish selectedDish = dishesTableView.getSelectionModel().getSelectedItem();
        if (selectedDish != null) {
            client.sendToServer(new Message("DeleteDish", selectedDish.getId()));
            dishList.remove(selectedDish);  // Remove from the local list
        }
    }
    /**
     * Populates the edit fields with the selected dish's data.
     *
     * @param dish the Dish to be edited.
     */
    private void populateEditFields(Dish dish) {
        editDishNameField.setText(dish.getName());
        //editDishTypeField.setText(dish.getType());
        editDishTypeComboBox.setValue(dish.getType());
        editDishSizeField.setText(dish.getSize());
        editDishPriceField.setText(String.valueOf(dish.getPrice()));
    }
    /**
     * Clears the fields for adding a new dish.
     */
    private void clearNewDishFields() {
        newDishNameField.clear();
        newDishTypeField.clear();
        newDishSizeField.clear();
        newDishPriceField.clear();
    }
    /**
     * Clears the fields for editing a dish.
     */
    private void clearEditDishFields() {
        editDishNameField.clear();
        editDishTypeField.clear();
        editDishSizeField.clear();
        editDishPriceField.clear();
    }

	public void setCurrentrestaurant(Restaurant currentrestaurant) {
		this.currentrestaurant = currentrestaurant;
	}
}
