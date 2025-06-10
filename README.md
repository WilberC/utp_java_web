# UTP Web Development Project

A Spring Boot web application with Thymeleaf, Bootstrap, JPA, and JWT authentication.

## Technologies Used

- Java 21
- Spring Boot 3.2.3
- Spring Security with JWT
- Spring Data JPA
- Thymeleaf
- Bootstrap
- PostgreSQL 16
- Flyway for database migrations
- Docker & Docker Compose
- Maven
- Lombok

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Docker and Docker Compose
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

## Getting Started

1. Clone the repository:
```bash
git clone <repository-url>
cd web-development
```

2. Start the PostgreSQL database:
```bash
docker-compose up -d
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Database Management

### Using Docker

The project uses PostgreSQL 16 running in Docker. The database configuration is managed through Docker Compose.

To start the database:
```bash
docker-compose up -d
```

To stop the database:
```bash
docker-compose down
```

To view database logs:
```bash
docker-compose logs -f postgres
```

### Database Migrations

The project uses Flyway for database migrations. Migration scripts are located in `src/main/resources/db/migration/`.

To create a new migration:
1. Create a new SQL file in `src/main/resources/db/migration/`
2. Name it following the pattern: `V{version}__{description}.sql`
   - Example: `V2__add_user_roles.sql`
3. Write your SQL migration script
4. Run the application - Flyway will automatically apply the migration

To manually run migrations:
```bash
mvn flyway:migrate
```

To clean the database (WARNING: This will delete all data):
```bash
mvn flyway:clean
```

### Seed Data

The project includes comprehensive seed data in Spanish for development and testing purposes. The seed data is automatically applied when running migrations and includes:

#### Users (10 total)
- **1 Admin user**: `admin@utp.edu.pe` / `mypassword123`
- **9 Customer users** with various statuses (ACTIVE, INACTIVE, SUSPENDED)
- All users share the same password: `mypassword123`

#### Categories (10 total)
- Electrónicos, Libros, Ropa, Hogar y Jardín, Deportes y Aire Libre
- Juguetes y Juegos, Salud y Belleza, Automotriz, Alimentos y Bebidas, Suministros de Oficina

#### Products (20 total)
- Electronics: Laptops, smartphones, headphones
- Books: Programming books (Clean Code, Design Patterns, Spring Boot)
- Clothing: T-shirts, jeans, running shoes
- Various other products across all categories
- All product descriptions and names are in Spanish

#### Orders (10 total)
- Various order statuses: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
- Different payment methods: CREDIT_CARD, BANK_TRANSFER, CASH_ON_DELIVERY
- Realistic order amounts and delivery dates

#### Additional Data
- **Payment Transactions**: 8 transactions with various statuses
- **Email Templates**: 5 templates in Spanish (welcome, order confirmation, shipping, password reset, promotional)
- **Notifications**: 15 notifications for order updates
- **User Notification Preferences**: Preferences for all users
- **Report Refresh Logs**: 30 log entries for analytics

#### Sample Login Credentials
```
Admin:     admin@utp.edu.pe / mypassword123
Customer:  juan.perez@email.com / mypassword123
Customer:  maria.garcia@email.com / mypassword123
```

The seed data provides a realistic e-commerce environment for testing all application features including user management, product catalog, order processing, and reporting.

### Database Connection Details

- Host: localhost
- Port: 5432
- Database: utp_web_dev
- Username: utp_user
- Password: utp_password

## Features

- RESTful API endpoints
- JWT-based authentication
- Thymeleaf templates with Bootstrap
- PostgreSQL database with Flyway migrations
- Spring Security integration
- JPA for data persistence

## Project Structure

```
src/main/java/com/utp/webdevelopment/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── model/          # Entity classes
├── repository/     # JPA repositories
├── service/        # Business logic
├── security/       # Security related classes
└── WebDevelopmentApplication.java

src/main/resources/
├── db/
│   └── migration/  # Flyway migration scripts
├── static/         # Static resources (CSS, JS, images)
├── templates/      # Thymeleaf templates
└── application.properties
```

## Security

The application uses JWT (JSON Web Tokens) for authentication. The JWT secret key is configured in `application.properties`. Make sure to change it in production.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 