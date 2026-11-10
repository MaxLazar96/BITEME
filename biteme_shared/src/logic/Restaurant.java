package logic;

import java.io.Serializable;
/**
 * The {@code Restaurant} class represents a restaurant entity in a restaurant management system.
 * It includes details such as the restaurant's ID, name, address, and branch.
 * The class implements {@link Serializable} to allow instances of {@code Restaurant}
 * to be serialized for storage or transmission.
 * 
 * <p>
 * The class provides a default constructor and a parameterized constructor for creating
 * a restaurant with specified attributes. It also includes standard getters and setters
 * for accessing and modifying the restaurant's properties.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * Restaurant restaurant = new Restaurant(1, "The Food Place", "123 Main St", "Downtown");
 * System.out.println(restaurant);
 * }
 * </pre>
 * </p>
 * 
 * @version 1.0
 * @since 1.0
 */

public class Restaurant implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String address;
    private String branch;

    /**
     * Constructs a new {@code Restaurant} with no specified attributes.
     * This can be used as a default constructor.
     */
    public Restaurant() {
    }

    /**
     * Constructs a new {@code Restaurant} with the specified ID, name, address, and branch.
     * 
     * @param id      the unique identifier for the restaurant
     * @param name    the name of the restaurant
     * @param address the address of the restaurant
     * @param branch  the branch of the restaurant
     */
    public Restaurant(int id, String name, String address, String branch) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.branch = branch;
    }

    /**
     * Returns the unique identifier for the restaurant.
     * 
     * @return the restaurant ID
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the unique identifier for the restaurant.
     * 
     * @param id the restaurant ID to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Returns the name of the restaurant.
     * 
     * @return the restaurant name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the restaurant.
     * 
     * @param name the restaurant name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Returns the address of the restaurant.
     * 
     * @return the restaurant address
     */
    public String getAddress() {
        return address;
    }
    /**
     * Sets the address of the restaurant.
     * 
     * @param address the restaurant address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * Returns the branch of the restaurant.
     * 
     * @return the restaurant branch
     */
    public String getBranch() {
        return branch;
    }
    /**
     * Sets the branch of the restaurant.
     * 
     * @param branch the restaurant branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }
    /**
     * Returns a string representation of the restaurant, including its ID, name, address, and branch.
     * 
     * @return a string representation of the restaurant
     */
    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
