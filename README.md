# 🛒 Spring Boot E-Commerce Backend API

A robust RESTful backend system built with Spring Boot and MongoDB. This project handles the complete lifecycle of an e-commerce transaction, including product management, smart cart logic, transactional order creation, and asynchronous payment processing.

## 🚀 Key Features
* **Inventory Management:** Create and list products with stock tracking.
* **Smart Cart:** Auto-updates quantities for existing items; validates stock before adding.
* **Order Processing:** Atomic transactions to ensure stock is deducted only when orders are confirmed.
* **Mock Payment Service:** Simulates a real-world payment gateway with a 3-second processing delay and asynchronous Webhook callbacks.

## 🛠️ Tech Stack
* **Language:** Java 17+
* **Framework:** Spring Boot 3.x (Web, Data MongoDB)
* **Database:** MongoDB Atlas (Cloud)
* **Testing:** Postman

## ⚙️ Setup & Configuration

1.  **Clone the repository** to your local machine.
2.  **Configure MongoDB:**
    Open `src/main/resources/application.properties` and add your MongoDB Atlas connection string:
    ```properties
    spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster0.xyz.mongodb.net/?retryWrites=true&w=majority
    spring.data.mongodb.database=ecommerce
    ```
3.  **Run the Application:**
    Run `EcommerceApplication.java` from your IDE. The server will start on port `8080`.

## 📡 API Documentation

### Product APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/products` | Create a new product. |
| `GET` | `/api/products` | Retrieve all available products. |

### Cart APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/cart/add` | Add item to cart (validates stock). |
| `GET` | `/api/cart/{userId}` | Get current user's cart. |
| `DELETE`| `/api/cart/{userId}/clear`| Empty the cart. |

### Order & Payment APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/orders` | Create order from cart items. |
| `GET` | `/api/orders/{id}` | Get order details and status. |
| `POST` | `/api/payments/create` | Initiate mock payment (Starts 3s timer). |

## 🔄 Business Logic Flow
1.  **Order Creation:** User places order → Stock checked → Order status set to `CREATED`.
2.  **Payment:** User requests payment → System returns `PENDING` immediately.
3.  **Processing:** Background service waits 3 seconds.
4.  **Completion:** System triggers a self-webhook to update Order status to `PAID`.
