package server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.Dish;
import logic.Order;
import logic.Restaurant;
/**
 * The {@code DBConnector} class manages the connection to a MySQL database and provides
 * methods to perform various operations related to users, orders, dishes, and restaurants.
 * 
 * <p>This class includes methods for:
 * <ul>
 *   <li>Connecting to the database</li>
 *   <li>Updating order statuses</li>
 *   <li>Calculating time differences for orders</li>
 *   <li>Managing restaurant and dish data</li>
 *   <li>Retrieving reports and statistics</li>
 * </ul>
 * 
 * <p>Each method handles a specific database query or update, often wrapping the SQL
 * logic in a way that makes it easier to use in a higher-level application.
 * 
 * <p>This class uses prepared statements to prevent SQL injection attacks.
 * Connections are managed with auto-commit control to ensure database transactions
 * are atomic and reliable.
 * 
 * @see java.sql.Connection
 * @see java.sql.PreparedStatement
 * @see java.sql.ResultSet
 */
public class DBConnector {

	private Connection conn;
	/**
	 * Establishes a connection to the MySQL database using the provided database name and password.
	 * The method initializes the connection with the MySQL JDBC driver and handles any exceptions
	 * that may occur during the connection process.
	 *
	 * <p>If the connection is successful, a confirmation message is printed to the console.
	 * If the connection fails, error messages are printed, detailing the type of failure.
	 *
	 * @param dbName      The name of the database to connect to.
	 * @param dbPassword  The password for the database user.
	 */
	public void connectToDB(String dbName, String dbPassword) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver definision succeeded");
			String url = "jdbc:mysql://localhost:3306/" + dbName + "?serverTimezone=IST";
			conn = DriverManager.getConnection(url, "root", dbPassword);
			System.out.println("Connected to database: " + dbName);
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver definition failed" + ex.getMessage());
		} catch (SQLException ex) {
			System.out.println("SQLExcetion: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to database: " + e.getMessage());
		}
	}
	/**
	 * Retrieves the user type for a given username and password from the database.
	 * The method executes a SQL query to fetch the `usertype` associated with the provided
	 * credentials. If the credentials match an existing user, the user type is returned.
	 * Otherwise, the method returns {@code null}.
	 *
	 * <p>If an SQL exception occurs during the query, the exception is caught, and {@code null}
	 * is returned. The method also prints an error message to the console if no user is found
	 * with the provided credentials.
	 *
	 * @param username  The username of the user whose type is to be retrieved.
	 * @param password  The password associated with the provided username.
	 * @return          The user type as a {@code String} if the user is found, or {@code null}
	 *                  if no matching user is found or an exception occurs.
	 */
	public String getUserType(String username, String password) {
		String query = "SELECT usertype FROM users WHERE username = ? AND password = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("usertype");
			} else {
				System.out.println("No users found with this username and password!");
				return null; // No user found with given credentials
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null; // Handle the exception appropriately
		}
	}
	/**
	 * Updates the status of an order to "In Preparation" and sets the current time as the start time.
	 * The method executes an SQL `UPDATE` query on the `orders` table, modifying the status and start time
	 * for the order identified by the provided order ID. The start time is set to the current system time.
	 *
	 * <p>If the update is successful, the method returns {@code true}. If an `SQLException` occurs or no
	 * rows are affected, the method returns {@code false} and prints the stack trace of the exception.
	 *
	 * @param orderId  The ID of the order to be updated.
	 * @return         {@code true} if the order was successfully updated; {@code false} otherwise.
	 */
	public boolean updateInPreperation(int orderId) {
		String query = "UPDATE orders SET status = ?, start_time = ? WHERE id = ?";
		LocalDateTime currentTime = LocalDateTime.now();
		String startTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, "In Preperation");
			pstmt.setString(2, startTime);
			pstmt.setInt(3, orderId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Updates the status of an order to "Delivered to client" and sets the current time as the end time.
	 * The method executes an SQL `UPDATE` query on the `orders` table, modifying the status and end time
	 * for the order identified by the provided order ID. The end time is set to the current system time.
	 *
	 * <p>If the update is successful, the method returns {@code true}. If an `SQLException` occurs or no
	 * rows are affected, the method returns {@code false} and prints the stack trace of the exception.
	 *
	 * @param orderId  The ID of the order to be updated.
	 * @return         {@code true} if the order was successfully updated; {@code false} otherwise.
	 */
	public boolean updateAccept(int orderId) {
		String query = "UPDATE orders SET status = ?, end_time = ? WHERE id = ?";
		LocalDateTime currentTime = LocalDateTime.now();
		String endTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, "Delivered to client");
			pstmt.setString(2, endTime);
			pstmt.setInt(3, orderId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Updates the credits for a user in the database.
	 * The method executes an SQL `UPDATE` query on the `users` table, modifying the `credits`
	 * field for the user identified by the provided user ID.
	 *
	 * <p>If the update is successful, the method returns {@code true}. If an `SQLException`
	 * occurs or no rows are affected, the method returns {@code false} and prints the stack trace of the exception.
	 *
	 * @param UserId  The ID of the user whose credits need to be updated.
	 * @param credits The new credit amount to be set for the user.
	 * @return        {@code true} if the credits were successfully updated; {@code false} otherwise.
	 */
	public boolean UpdatesCredits(int UserId, double credits) {
		System.out.println("\n<DBConnector> user id: " + UserId + "credits: " + credits + "recieved\n");
		String query = "UPDATE users SET credits = ? WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setDouble(1, credits);
			pstmt.setInt(2, UserId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Marks an order as late in the database by setting the `late` field to 1 for the specified order ID.
	 * The method executes an SQL `UPDATE` query on the `orders` table, updating the `late` field
	 * for the order identified by the provided order ID.
	 *
	 * <p>If the update is successful, the method returns {@code true}. If an `SQLException`
	 * occurs or no rows are affected, the method returns {@code false} and prints the stack trace of the exception.
	 *
	 * @param orderId The ID of the order that needs to be marked as late.
	 * @return        {@code true} if the order was successfully marked as late; {@code false} otherwise.
	 */
	public boolean UpdateLate(int orderId) {
		System.out.println("\n<DBConnector> late = 1 in orderId " + "orderId");
		String query = "UPDATE orders SET late = ? WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, 1);
			pstmt.setInt(2, orderId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Updates the status of an order to "In Delivery" in the database for the specified order ID.
	 * This method executes an SQL `UPDATE` query on the `orders` table, setting the `status` field
	 * to "In Delivery" for the order identified by the provided order ID.
	 *
	 * <p>If the update is successful, the method returns {@code true}. If an `SQLException`
	 * occurs or no rows are affected, the method returns {@code false} and prints the stack trace of the exception.
	 *
	 * @param orderId The ID of the order that needs to have its status updated to "In Delivery".
	 * @return        {@code true} if the order status was successfully updated; {@code false} otherwise.
	 */
	public boolean updateInDelivery(int orderId) {
		String query = "UPDATE orders SET status = ? WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, "In Delivery");
			pstmt.setInt(2, orderId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Calculates the time difference in minutes between the start and end times of an order
	 * specified by the given order ID. The method retrieves the start and end times from the database,
	 * and then calculates the difference using the {@link #calculateDifferenceInMinutes(String, String)} method.
	 *
	 * <p>If the start or end time cannot be found, or if an SQL exception occurs, the method returns {@code -1}.
	 *
	 * @param orderId The ID of the order for which the time difference is to be calculated.
	 * @return        The time difference in minutes between the order's start and end times,
	 *                or {@code -1} if an error occurs or the times are not found.
	 */
	public int calculateTimeDifference(int orderId) {
		String query = "SELECT start_time, end_time FROM orders WHERE id = ?";
		String startTime = null;
		String endTime = null;

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, orderId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				startTime = rs.getString("start_time");
				endTime = rs.getString("end_time");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1; // Return -1 in case of an error
		}

		// Ensure both times are retrieved
		if (startTime == null || endTime == null) {
			System.out.println("Start time or End time not found for order ID: " + orderId);
			return -1;
		}

		// Now calculate the time difference using the calculateDifferenceInMinutes
		// method
		return calculateDifferenceInMinutes(startTime, endTime);
	}

	/**
	 * Calculates the difference in minutes between two date-time strings formatted as "yyyy-MM-dd HH:mm:ss".
	 *
	 * <p>This method parses the provided start and end times into {@link LocalDateTime} objects using
	 * the specified date-time format. It then calculates the difference in minutes between the two
	 * date-time values using {@link ChronoUnit#MINUTES}.
	 *
	 * @param startTime The start time as a string in the format "yyyy-MM-dd HH:mm:ss".
	 * @param endTime   The end time as a string in the format "yyyy-MM-dd HH:mm:ss".
	 * @return          The difference in minutes between the start and end times.
	 * @throws DateTimeParseException if the date-time strings cannot be parsed.
	 */
	public static int calculateDifferenceInMinutes(String startTime, String endTime) {
		// Define the DateTime format used in the input strings
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// Parse the start and end times to LocalDateTime objects
		LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);

		// Calculate the difference in minutes
		long minutesDifference = ChronoUnit.MINUTES.between(startDateTime, endDateTime);

		// Convert the difference to an int and return it
		return (int) minutesDifference;
	}
	/**
	 * Retrieves a list of active orders for a specific user from the database.
	 *
	 * <p>This method fetches orders that have not yet been delivered to the client for a given user.
	 * It constructs each order with its details, including the order ID, delivery type, status, and total price.
	 * The method groups items by order ID and concatenates dish details with any remarks.
	 *
	 * @param userId The ID of the user whose active orders are to be retrieved.
	 * @return A list of {@link Order} objects representing the active orders for the specified user.
	 *         If no active orders are found, an empty list is returned.
	 * @throws SQLException if a database access error occurs.
	 */
	public List<Order> getActiveOrdersForUser(int userId) {
		List<Order> activeOrders = new ArrayList<>();
		String query = "SELECT o.id AS order_id, "
				+ "GROUP_CONCAT(CONCAT(d.name, ' (Remark: ', IFNULL(oi.remark, 'No Remark'), ')') SEPARATOR ', ') AS details, "
				+ "o.delivery_type, o.status, o.total_price " + "FROM orders o "
				+ "JOIN order_items oi ON o.id = oi.order_id " + "JOIN dishes d ON oi.dish_id = d.id "
				+ "WHERE o.user_id = ? AND o.status != 'Delivered to client' "
				+ "GROUP BY o.id, o.delivery_type, o.status, o.total_price";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int orderId = rs.getInt("order_id");
				String details = rs.getString("details");
				String deliveryType = rs.getString("delivery_type");
				String status = rs.getString("status");
				double totalPrice = rs.getDouble("total_price");

				Order order = new Order(orderId, details, deliveryType, status, totalPrice);
				activeOrders.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return activeOrders;
	}
	/**
	 * Places a new order in the database and associates the provided dishes with the order.
	 *
	 * <p>This method first inserts the order details into the `orders` table. If the order is
	 * successfully inserted, it retrieves the generated order ID and then inserts each dish
	 * associated with the order into the `order_items` table.
	 *
	 * @param user_id       The ID of the user placing the order.
	 * @param address       The delivery address for the order.
	 * @param phone_num     The phone number associated with the order.
	 * @param email         The email address associated with the order.
	 * @param total_price   The total price of the order.
	 * @param status        The current status of the order.
	 * @param res_id        The ID of the restaurant associated with the order.
	 * @param start_time    The start time of the order in `yyyy-MM-dd HH:mm:ss` format.
	 * @param end_time      The end time of the order in `yyyy-MM-dd HH:mm:ss` format.
	 * @param delivery_type The type of delivery for the order (e.g., "Delivery" or "Pickup").
	 * @param req_time      The required time for the order.
	 * @param dishes        A list of {@link Dish} objects representing the dishes included in the order.
	 * @return {@code true} if the order was placed successfully and all dishes were added; {@code false} otherwise.
	 * @throws SQLException if a database access error occurs.
	 */
	public boolean placeNewOrder(int user_id, String address, String phone_num, String email, double total_price,
			String status, int res_id, String start_time, String end_time, String delivery_type, String req_time,
			List<Dish> dishes) {
		String orderQuery = "INSERT INTO orders (user_id, address, phone_number, email, total_price, status, restaurant_id, start_time, end_time, delivery_type, required_time)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		String getLastOrderIdQuery = "SELECT MAX(id) AS last_id FROM orders";
		String orderItemsQuery = "INSERT INTO order_items (order_id, dish_id, price, remark) VALUES (?, ?, ?, ?)";

		try (PreparedStatement pstmtOrder = conn.prepareStatement(orderQuery);
				PreparedStatement pstmtLastOrderId = conn.prepareStatement(getLastOrderIdQuery);
				PreparedStatement pstmtOrderItems = conn.prepareStatement(orderItemsQuery)) {

			// Insert the new order
			pstmtOrder.setInt(1, user_id);
			pstmtOrder.setString(2, address);
			pstmtOrder.setString(3, phone_num);
			pstmtOrder.setString(4, email);
			pstmtOrder.setDouble(5, total_price);
			pstmtOrder.setString(6, status);
			pstmtOrder.setInt(7, res_id);
			pstmtOrder.setString(8, start_time);
			pstmtOrder.setString(9, end_time);
			pstmtOrder.setString(10, delivery_type);
			pstmtOrder.setString(11, req_time);
			int rowsInserted = pstmtOrder.executeUpdate();

			if (rowsInserted > 0) {
				// Retrieve the last inserted order_id
				ResultSet rs = pstmtLastOrderId.executeQuery();
				if (rs.next()) {
					int order_id = rs.getInt("last_id");

					// Insert each dish into the order_items table
					for (Dish dish : dishes) {
						pstmtOrderItems.setInt(1, order_id);
						pstmtOrderItems.setInt(2, dish.getId()); // Assuming Dish class has a getId() method
						pstmtOrderItems.setDouble(3, dish.getPrice());
						pstmtOrderItems.setString(4, dish.getRemark());
						pstmtOrderItems.addBatch();
					}
					pstmtOrderItems.executeBatch();
				}
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Retrieves a list of orders associated with the restaurant where the specified user works.
	 *
	 * <p>This method queries the database to fetch all orders placed at the restaurant that the
	 * user (identified by the provided user ID) is associated with. The method joins the `orders`,
	 * `order_items`, and `dishes` tables to gather order details, including the status of each order
	 * and the list of dishes associated with it. The dishes are represented as a concatenated string
	 * with remarks.
	 *
	 * @param userId The ID of the user whose associated restaurant's orders are to be retrieved.
	 * @return A list of {@link Order} objects representing the orders for the restaurant associated with the user.
	 */
	public List<Order> getOrdersForRestaurant(int userId) {
		System.out.println("getOrdersForRestaurant invoked with userID: " + userId);
		List<Order> orders = new ArrayList<>();
		String query = "SELECT " + "o.id AS order_id, " + "o.status AS order_status, "
				+ "GROUP_CONCAT(CONCAT(d.name, ' (Remark: ', IFNULL(oi.remark, 'No Remark'), ')') SEPARATOR ', ') AS dishes "
				+ "FROM orders o " + "JOIN order_items oi ON o.id = oi.order_id "
				+ "JOIN dishes d ON oi.dish_id = d.id "
				+ "WHERE o.restaurant_id = (SELECT restaurant_id FROM worker WHERE user_id = ?) "
				+ "GROUP BY o.id, o.status";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int orderId = rs.getInt("order_id");
				String orderStatus = rs.getString("order_status");
				String dishes = rs.getString("dishes");

				Order order = new Order();
				order.setId(orderId);
				order.setStatus(orderStatus);
				order.setDishesFromString(dishes); // Assuming a method to parse dishes string into list of dishes
				orders.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("DBConnector orders: " + orders.toString());
		return orders;
	}

//	
//	private List<Dish> getDishesForOrder(int orderId) {
//	    String query = "SELECT * FROM order_items WHERE order_id = ?";
//	    List<Dish> dishes = new ArrayList<>();
//
//	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//	        pstmt.setInt(1, orderId);
//	        ResultSet rs = pstmt.executeQuery();
//
//	        while (rs.next()) {
//	            Dish dish = new Dish();
//	            dish.setName(rs.getString("dish_name")); // Assuming you have a dish name in your table
//	            dish.setRemark(rs.getString("remark")); // Assuming you have remarks in your table
//
//	            dishes.add(dish);
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//
//	    return dishes;
//	}
	/**
	 * Adds the latest dish to the menu of a specified restaurant.
	 *
	 * <p>This method inserts a new entry into the `menu` table by selecting the maximum dish ID
	 * from the `dishes` table and associating it with the given restaurant ID. The assumption is that 
	 * the dish with the highest ID is the most recently added dish.
	 *
	 * @param restId The ID of the restaurant to which the latest dish should be added.
	 * @return {@code true} if the dish was successfully added to the menu; {@code false} otherwise.
	 */
	public boolean addDishToMenuTable(int restId) {
		String query = "INSERT INTO menu (dish_id, restaurant_id) SELECT MAX(id), ? FROM dishes";
		// String query = "INSERT INTO menu (dish_id, restaurant_id) SELECT
		// MAX(dish_id)+1, ? FROM menu";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, restId);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Return true if the dish was added successfully
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Retrieves the restaurant associated with a specific worker based on their user ID.
	 *
	 * <p>This method queries the database to find the restaurant where the worker is employed.
	 * The worker is identified by the provided user ID, and the method returns the corresponding
	 * restaurant details.
	 *
	 * @param userId The ID of the user (worker) whose associated restaurant is to be retrieved.
	 * @return A {@link Restaurant} object containing the restaurant details (ID, name, address, branch)
	 *         if found, or {@code null} if no matching restaurant is found or if an error occurs.
	 */
	public Restaurant getRestaurantForWorker(int userId) {
		String query = "SELECT r.id, r.name, r.address, r.branch FROM worker w "
				+ "JOIN restaurants r ON w.restaurant_id = r.id " + "WHERE w.user_id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String address = rs.getString("address");
				String branch = rs.getString("branch");

				return new Restaurant(id, name, address, branch);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Adds a new customer to the database with the provided details.
	 *
	 * <p>This method inserts a new record into the `users` table with the given customer information.
	 * It returns a boolean indicating whether the operation was successful.
	 *
	 * @param id The unique identifier for the customer.
	 * @param firstName The first name of the customer.
	 * @param lastName The last name of the customer.
	 * @param email The email address of the customer.
	 * @param address The physical address of the customer.
	 * @param username The username chosen by the customer.
	 * @param password The password chosen by the customer.
	 * @param phoneNumber The contact phone number of the customer.
	 * @param credits The initial amount of credits the customer has.
	 * @param homeBranch The home branch associated with the customer.
	 * @param userType The type of user (e.g., customer, admin).
	 * @return {@code true} if the customer was successfully added to the database; {@code false} otherwise.
	 */
	public boolean addCustomerToDB(String id, String firstName, String lastName, String email, String address,
			String username, String password, String phoneNumber, double credits, String homeBranch, String userType) {
		String query = "INSERT INTO users (id, firstname, lastname, email, address, username, password, phonenumber, credits, homebranch, usertype) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id);
			pstmt.setString(2, firstName);
			pstmt.setString(3, lastName);
			pstmt.setString(4, email);
			pstmt.setString(5, address);
			pstmt.setString(6, username);
			pstmt.setString(7, password);
			pstmt.setString(8, phoneNumber);
			pstmt.setDouble(9, credits);
			pstmt.setString(10, homeBranch);
			pstmt.setString(11, userType);

			int rowsInserted = pstmt.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Retrieves a user from the database based on the provided username and password.
	 *
	 * <p>This method queries the `users` table to find a user that matches the given username and password.
	 * If a matching user is found, a `User` object is populated with the user's details and returned.
	 * If no match is found, the method returns {@code null}.
	 *
	 * @param username The username of the user to be retrieved.
	 * @param password The password of the user to be retrieved.
	 * @return A {@link User} object containing the user's details if found; {@code null} if no user is found with the given credentials.
	 */
	public User getUserByUsernameAndPassword(String username, String password) {
		User user = null;
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				user.setEmail(rs.getString("email"));
				user.setAddress(rs.getString("address"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setPhoneNumber(rs.getString("phonenumber"));
				user.setCredits(rs.getDouble("credits"));
				user.setHomeBranch(rs.getString("homebranch"));
				user.setUserType(rs.getString("usertype"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}
	/**
	 * Retrieves a list of all restaurants from the database.
	 *
	 * <p>This method queries the `restaurants` table to retrieve all restaurant records.
	 * Each record is mapped to a `Restaurant` object and added to a list, which is then returned.
	 *
	 * @return A list of {@link Restaurant} objects representing all the restaurants in the database.
	 *         The list will be empty if there are no restaurants or if an error occurs during the query.
	 */
	public List<Restaurant> getAllRestaurants() {
		List<Restaurant> restaurants = new ArrayList<>();
		String query = "SELECT * FROM restaurants";

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				Restaurant restaurant = new Restaurant(rs.getInt("id"), rs.getString("name"), rs.getString("address"),
						rs.getString("branch"));
				restaurants.add(restaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}
	/**
	 * Retrieves a list of dishes offered by a specific restaurant.
	 *
	 * <p>This method queries the database to fetch all dishes associated with a given restaurant ID.
	 * The method joins the `dishes`, `menu`, and `restaurants` tables to filter dishes based on the
	 * restaurant ID provided. Each dish is mapped to a {@link Dish} object and added to a list, which is then returned.
	 *
	 * @param restaurantId The ID of the restaurant whose dishes are to be retrieved.
	 * @return A list of {@link Dish} objects representing the dishes offered by the specified restaurant.
	 *         The list will be empty if no dishes are found or if an error occurs during the query.
	 */
	public List<Dish> getDishesByRestaurantId(int restaurantId) {
		List<Dish> dishes = new ArrayList<>();

		String query = "SELECT d.id, d.name, d.type, d.size, d.price, d.roast_level, d.remark " + "FROM dishes d "
				+ "JOIN menu m ON d.id = m.dish_id " + "JOIN restaurants r ON r.id = m.restaurant_id "
				+ "WHERE r.id = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, restaurantId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Dish dish = new Dish(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getString("size"),
						rs.getDouble("price"), rs.getString("roast_level"), rs.getString("remark"));
				dishes.add(dish);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions properly in a production environment
		}

		return dishes;
	}
	/**
	 * Retrieves a list of dishes available for a specific user based on their associated restaurant.
	 *
	 * <p>This method queries the database to fetch all dishes that are available in the restaurant
	 * where the user, identified by their user ID, is employed. The method joins the `dishes`, `menu`,
	 * and `worker` tables to filter dishes based on the restaurant associated with the user.
	 * Each dish is mapped to a {@link Dish} object and added to a list, which is then returned.
	 *
	 * @param userId The ID of the user whose associated restaurant's dishes are to be retrieved.
	 * @return A list of {@link Dish} objects representing the dishes available for the user.
	 *         The list will be empty if no dishes are found.
	 * @throws SQLException If a database access error occurs.
	 */
	public List<Dish> getDishesForUser(int userId) throws SQLException {
		List<Dish> dishes = new ArrayList<>();

		String query = "SELECT d.id,d.name, d.type, d.size, d.price " + "FROM dishes d "
				+ "JOIN menu m ON d.id = m.dish_id " + "JOIN worker w ON m.restaurant_id = w.restaurant_id "
				+ "WHERE w.user_id = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int dishId = rs.getInt("id");
				String name = rs.getString("name");
				String type = rs.getString("type");
				String size = rs.getString("size");
				double price = rs.getDouble("price");

				Dish dish = new Dish(dishId, name, type, size, price);
				dishes.add(dish);
			}
		}

		return dishes;
	}

//	  public boolean DeleteDishByDishId(int dishId) {
//		  System.out.println("<DBConnector> dishId recieved for delete: " + dishId);
//		  String query = "DELETE FROM dishes WHERE id = ?";
//		  try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//				pstmt.setInt(1, dishId);
//				int rowsInserted = pstmt.executeUpdate();
//				return rowsInserted > 0;
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return false;
//			}
//	  }
	/**
	 * Deletes a dish from the database, including its entries in both the `menu` and `dishes` tables.
	 *
	 * <p>This method performs a transactional delete operation, first removing the dish from the `menu` table
	 * and then from the `dishes` table. If any step fails, the transaction is rolled back to ensure data consistency.
	 *
	 * @param dishId The ID of the dish to be deleted.
	 * @return {@code true} if the dish was successfully deleted from both tables; {@code false} otherwise.
	 */
	public boolean DeleteDishByDishId(int dishId) {
		System.out.println("<DBConnector> dishId received for delete: " + dishId);

		// Query to delete from 'menu' table first
		String deleteFromMenuQuery = "DELETE FROM menu WHERE dish_id = ?";

		// Query to delete from 'dishes' table
		String deleteFromDishesQuery = "DELETE FROM dishes WHERE id = ?";

		PreparedStatement pstmt = null;
		try {
			// Start transaction
			conn.setAutoCommit(false);

			// Deleting from menu
			pstmt = conn.prepareStatement(deleteFromMenuQuery);
			pstmt.setInt(1, dishId);
			int rowsDeletedFromMenu = pstmt.executeUpdate();

			// Deleting from dishes
			pstmt = conn.prepareStatement(deleteFromDishesQuery);
			pstmt.setInt(1, dishId);
			int rowsDeletedFromDishes = pstmt.executeUpdate();

			// Commit transaction if both deletes were successful
			conn.commit();

			return rowsDeletedFromMenu > 0 && rowsDeletedFromDishes > 0;
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback(); // rollback if any exception occurs
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			return false;
		} finally {
			// Clean up
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null) {
					conn.setAutoCommit(true); // Reset to default
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * Adds a new dish to the `dishes` table in the database.
	 *
	 * <p>This method inserts a dish into the `dishes` table with the specified name, type, size, and price.
	 * The dish details are provided through the {@link Dish} object passed as a parameter.
	 *
	 * @param dish The {@link Dish} object containing the details of the dish to be added.
	 * @return {@code true} if the dish was successfully added to the database; {@code false} otherwise.
	 */
	public boolean addDishToDB(Dish dish) {
		String query = "INSERT INTO dishes (name, type, size, price) VALUES (?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, dish.getName());
			stmt.setString(2, dish.getType());
			stmt.setString(3, dish.getSize());
			stmt.setDouble(4, dish.getPrice());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Return true if the dish was added successfully
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Updates an existing dish in the `dishes` table in the database.
	 *
	 * <p>This method updates the details of a dish in the `dishes` table based on the dish ID provided
	 * in the {@link Dish} object. The dish's name, type, size, and price are updated in the database.
	 *
	 * @param dish The {@link Dish} object containing the updated details of the dish, including its ID.
	 * @return {@code true} if the dish was successfully updated in the database; {@code false} otherwise.
	 */
	public boolean EditDishInDB(Dish dish) {
		String query = "UPDATE dishes SET name = ?, type = ?, size = ?, price = ? WHERE id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, dish.getName());
			stmt.setString(2, dish.getType());
			stmt.setString(3, dish.getSize());
			stmt.setDouble(4, dish.getPrice());
			stmt.setInt(5, dish.getId());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Return true if the dish was added successfully
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Retrieves a list of restaurants that belong to a specific branch.
	 *
	 * <p>This method queries the `restaurants` table in the database to fetch all restaurants that 
	 * belong to the specified branch. The method returns a list of {@link Restaurant} objects, each
	 * representing a restaurant in the specified branch.
	 *
	 * @param Manager_branch The branch name used to filter restaurants.
	 * @return A list of {@link Restaurant} objects that belong to the specified branch.
	 */
	public List<Restaurant> getRestaurantsByBranch(String Manager_branch) {
		List<Restaurant> restaurants = new ArrayList<>();
		String query = "SELECT * FROM restaurants WHERE branch = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, Manager_branch);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int res_id = rs.getInt("id");
				String name = rs.getString("name");
				String address = rs.getString("address");
				String branch = rs.getString("branch");

				Restaurant restaurant = new Restaurant(res_id, name, address, branch);
				restaurants.add(restaurant);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurants;
	}
	/**
	 * Calculates the total income generated by a specific restaurant within a given date range.
	 *
	 * <p>This method executes a query on the `orders` table to sum up the total prices of all orders
	 * placed at a specified restaurant within the provided date range. If no orders match the criteria, 
	 * the method returns 0.0.
	 *
	 * @param res_id The ID of the restaurant for which the income is being calculated.
	 * @param first_date The start date of the date range.
	 * @param last_date The end date of the date range.
	 * @return The total income generated by the restaurant within the specified date range, or 0.0 
	 *         if no matching records are found.
	 * @throws SQLException If a database access error occurs.
	 */
	public Double calculateIncomeByDate(int res_id, LocalDate first_date, LocalDate last_date) throws SQLException {
		String sql = "SELECT SUM(total_price) AS total_sum " + "FROM orders " + "WHERE restaurant_id = ? "
				+ "AND DATE(start_time) BETWEEN ? AND ?;";

		try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
			// Set the parameters
			preparedStatement.setInt(1, res_id);
			preparedStatement.setDate(2, Date.valueOf(first_date)); // Convert LocalDate to java.sql.Date
			preparedStatement.setDate(3, Date.valueOf(last_date)); // Convert LocalDate to java.sql.Date

			// Execute the query
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getDouble("total_sum");
				} else {
					return 0.0; // No matching records
				}
			}
		}

	}
	
	/**
	 * Retrieves the count of orders for each dish in a specific restaurant within a given date range.
	 *
	 * <p>This method executes a query to find out how many times each dish was ordered 
	 * at a specified restaurant within the provided date range. The results are returned as a list of strings, 
	 * where each string contains the dish name and the number of times it was ordered.
	 *
	 * @param restaurantId The ID of the restaurant for which the dish order counts are being retrieved.
	 * @param startDate The start date of the date range.
	 * @param endDate The end date of the date range.
	 * @return A list of strings, each containing the dish name and the number of times it was ordered,
	 *         sorted in descending order by the number of times ordered.
	 * @throws SQLException If a database access error occurs.
	 */
    public List<String> getDishOrderCounts(int restaurantId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT d.name AS dish_name, " +
                     "COALESCE(COUNT(o.id), 0) AS times_ordered " +
                     "FROM dishes d " +
                     "LEFT JOIN menu m ON d.id = m.dish_id " +
                     "LEFT JOIN order_items oi ON d.id = oi.dish_id " +
                     "LEFT JOIN orders o ON oi.order_id = o.id " +
                     "AND o.restaurant_id = ? " +
                     "AND DATE(o.start_time) BETWEEN ? AND ? " +
                     "WHERE m.restaurant_id = ? " +
                     "GROUP BY d.id, d.name " +
                     "ORDER BY times_ordered DESC;";

        List<String> dishOrderCounts = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            // Set the restaurantId and date range parameters
            preparedStatement.setInt(1, restaurantId);
            preparedStatement.setDate(2, Date.valueOf(startDate));
            preparedStatement.setDate(3, Date.valueOf(endDate));
            preparedStatement.setInt(4, restaurantId);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String dishName = resultSet.getString("dish_name");
                    int timesOrdered = resultSet.getInt("times_ordered");

                    // Create a string with the dish name and the number of times it was ordered
                    String resultString = dishName + ": " + timesOrdered + " times";

                    // Add the string to the list
                    dishOrderCounts.add(resultString);
                }
            }
        }

        return dishOrderCounts;
    }
    /**
     * Retrieves statistics on late orders for a specific restaurant within a given date range.
     *
     * <p>This method executes a query to count the number of late orders and the total number of orders 
     * for a specified restaurant within the provided date range. The results are returned as an array 
     * of integers, where the first element represents the count of late orders and the second element 
     * represents the total number of orders.
     *
     * @param restaurantId The ID of the restaurant for which the late order statistics are being retrieved.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return An array of integers, where the first element is the count of late orders and the second element is the total number of orders.
     * @throws SQLException If a database access error occurs.
     */
    public int[] getLateOrderStats( int restaurantId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT " +
                     "SUM(CASE WHEN o.late = 1 THEN 1 ELSE 0 END) AS late_orders_count, " +
                     "COUNT(o.id) AS total_orders_count " +
                     "FROM orders o " +
                     "WHERE o.restaurant_id = ? " +
                     "AND DATE(o.start_time) BETWEEN ? AND ?";

        int[] orderStats = new int[2]; // Index 0 for late orders, index 1 for total orders

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, restaurantId);
            preparedStatement.setDate(2, Date.valueOf(startDate));
            preparedStatement.setDate(3, Date.valueOf(endDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    orderStats[0] = resultSet.getInt("late_orders_count"); // Late orders count
                    orderStats[1] = resultSet.getInt("total_orders_count"); // Total orders count
                }
            }
        }

        return orderStats;
    }
    /**
     * Retrieves quarterly order statistics for a specified date range.
     *
     * <p>This method queries the database to count the number of orders and calculate the total revenue 
     * for each day within the specified date range. The results are stored in a map where the key is the 
     * date (as a string) and the value is an array of integers, where the first element represents the 
     * number of orders on that day and the second element represents the total revenue for that day.
     *
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return A map where the key is the date (in "yyyy-MM-dd" format) and the value is an array of 
     *         integers. The first element of the array represents the number of orders on that day, 
     *         and the second element represents the total revenue for that day.
     * @throws SQLException If a database access error occurs.
     */
    public Map<String, int[]> getQuarterlyOrderStats( LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT DATE(o.start_time) AS order_day, COUNT(o.id) AS order_count, SUM(o.total_price) AS total_revenue " +
                     "FROM orders o " +
                     "WHERE o.start_time BETWEEN ? AND ? " +
                     "GROUP BY DATE(o.start_time) " +
                     "ORDER BY DATE(o.start_time)";

        Map<String, int[]> orderStatsMap = new HashMap<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            
            System.out.println("<DBConnector> Executing query: " + preparedStatement.toString());


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String day = resultSet.getString("order_day");
                    int orderCount = resultSet.getInt("order_count");
                    int totalRevenue = resultSet.getInt("total_revenue");
                    
                    System.out.println("Day: " + day + ", Orders: " + orderCount + ", Revenue: " + totalRevenue);


                    // Store the result in the map
                    orderStatsMap.put(day, new int[]{orderCount, totalRevenue});
                }
            }
        }
        
        System.out.println("<DBConnector> Final Report Data: " + orderStatsMap.toString());
        
        return orderStatsMap;
    }
    
    
    
}
