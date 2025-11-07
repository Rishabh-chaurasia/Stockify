package services;

import java.sql.*;
import java.util.Scanner;

public class TransactionService {

    public static void recordTransaction(Connection con, String username, String stockName, String type,
                                         int qty, float price, float amount) {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO transactions (username, stockName, type, quantity, price, amount, txn_time) VALUES (?,?,?,?,?,?,CURRENT_TIMESTAMP)")) {
            ps.setString(1, username);
            ps.setString(2, stockName);
            ps.setString(3, type);
            ps.setInt(4, qty);
            ps.setFloat(5, price);
            ps.setFloat(6, amount);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Record transaction error: " + e.getMessage());
        }
    }

    public static void transactionHistory(Connection con, Scanner sc, String username) {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM transactions WHERE username=? ORDER BY txn_time DESC")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("TRANSACTION HISTORY");
                System.out.println("--------------------------------------------------");
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    System.out.println("Type: " + rs.getString("type") + " | Stock: " + rs.getString("stockName")
                            + " | Qty: " + rs.getInt("quantity") + " | Price: " + rs.getFloat("price")
                            + " | Amount: " + rs.getFloat("amount") + " | Time: " + rs.getTimestamp("txn_time"));
                }
                if (!any) System.out.println("No transactions found.");
            }
        } catch (Exception e) {
            System.out.println("Transaction history error: " + e.getMessage());
        }
        System.out.println("Press any key to go back");
        sc.next();
    }
}
