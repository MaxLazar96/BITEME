package logic;

import java.io.Serializable;

/**
 * The {@code Message} class represents a simple message that can be used to communicate
 * commands and data between different parts of a system, such as between a client and a server.
 * The class implements {@link Serializable} to allow instances of {@code Message}
 * to be serialized for storage or transmission.
 * 
 * <p>
 * A {@code Message} object consists of a command, which is a {@link String}, and optional
 * additional data, which can be any {@link Object}. The class provides constructors
 * for creating messages with just a command or with both a command and data.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * Message msg1 = new Message("LOGIN");
 * Message msg2 = new Message("UPDATE", new User("username", "password"));
 * }
 * </pre>
 * </p>
 * 
 * @version 1.0
 * @since 1.0
 */

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String command;
    private Object data;

    /**
     * Constructs a new {@code Message} with the specified command.
     * 
     * @param command the command to be included in the message
     */
    public Message(String command) {
        this.command = command;
    }

    /**
     * Constructs a new {@code Message} with the specified command and additional data.
     * 
     * @param command the command to be included in the message
     * @param data    the additional data to be included in the message
     */
    public Message(String command, Object data) {
        this.command = command;
        this.data = data;
    }
    /**
     * Returns the command included in the message.
     * 
     * @return the command
     */
    public String getCommand() {
        return command;
    }
    /**
     * Returns the additional data included in the message.
     * 
     * @return the data, or {@code null} if no data was provided
     */
    public Object getData() {
        return data;
    }
}
