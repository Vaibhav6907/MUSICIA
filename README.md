<<<<<<< HEAD
# Musicia - Music Streaming Web Application

Musicia is a web-based music streaming platform built with Java Servlets and JDBC, allowing artists to upload their songs and users to listen to them.

## Features - 

- User authentication (Login/Register)
- Artist registration
- Song upload for artists
- Music streaming
- Cover art support
- Responsive design

## Prerequisites - 

- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Tomcat 9 or higher

## Setup - 

1. Clone the repository
2. Create the database:
   ```sql
   mysql -u root -p < src/main/resources/database.sql
   ```

3. Configure the database connection in `src/main/resources/db.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/musicia
   db.username=your_username
   db.password=your_password
   db.driver=com.mysql.cj.jdbc.Driver
   ```

4. Build the project:
   ```bash
   mvn clean package
   ```

5. Deploy the WAR file to Tomcat:
   - Copy the generated WAR file from `target/musicia-1.0-SNAPSHOT.war` to Tomcat's `webapps` directory
   - Start Tomcat

6. Access the application:
   ```
   http://localhost:8080/musicia
   ```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── musicia/
│   │           ├── dao/
│   │           │   ├── UserDAO.java
│   │           │   └── SongDAO.java
│   │           ├── model/
│   │           │   ├── User.java
│   │           │   └── Song.java
│   │           ├── servlet/
│   │           │   ├── AuthServlet.java
│   │           │   └── SongServlet.java
│   │           └── util/
│   │               └── DatabaseUtil.java
│   ├── resources/
│   │   ├── db.properties
│   │   └── database.sql
│   └── webapp/
│       ├── css/
│       │   └── style.css
│       ├── js/
│       │   ├── auth.js
│       │   └── player.js
│       ├── WEB-INF/
│       │   └── web.xml
│       ├── index.html
│       ├── login.html
│       ├── register.html
│       └── upload.html
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

# Direct Link for war file:-
https://github.com/Vaibhav6907/MUSICIA/blob/main/target/musicia-1.0-SNAPSHOT.war
## License

This project is licensed under the MIT License.
=======
# musicplayer
>>>>>>> 745a7b16041faf80aceff6ed622625bcf0a48c7c
