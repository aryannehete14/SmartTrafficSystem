---

# Smart Traffic System 🚦

A Java-based intelligent traffic monitoring application that processes vehicle data, detects speed violations in real-time, and logs records into a MySQL database. The system features a modern Swing GUI and utilizes Java Streams for efficient data processing.

## 🌟 Features
- **Real-time Violation Detection**: Automatically identifies vehicles exceeding speed limits (default > 80 km/h).
- **Dynamic Fine Calculation**: Pluggable logic to calculate fines based on the severity of the speed violation.
- **Emergency Vehicle Bypass**: Smart rules to ensure emergency vehicles (Ambulances, Fire Trucks) are not flagged for speeding.
- **MySQL Integration**: Persistent storage for all violation records, including vehicle ID, speed, zone, and fine amount.
- **Interactive GUI**: User-friendly interface built with Java Swing for manual event entry and instant feedback.
- **Functional Programming**: Leverages Java 8+ Features like `Streams`, `Lambda Expressions`, `Optional` for null safety, and `Parallel Streams` for high-performance processing.

## 🛠️ Tech Stack
- **Language**: Java 17+
- **GUI Framework**: Java Swing
- **Database**: MySQL 8.0
- **Library**: MySQL Connector/J (JDBC)

## 📂 Project Structure
- `SmartTrafficSystem.java`: Contains the core logic, data models (VehicleEvent, ViolationRecord), and database connection methods.
- `TrafficUI.java`: The graphical interface for interacting with the system.

## 🚀 Getting Started

### 1. Database Setup
Ensure you have MySQL installed and running. Create the database and table using the following commands:

```sql
CREATE DATABASE trafficmanagement;
USE trafficmanagement;

CREATE TABLE violations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id VARCHAR(50),
    speed DOUBLE,
    zone VARCHAR(50),
    fine INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. Configure Credentials
Update the database credentials in `SmartTrafficSystem.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/trafficmanagement";
private static final String USER = "root";       // Your MySQL username
private static final String PASS = "your_password";  // Your MySQL password
```

### 3. Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/aryannehete14/SmartTrafficSystem.git
   ```
2. Add the `mysql-connector-java.jar` to your project classpath.
3. Compile the files:
   ```bash
   javac SmartTrafficSystem.java TrafficUI.java
   ```
4. Run the application:
   ```bash
   java TrafficUI
   ```

## 📸 Application Preview
The UI provides fields for Vehicle ID, Speed, and Zone. Upon clicking "Process Event," the system checks if the vehicle is speeding and not an emergency vehicle. If a violation is found, it calculates the fine and saves the record to the MySQL database.

---

### Author
**Aryan Nehete** [GitHub Profile](https://github.com/aryannehete14)
