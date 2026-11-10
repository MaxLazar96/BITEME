package client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Dish;
import logic.Message;
import logic.Order;
import logic.Restaurant;
import ocsf.client.AbstractClient;
/**
 * The {@code Client} class extends {@code AbstractClient} and serves as the client-side communication handler
 * with the server. It manages various controllers and processes messages received from the server.
 */
public class Client extends AbstractClient {
	private ClientController clientController;
	private Consumer<List<Dish>> onDishListReceived;
	private RestaurantWorkerController restaurantWorkerController;
	private ActiveOrdersController activeOrdersController;
	private MenuEditorController menuEditorController;
	private WatchReportsController watchreportscontroller;
	private QuarterlyReportsController QuarterlyReportsController;

	
	
	public void setMenuEditorController(MenuEditorController menuEditorController) {
	    this.menuEditorController = menuEditorController;
	}


	public void setActiveOrdersController(ActiveOrdersController activeOrdersController) {
		this.activeOrdersController = activeOrdersController;
	}

	public RestaurantWorkerController getRestaurantWorkerController() {
		return restaurantWorkerController;
	}
	 /**
     * Constructs a new {@code Client} with the specified host, port, and {@code ClientController}.
     *
     * @param host            the host address of the server
     * @param port            the port number to connect to
     * @param clientController the client controller instance to associate with this client
     */
	public Client(String host, int port, ClientController clientController) {
		super(host, port);
		this.clientController = clientController;
		try {
			openConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to server.");
		}
	}
	/**
     * Handles messages received from the server and delegates them to appropriate controllers or handlers.
     *
     * @param msg the message object received from the server
     */
	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("handleMessageFromServer called with message: " + msg);
		if (msg instanceof Message) {
			Message message = (Message) msg;
			System.out.println("Received message from server: " + message.getCommand());

			// Process different message commands
			switch (message.getCommand()) {

			case "loginSuccess":
				String userData = (String) message.getData(); // Get the user data string
				String[] userFields = userData.split(","); // Split the string into fields

				// Create a new User object from the string data
				User user = new User(Integer.parseInt(userFields[0]), // id
						userFields[1], // firstName
						userFields[2], // lastName
						userFields[3], // email
						userFields[4], // address
						userFields[5], // username
						userFields[6], // password
						userFields[7], // phoneNumber
						Double.parseDouble(userFields[8]), // credits
						userFields[9], // homeBranch
						userFields[10] // userType
				);
				clientController.setUser(user); // Store the User object in the controller
				System.out.println("User data received: " + user.toString());
				clientController.processServerMessage(user.getUserType());
				break;

			case "addCustomerSuccess":
				break;
			case "PlaceNewOrder":
				showAlert("Success", message.getData().toString());
				break;

			case "addCustomerFailure":
				break;
			case "PlaceNewOrderFail":
				showAlert("Failure", message.getData().toString());
				break;

			case "restaurantsList":
				List<Restaurant> restaurants = (List<Restaurant>) message.getData();
				clientController.getSelectRestaurantController().loadRestaurants(restaurants);
				break;

			case "dishesList":
				List<Dish> dishes = (List<Dish>) message.getData();
				if (onDishListReceived != null) {
					Platform.runLater(() -> onDishListReceived.accept(dishes));
				}
				break;
			case "ActiveOrdersResponse":
			    List<Order> activeOrders = (List<Order>) message.getData();
			    System.out.println("<client.java> ActiveOrdersResponse recieved with " + activeOrders.toString());
			    if (activeOrdersController != null) {
			        Platform.runLater(() -> activeOrdersController.handleActiveOrdersResponse(activeOrders));
			    }
			    else {
			    	System.out.println("<client.java> activeOrdersController is null");
			    }
			    break;
			    
			case "TimeDifference":
				int difference = (int)message.getData();
				System.out.println("<Client.java> time difference recieved: " + difference);
				activeOrdersController.setTimeDifference(difference);
				break;
			case "EditDishSuccess":
				showAlert("Succssesful Edit", (String)message.getData());
				break;


//			case "OrdersForRestaurant":
//				List<Order> orders = (List<Order>) message.getData();
//				if (restaurantWorkerController != null) {
//					Platform.runLater(() -> restaurantWorkerController.handleOrdersForRestaurantResponse(orders));
//				}
//				System.out.println("orders are:"+orders.toString());
//				break;
			case "OrdersForRestaurant":
			    List<Order> orders = (List<Order>) message.getData();
			    System.out.println("Orders from Server: " + orders);

			    // Set the controller if it's null
			    if (restaurantWorkerController == null) {
			        restaurantWorkerController = clientController.getRestaurantWorkerController();
			    }
			    
			    if (restaurantWorkerController != null) {
			        Platform.runLater(() -> {
			            System.out.println("Updating orders in RestaurantWorkerController");
			            restaurantWorkerController.handleOrdersForRestaurantResponse(orders);
			        });
			    } else {
			        System.out.println("restaurantWorkerController is still null!");
			    }
			    break;

				
			case "RestaurantInfo":
				Restaurant restaurant = (Restaurant) message.getData();
				System.out.println("restaurant recieved in client: " + restaurant.toString());

				Platform.runLater(() -> {
					RestaurantWorkerController restaurantWorkerController = clientController
							.getRestaurantWorkerController();
					restaurantWorkerController.setRestaurant(restaurant);
				});
				break;
			case "DishesForUserResponse":
			    List<Dish> dishesforcertified = (List<Dish>) message.getData();
			    System.out.println("<client.java> DishesForUserResponse received: " + dishesforcertified);

			    if (menuEditorController != null) {
			        Platform.runLater(() -> {
			            System.out.println("\n<Client.java> Updating MenuEditorController with dishes");
			            menuEditorController.handleDishesResponse(dishesforcertified);
			        });
			    } else {
			        System.out.println("<client.java> MenuEditorController is null!");
			    }
			    break;
			    
			case "RestaurantForCertified":
				Restaurant restaurant_2 = (Restaurant)message.getData();
				//------------------------------------------------------
				menuEditorController.setCurrentrestaurant(restaurant_2);//|
				//------------------------------------------------------
				break;
			case "RestaurantsByBranch":
				List<Restaurant> restaurant_3 = (List<Restaurant>)message.getData();
				watchreportscontroller.setRestaurantlist(restaurant_3);
			break;
			
			case "income":
				double income = (Double)message.getData();
				System.out.println("<Client.java> Income recieved : " + income);
				watchreportscontroller.setIncome(income);
				break;
			case "orderReport":
				List<String> order_report = (List<String>)message.getData();
				watchreportscontroller.setOrdersForReport(order_report);
				break;
			case "PerformanceReport":
				int[] performance_report = (int[])message.getData();
				watchreportscontroller.setPerformance_stats(performance_report);
				break;
			case "QuarterlyReportResponse":
			    handleQuarterlyReportResponse(message);
				break;
				
			default:
				// Delegate handling to the client controller
				clientController.processServerMessage(message.getCommand());
				break;
			}
		} else if (msg instanceof String) {
			String message = (String) msg;
			System.out.println("Received message from server: " + message);

			// Delegate handling to the client controller
			clientController.processServerMessage(message);

		} else {
			System.out.println("Received unknown object from server: " + msg);
		}
	}
	/**
	 * Handles the response from the server containing quarterly report data. 
	 * The data is expected to be a map where the key is a date (as a String) 
	 * and the value is an array of integers representing the order count 
	 * and total revenue for that date.
	 *
	 * @param message the {@code Message} object received from the server containing the quarterly report data
	 */
	private void handleQuarterlyReportResponse(Message message) {
	    // The data from the server will be a Map<String, int[]> where
	    // the key is the date (as a String), and the value is an int[] with [orderCount, totalRevenue]
	    Map<String, int[]> quarterlyStats = (Map<String, int[]>) message.getData();

	    if (QuarterlyReportsController != null) {
	        Platform.runLater(() -> QuarterlyReportsController.displayQuarterlyReport(quarterlyStats));
	    } else {
	        System.out.println("<Client.java> QuarterlyReportsController is not set.");
	    }
	}
	
	/**
	 * Sets the callback to be executed when a list of dishes is received from the server.
	 * This callback is typically used to process and display the dishes in the client UI.
	 *
	 * @param onDishListReceived a {@code Consumer<List<Dish>>} that will be invoked 
	 *                           when the list of dishes is received from the server.
	 */
	public void setOnDishListReceived(Consumer<List<Dish>> onDishListReceived) {
		this.onDishListReceived = onDishListReceived;
	}
	/**
	 * Sets the {@link RestaurantWorkerController} instance for the client.
	 * This method is used to establish a reference to the {@code RestaurantWorkerController}
	 * so that the client can interact with it, typically to update the UI or handle
	 * responses related to restaurant worker operations.
	 *
	 * @param restaurantWorkerController the {@code RestaurantWorkerController} instance
	 *                                   to be associated with the client.
	 */
	public void setRestaurantWorkerController(RestaurantWorkerController restaurantWorkerController) {
		this.restaurantWorkerController = restaurantWorkerController;
	}
	/**
	 * Sends a {@link Message} to the server.
	 * This method is used to communicate with the server by sending a message object,
	 * which can include commands or data. It prints out the command being sent
	 * for debugging purposes and handles any exceptions that may occur during the process.
	 *
	 * @param message the {@code Message} object containing the command and data to be sent to the server.
	 */
	public void sendToServer(Message message) {
		try {
			System.out.println("Sending message to server from client: " + message.getCommand());
			sendToServer((Object) message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Displays an informational alert on the JavaFX Application Thread.
	 * This method ensures that the alert dialog is created and shown on the correct thread,
	 * which is necessary for modifying the JavaFX user interface.
	 *
	 * @param title   the title of the alert dialog window.
	 * @param message the content text of the alert dialog, which provides detailed information to the user.
	 */
	private void showAlert(String title, String message) {
		// This ensures that the alert is displayed on the JavaFX Application Thread
		javafx.application.Platform.runLater(() -> {
			javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
					javafx.scene.control.Alert.AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		});
	}

	/**
	 * Sets the reference to the {@link WatchReportsController} instance.
	 * This method allows the {@link Client} class to interact with the 
	 * {@link WatchReportsController} by storing the provided instance for future use.
	 *
	 * @param watchreportscontroller the {@link WatchReportsController} instance to be set.
	 */
	public void setWatchreportscontroller(WatchReportsController watchreportscontroler) {
		this.watchreportscontroller = watchreportscontroler;
	}

	/**
	 * Sets the {@link QuarterlyReportsController} instance for this client.
	 * This method allows the client to interact with the {@link QuarterlyReportsController}
	 * by storing a reference to it, enabling updates and data exchange between the two.
	 *
	 * @param quarterlyReportsController the {@link QuarterlyReportsController} instance to be associated with this client.
	 */
	public void setQuarterlyReportsController(QuarterlyReportsController quarterlyReportsController) {
		QuarterlyReportsController = quarterlyReportsController;
	}
}
