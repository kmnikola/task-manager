# Task Manager

A Spring Boot-based application for collaborative task management for small teams. The application allows to create workplaces in which the user can prepare task-lists, with categorization, scheduling and user management.
The workplaces can be shared. The application splits the functionality between the owner of the workplace who manages the tasks and their attributes, and view-only users who can fulfill a task in their view.

## Features

- User authentication and registration
- Task creation, editing, and listing
- Category and recurrence management
- Profile and workplace management
- Role-based access control

## Technologies

- Java 17+
- Spring Boot
- Spring Security
- Thymeleaf
- H2/MySQL (configure in `application.properties`)
- Maven

## Project Structure

- `src/main/java/pl/coderslab/` - Main Java source code
  - `auth/` - Authentication and security
  - `category/` - Category management
  - `profile/` - User profiles
  - `recurrence/` - Recurrence logic
  - `recurrenceSet/` - Recurrence sets
  - `role/` - User roles
  - `task/` - Task management
  - `user/` - User accounts
  - `workplace/` - Workplaces
  - `workplaceGroup/` - Workplace groups
  - `access/` - Access evaluators
- `src/main/resources/`
  - `templates/` - Thymeleaf HTML templates
  - `static/` - Static resources (CSS, JS)
  - `application.properties` - App configuration

## License
This project is for educational purposes only.
