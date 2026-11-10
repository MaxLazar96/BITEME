package logic;

import java.io.Serializable;
/**
 * The {@code Dish} class represents a dish in a restaurant menu. It implements 
 * the {@link Serializable} and {@link Cloneable} interfaces to allow dishes to be serialized 
 * and cloned. Each dish has attributes like ID, name, type, size, price, roast level, and remark.
 * <p>
 * The class provides various constructors for creating a dish with different combinations of attributes,
 * along with standard getters and setters for accessing and modifying the dish's properties.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * Dish dish = new Dish("Espresso", "Coffee", "Small", 2.50);
 * dish.setRemark("No sugar");
 * System.out.println(dish);
 * }
 * </pre>
 * </p>
 * 
 * @version 1.0
 * @since 1.0
 */

public class Dish implements Serializable,Cloneable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String type;
    private String size;
    private double price;
    public Dish(String name, String type, String size, double price) {
		super();
		this.name = name;
		this.type = type;
		this.size = size;
		this.price = price;
	}

	private String roastLevel;
    private String remark;
    
    /**
     * Constructs a new {@code Dish} with the specified name, type, size, and price.
     *
     * @param name  the name of the dish
     * @param type  the type or category of the dish
     * @param size  the size of the dish
     * @param price the price of the dish
     */

    public Dish(int id, String name, String type, String size, double price) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.size = size;
		this.price = price;
	}
    /**
     * Constructs a new {@code Dish} with the specified ID, name, type, size, and price.
     *
     * @param id    the unique identifier for the dish
     * @param name  the name of the dish
     * @param type  the type or category of the dish
     * @param size  the size of the dish
     * @param price the price of the dish
     */

    /**
     * Constructs a new {@code Dish} with no specified attributes. This can be used
     * as a default constructor.
     */
    public Dish() {}
    
    

    
    /**
     * Constructs a new {@code Dish} with the specified attributes, including roast level and remark.
     *
     * @param id         the unique identifier for the dish
     * @param name       the name of the dish
     * @param type       the type or category of the dish
     * @param size       the size of the dish
     * @param price      the price of the dish
     * @param roastLevel the roast level of the dish (e.g., for coffee)
     * @param remark     any special remark or note about the dish
     */
    public Dish(int id, String name, String type, String size, double price, String roastLevel, String remark) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.roastLevel = roastLevel;
        this.remark = remark;
    }
    /**
     * Constructs a new {@code Dish} with the specified name and remark. 
     * This constructor initializes other attributes to default or placeholder values.
     *
     * @param name   the name of the dish
     * @param remark any special remark or note about the dish
     */
    
    public Dish(String name, String remark) {
        this.id = 0; // default or placeholder value
        this.name = name;
        this.type = ""; // default or placeholder value
        this.size = ""; // default or placeholder value
        this.price = 0.0; // default or placeholder value
        this.roastLevel = ""; // default or placeholder value
        this.remark = remark;
    }
    

    /**
     * Returns the ID of the dish.
     *
     * @return the ID of the dish
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the ID of the dish.
     *
     * @param id the new ID of the dish
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Returns the name of the dish.
     *
     * @return the name of the dish
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the dish.
     *
     * @param name the new name of the dish
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the type of the dish.
     *
     * @return the type of the dish
     */
    public String getType() {
        return type;
    }
    
    /**
     * Sets the type of the dish.
     *
     * @param type the new type of the dish
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Returns the size of the dish.
     *
     * @return the size of the dish
     */
    public String getSize() {
        return size;
    }
    
    /**
     * Sets the size of the dish.
     *
     * @param size the new size of the dish
     */
    public void setSize(String size) {
        this.size = size;
    }
    
    /**
     * Returns the price of the dish.
     *
     * @return the price of the dish
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Sets the price of the dish.
     *
     * @param price the new price of the dish
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * Returns the roast level of the dish.
     *
     * @return the roast level of the dish
     */
    public String getRoastLevel() {
        return roastLevel;
    }
    /**
     * Sets the roast level of the dish.
     *
     * @param roastLevel the new roast level of the dish
     */
    public void setRoastLevel(String roastLevel) {
        this.roastLevel = roastLevel;
    }
    /**
     * Returns any special remark or note about the dish.
     *
     * @return the remark of the dish
     */
    public String getRemark() {
        return remark;
    }
    /**
     * Sets a special remark or note about the dish.
     *
     * @param remark the new remark of the dish
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public Dish clone() {
        try {
            return (Dish) super.clone();
        } catch (CloneNotSupportedException e) {
            // If cloning is not supported, this should not happen since we are implementing Cloneable
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Creates and returns a copy of this {@code Dish}. This method implements the 
     * {@link Cloneable} interface.
     *
     * @return a clone of this {@code Dish}
     * @throws RuntimeException if the cloning is not supported, though this exception 
     *         should not occur as the class implements {@link Cloneable}
     */
    @Override
    public String toString() {
        return "Dish{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                ", price=" + price +
                ", roastLevel='" + roastLevel + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
