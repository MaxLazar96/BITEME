# BITEME

A client-server food delivery system in Java and JavaFX: customers choose a restaurant by location and place orders, restaurant staff manage menus and orders, and managers view reports. The server handles everything against a MySQL database.

> Team project (group 15), B.Sc. Software Engineering (Braude College), 4 students.
> **My part:** most of the server side: client-server communication, order processing, location-based restaurant selection, and the MySQL queries.

![BITEME client screen](docs/screenshot-client.png)
<!-- Add a screenshot of the client app at docs/screenshot-client.png -->

## Features
- **Restaurant selection by location**: customers see restaurants near them
- **Full order flow**: from choosing dishes to checkout and order status
- **Client-server architecture**: a Java client app talks to a Java server over the network
- **MySQL database** for users, restaurants, menus and orders
- **Role-based screens:** customer, restaurant worker, certified worker, branch manager and CEO
- **Quarterly reports** for managers

## Tech stack
| Area | Tools |
|---|---|
| Language | Java |
| Database | MySQL |
| Architecture | Client-server, object-oriented design |
| UI | JavaFX (FXML) |
| Networking | OCSF client-server framework |

## Project structure
```
biteme_server/   Server: client connections, request handling, MySQL access (DBConnector)
biteme_client/   JavaFX client: one screen and controller per role (customer, worker, manager, CEO)
biteme_shared/   Shared entities: Order, Dish, Restaurant
OCSF/            Object Client-Server Framework used for networking
docs/javadoc/    Generated API documentation
```

## Run it locally
1. Import `OCSF`, `biteme_shared`, `biteme_server` and `biteme_client` into Eclipse as existing projects (Java with JavaFX).
2. Create a MySQL database with the tables `users`, `restaurants`, `menu`, `dishes`, `orders` and `order_items`. The server connects as `root` on `localhost:3306`.
3. Run `ServerApp` and enter the database name and password.
4. Run `ClientUI`, connect to the server's IP, and log in.
