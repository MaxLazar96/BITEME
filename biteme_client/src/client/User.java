package client;

import java.io.Serializable;
/**
 * The {@code User} class represents a user in the system. This class implements {@code Serializable}
 * to allow user objects to be transferred over the network. It contains all the necessary details
 * about a user such as their ID, name, contact information, account credits, and user type.
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
     * Default constructor for creating a new {@code User} object with default values.
     */
    public User() {
    }
    /**
     * Constructs a new {@code User} object with the specified details.
     *
     * @param id          the user's unique identifier
     * @param firstName   the user's first name
     * @param lastName    the user's last name
     * @param email       the user's email address
     * @param address     the user's physical address
     * @param username    the user's username
     * @param password    the user's password
     * @param phoneNumber the user's phone number
     * @param credits     the user's account credits
     * @param homeBranch  the user's home branch
     * @param userType    the type of user (e.g., customer, admin, etc.)
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
     * Gets the user's unique identifier.
     *
     * @return the user's ID
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the user's unique identifier.
     *
     * @param id the new ID to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the user's first name.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Sets the user's first name.
     *
     * @param firstName the new first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * Gets the user's last name.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Sets the user's last name.
     *
     * @param lastName the new last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Gets the user's email address.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the user's email address.
     *
     * @param email the new email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gets the user's physical address.
     *
     * @return the address of the user
     */
    public String getAddress() {
        return address;
    }
    /**
     * Sets the user's physical address.
     *
     * @param address the new address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * Gets the user's username.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the user's username.
     *
     * @param username the new username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Gets the user's password.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets the user's password.
     *
     * @param password the new password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Gets the user's phone number.
     *
     * @return the phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * Sets the user's phone number.
     *
     * @param phoneNumber the new phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * Gets the user's account credits.
     *
     * @return the user's credits
     */
    public double getCredits() {
        return credits;
    }
    /**
     * Sets the user's account credits.
     *
     * @param credits the new amount of credits to set
     */
    public void setCredits(double credits) {
        this.credits = credits;
    }
    /**
     * Gets the user's home branch.
     *
     * @return the home branch of the user
     */
    public String getHomeBranch() {
        return homeBranch;
    }

    /**
     * Sets the user's home branch.
     *
     * @param homeBranch the new home branch to set
     */
    public void setHomeBranch(String homeBranch) {
        this.homeBranch = homeBranch;
    }
    /**
     * Gets the user's type (e.g., customer, admin).
     *
     * @return the type of the user
     */
    public String getUserType() {
        return userType;
    }
    /**
     * Sets the user's type (e.g., customer, admin).
     *
     * @param userType the new user type to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
    /**
     * Returns a string representation of the user object.
     *
     * @return a string containing all the details of the user
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
