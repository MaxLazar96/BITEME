package server;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import logic.Dish;
import logic.Message;
import logic.Order;
import logic.Restaurant;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
/**
 * The {@code Server} class extends {@link AbstractServer} and handles client-server communication,
 * processing various requests from clients, and interacting with the database via {@link DBConnector}.
 * It is responsible for managing client connections, handling messages from clients,
 * and performing operations such as login, order placement, report generation, and more.
 */
public class Server extends AbstractServer {

    private DBConnector dbConnector;
    private ServerClientViewController controller;
    private ObservableList<ClientInfo> clientList;
    /**
     * Constructs a {@code Server} object on the specified port and initializes the database connector.
     *
     * @param port the port number to listen for connections on
     */
    public Server(int port) {
        super(port);
        dbConnector = new DBConnector();
        System.out.println("Server initialized on port: " + port);
    }
    /**
     * Sets the {@link ServerClientViewController} that will update the client list view.
     *
     * @param controller the controller responsible for updating the client list
     */
    public void setController(ServerClientViewController controller) {
        this.controller = controller;
    }
    /**
     * Sets the observable list of clients connected to the server.
     *
     * @param clientList the list of connected clients
     */
    public void setClientList(ObservableList<ClientInfo> clientList) {
        this.clientList = clientList;
    }
    /**
     * Connects the server to the specified database using the provided credentials.
     *
     * @param dbName     the name of the database to connect to
     * @param dbPassword the password for the database
     */
    public void connectToDB(String dbName, String dbPassword) {
        dbConnector.connectToDB(dbName, dbPassword);
        System.out.println("Connected to database: " + dbName);
    }
    /**
     * Handles messages received from clients, delegating tasks based on the command in the message.
     *
     * @param msg    the message received from the client
     * @param client the client that sent the message
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof Message) {
            Message message = (Message) msg;
            System.out.println(message.getCommand() + " - command recieved in the server side");
            switch (message.getCommand()) {
                case "login":
                    handleLoginRequest(message, client);
                    break;
                case "addCustomer":
                    handleAddCustomer(message.getData(),client);
                    break;
                case "exit":
                    handleClientExit(client);
                    break;
                case "getRestaurants":
                    handleGetRestaurantsRequest(client);
                    break;
                case "getDishes":
                    handleGetDishesRequest(message, client); // Add this line
                    break;
                case "PlaceOrder":
                	handlePlaceNewOrderRequest(message,client);
                	break;
                case "GetOrdersForRestaurant":
                	System.out.println("GetOrdersForRestaurant recieved");
                	handleGetOrdersForRestaurant(message,client);
                	break;
                case "UpdateInPreperation":
                	handleUpdateInPreperation(message,client);
                	break;
                case "UpdateInDelivery":
                	handleUpdateInDelivery(message,client);
                	break;
                case "GetActiveOrders":
                    handleGetActiveOrders(message, client);
                    break;
                case "AcceptDelivery":
                	handleAcceptDelivery(message, client);
                	break;
                case "Times":
                	handleTimes(message, client);
                	break;
                case "updateCredits":
                	updatedCredits(message,client);
                	break;
                case "updateLate":
                	updateLate(message,client);
                	break;
                case "GetDishesForUser":
                    handleGetDishesForUser(message, client);
                    break;
                case "DeleteDish":
                	handleDeleteDish(message,client);
                	break;
                case "AddNewDish":
                	System.out.println("<Server.java> recieved command in server: " + message.getCommand());
                	handleAddNewDish(message,client);
                	break;
                case "EditDish":
                	handleEditDish(message,client);
                	break;
                case "restaurantForCertified":
                	handleRestaurantForCertified(message,client);
                	break;
                case "AddNewDishToMenu":
                	System.out.println("<Server.java> recieved command in server: " + message.getCommand());
                	handleAddNewDishToMenu(message,client);
                	break;
                case "restaurantsByBranch":
                	handleRestaurantsByBranch(message,client);
                	break;
                case "incomeReport":
                	handleIncomeReport(message,client);
                case "OrderReport":
                	handleOrderReport(message,client);
                	break;
                case "Performance_Report":
                	handlePerformanceReport(message,client);
                break;
                case "RequestQuarterlyReport":
                	handleQuarterlyReport(message,client);
                	break;
                	

                default:
                    System.out.println("Unknown message recieved in server");
                    break;
            }
        }
    }
    /**
     * Handles the generation of the quarterly report based on the received quarter.
     * The method determines the start and end dates for the specified quarter,
     * retrieves the corresponding statistics from the database, and sends them back to the client.
     *
     * @param message The message received from the client, containing the quarter as an integer.
     * @param client  The client connection that sent the message.
     */
    private void handleQuarterlyReport(Message message, ConnectionToClient client) {
        int quarter = (int) message.getData();
        System.out.println("<Server.java> received quarter in server: " + quarter);
        
        LocalDate startDate = null;
        LocalDate endDate = null;

        // Determine the start and end dates based on the quarter
        switch (quarter) {
            case 1:
                startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
                endDate = LocalDate.of(LocalDate.now().getYear(), 3, 31);
                break;
            case 2:
                startDate = LocalDate.of(LocalDate.now().getYear(), 4, 1);
                endDate = LocalDate.of(LocalDate.now().getYear(), 6, 30);
                break;
            case 3:
                startDate = LocalDate.of(LocalDate.now().getYear(), 7, 1);
                endDate = LocalDate.of(LocalDate.now().getYear(), 9, 30);
                break;
            case 4:
                startDate = LocalDate.of(LocalDate.now().getYear(), 10, 1);
                endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
                break;
            default:
                System.out.println("Invalid quarter received");
                return; // Exit the method if the quarter is invalid
        }
        System.out.println("<Server.java> start date: " + startDate + " end date: " + endDate);

        try {
            // Call the DBConnector method to get the statistics          
            Map<String, int[]> quarterlyStats = dbConnector.getQuarterlyOrderStats(startDate, endDate);
            System.out.println("\n<Server.java> Report Data is: " + quarterlyStats.toString());

            // Send the data back to the client
            client.sendToClient(new Message("QuarterlyReportResponse", quarterlyStats));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message("Error", "Failed to retrieve quarterly report data."));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    
    /**
     * Handles the performance report request from the client. This method retrieves
     * statistics on late orders for a specific restaurant within a specified date range
     * and sends the results back to the client.
     *
     * @param message The message received from the client, containing an array of data:
     *                - [0]: Integer representing the restaurant ID.
     *                - [1]: LocalDate representing the start date of the report.
     *                - [2]: LocalDate representing the end date of the report.
     * @param client  The client connection that sent the message.
     */
    private void handlePerformanceReport(Message message,  ConnectionToClient client) {
    	Object[] obj = (Object[])message.getData();
    	int res_id = (int)obj[0];
    	LocalDate first_date = (LocalDate)obj[1];
    	LocalDate last_date = (LocalDate)obj[2];
    	try {
			int[]Performance_Report = dbConnector.getLateOrderStats(res_id,first_date,last_date);
			System.out.println("<Server.java> Late Stats Recieved: " + Performance_Report.toString());
			client.sendToClient(new Message("PerformanceReport",Performance_Report));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Handles the order report request from the client. This method retrieves statistics
     * on the number of times each dish was ordered within a specified date range for a specific
     * restaurant, and sends the results back to the client.
     *
     * @param message The message received from the client, containing an array of data:
     *                - [0]: Integer representing the restaurant ID.
     *                - [1]: LocalDate representing the start date of the report.
     *                - [2]: LocalDate representing the end date of the report.
     * @param client  The client connection that sent the message.
     */
    private void handleOrderReport (Message message,  ConnectionToClient client) {
    	Object[] obj = (Object[])message.getData();
    	int res_id = (int)obj[0];
    	LocalDate first_date = (LocalDate)obj[1];
    	LocalDate last_date = (LocalDate)obj[2];
    	try {
			List<String> orderReport = dbConnector.getDishOrderCounts(res_id,first_date,last_date);
			System.out.println("\n<Server.java> Dishes Recieved: " + orderReport.toString());
			client.sendToClient(new Message("orderReport",orderReport));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Handles the income report request from the client. This method calculates the total income
     * generated by a specific restaurant within a given date range and sends the result back to the client.
     *
     * @param message The message received from the client, containing an array of data:
     *                - [0]: Integer representing the restaurant ID.
     *                - [1]: LocalDate representing the start date for the income calculation.
     *                - [2]: LocalDate representing the end date for the income calculation.
     * @param client  The client connection that sent the message.
     */
    private void handleIncomeReport(Message message,  ConnectionToClient client) {
    	Object[] obj = (Object[])message.getData();
    	int res_id = (int)obj[0];
    	LocalDate first_date = (LocalDate)obj[1];
    	LocalDate last_date = (LocalDate)obj[2];
    	try {
			Double income = dbConnector.calculateIncomeByDate(res_id,first_date,last_date);
			System.out.println("\n<Server.java> Income was: " + income);
			client.sendToClient(new Message("income",income));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Handles the request from the client to retrieve a list of restaurants by a specific branch.
     * This method queries the database for restaurants associated with the provided branch name
     * and sends the list of restaurants back to the client.
     *
     * @param message The message received from the client, containing the branch name as a String.
     * @param client  The client connection that sent the message.
     */
    private void handleRestaurantsByBranch(Message message,  ConnectionToClient client) {
    	String branch = (String)message.getData();
    	System.out.println("<Server.java> recieved branch in handleRestaurantsByBranch: " + branch);
    	List<Restaurant> restaurants = dbConnector.getRestaurantsByBranch(branch);
        try {
			client.sendToClient(new Message("RestaurantsByBranch", restaurants));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    /**
     * Handles the request from the client to add a new dish to the menu of a specific restaurant.
     * This method adds a dish to the menu table in the database for the given restaurant ID.
     *
     * @param message The message received from the client, containing the restaurant ID as an integer.
     * @param client  The client connection that sent the message.
     */
    private void handleAddNewDishToMenu(Message message, ConnectionToClient client) {
    	int restaurantId = (int)message.getData();
    	System.out.println("<Server.java> recieved restaurant id in handleAddNewDishToMenu: " + restaurantId);
    	boolean booli = dbConnector.addDishToMenuTable(restaurantId);
    }
    /**
     * Handles the request from the client to retrieve the restaurant associated with a certified worker.
     * This method fetches the restaurant information from the database based on the provided user ID 
     * and sends it back to the client.
     *
     * @param message The message received from the client, containing the user ID as an integer.
     * @param client  The client connection that sent the message.
     */
    private void handleRestaurantForCertified(Message message, ConnectionToClient client) {
    	int userId = (int)message.getData();
    	System.out.println("<Server.java> userId recieved in handleRestaurantForCertified: " + userId);
    	Restaurant restaurant = dbConnector.getRestaurantForWorker(userId);
        System.out.println("<Server.java> restaurant recieved in handleRestaurantForCertified from DBConnector"+restaurant.toString());
        try {
            client.sendToClient(new Message("RestaurantForCertified", restaurant));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    /**
     * Handles the request from the client to edit an existing dish in the database.
     * This method receives the updated Dish object from the client, attempts to update the dish
     * information in the database, and sends a confirmation message back to the client based on the result.
     *
     * @param message The message received from the client, containing the Dish object to be edited.
     * @param client  The client connection that sent the message.
     */
    private void handleEditDish(Message message , ConnectionToClient client) {
    	try {
            // Extract the Dish object from the message
            Dish newDish = (Dish) message.getData();

            // Insert the new dish into the database
            boolean isSuccess = dbConnector.EditDishInDB(newDish);

            // Send confirmation back to the client
            if (isSuccess) {
                client.sendToClient(new Message("EditDishSuccess", "Dish Edited successfully."));
            } else {
                client.sendToClient(new Message("EditDishFailure", "Failed to Edit the dish."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message("EditDishError", "An error occurred while editing the dish."));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    /**
     * Handles the addition of a new dish to the database. This method is triggered when the server
     * receives a "AddNewDish" command from a client. It extracts the {@link Dish} object from the 
     * incoming message, attempts to add the dish to the database, and sends a confirmation 
     * message back to the client indicating whether the operation was successful.
     *
     * @param message the {@link Message} object containing the command and the {@link Dish} data to be added.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleAddNewDish (Message message, ConnectionToClient client) {
    	try {
            // Extract the Dish object from the message
            Dish newDish = (Dish) message.getData();

            // Insert the new dish into the database
            boolean isSuccess = dbConnector.addDishToDB(newDish);

            // Send confirmation back to the client
            if (isSuccess) {
                client.sendToClient(new Message("AddNewDishSuccess", "Dish added successfully."));
            } else {
                client.sendToClient(new Message("AddNewDishFailure", "Failed to add the dish."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message("AddNewDishError", "An error occurred while adding the dish."));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    /**
     * Handles the deletion of a dish from the database. This method is triggered when the server
     * receives a "DeleteDish" command from a client. It extracts the dish ID from the incoming message,
     * attempts to delete the dish with the specified ID from the database, and logs the operation.
     *
     * @param message the {@link Message} object containing the command and the ID of the dish to be deleted.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleDeleteDish(Message message, ConnectionToClient client) {
    	int dishId = (int)message.getData();
    	System.out.println("<Server.java> recieved dish Id for deleting: " + dishId);
    	boolean booli = dbConnector.DeleteDishByDishId(dishId);
    }
    
    /**
     * Handles the retrieval of dishes associated with a specific user. This method is triggered
     * when the server receives a "GetDishesForUser" command from a client. It extracts the user ID
     * from the incoming message, fetches the list of dishes associated with the user from the database,
     * and sends the list back to the client. In case of an error during database access or communication,
     * an error message is sent to the client.
     *
     * @param message the {@link Message} object containing the command and the user ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleGetDishesForUser(Message message, ConnectionToClient client) {
        int userId = (int) message.getData();
        System.out.println("\n<Server.java> userId recieved in handleGetDishesForUser");
        try {
            List<Dish> dishes = dbConnector.getDishesForUser(userId);
            client.sendToClient(new Message("DishesForUserResponse", dishes));
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message("DishesForUserResponse", "Failed to retrieve dishes due to a database error."));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message("DishesForUserResponse", "Failed to retrieve dishes due to a communication error."));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * Handles the update of the "late" status for an order. This method is triggered
     * when the server receives an "updateLate" command from a client. It extracts the order ID
     * from the incoming message and updates the "late" status of the corresponding order in the database.
     *
     * @param message the {@link Message} object containing the command and the order ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void updateLate(Message message, ConnectionToClient client) {
    	int orderId = (int)message.getData();
    	boolean booli = dbConnector.UpdateLate(orderId);
    }
    /**
     * Handles the update of a user's credits in the database. This method is triggered
     * when the server receives an "updateCredits" command from a client. It extracts the user ID
     * and the updated credits value from the incoming message, and then updates the credits
     * for the corresponding user in the database.
     *
     * @param message the {@link Message} object containing the command and the user ID and credits data.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void updatedCredits(Message message, ConnectionToClient client) {
    	//double credits = (Double)message.getData();
    	Object[] obj = (Object[])message.getData();
    	int UserId = (int)obj[0];
    	double credits = (double)obj[1];
    	System.out.println("\n<Server.java> credits recieved in updatedCredits: " + credits);
    	System.out.println("<Server.java> UserId recieved in updatedCredits: " + UserId + "\n");
    	boolean booli = dbConnector.UpdatesCredits(UserId,credits);
    	
    }
    
    /**
     * Handles the calculation of time differences for a specific order. This method is triggered
     * when the server receives a "Times" command from a client. It retrieves the order ID from the
     * incoming message, calculates the time difference for the order using the {@link DBConnector},
     * and sends the result back to the client.
     *
     * @param message the {@link Message} object containing the command and the order ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleTimes(Message message, ConnectionToClient client) {
    	int orderId = (Integer) message.getData();
    	System.out.println("<Server.java> Order ID recieved in handleTimes: " + orderId);
    	int zeldner = dbConnector.calculateTimeDifference(orderId);
    	
    	try {
			client.sendToClient(new Message("TimeDifference",zeldner));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    /**
     * Handles the acceptance of a delivery order. This method is triggered when the server receives
     * an "AcceptDelivery" command from a client. It retrieves the order ID from the incoming message
     * and updates the delivery status of the order in the database using the {@link DBConnector}.
     *
     * @param message the {@link Message} object containing the command and the order ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleAcceptDelivery (Message message, ConnectionToClient client) {
    	int orderId = (Integer) message.getData();
    	System.out.println("<Server.java> Order ID recieved in handleUpdateInDelivery: " + orderId);
    	boolean booli = dbConnector.updateAccept(orderId);
    }
    /**
     * Handles the retrieval of active orders for a specific user. This method is triggered when the server 
     * receives a "GetActiveOrders" command from a client. It retrieves the user ID from the incoming message,
     * fetches the list of active orders associated with that user from the database using the {@link DBConnector}, 
     * and sends the list back to the client.
     *
     * @param message the {@link Message} object containing the command and the user ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleGetActiveOrders(Message message, ConnectionToClient client) {
        int userId = (Integer) message.getData();

        // Fetch active orders for the user from the database
        List<Order> activeOrders = dbConnector.getActiveOrdersForUser(userId);

        System.out.println("\n<Server.java> Active Orders: " + activeOrders.toString());
        // Send the list of active orders back to the client
        try {
            client.sendToClient(new Message("ActiveOrdersResponse", activeOrders));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handles the update of an order's status to "In Delivery". This method is triggered when the server 
     * receives an "UpdateInDelivery" command from a client. It retrieves the order ID from the incoming 
     * message and updates the status of the order to "In Delivery" in the database using the {@link DBConnector}.
     *
     * @param message the {@link Message} object containing the command and the order ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleUpdateInDelivery(Message message, ConnectionToClient client){
    	int orderId = (Integer)message.getData();
    	System.out.println("Order ID recieved in handleUpdateInDelivery: " + orderId);
    	 boolean booli = dbConnector.updateInDelivery(orderId);
    }
    /**
     * Handles the update of an order's status to "In Preparation". This method is triggered when the server 
     * receives an "UpdateInPreperation" command from a client. It retrieves the order ID from the incoming 
     * message and updates the status of the order to "In Preparation" in the database using the {@link DBConnector}.
     *
     * @param message the {@link Message} object containing the command and the order ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleUpdateInPreperation(Message message, ConnectionToClient client){
    	int orderId = (Integer)message.getData();
    	System.out.println("Order ID recieved in handleUpdateInPreperation: " + orderId);
    	 boolean booli = dbConnector.updateInPreperation(orderId);

    }
    /**
     * Handles the retrieval of orders for a specific restaurant. This method is triggered when the server 
     * receives a "GetOrdersForRestaurant" command from a client. It retrieves the user ID from the incoming 
     * message, fetches the associated orders for the restaurant using the {@link DBConnector}, and sends 
     * the list of orders back to the client.
     *
     * @param message the {@link Message} object containing the command and the user ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleGetOrdersForRestaurant(Message message, ConnectionToClient client) {
    	System.out.println("handleGetOrdersForRestaurant called");
        int userId = (Integer) message.getData();
        
        // Call the DBConnector method to fetch orders for the restaurant
        List<Order> orders = dbConnector.getOrdersForRestaurant(userId);
        System.out.println("orders are-> "+orders.toString());
        // Send the orders back to the client
        try {
            client.sendToClient(new Message("OrdersForRestaurant", orders));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handles the login process for a restaurant worker. This method retrieves the {@link User} object 
     * from the incoming message, fetches the restaurant associated with the worker using the {@link DBConnector}, 
     * and sends the restaurant information back to the client.
     *
     * @param message the {@link Message} object containing the {@link User} data.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleRestaurantWorkerLogin(Message message, ConnectionToClient client) {
        User user = (User) message.getData();
        
        // Get the restaurant associated with this user
        Restaurant restaurant = dbConnector.getRestaurantForWorker(user.getId());
        System.out.println(restaurant.toString());
        
        // Send the restaurant information back to the client
        try {
            client.sendToClient(new Message("RestaurantInfo", restaurant));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the process of placing a new order. This method retrieves the {@link Order} object 
     * from the incoming message, extracts the list of {@link Dish} objects associated with the order, 
     * and passes the order details to the {@link DBConnector} to save the order in the database. 
     * It then sends a confirmation message back to the client indicating whether the order was successfully placed.
     *
     * @param message the {@link Message} object containing the {@link Order} data.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handlePlaceNewOrderRequest(Message message, ConnectionToClient client) {
    	System.out.println("handeling placing a new order");
    	Order newOrder = (Order)message.getData();
    	List<Dish> dishes = newOrder.getDishes();
    	
    	
    	boolean success = dbConnector.placeNewOrder(
    			newOrder.getUserId(),
    			newOrder.getAddress(),
    			newOrder.getPhoneNumber(),
    			newOrder.getEmail(),
    			newOrder.getTotalPrice(),
    			newOrder.getStatus(),
    			newOrder.getRestaurantId(),
    			newOrder.getStartTime(),
    			newOrder.getEndTime(),
    			newOrder.getDeliveryType(),
    			newOrder.getRequiredTime(),
    			dishes);
    	
    	// Send a confirmation message back to the client
        try {
            if (success) {
                client.sendToClient(new Message("PlaceNewOrder", "new order placed successfully."));
                System.out.println("new order placed successfully.");
            } else {
                client.sendToClient(new Message("PlaceNewOrderFail", "Failed to place a new order."));
                System.out.println("Failed to Place an order.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handles the request to retrieve a list of dishes for a specific restaurant. This method 
     * extracts the restaurant ID from the incoming {@link Message}, fetches the corresponding 
     * list of {@link Dish} objects from the database via the {@link DBConnector}, and sends 
     * the list back to the client.
     *
     * @param message the {@link Message} object containing the restaurant ID.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleGetDishesRequest(Message message, ConnectionToClient client) {
        int restaurantId = (int) message.getData(); // Assuming the restaurant ID is sent as a string
        
        // Fetch dishes from the database using the restaurant ID
        List<Dish> dishes = dbConnector.getDishesByRestaurantId(restaurantId);
        
        try {
            // Send the list of dishes back to the client
            client.sendToClient(new Message("dishesList", dishes));
            System.out.println("Sent dish list to client: " + client.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the request to retrieve a list of all restaurants. This method fetches 
     * the list of {@link Restaurant} objects from the database using the {@link DBConnector}, 
     * and sends the list back to the requesting client.
     *
     * @param client the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleGetRestaurantsRequest(ConnectionToClient client) {
        try {
            List<Restaurant> restaurants = dbConnector.getAllRestaurants(); // Assume this method exists in DBConnector
            client.sendToClient(new Message("restaurantsList", restaurants));
            System.out.println("Sent restaurant list to client: " + client.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handles the addition of a new customer to the system. This method processes the incoming
     * customer data, attempts to add the customer to the database via the {@link DBConnector},
     * and sends a confirmation or failure message back to the client based on the success of the operation.
     *
     * @param data   the customer data received from the client, expected to be an array of {@link String} values.
     *               The array should contain customer details such as ID, first name, last name, email, address,
     *               username, password, phone number, credits, home branch, and user type.
     * @param client the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleAddCustomer(Object data,ConnectionToClient client) {
    	System.out.println("handeling adding a new customer");
        if (data instanceof String[]) {
            String[] customerData = (String[]) data;

            // Assume DBConnector has a method to add a customer to the database
            boolean success = dbConnector.addCustomerToDB(
                customerData[0], // ID
                customerData[1], // First Name
                customerData[2], // Last Name
                customerData[3], // Email
                customerData[4], // Address
                customerData[5], // Username
                customerData[6], // Password
                customerData[7], // Phone Number
                Integer.parseInt(customerData[8]), // Credits
                customerData[9], // Home Branch
                customerData[10]  // User Type
            );

         // Send a confirmation message back to the client
            try {
                if (success) {
                    client.sendToClient(new Message("addCustomerSuccess", "Customer added successfully."));
                    System.out.println("Customer added successfully.");
                } else {
                    client.sendToClient(new Message("addCustomerFailure", "Failed to add customer."));
                    System.out.println("Failed to add customer.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private void handleLoginRequest(Message message, ConnectionToClient client) {
//        String[] credentials = (String[]) message.getData();
//        String username = credentials[0];
//        String password = credentials[1];
//
//        User user = dbConnector.getUserByUsernameAndPassword(username, password);
//
//        if (user != null) {
//            try {
//                // Create a string from the user data
//                String userData = user.getId() + "," + user.getFirstName() + "," + user.getLastName() + ","
//                    + user.getEmail() + "," + user.getAddress() + "," + user.getUsername() + ","
//                    + user.getPassword() + "," + user.getPhoneNumber() + "," + user.getCredits() + ","
//                    + user.getHomeBranch() + "," + user.getUserType();
//
//                client.sendToClient(new Message("loginSuccess", userData)); // Send the string instead of User object
//                System.out.println("User data string sent to client: " + userData);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                client.sendToClient(new Message("loginFailure", "Invalid credentials"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
    /**
     * Handles the login request from a client. This method retrieves the credentials from the client,
     * validates them against the database using {@link DBConnector}, and responds with the appropriate
     * message based on the validity of the credentials. If the credentials are valid, the user's data
     * is sent back to the client. Additionally, if the user is identified as a restaurant worker, 
     * further processing is done to retrieve and send restaurant-specific information.
     *
     * @param message the {@link Message} object containing the client's credentials as a {@link String} array.
     *                The array is expected to contain the username at index 0 and the password at index 1.
     * @param client  the {@link ConnectionToClient} object representing the client that sent the request.
     */
    private void handleLoginRequest(Message message, ConnectionToClient client) {
        String[] credentials = (String[]) message.getData();
        String username = credentials[0];
        String password = credentials[1];

        User user = dbConnector.getUserByUsernameAndPassword(username, password);

        if (user != null) {
            try {
                // Create a string from the user data
                String userData = user.getId() + "," + user.getFirstName() + "," + user.getLastName() + ","
                    + user.getEmail() + "," + user.getAddress() + "," + user.getUsername() + ","
                    + user.getPassword() + "," + user.getPhoneNumber() + "," + user.getCredits() + ","
                    + user.getHomeBranch() + "," + user.getUserType();

                client.sendToClient(new Message("loginSuccess", userData)); // Send the string instead of User object
                System.out.println("User data string sent to client: " + userData);

                // Check if the user is a restaurant worker and handle accordingly
                if (user.getUserType().equalsIgnoreCase("restaurantworker")) {
                	System.out.println("User is a restaurant worker");
                    handleRestaurantWorkerLogin(new Message("restaurantWorkerLogin", user), client);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                client.sendToClient(new Message("loginFailure", "Invalid credentials"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called by the server framework when a new client successfully connects to the server.
     * This method updates the client list in the server's user interface to reflect the new connection
     * and logs the connection event by printing the client's IP address.
     *
     * @param client the {@link ConnectionToClient} object representing the newly connected client.
     *               This object can be used to send messages to the client or retrieve client-specific information.
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        controller.updateClientList(client, "Connected");
        System.out.println("Client connected: " + client.getInetAddress().getHostAddress());
    }
    /**
     * Called by the server framework when a client disconnects from the server.
     * This method updates the client list in the server's user interface to reflect the disconnection
     * and logs the disconnection event by printing the client's IP address.
     *
     * @param client the {@link ConnectionToClient} object representing the client that has disconnected.
     *               This object can be used to retrieve client-specific information related to the disconnection.
     */
    @Override
    protected void clientDisconnected(ConnectionToClient client) {
        controller.updateClientList(client, "Disconnected");
        System.out.println("Client disconnected: " + client.getInetAddress().getHostAddress());
    }
    /**
     * Handles the event when a client explicitly requests to exit.
     * This method first processes the disconnection by updating the server's client list and then
     * closes the connection to the client to ensure a clean disconnect.
     *
     * @param client the {@link ConnectionToClient} object representing the client that is exiting.
     *               This object is used to close the connection after handling the disconnection.
     */
    private void handleClientExit(ConnectionToClient client) {
        clientDisconnected(client);
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 5555; // Define your port here
        Server server = new Server(port);
        try {
            server.listen(); // Start listening for connections
            System.out.println("Server started and listening on port: " + port);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
