package client;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import logic.Dish;
/**
 * The {@code ButtonTableCellFactory} class is a custom cell factory for adding a button to a TableView column.
 * This button, when clicked, will add the corresponding dish to the user's cart.
 *
 * <p>This class is intended for use with a TableView that displays a list of {@code Dish} objects. Each cell
 * in the specified column will contain an "Add" button. When the button is clicked, the associated dish is
 * added to the cart.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * TableColumn<Dish, Void> colAdd = new TableColumn<>("Action");
 * colAdd.setCellFactory(new ButtonTableCellFactory());
 * }
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
public class ButtonTableCellFactory implements Callback<TableColumn<Dish, Void>, TableCell<Dish, Void>> {
	/**
     * Creates a TableCell with a button for each cell in the column.
     *
     * @param param the TableColumn parameter for which the cell is being created
     * @return a TableCell containing a button that adds the corresponding dish to the cart
     */
    @Override
    public TableCell<Dish, Void> call(final TableColumn<Dish, Void> param) {
        return new TableCell<Dish, Void>() {

            private final Button button = new Button("Add");

            {
                button.setOnAction(event -> {
                    Dish dish = getTableView().getItems().get(getIndex());
                    addToCart(dish);
                });
            }
            /**
             * Updates the item within the cell. This method is called whenever the cell is updated.
             *
             * @param item the new item for the cell
             * @param empty whether or not this cell represents data
             */
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        };
    }
    /**
     * Adds the specified dish to the cart. This method is triggered when the "Add" button is clicked.
     *
     * @param dish the {@code Dish} object to be added to the cart
     */
    private void addToCart(Dish dish) {
        // This method should add the dish to the cart
        // You can pass this dish to the appropriate method in your MenuController or handle it here
        System.out.println("Added to cart: " + dish.getName());
    }
}
