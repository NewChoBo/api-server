# Project Name

## Description

This project is a Spring Boot application that manages users and roles. It includes features such as
user authentication, role management, and secure password storage.

## Technologies Used

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Gradle

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7.5 or higher

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/your-repo.git
    ```
2. Navigate to the project directory:
    ```sh
    cd your-repo
    ```
3. Build the project:
    ```sh
    ./gradlew build
    ```

### Running the Application

1. Run the application:
    ```sh
    ./gradlew bootRun
    ```
2. The application will be available at `http://localhost:8080`.

## API Endpoints

### Authentication

- **POST /api/auth/signin**: Sign in with username and password.

### User Management

- **GET /api/users**: Retrieve all users.
- **POST /api/users**: Create a new user.
- **GET /api/users/{id}**: Retrieve a user by ID.
- **PUT /api/users/{id}**: Update a user by ID.
- **DELETE /api/users/{id}**: Delete a user by ID.

### Role Management

- **GET /api/roles**: Retrieve all roles.
- **POST /api/roles**: Create a new role.
- **GET /api/roles/{id}**: Retrieve a role by ID.
- **PUT /api/roles/{id}**: Update a role by ID.
- **DELETE /api/roles/{id}**: Delete a role by ID.

## Security

Passwords are securely hashed using `BCryptPasswordEncoder` before being stored in the database.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

## License

This project is licensed under the MIT License - see the `LICENSE` file for details.