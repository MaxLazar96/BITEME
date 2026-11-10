package client;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import logic.Dish;
/**
 * The ComboBoxTableCellFactory class is used to create custom TableCells
 * that contain a ComboBox for selecting roast levels within a TableView.
 * It implements the Callback interface to provide a factory method for
 * creating TableCell instances.
 */
public class ComboBoxTableCellFactory implements Callback<TableColumn<Dish, String>, TableCell<Dish, String>> {
	/**
     * Creates a new TableCell containing a ComboBox for each row in the TableView.
     * The ComboBox allows users to select a roast level for each Dish item.
     *
     * @param param the TableColumn to which the cell belongs.
     * @return a TableCell instance with a ComboBox for selecting roast levels.
     */
    @Override
    public TableCell<Dish, String> call(TableColumn<Dish, String> param) {
        return new TableCell<Dish, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();
            /**
             * Updates the TableCell by setting the ComboBox with available roast levels
             * and binding the selected value to the corresponding Dish item.
             *
             * @param item the current value to be displayed in the ComboBox.
             * @param empty whether or not this cell is empty.
             */
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    comboBox.getItems().setAll("N/A","R", "MR", "M", "MW", "WD");
                    comboBox.setValue(item); // Set the current value
                    setGraphic(comboBox);

                    // Save the selected roast level to the dish when the user selects something
                    comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                        Dish dish = getTableView().getItems().get(getIndex());
                        dish.setRoastLevel(newValue); // Assuming `setRoastLevel` is a method in `Dish` class
                    });
                }
            }
        };
    }
}
