package client;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Dish;
import logic.Restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * The MenuController class handles the display and interaction with the restaurant's menu.
 * It allows users to view dishes by category, add them to a cart, and proceed to checkout.
 */
public class MenuController {

    @FXML
    private TableView<Dish> entreesTableView, startersTableView, saladsTableView, dessertsTableView, drinksTableView, sidesTableView;
    @FXML
    private TableView<Dish> cartTableView;

    @FXML
    private TableColumn<Dish, String> entreeNameColumn, entreeDescriptionColumn,entreeRoastLevelColumn;
    @FXML
    private TableColumn<Dish, Double> entreePriceColumn;
    @FXML
    private TableColumn<Dish, Void> entreeAddColumn;
    @FXML
    private TableColumn<Dish, String> entreeRemarkColumn;

    @FXML
    private TableColumn<Dish, String> starterNameColumn, starterDescriptionColumn;
    @FXML
    private TableColumn<Dish, Double> starterPriceColumn;
    @FXML
    private TableColumn<Dish, Void> starterAddColumn;
    @FXML
    private TableColumn<Dish, String> starterRemarkColumn;

    @FXML
    private TableColumn<Dish, String> saladNameColumn, saladDescriptionColumn;
    @FXML
    private TableColumn<Dish, Double> saladPriceColumn;
    @FXML
    private TableColumn<Dish, Void> saladAddColumn;
    @FXML
    private TableColumn<Dish, String> saladRemarkColumn;

    @FXML
    private TableColumn<Dish, String> dessertNameColumn, dessertDescriptionColumn;
    @FXML
    private TableColumn<Dish, Double> dessertPriceColumn;
    @FXML
    private TableColumn<Dish, Void> dessertAddColumn;
    @FXML
    private TableColumn<Dish, String> dessertRemarkColumn;

    @FXML
    private TableColumn<Dish, String> drinkNameColumn, drinkDescriptionColumn;
    @FXML
    private TableColumn<Dish, Double> drinkPriceColumn;
    @FXML
    private TableColumn<Dish, Void> drinkAddColumn;
    @FXML
    private TableColumn<Dish, String> drinkRemarkColumn;

    @FXML
    private TableColumn<Dish, String> sideNameColumn, sideDescriptionColumn;
    @FXML
    private TableColumn<Dish, Double> sidePriceColumn;
    @FXML
    private TableColumn<Dish, Void> sideAddColumn;
    @FXML
    private TableColumn<Dish, String> sideRemarkColumn;

    @FXML
    private TableColumn<Dish, String> cartDishNameColumn, cartRemarksColumn;
    @FXML
    private TableColumn<Dish, Integer> cartQuantityColumn;
    @FXML
    private TableColumn<Dish, Double> cartPriceColumn;
    @FXML
    private TableColumn<Dish, Void> cartRemoveColumn;

    // Other fields such as the client and user
    private Client client;
    private User currentUser;
    private Restaurant selectedRestaurant;

    private ObservableList<Dish> cartItems = FXCollections.observableArrayList();
    /**
     * Sets the client instance for communication with the server.
     *
     * @param client the Client instance to be set.
     */
    public void setClient(Client client) {
        this.client = client;
    }
    /**
     * Sets the current user.
     *
     * @param user the User instance representing the current user.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }
    /**
     * Sets the selected restaurant.
     *
     * @param restaurant the Restaurant instance representing the selected restaurant.
     */
    public void setSelectedRestaurant(Restaurant restaurant) {
        this.selectedRestaurant = restaurant;
    }
    /**
     * Initializes the MenuController. This method sets up the tables for displaying dishes and the cart.
     */
    @FXML
    private void initialize() {
        setupDishTable(entreeNameColumn, entreeDescriptionColumn, entreePriceColumn, entreeRemarkColumn);
        setupRoastLevelColumn(entreeRoastLevelColumn);
        setupDishTable(starterNameColumn, starterDescriptionColumn, starterPriceColumn, starterRemarkColumn);
        setupDishTable(saladNameColumn, saladDescriptionColumn, saladPriceColumn, saladRemarkColumn);
        setupDishTable(dessertNameColumn, dessertDescriptionColumn, dessertPriceColumn, dessertRemarkColumn);
        setupDishTable(drinkNameColumn, drinkDescriptionColumn, drinkPriceColumn, drinkRemarkColumn);
        setupDishTable(sideNameColumn, sideDescriptionColumn, sidePriceColumn, sideRemarkColumn);

        setupAddButtonColumn(entreeAddColumn, entreesTableView);
        setupAddButtonColumn(starterAddColumn, startersTableView);
        setupAddButtonColumn(saladAddColumn, saladsTableView);
        setupAddButtonColumn(dessertAddColumn, dessertsTableView);
        setupAddButtonColumn(drinkAddColumn, drinksTableView);
        setupAddButtonColumn(sideAddColumn, sidesTableView);

        setupCartTable();
    }
    /**
     * Sets up the roast level column with a ComboBox cell factory.
     *
     * @param roastLevelColumn the TableColumn for roast levels.
     */
    private void setupRoastLevelColumn(TableColumn<Dish, String> roastLevelColumn) {
        roastLevelColumn.setCellFactory(new ComboBoxTableCellFactory());
    }
    /**
     * Sets up the dish table with the provided columns for name, description, price, and remarks.
     *
     * @param nameColumn       the TableColumn for dish names.
     * @param descriptionColumn the TableColumn for dish descriptions.
     * @param priceColumn      the TableColumn for dish prices.
     * @param remarkColumn     the TableColumn for dish remarks.
     */
    private void setupDishTable(TableColumn<Dish, String> nameColumn,
                                TableColumn<Dish, String> descriptionColumn,
                                TableColumn<Dish, Double> priceColumn,
                                TableColumn<Dish, String> remarkColumn) {
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSize())); // Assuming size is the description
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        remarkColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRemark())); // Bind the remark field
    }
    /**
     * Sets up the add button column for each dish table, allowing users to add items to the cart.
     *
     * @param addColumn the TableColumn for the add button.
     * @param tableView the TableView containing the dishes.
     */
    private void setupAddButtonColumn(TableColumn<Dish, Void> addColumn, TableView<Dish> tableView) {
        addColumn.setCellFactory(new Callback<TableColumn<Dish, Void>, TableCell<Dish, Void>>() {
            @Override
            public TableCell<Dish, Void> call(TableColumn<Dish, Void> param) {
                return new TableCell<Dish, Void>() {
                    private final Button addButton = new Button("Add");

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            addButton.setOnAction(event -> {
                                Dish selectedDish = getTableView().getItems().get(getIndex());
                                addToCart(selectedDish);
                            });
                            setGraphic(addButton);
                        }
                    }
                };
            }
        });
    }
    /**
     * Adds a dish to the cart. A clone of the dish is created before adding to the cart to preserve the original.
     *
     * @param dish the Dish to be added to the cart.
     */
    private void addToCart(Dish dish) {
    	 // Clone the dish before adding to the cart
        Dish dishClone = dish.clone();

        // Add the cloned dish to the cart
        cartItems.add(dishClone);
        cartTableView.setItems(cartItems);
        
//        // Add the selected dish to the cart
//        cartItems.add(dish);
//        cartTableView.setItems(cartItems);
    }
    /**
     * Sets up the cart table with the dish name, remarks, quantity, price, and remove button.
     */
    private void setupCartTable() {
        cartDishNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        cartRemarksColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRemark())); // Bind the remark to the cart
        cartQuantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(1).asObject()); // Assuming 1 for simplicity
        cartPriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());

        cartRemoveColumn.setCellFactory(new Callback<TableColumn<Dish, Void>, TableCell<Dish, Void>>() {
            @Override
            public TableCell<Dish, Void> call(TableColumn<Dish, Void> param) {
                return new TableCell<Dish, Void>() {
                    private final Button removeButton = new Button("Remove");

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            removeButton.setOnAction(event -> {
                                Dish dish = getTableView().getItems().get(getIndex());
                                removeFromCart(dish);
                            });
                            setGraphic(removeButton);
                        }
                    }
                };
            }
        });
    }
    /**
     * Removes a dish from the cart.
     *
     * @param dish the Dish to be removed from the cart.
     */
    private void removeFromCart(Dish dish) {
        // Remove the selected dish from the cart
        cartItems.remove(dish);
        cartTableView.setItems(cartItems);
    }
    /**
     * Handles the checkout button click, opening the delivery choice screen.
     */
    @FXML
    private void handleCheckoutButton() {
    	System.out.println("handleCheckoutButton called");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DeliveryChoiceScreen.fxml"));
            Parent root = loader.load();
            System.out.println("FXML loaded");

            DeliveryChoiceController deliveryChoiceController = loader.getController();
            deliveryChoiceController.setCartItems(new ArrayList<>(cartItems)); // Pass the current cart items
            //deliveryChoiceController.populateOrderSummary();

            // Pass the instances of client, currentUser, and selectedRestaurant
            deliveryChoiceController.setClient(client);
            deliveryChoiceController.setCurrentUser(currentUser);
            deliveryChoiceController.setSelectedRestaurant(selectedRestaurant);

            
            Stage stage = new Stage();
            stage.setTitle("Delivery Choices");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the delivery choice screen.");
        }
    }
    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert dialog.
     * @param message the message content of the alert dialog.
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    @FXML
//    private void handleCheckoutButton() {
//        // Handle checkout process
//        System.out.println("Checkout: " + cartItems);
//        // You can send the order to the server or save it to the database here
//    }
    /**
     * Loads dishes into the appropriate category tables based on their type.
     *
     * @param dishes the list of dishes to be loaded.
     */
    public void loadDishes(List<Dish> dishes) {
        // Clear current items in the tables
        entreesTableView.getItems().clear();
        startersTableView.getItems().clear();
        saladsTableView.getItems().clear();
        dessertsTableView.getItems().clear();
        drinksTableView.getItems().clear();
        sidesTableView.getItems().clear();

        // Categorize dishes
        for (Dish dish : dishes) {
            switch (dish.getType().toLowerCase()) {
                case "entree":
                    entreesTableView.getItems().add(dish);
                    break;
                case "starter":
                    startersTableView.getItems().add(dish);
                    break;
                case "salad":
                    saladsTableView.getItems().add(dish);
                    break;
                case "dessert":
                    dessertsTableView.getItems().add(dish);
                    break;
                case "drink":
                    drinksTableView.getItems().add(dish);
                    break;
                case "side":
                    sidesTableView.getItems().add(dish);
                    break;
                default:
                    // Handle unknown types if necessary
                    break;
            }
        }
    }

}
