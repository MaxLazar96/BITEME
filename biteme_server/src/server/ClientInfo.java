package server;
/**
 * The {@code ClientInfo} class represents information about a client connected to a server.
 * It includes details such as the client's IP address, host name, and connection status.
 * This class is used to encapsulate client-related information for tracking and management purposes.
 * 
 * <p>
 * The class provides a constructor for initializing the client information and
 * standard getters for accessing the client's properties.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * ClientInfo client = new ClientInfo("192.168.1.10", "client-hostname", "Connected");
 * System.out.println(client.getIpAddress());
 * }
 * </pre>
 * </p>
 * 
 * @version 1.0
 * @since 1.0
 */
public class ClientInfo {
    private String ipAddress;
    private String hostName;
    private String status;
    /**
     * Constructs a new {@code ClientInfo} object with the specified IP address, host name, and status.
     * 
     * @param ipAddress the IP address of the client
     * @param hostName  the host name of the client
     * @param status    the connection status of the client (e.g., "Connected", "Disconnected")
     */
    public ClientInfo(String ipAddress, String hostName, String status) {
        this.ipAddress = ipAddress;
        this.hostName = hostName;
        this.status = status;
    }
    /**
     * Returns the IP address of the client.
     * 
     * @return the IP address
     */
    public String getIpAddress() {
        return ipAddress;
    }
    /**
     * Returns the host name of the client.
     * 
     * @return the host name
     */
    public String getHostName() {
        return hostName;
    }
    /**
     * Returns the connection status of the client.
     * 
     * @return the status
     */
    public String getStatus() {
    	return status;
    }
}