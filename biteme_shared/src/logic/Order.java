package logic;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code Order} class represents a customer's order in a restaurant system.
 * It includes details such as the dishes ordered, user information, delivery details,
 * and the status of the order. The class implements {@link Serializable} to allow
 * instances of {@code Order} to be serialized for storage or transmission.
 * 
 * <p>
 * The class provides various constructors for creating an order with different sets of
 * attributes, as well as standard getters and setters for accessing and modifying the order's properties.
 * Additionally, the class includes methods to convert the list of dishes to and from a string
 * representation.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * Order order = new Order(dishesList, userId, "123 Main St", "555-1234", "user@example.com", 
 *                        50.00, "Pending", 1, "12:00 PM", "12:30 PM", "Delivery", "1:00 PM");
 * System.out.println(order.getDishesAsString());
 * }
 * </pre>
 * </p>
 * 
 * @version 1.0
 * @since 1.0
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Dish> dishes;
    private int id;
    /**
     * Returns the unique identifier for the order.
     * 
     * @return the order ID
     */
    public int getId() {
		return id;
	}
    /**
     * Sets the unique identifier for the order.
     * 
     * @param id the order ID to set
     */
	public void setId(int id) {
		this.id = id;
	}

	private int userId;
    private String address;
    private String phoneNumber;
    private String email;
    private double totalPrice;
    private String status;
    private int restaurantId;
    private String startTime;
    private String endTime;
    private String deliveryType;
    private String requiredTime;

    /**
     * Constructs a new {@code Order} with no specified attributes.
     * This can be used as a default constructor.
     */
    public Order() {}
    
    
    /**
     * Constructs a new {@code Order} with the specified ID, list of dishes in string format,
     * delivery type, status, and total price.
     * 
     * @param id           the unique identifier for the order
     * @param dishesStr    a string representing the list of dishes
     * @param deliveryType the type of delivery (e.g., "Delivery" or "Pickup")
     * @param status       the status of the order (e.g., "Pending", "Completed")
     * @param totalPrice   the total price of the order
     */
    public Order(int id, String dishesStr, String deliveryType, String status, double totalPrice) {
        this.id = id;
        this.setDishesFromString(dishesStr);
        this.deliveryType = deliveryType;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    /**
     * Constructs a new {@code Order} with the specified attributes.
     * 
     * @param dishes        the list of {@link Dish} objects in the order
     * @param userId        the ID of the user who placed the order
     * @param address       the delivery address for the order
     * @param phoneNumber   the phone number associated with the order
     * @param email         the email address associated with the order
     * @param totalPrice    the total price of the order
     * @param status        the status of the order (e.g., "Pending", "Completed")
     * @param restaurantId  the ID of the restaurant where the order was placed
     * @param startTime     the start time of the order
     * @param endTime       the end time of the order
     * @param deliveryType  the type of delivery (e.g., "Delivery" or "Pickup")
     * @param requiredTime  the required time for the order to be ready or delivered
     */
    public Order(List<Dish> dishes, int userId, String address, String phoneNumber, String email, double totalPrice, String status, int restaurantId, String startTime, String endTime, String deliveryType, String requiredTime) {
        this.dishes = dishes;
        this.userId = userId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.totalPrice = totalPrice;
        this.status = status;
        this.restaurantId = restaurantId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deliveryType = deliveryType;
        this.requiredTime = requiredTime;
    }

    /**
     * Returns the list of {@link Dish} objects in the order.
     * 
     * @return the list of dishes
     */
    public List<Dish> getDishes() {
        return dishes;
    }
    
    /**
     * Sets the list of {@link Dish} objects in the order.
     * 
     * @param dishes the list of dishes to set
     */
    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
    /**
     * Returns the ID of the user who placed the order.
     * 
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }
    /**
     * Sets the ID of the user who placed the order.
     * 
     * @param userId the user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * Returns the delivery address for the order.
     * 
     * @return the delivery address
     */
    public String getAddress() {
        return address;
    }
    /**
     * Sets the delivery address for the order.
     * 
     * @param address the delivery address to set
     */

    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * Returns the phone number associated with the order.
     * 
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * Sets the phone number associated with the order.
     * 
     * @param phoneNumber the phone number to set
     */

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * Returns the email address associated with the order.
     * 
     * @return the email address
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email address associated with the order.
     * 
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Returns the total price of the order.
     * 
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }
    /**
     * Sets the total price of the order.
     * 
     * @param totalPrice the total price to set
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    /**
     * Returns the status of the order.
     * 
     * @return the order status
     */
    public String getStatus() {
        return status;
    }
    /**
     * Sets the status of the order.
     * 
     * @param status the order status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * Returns the ID of the restaurant where the order was placed.
     * 
     * @return the restaurant ID
     */
    public int getRestaurantId() {
        return restaurantId;
    }
    /**
     * Sets the ID of the restaurant where the order was placed.
     * 
     * @param restaurantId the restaurant ID to set
     */
    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
    /**
     * Returns the start time of the order.
     * 
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }
    /**
     * Sets the start time of the order.
     * 
     * @param startTime the start time to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    /**
     * Returns the end time of the order.
     * 
     * @return the end time
     */
    public String getEndTime() {
        return endTime;
    }
    /**
     * Sets the end time of the order.
     * 
     * @param endTime the end time to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    /**
     * Returns the delivery type of the order.
     * 
     * @return the delivery type
     */
    public String getDeliveryType() {
        return deliveryType;
    }
    /**
     * Sets the delivery type of the order.
     * 
     * @param deliveryType the delivery type to set
     */
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
    /**
     * Returns the required time for the order to be ready or delivered.
     * 
     * @return the required time
     */
    public String getRequiredTime() {
        return requiredTime;
    }
    /**
     * Sets the required time for the order to be ready or delivered.
     * 
     * @param requiredTime the required time to set
     */
    public void setRequiredTime(String requiredTime) {
        this.requiredTime = requiredTime;
    }
    /**
     * Sets the list of dishes for the order from a string representation.
     * 
     * @param dishesStr a string representing the list of dishes, where each dish is in the format "Dish Name (Remark: RemarkText)"
     */
    public void setDishesFromString(String dishesStr) {
        this.dishes = Arrays.stream(dishesStr.split(", "))
                            .map(dishStr -> {
                                String[] parts = dishStr.split(" \\(Remark: ");
                                String name = parts[0];
                                String remark = parts[1].replace(")", "");
                                return new Dish(name, remark);
                            })
                            .collect(Collectors.toList());
    }
    /**
     * Returns the list of dishes in the order as a string representation.
     * 
     * @return a string representing the list of dishes, where each dish is in the format "Dish Name (Remark: RemarkText)"
     */
    public String getDishesAsString() {
        return dishes.stream()
                     .map(dish -> dish.getName() + " (Remark: " + (dish.getRemark() != null ? dish.getRemark() : "No Remark") + ")")
                     .collect(Collectors.joining(", "));
    }

    // Additional methods as needed
}
