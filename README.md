# Lost and Found Management System (Java Swing + MySQL)

A Maven-based desktop application designed to open directly in IntelliJ IDEA. It includes account registration/login, lost/found item reporting, item listing/filtering, and claim submission. Data is stored in MySQL through JDBC.

## Requirements
- JDK 17 or newer
- IntelliJ IDEA
- MySQL Server 8.0+ and MySQL Workbench (recommended)
- Internet connection on first Maven import so IntelliJ can download MySQL Connector/J

## Database setup
1. Start MySQL Server.
2. Open `database/lost_found_db.sql` in MySQL Workbench and execute it.
3. Open `src/main/resources/db.properties`.
4. Set `db.user` and `db.password` to your MySQL username and password. For example:
   `db.user=root`
   `db.password=your_actual_password`
   Do not upload real passwords to a public GitHub repository.

## Open and run in IntelliJ IDEA
1. Extract this ZIP.
2. IntelliJ IDEA → **File → Open** → select the extracted `Lost_and_Found_IntelliJ_Project` folder (the one containing `pom.xml`).
3. If prompted, choose **Load Maven Project** / **Trust Project**.
4. Set Project SDK to JDK 17: File → Project Structure → Project SDK.
5. Wait for Maven dependencies to finish downloading.
6. Confirm MySQL is running and the database script has been executed.
7. Open `src/main/java/com/alok/lostfound/Main.java` and click the green Run icon.

## First use
- Select **Create account** on the login window.
- Register a normal user, log in, and report LOST or FOUND items.
- Select a row and submit a claim with a message.
- Use the filter to show all, lost, or found items.

## Connectivity notes
- JDBC driver is provided by Maven (`mysql-connector-j` in `pom.xml`).
- Database credentials are read from `db.properties`.
- If you see `Communications link failure`, start MySQL and check host/port.
- If you see `Access denied`, correct the username/password in `db.properties`.
- If you see `Unknown database`, execute the SQL file.
- This version does not include a preconfigured admin account or claim-approval screen. Claims are stored in the `claims` table for later review.

## GitHub safety
Before publishing publicly, replace the example password in `db.properties` with a placeholder such as `YOUR_MYSQL_PASSWORD`. Never commit your real database password.
