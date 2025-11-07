package services;

import utils.*;
import menus.*;
import java.sql.*;
import java.util.Scanner;

public class UserService {

    public static boolean loggedIn = false;

    // ✅ METHOD: CHECK EMPTY INPUT
    private static String getNonEmptyInput(Scanner sc, String msg) {
        String input;
        while (true) {
            System.out.print(msg);
            input = sc.nextLine().trim();
            if (!input.isEmpty()) break;
            System.out.println("❌ Input cannot be empty!\n");
        }
        return input;
    }

    // ✅ EMAIL FORMAT CHECK
    private static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // ✅ PHONE FORMAT CHECK
    private static boolean isValidPhone(String phone) {
        return phone.matches("^[6-9][0-9]{9}$");
    }

    // ✅ AADHAR FORMAT CHECK
    private static boolean isValidAadhar(String aadhar) {
        return aadhar.matches("^[0-9]{12}$");
    }

    // ✅ PAN FORMAT CHECK (AAAAA9999A)
    private static boolean isValidPAN(String pan) {
        return pan.toUpperCase().matches("^[A-Z]{5}[0-9]{4}[A-Z]$");
    }

    // ✅ DUPLICATE CHECK
    private static boolean isDuplicate(Connection con, String field, Object value) throws Exception {
        String query = "SELECT " + field + " FROM users WHERE " + field + " = ?";
        PreparedStatement ps = con.prepareStatement(query);

        if (value instanceof String) ps.setString(1, (String) value);
        else if (value instanceof Long) ps.setLong(1, (Long) value);

        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    // ✅ REGISTRATION SYSTEM WITH FULL VALIDATION
    public static void register(Connection con, Scanner sc) {
        try {
            sc.nextLine(); // clear buffer

            // NAME
            String name = getNonEmptyInput(sc, "Name: ");

            // ✅ EMAIL LOOP
            String email;
            while (true) {
                email = getNonEmptyInput(sc, "Email: ");
                if (!isValidEmail(email)) {
                    System.out.println("❌ Invalid Email Format!\n");
                    continue;
                }
                if (isDuplicate(con, "email", email)) {
                    System.out.println("❌ Email already exists! Enter a different one.\n");
                    continue;
                }
                break;
            }

            // ✅ PHONE LOOP
            String phoneStr;
            long phone;
            while (true) {
                phoneStr = getNonEmptyInput(sc, "Phone: ");

                if (!isValidPhone(phoneStr)) {
                    System.out.println("❌ Invalid Phone Number! Must be 10 digits and start with 6-9.\n");
                    continue;
                }

                phone = Long.parseLong(phoneStr);

                if (isDuplicate(con, "phone", phone)) {
                    System.out.println("❌ Phone already exists! Enter a different one.\n");
                    continue;
                }
                break;
            }

            // AGE
            int age;
            while (true) {
                String ageStr = getNonEmptyInput(sc, "Age: ");
                try {
                    age = Integer.parseInt(ageStr);
                    if (age <= 0) throw new Exception();
                    break;
                } catch (Exception e) {
                    System.out.println("❌ Invalid Age! Enter a valid number.\n");
                }
            }

            // ✅ AADHAR LOOP
            String aadharStr;
            long aadhar;
            while (true) {
                aadharStr = getNonEmptyInput(sc, "Aadhar: ");

                if (!isValidAadhar(aadharStr)) {
                    System.out.println("❌ Aadhar must be 12 digits!\n");
                    continue;
                }

                aadhar = Long.parseLong(aadharStr);

                if (isDuplicate(con, "aadhar", aadhar)) {
                    System.out.println("❌ Aadhar already exists! Enter a different one.\n");
                    continue;
                }
                break;
            }

            // ✅ PAN LOOP
            String pan;
            while (true) {
                pan = getNonEmptyInput(sc, "PAN: ").toUpperCase();

                if (!isValidPAN(pan)) {
                    System.out.println("❌ Invalid PAN format (AAAAA9999A)!\n");
                    continue;
                }

                if (isDuplicate(con, "pan", pan)) {
                    System.out.println("❌ PAN already exists! Enter a different one.\n");
                    continue;
                }
                break;
            }

            // INCOME
            double income;
            while (true) {
                String incomeStr = getNonEmptyInput(sc, "Income: ");
                try {
                    income = Double.parseDouble(incomeStr);
                    break;
                } catch (Exception e) {
                    System.out.println("❌ Invalid income amount!\n");
                }
            }

            // ✅ USERNAME LOOP
            String username;
            while (true) {
                username = getNonEmptyInput(sc, "Username: ");

                if (isDuplicate(con, "username", username)) {
                    System.out.println("❌ Username already exists! Enter a different one.\n");
                    continue;
                }
                break;
            }

            String password = getNonEmptyInput(sc, "Password: ");

            // ✅ INSERT INTO DATABASE
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users(name,email,phone,age,aadhar,pan,income,username,password) VALUES(?,?,?,?,?,?,?,?,?)"
            );

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setLong(3, phone);
            ps.setInt(4, age);
            ps.setLong(5, aadhar);
            ps.setString(6, pan.toUpperCase());
            ps.setDouble(7, income);
            ps.setString(8, username);
            ps.setString(9, password);

            ps.executeUpdate();

            // ✅ CREATE WALLET
            PreparedStatement ws = con.prepareStatement(
                "INSERT INTO wallet(username, amount) VALUES (?, ?)"
            );
            ws.setString(1, username);
            ws.setFloat(2, 100000);
            ws.executeUpdate();

            System.out.println("\n✅ Registration Successful!");
            System.out.println("₹100000 credited to your wallet.");

            System.out.print("Press Enter to continue...");
            sc.nextLine();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ✅ LOGIN SAME AS BEFORE
    public static void login(Connection con, Scanner sc) {
        sc.nextLine();

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        try {
            String q = "SELECT name FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, username);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                loggedIn = true;
                DashboardMenu.display(con, sc, rs.getString("name"), username);
            } else {
                System.out.println("Invalid credentials!");
                System.out.print("Press Enter...");
                sc.nextLine();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ✅ PROFILE
    public static void profile(Connection con, Scanner sc, String username) {

        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT users.name, users.email, users.phone, users.age, users.aadhar, " +
                "users.pan, wallet.amount FROM users INNER JOIN wallet " +
                "ON users.username = wallet.username WHERE users.username = ?"
            );

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Name: " + rs.getString(1));
                System.out.println("Email: " + rs.getString(2));
                System.out.println("Phone: " + rs.getLong(3));
                System.out.println("Age: " + rs.getInt(4));
                System.out.println("Aadhar: " + rs.getLong(5));
                System.out.println("PAN: " + rs.getString(6));
                System.out.println("Wallet Balance: ₹" + rs.getFloat(7));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.print("Press Enter to go back...");
        sc.nextLine();
    }
}
