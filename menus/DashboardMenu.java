package menus;
import services.*;
import java.sql.Connection;
import java.util.Scanner;

public class DashboardMenu {

    public static void display(Connection con, Scanner sc, String name, String username) {

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("Welcome " + name);
            System.out.println("----------- STOKIFY DASHBOARD -----------");
            System.out.println("1. Profile");
            System.out.println("2. Portfolio");
            System.out.println("3. Search Stocks");
            System.out.println("4. Buy Stock");
            System.out.println("5. Sell Stock");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> UserService.profile(con, sc, username);
                case 2 -> PortfolioService.view(con, sc, username);
                case 3 -> StockServiceAPI.search( sc);
                case 4 -> PortfolioService.buyStock(con, sc, username, name);
                case 5 -> PortfolioService.sellStock(con, sc, username, name);

                case 6 -> {
                    System.out.println("Logged out!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
