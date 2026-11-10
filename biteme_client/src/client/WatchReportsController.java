package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Message;
//import logic.ReportData;
import logic.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
/**
 * The {@code WatchReportsController} class handles the logic for generating and displaying different types
 * of reports for a selected restaurant. The reports include Income Report, Order Report, and Performance Report.
 * This class interacts with the server to retrieve the necessary data and displays it to the user.
 */
public class WatchReportsController {

    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private ComboBox<String> restaurantComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    @FXML
    private PieChart performancePieChart;
    
    private Client client;
    private User currentUser;
    private List<Restaurant> restaurantlist;
    private Double income = 0.0;
    private List<String> ordersForReport;
    private int[] Performance_stats;
    
    public int[] getPerformance_stats() {
		return Performance_stats;
	}
    /**
     * Initializes the controller, setting up the ComboBox with report types and setting default visibility
     * for the performance pie chart.
     */
	@FXML
    private void initialize() {
        // Initialize ComboBox with report types
        reportTypeComboBox.setItems(FXCollections.observableArrayList(
            "Income Report", "Order Report", "Performance Report"));

        // Set default visibility
        performancePieChart.setVisible(false);
    }
	/**
     * Populates the restaurant ComboBox with the names of restaurants available in the current user's branch.
     */
    private void populateRestaurantComboBox() {
        // Extract the names of the restaurants from the list
        List<String> restaurantNames = restaurantlist.stream()
                                                      .map(Restaurant::getName)  // Extract the name of each restaurant
                                                      .collect(Collectors.toList());

        // Add the restaurant names to the ComboBox
        restaurantComboBox.getItems().addAll(restaurantNames);
    }

    /**
     * Sets the {@code Client} instance for this controller.
     *
     * @param client the client instance to set
     */
    public void setClient(Client client) {
        this.client = client;
        client.setWatchreportscontroller(this);
    }
    /**
     * Sets the {@code User} instance for this controller and sends a request to the server
     * to get the list of restaurants in the user's branch.
     *
     * @param user the user instance to set
     */
    public void setUser(User user) {
        this.currentUser = user;
        client.sendToServer(new Message("restaurantsByBranch",currentUser.getHomeBranch()));
    }
    /**
     * Retrieves the ID of a restaurant by its name from a list of restaurants.
     *
     * @param restaurantList the list of restaurants
     * @param restaurantName the name of the restaurant
     * @return the ID of the restaurant, or -1 if not found
     */
    public int getRestaurantIdByName(List<Restaurant> restaurantList, String restaurantName) {
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                return restaurant.getId(); // Assuming Restaurant class has a getId() method
            }
        }
        return -1; // Or another suitable value indicating "not found"
    }

    /**
     * Handles the generation of reports based on user selections. It sends requests to the server
     * to generate the appropriate report (Income, Order, or Performance).
     */
    @FXML
    private void handleGenerateReport() {
    	System.out.println("\n\nCurrent User id and branch: " + currentUser.getId() + " " + currentUser.getHomeBranch());
    	System.out.println("\n\nBranch restaurants are: " + restaurantlist.toString());
    	System.out.println("\nSelected report : " + reportTypeComboBox.getValue() + "\nSelected restaurant: " + restaurantComboBox.getValue());
    	int restaurantId = getRestaurantIdByName(restaurantlist, restaurantComboBox.getValue());
    	System.out.println("The id of " + restaurantComboBox.getValue() + " is  " +restaurantId);         
    	
    	LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
    	 if (startDate == null || endDate == null) {
    	        // Show an alert or handle the error gracefully
    	        showAlert("Error", "Please select both start and end dates.");
    	        return;
    	 }
//        String selectedReportType = reportTypeComboBox.getValue();
        
        System.out.println("start date: " + startDate.toString() + "endDate: " + endDate.toString());
        
        //SHOW INCOME REPORT
        if (reportTypeComboBox.getValue().equals("Income Report")) {
        	Object[] obj = new Object[]{restaurantId, startDate,endDate};
        	client.sendToServer(new Message("incomeReport",obj));
        	
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	showAlert("The income in the selected dates:",income.toString());
        }
        
        //SHOW ORDER REPORT
        if (reportTypeComboBox.getValue().equals("Order Report")) {
        	Object[] obj = new Object[]{restaurantId, startDate,endDate};
        	client.sendToServer(new Message("OrderReport",obj));
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	String show = String.join("\n", ordersForReport);
        	showAlert("Dish Order Counts", show);
	
        }
        
        //SHOW PERFORMANCE REPORT
        if (reportTypeComboBox.getValue().equals("Performance Report")) {
        	Object[] obj = new Object[]{restaurantId, startDate,endDate};
        	client.sendToServer(new Message("Performance_Report",obj));
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	//invoke generatePieChart here.
            generatePieChart();

        }
        
    }
    

    /**
     * Generates and displays a pie chart showing the ratio of late orders to on-time orders.
     */
    public void generatePieChart() {
        if (Performance_stats == null || Performance_stats.length != 2) {
            System.out.println("\n<WatchReportsController> :Invalid Performance_stats array.");
            return;
        }

        int lateOrdersCount = Performance_stats[0];
        int totalOrdersCount = Performance_stats[1];

        if (totalOrdersCount > 0) {
            // Calculate on-time orders
            int onTimeOrdersCount = totalOrdersCount - lateOrdersCount;

            // Prepare data for PieChart
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Late Orders", lateOrdersCount),
                            new PieChart.Data("On-time Orders", onTimeOrdersCount)
                    );

            // Create the PieChart
            PieChart pieChart = new PieChart(pieChartData);
            pieChart.setTitle("Late Orders vs On-time Orders");

            // Create a new Stage (window) for the PieChart
            Stage chartStage = new Stage();
            chartStage.setTitle("Performance Report - Late Orders PieChart");

            // Set the scene and show the stage
            Scene scene = new Scene(pieChart, 800, 600);
            chartStage.setScene(scene);
            chartStage.show();
        } else {
            System.out.println("No data available to display.");
        }
    }
    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the content of the alert
     */
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the list of restaurants and populates the restaurant ComboBox.
     *
     * @param restaurantList the list of restaurants to set
     */
	public void setRestaurantlist(List<Restaurant> restaurant_3) {
		this.restaurantlist = restaurant_3;
		if (restaurantlist != null) {
			populateRestaurantComboBox();
		}
		else {
			System.out.println("\n<WatchReportsController>restaurantlist is still null!");
		}
	}
	/**
     * Sets the income value for the income report.
     *
     * @param income the income to set
     */
	public void setIncome(Double income) {
		this.income = income;
	}
	/**
     * Sets the list of orders for the order report.
     *
     * @param ordersForReport the list of orders to set
     */
	public void setOrdersForReport(List<String> ordersForReport) {
		this.ordersForReport = ordersForReport;
	}
	/**
     * Sets the performance statistics for the performance report.
     *
     * @param performanceStats the performance statistics array to set
     */
	public void setPerformance_stats(int[] performance_stats) {
		Performance_stats = performance_stats;
	}

}
