package services;

import java.sql.*;
import java.util.Scanner;

public class WatchlistService {

    public static void watchlistMenu(Connection con, Scanner sc, String username) {
        while (true) {
            System.out.println("WATCHLIST MENU");
            System.out.println("1. View watchlist");
            System.out.println("2. Add stock to watchlist");
            System.out.println("3. Remove stock from watchlist");
            System.out.println("4. Back");
            int ch = sc.nextInt();
            sc.nextLine();
            switch (ch) {
                case 1 -> viewWatchlist(con, username);
                case 2 -> addToWatchlist(con, sc, username);
                case 3 -> removeFromWatchlist(con, sc, username);
                case 4 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public static void viewWatchlist(Connection con, String username) {
        try (PreparedStatement ps = con.prepareStatement("SELECT stockName FROM watchlist WHERE username=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Your Watchlist:");
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    System.out.println("- " + rs.getString("stockName"));
                }
                if (!any) System.out.println("(empty)");
            }
        } catch (Exception e) { System.out.println("Watchlist view error: " + e.getMessage()); }
    }

    public static void addToWatchlist(Connection con, Scanner sc, String username) {
        System.out.print("Enter stock name to add: ");
        String stock = sc.nextLine();
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO watchlist (username, stockName) VALUES (?,?)")) {
            ps.setString(1, username);
            ps.setString(2, stock);
            ps.executeUpdate();
            System.out.println("Added to watchlist.");
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("Already in watchlist.");
        } catch (Exception e) {
            System.out.println("Add watchlist error: " + e.getMessage());
        }
    }

    public static void removeFromWatchlist(Connection con, Scanner sc, String username) {
        System.out.print("Enter stock name to remove: ");
        String stock = sc.nextLine();
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM watchlist WHERE username=? AND stockName=?")) {
            ps.setString(1, username); ps.setString(2, stock);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Removed.");
            else System.out.println("Not found in watchlist.");
        } catch (Exception e) { System.out.println("Remove watchlist error: " + e.getMessage()); }
    }
}

