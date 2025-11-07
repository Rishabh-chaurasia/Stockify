package services;

import java.sql.Connection;
import java.util.Scanner;

public class SupportService {

    public static void helpSupport(Connection con, Scanner sc, String username) {
        sc.nextLine();
        System.out.println("HELP & SUPPORT");
        System.out.println("For any issues email: support@stokify.example");
        System.out.println("Or call: +91-XXXXXXXXXX");
        System.out.println("Type a short message to send to support (this demo will just print it):");
        String msg = sc.nextLine();
        System.out.println("Message received. Support will contact you soon. (demo)");
        System.out.println("Press any key to continue...");
        sc.next();
    }

    public static void about(Connection con, Scanner sc) {
        System.out.println("ABOUT STOKIFY");
        System.out.println("Stokify - A Virtual Stock Market Assistant");
        System.out.println("Version: 1.0");
        System.out.println("Developed by: Your Name");
        System.out.println("This is a demo console application for educational purposes.");
        System.out.println("Press any key to go back");
        sc.next();
    }
}
