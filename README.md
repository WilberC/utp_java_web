# UTP Mini Market Expresst Project

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

## Environment Setup

The project uses environment variables for configuration. **All environment variables are required** and must be set before running the application.

1. Copy the example environment file:
```bash
cp .env.example .env
```

2. Edit the `.env` file with your configuration values:
```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=utp_web_dev
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# JWT Configuration
JWT_SECRET=your-256-bit-secret-key-here-make-it-long-and-secure-in-production
JWT_EXPIRATION=86400000

# Server Configuration
SERVER_PORT=8080
SERVER_CONTEXT_PATH=/

# Application Configuration
SPRING_PROFILES_ACTIVE=dev
```

**Important Security Notes:**
- Never commit the `.env` file to version control (it's already in `.gitignore`)
- Use strong, unique passwords in production
- Generate a secure JWT secret key for production environments
- The `.env.example` file contains placeholder values and is safe to commit
- **All environment variables are required** - the application will fail to start if any are missing

## Getting Started

1. Clone the repository:
```bash
git clone <repository-url>
cd web-development
```

2. Set up environment variables (see Environment Setup section above)

3. Start the PostgreSQL database:
```bash
docker-compose up -d
```

4. Build the project:
```bash
mvn clean install
```

5. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

**Main Application URL:** `http://localhost:8080/admin/dashboard`

## Application Routes

This is a **web application** (not a REST API) that serves HTML pages using Thymeleaf templates. All routes return HTML views and follow web application conventions.

### Main Routes
- `GET /` → Redirects to dashboard
- `GET /admin/dashboard` → Main dashboard with statistics
- `GET /login` → Login page
- `GET /logout` → Logout functionality

### User Management (`/admin/users`)
- `GET /admin/users` → List all users with pagination and search
- `GET /admin/users/create` → Show user creation form
- `POST /admin/users/create` → Create new user
- `GET /admin/users/{id}` → View user details
- `GET /admin/users/{id}/edit` → Show user edit form
- `POST /admin/users/{id}/edit` → Update user
- `POST /admin/users/{id}/delete` → Delete user
- `GET /admin/users/stats` → User statistics page

### Product Management (`/admin/products`)
- `GET /admin/products` → List all products with pagination, search, and filtering
- `GET /admin/products/create` → Show product creation form
- `POST /admin/products/create` → Create new product
- `GET /admin/products/{id}` → View product details
- `GET /admin/products/{id}/edit` → Show product edit form
- `POST /admin/products/{id}/edit` → Update product
- `POST /admin/products/{id}/delete` → Delete product
- `POST /admin/products/{id}/toggle-featured` → Toggle featured status
- `GET /admin/products/featured` → List featured products
- `GET /admin/products/low-stock` → List products with low stock
- `GET /admin/products/stats` → Product statistics page

### Category Management (`/admin/categories`)
- `GET /admin/categories` → List all categories
- `GET /admin/categories/create` → Show category creation form
- `POST /admin/categories/create` → Create new category
- `GET /admin/categories/{id}` → View category details
- `GET /admin/categories/{id}/edit` → Show category edit form
- `POST /admin/categories/{id}/edit` → Update category
- `POST /admin/categories/{id}/delete` → Delete category

### Order Management (`/admin/orders`)
- `GET /admin/orders` → List all orders with pagination and status filtering
- `GET /admin/orders/{id}` → View order details
- `POST /admin/orders/{id}/update-status` → Update order status
- `GET /admin/orders/pending` → List pending orders
- `GET /admin/orders/processing` → List processing orders
- `GET /admin/orders/shipped` → List shipped orders
- `GET /admin/orders/delivered` → List delivered orders
- `GET /admin/orders/cancelled` → List cancelled orders

### Reports (`/admin/reports`)
- `GET /admin/reports` → Reports dashboard with summary statistics
- `GET /admin/reports/sales` → Sales report with date range filtering
- `GET /admin/reports/products` → Product performance report
- `GET /admin/reports/customers` → Customer analytics report
- `GET /admin/reports/inventory` → Inventory status report
- `GET /admin/reports/export/sales` → Export sales report (PDF)
- `GET /admin/reports/export/products` → Export product report (PDF)

### URL Patterns and Conventions

The application follows consistent web application URL patterns:

- **List pages:** `/admin/{resource}` (GET)
- **Create forms:** `/admin/{resource}/create` (GET/POST)
- **View details:** `/admin/{resource}/{id}` (GET)
- **Edit forms:** `/admin/{resource}/{id}/edit` (GET/POST)
- **Delete actions:** `/admin/{resource}/{id}/delete` (POST)
- **Special views:** `/admin/{resource}/featured`, `/admin/{resource}/low-stock`
- **Statistics:** `/admin/{resource}/stats` (GET)

### Security

All admin routes (`/admin/**`) require authentication. The application uses:
- Spring Security for authentication and authorization
- JWT tokens for session management
- Role-based access control (ADMIN, CUSTOMER roles)

### Static Resources

- `GET /css/**` → CSS files (Bootstrap, custom styles)
- `GET /js/**` → JavaScript files
- `GET /images/**` → Image files
- `GET /static/**` → Other static resources

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

The database connection details are configured via environment variables in the `.env` file:

- Host: `${DB_HOST}`
- Port: `${DB_PORT}`
- Database: `${DB_NAME}`
- Username: `${DB_USERNAME}`
- Password: `${DB_PASSWORD}`

## Features

- **Web Application Interface** - Thymeleaf templates with Bootstrap for responsive UI
- **User Management** - Complete CRUD operations for users with role-based access
- **Product Management** - Product catalog with categories, stock management, and featured products
- **Order Management** - Order processing with status tracking and filtering
- **Category Management** - Product categorization system
- **Reporting System** - Comprehensive business reports and analytics
- **JWT-based Authentication** - Secure session management
- **PostgreSQL Database** - Robust data persistence with Flyway migrations
- **Spring Security Integration** - Role-based access control
- **JPA for Data Persistence** - Object-relational mapping with Hibernate

## Project Structure

```
src/main/java/com/utp/webdevelopment/
├── config/          # Configuration classes (Security, etc.)
├── controller/      # Web controllers (User, Product, Category, Order, Report)
├── model/          # Entity classes (User, Product, Category, Order, etc.)
├── repository/     # JPA repositories
├── service/        # Business logic services
├── security/       # Security related classes
└── WebDevelopmentApplication.java

src/main/resources/
├── db/
│   └── migration/  # Flyway migration scripts
├── static/         # Static resources (CSS, JS, images)
│   ├── admin/      # Admin panel templates
│   ├── auth/       # Authentication templates
│   └── layout/     # Layout templates
└── application.properties
```

## Security

The application uses JWT (JSON Web Tokens) for authentication. The JWT secret key is configured via the `JWT_SECRET` environment variable in the `.env` file. Make sure to:

- Use a strong, unique JWT secret key in production
- Never commit the `.env` file to version control
- Regularly rotate the JWT secret key for enhanced security
- Use HTTPS in production to protect token transmission

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 