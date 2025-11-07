
import java.sql.Connection;
import java.util.Scanner;
import menus.*;


public class Main {

    public static void main(String[] args) {

        try {
            Connection con = Database.getConnection();
            Scanner sc = new Scanner(System.in);

            MainMenu.display(con, sc);

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
