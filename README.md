# 🎵 Musicia – Music Streaming Web Application

Musicia is a **Java-based web music streaming platform** developed using **Java Servlets, JDBC, and MySQL**.  
The application allows **users to stream music online** and **artists to upload and manage their songs**, demonstrating core concepts of **server-side web development** and **MVC architecture**.

---

## 📌 Core Features Implementation

The project fully implements all required core functionalities as per project requirements:

### 🔐 User Authentication
- Secure user registration and login
- Session-based authentication for logged-in users

### 🎤 Artist Module
- Artist registration
- Song upload with cover image support

### 🎧 Music Streaming
- Users can browse and stream uploaded songs
- Real-time audio playback using JavaScript

### 🔑 Role-Based Access
- Separate permissions for artists and users
- Secure access control handled via Java Servlets

All modules are fully developed, integrated, and functional, providing a smooth user experience.

---

## ⚙️ Technology Stack

| Layer | Technology |
|------|-----------|
| Frontend | HTML, CSS, JavaScript |
| Backend | Java Servlets |
| Database | MySQL |
| Build Tool | Maven |
| Server | Apache Tomcat |

---

## 🏗️ System Architecture (MVC)

Musicia follows the **Model–View–Controller (MVC)** architecture:

### 🧩 Model
- `User.java`
- `Song.java`

### 🎨 View
- HTML, CSS, JavaScript files

### 🎮 Controller
- `AuthServlet`
- `SongServlet`

This architecture ensures clean code, modularity, and easy scalability.

---

## 🔗 Integration of Components

- Servlets act as controllers and handle HTTP requests
- DAO classes (`UserDAO`, `SongDAO`) manage database operations using JDBC
- Utility classes handle database connectivity
- Frontend communicates with backend through HTTP requests
- All components are smoothly integrated for seamless data flow

---

## 🧠 Event Handling & Processing

- JavaScript files (`auth.js`, `player.js`) manage user interactions
- Event listeners handle:
  - Login & registration
  - Play / pause music
  - Song uploads
- Optimized event handling improves performance and responsiveness

---

## ✅ Data Validation

- **Client-side validation**
  - Implemented using JavaScript to prevent empty or invalid inputs
- **Server-side validation**
  - Implemented in Servlets to ensure data integrity
- Invalid data is rejected before database insertion

---

## 🛡️ Error Handling & Robustness

- Try-catch blocks handle database and server exceptions
- Invalid login credentials are handled gracefully
- Upload and streaming errors do not crash the application
- User-friendly error messages improve system robustness

---

## 🧼 Code Quality & Innovation

- Clean and modular code structure
- Proper package separation: `dao`, `model`, `servlet`, `util`
- Reusable database utility class
- `PreparedStatement` used to prevent SQL injection

### 🌟 Innovative Features
- Real-time music streaming
- Artist-based song uploads
- Scalable architecture for future enhancements

---

## 📂 Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/musicia/
│   │       ├── dao/
│   │       │   ├── UserDAO.java
│   │       │   └── SongDAO.java
│   │       ├── model/
│   │       │   ├── User.java
│   │       │   └── Song.java
│   │       ├── servlet/
│   │       │   ├── AuthServlet.java
│   │       │   └── SongServlet.java
│   │       └── util/
│   │           └── DatabaseUtil.java
│   ├── resources/
│   │   ├── db.properties
│   │   └── database.sql
│   └── webapp/
│       ├── css/
│       ├── js/
│       ├── WEB-INF/
│       │   └── web.xml
│       ├── index.html
│       ├── login.html
│       ├── register.html
│       └── upload.html

```

## 🧪 Setup Instructions

### 🔧 Prerequisites
- Java 11 or higher  
- MySQL 8.0 or higher  
- Maven 3.6+  
- Apache Tomcat 9+  

### ⚙️ Installation Steps

1. **Clone the repository:**  
```bash
git clone <repository-url>
```

2. **Create the database:**
```bash
mysql -u root -p < src/main/resources/database.sql
```
3. **Configure database connection in db.properties:**
```properties
db.url=jdbc:mysql://localhost:3306/musicia
db.username=your_username
db.password=your_password
db.driver=com.mysql.cj.jdbc.Driver
```
4. **Build the project:**
```bash
mvn clean package
```
5. **Deploy the generated WAR file to Apache Tomcat**
6. **Access the application in your browser:**
```bash
http://localhost:8080/musicia
```
## ▶️ Usage Instructions
- Register as a user or artist  
- Login using valid credentials  
- Artists can upload songs with cover images  
- Users can browse and stream music  
- Logout securely after use  

---

## 🚀 Future Enhancements
- Playlist creation  
- Like & recommendation system  
- Admin dashboard  
- Cloud-based music storage  
