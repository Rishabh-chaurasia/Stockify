package menus;
import services.*;
import java.sql.Connection;
import java.util.Scanner;

public class MainMenu {

    public static void display(Connection con, Scanner sc) {

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("\nWELCOME TO STOKIFY");
            System.out.println("A Virtual Stock Market Assistant");
            System.out.println("--------------------------------------");
            System.out.println("1. User Login");
            System.out.println("2. New User Registration");
            System.out.println("3. Search Stocks");
            System.out.println("4. Today's Market Price");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> UserService.login(con, sc);

                case 2 -> UserService.register(con, sc);

                case 3 -> {
    System.out.print("Enter stock symbol (e.g. TCS, INFY, RELIANCE): ");
    String symbol = sc.next();

    float price = StockServiceAPI.getPrice(symbol);

    System.out.println("\nStock: " + symbol.toUpperCase());
    System.out.println("Price: " + price);
    System.out.println("------------------------------");
}


              case 4 ->{
    System.out.println("\n--- TODAY'S MARKET INDEX ---");

    float nifty = StockServiceAPI.getIndexPrice("^NSEI");
    float sensex = StockServiceAPI.getIndexPrice("^BSESN");

    System.out.println("NIFTY 50: " + (nifty > 0 ? nifty : "Error"));
    System.out.println("SENSEX  : " + (sensex > 0 ? sensex : "Error"));

    System.out.print("Press any key to continue...");
    sc.next();
              }



                case 5 -> {
                    System.out.println("Thank you for using Stokify!");
                    return;
                }

                default -> System.out.println("Invalid Choice!");
            }
        }
    }
}
