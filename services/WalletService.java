package services;

import java.sql.*;
import java.util.Scanner;

public class WalletService {

    public static void createWallet(Connection con, String username) {
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO wallet (username, amount) VALUES (?, ?)")) {
            ps.setString(1, username);
            ps.setFloat(2, 100000f);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Wallet creation error: " + e.getMessage());
        }
    }

    public static float checkBalance(Connection con, String username) {
        String query = "SELECT amount FROM wallet WHERE username = ?";
        try (PreparedStatement pt = con.prepareStatement(query)) {
            pt.setString(1, username);
            try (ResultSet rs = pt.executeQuery()) {
                if (rs.next()) return rs.getFloat(1);
                else return 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking balance: " + e.getMessage());
            return 0;
        }
    }

    public static void updateBalance(Connection con, String username, float newAmount) {
        try (PreparedStatement pt = con.prepareStatement("UPDATE wallet SET amount = ? WHERE username = ?")) {
            pt.setFloat(1, newAmount);
            pt.setString(2, username);
            pt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update balance error: " + e.getMessage());
        }
    }

    public static void depositMoney(Connection con, Scanner sc, String username) {
        System.out.print("Enter amount to deposit: ");
        float amount = sc.nextFloat();
        if (amount <= 0) { System.out.println("Invalid amount."); return; }
        try {
            float bal = checkBalance(con, username);
            float newBal = bal + amount;
            updateBalance(con, username, newBal);
            TransactionService.recordTransaction(con, username, "CASH", "DEPOSIT", 0, 0, amount);
            System.out.println("Deposit successful. New balance: " + newBal);
        } catch (Exception e) { System.out.println("Deposit error: " + e.getMessage()); }
    }

    public static void withdrawMoney(Connection con, Scanner sc, String username) {
        System.out.print("Enter amount to withdraw: ");
        float amount = sc.nextFloat();
        if (amount <= 0) { System.out.println("Invalid amount."); return; }
        try {
            float bal = checkBalance(con, username);
            if (amount > bal) { System.out.println("Insufficient balance."); return; }
            float newBal = bal - amount;
            updateBalance(con, username, newBal);
            TransactionService.recordTransaction(con, username, "CASH", "WITHDRAW", 0, 0, amount);
            System.out.println("Withdrawal successful. New balance: " + newBal);
        } catch (Exception e) { System.out.println("Withdraw error: " + e.getMessage()); }
    }
}
