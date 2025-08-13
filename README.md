# Stockify – Stock Trading Simulator

**Stockify** is a Java-based console application that simulates stock trading. Users can **buy and sell stocks, track portfolios, view today’s market prices, and manage their account**. It uses **MySQL** for backend data storage and provides a simple interactive interface via the console.

---

## Features

- **User Registration & Login:** Secure login system for existing users and registration for new users.  
- **Search Stocks:** Search for stocks by name or symbol.  
- **View Market Prices:** Get today’s market prices for different stocks.  
- **Portfolio Management:** Track stocks owned by the user and their transactions.  
- **Simple Menu Interface:** Easy-to-use console menu for navigating between features.  

---

## Technologies Used

- **Programming Language:** Java  
- **Database:** MySQL  
- **JDBC:** For database connectivity  
- **Console Interface:** Scanner class for input  

---

## Setup Instructions

1. **Install MySQL** and create a database named `stokify`.  
2. **Update database credentials** in the code if needed:  
```java
private static final String url = "jdbc:mysql://localhost:3306/stokify";
private static final String user = "root";
private static final String password = "Dhanbad";



 Create required tables in MySQL to store users, stocks, and transactions.

Compile and run the Java program:

Follow the on-screen menu to register, login, search stocks, and view market prices.

Future Improvements
Add real-time stock price updates using an API.

Implement buying/selling simulation with portfolio updates.

Add data validation and exception handling for better user experience.

Add a GUI using JavaFX or a web frontend.

Author
Rishabh Chaurasia

GitHub: github.com/rishabh-chaurasia

LinkedIn: linkedin.com/in/rishabh-chaursia
