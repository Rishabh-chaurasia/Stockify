# 💹 STOKIFY – A Virtual Stock Market Assistant

**STOKIFY** is a Java-based console application that allows users to virtually trade Indian stocks, manage portfolios, and track live market data using the **Yahoo Finance API**.  
This project simulates a real stock trading environment where users can register, buy/sell shares, and monitor profits — all with realistic validations and database integration.

---

## 🚀 Features

### 👤 User Management
- New user registration with complete validation:
  - Email format check
  - Unique email, phone, PAN, and Aadhar (no duplicates)
  - Phone: 10 digits & starts with 6–9
  - Aadhar: 12 digits only
  - PAN: Automatically converted to **UPPERCASE**
- Each new user gets ₹100000 virtual money in their wallet.
- Secure login and profile view.

### 💼 Portfolio Management
- Buy and sell stocks using real market prices.
- View complete investment portfolio with:
  - Stock name  
  - Quantity  
  - Average price  
  - Total investment value
- Auto-update investment and quantity after each transaction.

### 📈 Stock Data & Market Overview
- Fetch real-time stock prices from **Yahoo Finance API**.
- Search stock details (e.g., TCS, INFY, IRFC, etc.).
- View today’s **NIFTY 50** and **SENSEX** index live data.

### 💰 Wallet Management
- Each user has a dedicated wallet table linked to their username.
- Balance updates automatically after buy/sell transactions.

### 🧠 Backend Intelligence
- Modular structure with packages:
  - `menus` – Manages main & dashboard menus  
  - `services` – Handles user, portfolio, transaction, and stock logic  
  - `utils` – For database connections and helper utilities  
- Uses JDBC to connect with **MySQL database**.

---

## 🧩 Tech Stack

| Category | Technology |
|-----------|-------------|
| **Language** | Java (JDK 17+) |
| **Database** | MySQL |
| **API Used** | Yahoo Finance API |
| **JSON Parser** | org.json (json-20231013.jar) |
| **Connector** | mysql-connector-j-9.5.0.jar |
| **IDE (recommended)** | IntelliJ IDEA / VS Code / Eclipse |

---

## 🗃️ Database Schema

### Database: `stock`
Tables used:
- **users** – User information  
- **wallet** – User wallet balance  
- **portfolio** – Stocks owned by users  
- **transactions** – Buy/Sell history  
- **watchlist** – User’s favorite stocks  

You can create the schema using this script:

```sql
CREATE DATABASE stock;
USE stock;

CREATE TABLE users (
  name VARCHAR(100),
  email VARCHAR(150) UNIQUE,
  phone BIGINT UNIQUE,
  age INT,
  aadhar BIGINT UNIQUE,
  pan VARCHAR(20) UNIQUE,
  income DOUBLE,
  username VARCHAR(50) PRIMARY KEY,
  password VARCHAR(100)
);

CREATE TABLE wallet (
  username VARCHAR(50) PRIMARY KEY,
  amount FLOAT,
  FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE portfolio (
  id INT AUTO_INCREMENT PRIMARY KEY,
  stockName VARCHAR(100),
  averagePrice FLOAT,
  quantity INT,
  username VARCHAR(50),
  investment FLOAT,
  FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50),
  stockName VARCHAR(100),
  type VARCHAR(20),
  quantity INT,
  price FLOAT,
  amount FLOAT,
  txn_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);
