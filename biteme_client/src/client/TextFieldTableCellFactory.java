package client;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import logic.Dish;
/**
 * A custom TableCell factory for rendering and editing remarks in a TableView cell.
 * This factory creates cells that contain a TextField, allowing users to input and edit remarks for dishes.
 */
public class TextFieldTableCellFactory implements Callback<TableColumn<Dish, String>, TableCell<Dish, String>> {
	/**
     * Creates and returns a new TableCell that contains a TextField for editing remarks.
     *
     * @param param the TableColumn for which this TableCell is being created
     * @return a new TableCell containing a TextField
     */

    @Override
    public TableCell<Dish, String> call(TableColumn<Dish, String> param) {
        return new TableCell<Dish, String>() {
            private final TextField textField = new TextField();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    textField.setText(item);
                    setGraphic(textField);

                    // Save the remark to the dish when the user inputs something
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        Dish dish = getTableView().getItems().get(getIndex());
                        dish.setRemark(newValue); // Assuming `setRemark` is a method in `Dish` class
                    });
                }
            }
        };
    }
}
