package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Validators {

    // ✅ USERNAME must be unique
    public static boolean checkUsername(Connection con, String username) {
        String q = "SELECT username FROM users WHERE username=?";
        try (PreparedStatement ps = con.prepareStatement(q)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return !rs.next(); // ✅ true = username not found
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ EMAIL format + duplicate check
    public static boolean checkEmail(Connection con, String email) {
    String q = "SELECT email FROM `users` WHERE email=?";
    try (PreparedStatement ps = con.prepareStatement(q)) {
        ps.setString(1, email);
        try (ResultSet rs = ps.executeQuery()) {
            return !rs.next();
        }
    } catch (Exception e) {
        System.out.println("Email Check Error: " + e.getMessage());
        return false;
    }
}


    // ✅ PHONE format + duplicate check
    public static boolean checkPhone(Connection con, long phone) {

        // ✅ Check if phone is a valid Indian mobile number
        String s = String.valueOf(phone);
        if (!s.matches("^[6-9][0-9]{9}$")) {
            return false;
        }

        String q = "SELECT phone FROM users WHERE phone=?";
        try (PreparedStatement ps = con.prepareStatement(q)) {
            ps.setLong(1, phone);
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ AADHAR validation (12 digits)
    public static boolean checkAadhar(Connection con, long aadhar) {

        String s = String.valueOf(aadhar);
        if (!s.matches("^[0-9]{12}$")) {
            return false;
        }

        String q = "SELECT aadhar FROM users WHERE aadhar=?";
        try (PreparedStatement ps = con.prepareStatement(q)) {
            ps.setLong(1, aadhar);
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ PAN validation + duplicate check
    public static boolean checkPan(Connection con, String pan) {

        // ✅ Standard PAN format: ABCDE1234F
        if (!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
            return false;
        }

        String q = "SELECT pan FROM users WHERE pan=?";
        try (PreparedStatement ps = con.prepareStatement(q)) {
            ps.setString(1, pan.toUpperCase());
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (Exception e) {
            return false;
        }
    }
}
