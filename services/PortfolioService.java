package services;

import java.sql.*;
import java.util.Scanner;

public class PortfolioService {

    // ✅ VIEW PORTFOLIO
    public static void view(Connection con, Scanner sc, String username) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM portfolio WHERE username = ?"
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            float total = 0;
            System.out.println("--------------- YOUR PORTFOLIO ---------------");

            while (rs.next()) {
                String stock = rs.getString("stockName");
                float avg = rs.getFloat("averagePrice");
                int qty = rs.getInt("quantity");
                float inv = rs.getFloat("investment");

                total += inv;

                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Stock Name : " + stock);
                System.out.println("Average Price : " + avg);
                System.out.println("Quantity : " + qty);
                System.out.println("Investment : " + inv);
                System.out.println("--------------------------------------------------");
            }

            System.out.println("Total Investment : " + total);
            System.out.print("Press any key to go back...");
            sc.next();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ✅ BUY STOCK (correct logic + correct schema)
    public static void buyStock(Connection con, Scanner sc, String username, String name) {

        System.out.print("Enter Stock NSE code to buy: ");
        String stockName = sc.next();

        float price = StockServiceAPI.getPrice(stockName.toUpperCase());

        System.out.println("Price per share: " + price);
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        float invest = price * qty;

        try {
            // ✅ Check if stock already exists in portfolio
            PreparedStatement check = con.prepareStatement(
                "SELECT quantity, averagePrice, investment FROM portfolio WHERE username = ? AND stockName = ?"
            );
            check.setString(1, username);
            check.setString(2, stockName);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // ✅ Already exists → UPDATE
                int oldQty = rs.getInt("quantity");
                float oldInv = rs.getFloat("investment");

                int newQty = oldQty + qty;
                float newInv = oldInv + invest;
                float newAvg = newInv / newQty;

                PreparedStatement update = con.prepareStatement(
                    "UPDATE portfolio SET quantity=?, averagePrice=?, investment=? WHERE username=? AND stockName=?"
                );

                update.setInt(1, newQty);
                update.setFloat(2, newAvg);
                update.setFloat(3, newInv);
                update.setString(4, username);
                update.setString(5, stockName);

                update.executeUpdate();

            } else {
                // ✅ New stock → INSERT
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO portfolio (stockName, averagePrice, quantity, username, investment) VALUES (?, ?, ?, ?, ?)"
                );

                ps.setString(1, stockName);
                ps.setFloat(2, price);
                ps.setInt(3, qty);
                ps.setString(4, username);
                ps.setFloat(5, invest);

                ps.executeUpdate();
            }

            System.out.println("✅ Stock bought successfully!");
            System.out.print("Press any key to go back...");
            sc.next();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ✅ SELL STOCK (correct partial/full sell logic)
    public static void sellStock(Connection con, Scanner sc, String username, String name) {

        System.out.print("Enter Stock NSE code to sell: ");
        String stockName = sc.next();

        float sellPrice = StockServiceAPI.getPrice(stockName.toUpperCase());
        System.out.println("Current Price: " + sellPrice);

        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT quantity, averagePrice FROM portfolio WHERE username = ? AND stockName = ?"
            );
            ps.setString(1, username);
            ps.setString(2, stockName);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ You don't own this stock.");
                System.out.print("Press any key to go back...");
                sc.next();
                return;
            }

            int oldQty = rs.getInt("quantity");
            float avg = rs.getFloat("averagePrice");

            System.out.print("Enter quantity to sell: ");
            int qty = sc.nextInt();

            if (qty > oldQty) {
                System.out.println("❌ Not enough quantity to sell!");
                System.out.print("Press any key to go back...");
                sc.next();
                return;
            }

            // ✅ If selling all → delete row
            if (qty == oldQty) {
                PreparedStatement del = con.prepareStatement(
                    "DELETE FROM portfolio WHERE username=? AND stockName=?"
                );
                del.setString(1, username);
                del.setString(2, stockName);
                del.executeUpdate();
            } else {
                // ✅ Partial sell → update quantity & investment
                int newQty = oldQty - qty;
                float newInv = avg * newQty;

                PreparedStatement upd = con.prepareStatement(
                    "UPDATE portfolio SET quantity=?, investment=? WHERE username=? AND stockName=?"
                );
                upd.setInt(1, newQty);
                upd.setFloat(2, newInv);
                upd.setString(3, username);
                upd.setString(4, stockName);
                upd.executeUpdate();
            }

            System.out.println("✅ Sold " + qty + " shares at " + sellPrice);
            System.out.print("Press any key to go back...");
            sc.next();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
