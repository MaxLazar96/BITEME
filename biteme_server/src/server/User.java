package server;

import java.io.Serializable;

/**
 * The {@code User} class represents a user in the system. It contains user-related 
 * information such as personal details, account information, and user type.
 * 
 * <p>This class implements {@code Serializable} to allow user objects to be easily 
 * transmitted over a network or saved to a file.</p>
 * 
 * <p>Each user has the following attributes:</p>
 * <ul>
 *   <li>{@code id} - The unique identifier for the user</li>
 *   <li>{@code firstName} - The first name of the user</li>
 *   <li>{@code lastName} - The last name of the user</li>
 *   <li>{@code email} - The email address of the user</li>
 *   <li>{@code address} - The physical address of the user</li>
 *   <li>{@code username} - The username used by the user to log into the system</li>
 *   <li>{@code password} - The password associated with the user's account</li>
 *   <li>{@code phoneNumber} - The user's phone number</li>
 *   <li>{@code credits} - The user's account balance or credits</li>
 *   <li>{@code homeBranch} - The branch associated with the user</li>
 *   <li>{@code userType} - The type of user (e.g., customer, worker, manager)</li>
 * </ul>
 * 
 * @version 1.0
 * @since 1.0
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String username;
    private String password;
    private String phoneNumber;
    private double credits;
    private String homeBranch;
    private String userType;

    /**
     * Default constructor that initializes a new instance of the {@code User} class 
     * with default values.
     */
    public User() {
    }
    /**
     * Parameterized constructor that initializes a new instance of the {@code User} class 
     * with the specified values.
     * 
     * @param id the unique identifier for the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param address the physical address of the user
     * @param username the username used by the user to log into the system
     * @param password the password associated with the user's account
     * @param phoneNumber the user's phone number
     * @param credits the user's account balance or credits
     * @param homeBranch the branch associated with the user
     * @param userType the type of user (e.g., customer, worker, manager)
     */
    public User(int id, String firstName, String lastName, String email, String address, String username,
                String password, String phoneNumber, double credits, String homeBranch, String userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.credits = credits;
        this.homeBranch = homeBranch;
        this.userType = userType;
    }

    /**
     * Gets the unique identifier for the user.
     * 
     * @return the user's ID
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the unique identifier for the user.
     * 
     * @param id the user's ID
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the first name of the user.
     * 
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Sets the first name of the user.
     * 
     * @param firstName the user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * Gets the last name of the user.
     * 
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Sets the last name of the user.
     * 
     * @param lastName the user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Gets the email address of the user.
     * 
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email address of the user.
     * 
     * @param email the user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gets the physical address of the user.
     * 
     * @return the user's address
     */
    public String getAddress() {
        return address;
    }
    /**
     * Sets the physical address of the user.
     * 
     * @param address the user's address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * Gets the username of the user.
     * 
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the username of the user.
     * 
     * @param username the user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Gets the password of the user.
     * 
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets the password of the user.
     * 
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Gets the phone number of the user.
     * 
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * Sets the phone number of the user.
     * 
     * @param phoneNumber the user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * Gets the credits available in the user's account.
     * 
     * @return the user's credits
     */
    public double getCredits() {
        return credits;
    }
    /**
     * Sets the credits available in the user's account.
     * 
     * @param credits the user's credits
     */
    public void setCredits(double credits) {
        this.credits = credits;
    }
    /**
     * Gets the home branch associated with the user.
     * 
     * @return the user's home branch
     */
    public String getHomeBranch() {
        return homeBranch;
    }
    /**
     * Sets the home branch associated with the user.
     * 
     * @param homeBranch the user's home branch
     */
    public void setHomeBranch(String homeBranch) {
        this.homeBranch = homeBranch;
    }
    /**
     * Gets the user type (e.g., customer, worker, manager).
     * 
     * @return the user's type
     */
    public String getUserType() {
        return userType;
    }
    /**
     * Sets the user type (e.g., customer, worker, manager).
     * 
     * @param userType the user's type
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
    /**
     * Returns a string representation of the user.
     * 
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "User{" +
               "id='" + id + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", address='" + address + '\'' +
               ", username='" + username + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", credits=" + credits +
               ", homeBranch='" + homeBranch + '\'' +
               ", userType='" + userType + '\'' +
               '}';
    }
}

