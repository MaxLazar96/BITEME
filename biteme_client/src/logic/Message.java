package logic;
/**
 * The {@code Message} class represents a message object used for communication 
 * between the client and server. It implements the {@code Serializable} interface 
 * to allow instances of this class to be transmitted over a network or saved to a file.
 *
 * <p>Each message contains:</p>
 * <ul>
 *   <li>A {@code command} - A string representing the command or action to be performed.</li>
 *   <li>Optional {@code data} - An object containing additional data or information related to the command.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * // Creating a message with just a command
 * Message msg1 = new Message("login");
 *
 * // Creating a message with a command and additional data
 * Message msg2 = new Message("updateProfile", userProfile);
 * }
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String command;
    private Object data;

    /**
     * Constructs a {@code Message} object with the specified command.
     *
     * @param command the command to be associated with this message
     */
    public Message(String command) {
        this.command = command;
    }

    /**
     * Constructs a {@code Message} object with the specified command and data.
     *
     * @param command the command to be associated with this message
     * @param data the additional data to be sent with this message
     */
    public Message(String command, Object data) {
        this.command = command;
        this.data = data;
    }
    /**
     * Returns the command associated with this message.
     *
     * @return the command as a {@code String}
     */
    public String getCommand() {
        return command;
    }
    /**
     * Returns the additional data associated with this message.
     *
     * @return the data as an {@code Object}
     */
    public Object getData() {
        return data;
    }
}
