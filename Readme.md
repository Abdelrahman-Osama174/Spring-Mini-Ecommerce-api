# Spring E-commerce Backend Application

A fully functional **RESTful E-commerce API** built with **Spring Boot 3.x** (using Spring Boot 4.0.1 parent), **Spring Data JPA**, **PostgreSQL**, and **Lombok**. The application supports product management with image upload, order placement with stock deduction, and basic search functionality.

## Features

### Product Management
- Create, read, update, and delete products
- Upload and store product images as binary data in the database (`BYTEA` in PostgreSQL)
- Retrieve product images by product ID
- Search products by keyword (name, brand, category, description)

### Order Management
- Place orders with multiple items
- Automatic stock quantity deduction
- Unique order ID generation
- Retrieve all orders with detailed item information

### Technical Highlights
- Clean separation of concerns (Controller → Service → Repository)
- Proper use of DTOs for request/response
- Multipart file handling for image uploads
- CORS enabled for frontend integration
- Proper HTTP status code responses
- Lombok for reduced boilerplate

## Tech Stack

- **Java 21**
- **Spring Boot 3.x** (parent version 4.0.1)
- **Spring Data JPA** (Hibernate)
- **PostgreSQL**
- **Lombok**
- **Maven**

## API Endpoints

### Products (`/api`)

| Method | Endpoint                     | Description                              | Request Body / Params                          |
|--------|------------------------------|------------------------------------------|------------------------------------------------|
| GET    | `/products`                  | Get all products                         | -                                              |
| GET    | `/product/{id}`              | Get product by ID                        | Path variable: `id`                            |
| GET    | `/product/{id}/image`        | Get product image                        | Path variable: `id`                            |
| GET    | `/products/search?keyword=..`| Search products by keyword               | Query param: `keyword`                         |
| POST   | `/product`                   | Add new product with image               | Multipart: `product` (JSON string), `imageFile` |
| PUT    | `/product/{id}`              | Update product with optional new image   | Multipart: `product` (JSON string), `imageFile` |
| DELETE | `/product/{id}`              | Delete product                           | Path variable: `id`                            |

### Orders (`/api`)

| Method | Endpoint             | Description             | Request Body                     |
|--------|----------------------|-------------------------|----------------------------------|
| POST   | `/orders/place`      | Place a new order       | JSON: `OrderRequest`             |
| GET    | `/orders`            | Get all orders          | -                                |

## Data Models

### Product
- `id`, `name`, `brand`, `category`, `description`
- `price` (BigDecimal), `releaseDate`, `productAvailable`, `stockQuantity`
- Image fields: `imageName`, `imageType`, `imageData` (byte[])

### Order
- Custom `orderId` (e.g., ORD-XXXXXXXX)
- `customerName`, `customerEmail`, `status`, `orderDate`
- One-to-Many relationship with `OrderItem`

### OrderItem
- References `Product` and `Order`
- `quantity`, `totalPrice`

## Project Structure

```
src/main/java/com.abdelrahman.EcommerceApp/
├── controller/
│   ├── ProductController.java
│   └── OrderController.java
├── model/
│   ├── Product.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── dto/
│       ├── OrderRequest.java
│       ├── OrderResponse.java
│       ├── OrderItemRequest.java
│       └── OrderItemResponse.java
├── service/
│   ├── ProductService.java
│   └── OrderService.java
├── repository/
│   ├── ProductRepository.java
│   └── OrderRepository.java
```

## Database Configuration

Configure PostgreSQL in `src/main/resources/application.properties` or `application.yml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=postgres
spring.datasource.password=******
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> Note: Uses `@Lob` for image storage → PostgreSQL `BYTEA` column.

## How to Run

1. **Clone the repository**
2. **Set up PostgreSQL database**
3. **Update database credentials** in `application.properties`
4. **Build and run**:

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing with Postman

### Adding a Product
- POST `/api/product`
- Content-Type: `multipart/form-data`
- Form-data:
    - Key: `product` → Value: JSON string of product (e.g., `{"name":"iPhone 15", ...}`)
    - Key: `imageFile` → Select file

### Placing an Order
- POST `/api/orders/place`
- Content-Type: `application/json`
- Body (raw JSON):

```json
{
  "customerName": "John Doe",
  "email": "john@example.com",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

## Notes

- Image uploads are required for add/update product operations (can be made optional by checking `image.isEmpty()`).
- Stock is automatically reduced when an order is placed.
- Order status is initially set to `"PLACED"`.
- Uses `ObjectMapper` to parse JSON string in multipart requests (workaround for Spring's limitation with `@RequestBody` + multipart).

## Future Improvements

- Add authentication & authorization (JWT/Spring Security)
- Pagination for product/order lists
- Validation using `@Valid` and Bean Validation
- Exception handling with `@ControllerAdvice`
- Unit/integration tests
- Separate image storage (e.g., AWS S3 or local filesystem)
- Email notification on order placement
- Admin and user roles

## License

This project is for educational purposes. Feel free to use and modify as needed.

---
**Developed by Abdelrahman**  
January 2026