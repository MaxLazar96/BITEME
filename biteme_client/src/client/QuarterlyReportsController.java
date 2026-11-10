package client;

import java.util.Map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import logic.Message;


public class QuarterlyReportsController {

	@FXML
    private ComboBox<Integer> quarterComboBox;

    @FXML
    private Label reportLabel;
    
    @FXML
    private BarChart<String, Number> quarterlyBarChart;
    
    @FXML
    private BarChart<String, Number> ordersChart;

    @FXML
    private BarChart<String, Number> revenueChart;

    private Client client;
    private User currentUser;

    @FXML
    private void initialize() {
        // Populate the ComboBox with quarter values (1-4)
        //quarterComboBox.getItems().addAll(1, 2, 3, 4);
        //quarterComboBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
        quarterComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4));


    }

    public void setClient(Client client) {
        this.client = client;
        client.setQuarterlyReportsController(this); // Assuming you have a method to set this in Client.java
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleGenerateReport() {
        Integer selectedQuarter = quarterComboBox.getValue();

        if (selectedQuarter == null) {
            showAlert("Error", "Please select a quarter.");
            return;
        }

        // Send a message to the server to request the quarterly report data
        client.sendToServer(new Message("RequestQuarterlyReport", selectedQuarter));
    }
    
    public void displayQuarterlyReport(Map<String, int[]> quarterlyStats) {
        // Prepare data for the charts
        XYChart.Series<String, Number> orderSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();

        orderSeries.setName("Number of Orders");
        revenueSeries.setName("Total Revenue");

        for (Map.Entry<String, int[]> entry : quarterlyStats.entrySet()) {
            String date = entry.getKey();
            int[] stats = entry.getValue();
            int orderCount = stats[0];
            int totalRevenue = stats[1];

            orderSeries.getData().add(new XYChart.Data<>(date, orderCount));
            revenueSeries.getData().add(new XYChart.Data<>(date, totalRevenue));
        }

        // Clear any existing data
        ordersChart.getData().clear();
        revenueChart.getData().clear();

        // Add new data to the charts
        ordersChart.getData().add(orderSeries);
        revenueChart.getData().add(revenueSeries);
    }

    
//    public void handleQuarterlyReportResponse(Map<String, int[]> data) {
//        // Clear any existing data in the chart
//        quarterlyBarChart.getData().clear();
//
//        XYChart.Series<String, Number> ordersSeries = new XYChart.Series<>();
//        ordersSeries.setName("Number of Orders");
//
//        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
//        revenueSeries.setName("Revenue");
//
//        // Populate the chart with the data
//        for (Map.Entry<String, int[]> entry : data.entrySet()) {
//            String day = entry.getKey();
//            int[] values = entry.getValue(); // [0] = number of orders, [1] = revenue
//
//            ordersSeries.getData().add(new XYChart.Data<>(day, values[0]));
//            revenueSeries.getData().add(new XYChart.Data<>(day, values[1]));
//        }
//
//        quarterlyBarChart.getData().addAll(ordersSeries, revenueSeries);
//    }

//    // Method to update the UI with the generated report when it is received
//    public void displayReport(String reportData) {
//        reportLabel.setText(reportData);
//    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
