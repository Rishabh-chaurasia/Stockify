
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.IOException;


import javax.sound.sampled.SourceDataLine;
public class Stokify{
    private static final String url = "jdbc:mysql://localhost:3306/stokify";
    private static final String user = "root";
    private static final String password = "Dhanbad";
    static boolean logout=true;
    public static void main(String[] args) {

        try{
            Connection con = DriverManager.getConnection(url,user,password);
            Scanner sc = new Scanner(System.in);
            boolean flag = true;
       
            displayMenu();
            while(flag){
            int choice = sc.nextInt();
            switch (choice) {
                case 1:{
                    userlogin(con,sc);
                    break;
                }
                case 2:{
                    newUserRegistration(con,sc);
                    break;
                }
                case 3:{
                     searchStock(con,sc);
                    break;
                }
                case 4:{
                    todaysMarketPrice(sc);
                    break;
                }
                case 5:{
                    System.out.println("Thank you for using Stokify");
                    flag = false;
                    return;
                }
                default:{
                    System.out.println("Invalid choice!\nPlease Enter your choice again.");
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
            
            
                    displayMenu();
                }
                }
            }
        }catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    //FIRST PAGE 
    static void displayMenu(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\n");
        System.out.println("         WELCOME TO STOKIFY         ");   //header
        System.out.println("A Virtual Stock Marketing Assistant");   
        System.out.println("______________________________________");
        System.out.println("Please Select anyone from given below options : \n");
        System.out.println("1. USER LOGIN");
        System.out.println("2. NEW USER REGISTRATION");
        System.out.println("3. SEARCH STOCKS");
        System.out.println("4. TODAY'S MARKET PRICE");
        System.out.println("5. EXIT");
        System.out.print("Enter here :");
       
    }

    //USER REGISTRATION

    static void newUserRegistration(Connection con,Scanner sc){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("            NEW USER REGISTRATION              ");
        System.out.println("\n");
        System.out.print("Enter your name : ");
        String name = sc.next();
        sc.nextLine();
        System.out.print("Enter your email : ");
        String email = sc.next();
        sc.nextLine();
        int res1 =checkEmail(con, email);
        while(res1!=1){
            System.out.print("Enter your Email : ");
            email = sc.next();
            res1=checkEmail(con, email);
        }
        System.out.print("Enter your phone number : ");
        long phone = sc.nextLong();
        int res2 =checkPhone(con, phone);
        while(res2!=1){
            System.out.print("Enter your Phone number : ");
            phone = sc.nextLong();
            res2=checkPhone(con, phone);
        }
        System.out.print("Enter you age : ");
        int age = sc.nextInt();
        System.out.print("Enter your Aadhar number : ");
        long aadhar = sc.nextLong();
        int res3 =checkAadhar(con, aadhar);
        while(res3!=1){
            System.out.print("Enter your Aadhar number : ");
            aadhar = sc.nextLong();
            res3=checkAadhar(con, aadhar);
        }
        System.out.print("Enter your PAN number : ");
        String pan = sc.next();
        int res4 =checkPan(con, pan);
        while(res4!=1){
            System.out.print("Enter your PAN number : ");
            pan = sc.next();
            res4=checkPan(con, pan);
        }
        System.out.print("Enter your Annual Income : ");
        double income = sc.nextDouble();
        System.out.print("Enter your username (try to select unique username) : ");
        String username = sc.next();
        int res =checkUserName(con, username);
        while(res!=1){
            System.out.print("Enter your username (try to select unique username) : ");
            username = sc.next();
            res=checkUserName(con, username);
        }
        System.out.print("Enter your password : ");
        String pass = sc.next();
        try {
            String query = "INSERT INTO user values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2,email);
            ps.setLong(3, phone);
            ps.setInt(4,age);
            ps.setLong(5,aadhar);
            ps.setString(6,pan);
            ps.setDouble(7,income);
            ps.setString(8,username);
            ps.setString(9, pass);
            ps.executeUpdate();
            System.out.println("User registration successful");
            walletCreation(con,sc,username);
        } catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception
        }

        displayMenu();
        

    }
    static void walletCreation(Connection con , Scanner sc , String username){
        try {
            String query = "insert into wallet values(?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,username);
            ps.setInt(2, 100000);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    static int checkUserName(Connection con , String username){
        try {
            String query = "select username from user where username= ?";
            PreparedStatement ptm = con.prepareStatement(query);
            ptm.setString(1, username);
            ResultSet rs = ptm.executeQuery();

            if (rs.next()) {
                System.out.println("username already exists \nEnter username again"); //already same username is available
                return 0;
            } 
            else {
                return 1;
       }
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }

    //checking email
    static int checkEmail(Connection con , String email){
        try {
            String query = "select email from user where email= ?";
            PreparedStatement ptm = con.prepareStatement(query);
            ptm.setString(1,email);
            ResultSet rs = ptm.executeQuery();

            if (rs.next()) {
                System.out.println("Email already exists \nEnter Email again"); //already same username is available
                return 0;
            } 
            else {
                return 1;
       }
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }

    //Check phone number

    static int checkPhone(Connection con , long phone){
        try {
            String query = "select phone from user where phone= ?";
            PreparedStatement ptm = con.prepareStatement(query);
            ptm.setLong(1,phone);
            ResultSet rs = ptm.executeQuery();

            if (rs.next()) {
                System.out.println("Phone number already exists \nEnter Phone number again"); //already same username is available
                return 0;
            } 
            else {
                return 1;
       }
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }

    //check aadhar
    static int checkAadhar(Connection con , long aadhar){
        try {
            String query = "select aadhar from user where aadhar= ?";
            PreparedStatement ptm = con.prepareStatement(query);
            ptm.setLong(1,aadhar);
            ResultSet rs = ptm.executeQuery();

            if (rs.next()) {
                System.out.println("aadhar number already exists \nEnter aadhar number again"); //already same username is available
                return 0;
            } 
            else {
                return 1;
       }
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }

    //check Pan

    static int checkPan(Connection con , String pan){
        try {
            String query = "select pan from user where pan= ?";
            PreparedStatement ptm = con.prepareStatement(query);
            ptm.setString(1,pan);
            ResultSet rs = ptm.executeQuery();

            if (rs.next()) {
                System.out.println("Pan number already exists \nEnter Pan number again"); //already same username is available
                return 0;
            } 
            else {
                return 1;
       }
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }

    //USER LOGIN 

    static void userlogin(Connection con, Scanner sc){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("                   USER LOGIN                           ");
        System.out.println("\n");
        System.out.print("Enter username : ");
        String username = sc.next();
        
        sc.nextLine();
        System.out.print("Enter password : ");
        String pass = sc.next();;
     
        sc.nextLine();
        try{
            String query ="select username,password from user where username=? and password =?";
            PreparedStatement ptm = con.prepareStatement(query);
            ptm.setString(1,username);
            ptm.setString(2, pass);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                System.out.println("login successfull");
                logout=false;
            }
            else{
                System.out.println("Invalid Credentials ");
                System.out.println("Press 0 to back");
                String choice = sc.next();
                if(choice=="0"){
                    displayMenu();
                    return;
                }
                else
                    userlogin(con, sc);
            }
            String query1 = "select name from user where username= ?";
            PreparedStatement pt= con.prepareStatement(query1);
            pt.setString(1, username);
            ResultSet set1 = pt.executeQuery();
            String name="";
            while(set1.next()){
                 name = set1.getString("name");
            }

            dashboard(con, sc, name,username);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    static void dashboard(Connection con , Scanner sc,String name,String username){
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Welcome "+name);
        System.out.println("          STOKIFY         ");   //header
        System.out.println("A Virtual Stock Marketing Assistant");   
        System.out.println("______________________________________");
        System.out.println("Please Select anyone from given below options : \n");
        System.out.println("1. PROFILE");
        System.out.println("2. PORTFOLIO");
        System.out.println("3. SEARCH STOCKS");
        System.out.println("4. BUY STOCK");
        System.out.println("5. SELL STOCK");
        System.out.println("6. LOGOUT");
        System.out.print("Enter here :");
        boolean flag = true;
        while(flag){
            int choice = sc.nextInt();
            switch (choice) {
                case 1:{
                    profile(con,sc,username);
                    break;
                }
                case 2:{
                    portfolio(con,sc,username);
                    break;
                }
                case 3:{
                     searchStock(con,sc);
                    break;
                }
                case 4:{
                    buyStock(con,sc,username,name);
                   
                    break;
                }
                case 5:{
                    sellStock(con,sc,username,name);
                     break;
                }
                case 6:{
                    
                    System.out.println("Logging out..");
                    flag = false;
                    logout(con, sc, username);
                    break;
                }
                default:{
                    System.out.println("Invalid choice!\nPlease Enter your choice again.");
                    dashboard(con, sc,name,username);
                }
                }
            }
        }
//SEARCH STOCK
        static void searchStock(Connection con , Scanner sc){

            System.out.println("           SEARCH STOCK                ");
            System.out.println("\n");
            System.out.println("Enter the stock name :");
            String stockName = sc.next();
            sc.nextLine();
            try {
                String query = "SELECT * FROM company WHERE LOWER(nseCode)=LOWER(?)";
                PreparedStatement pt = con.prepareStatement(query);
                pt.setString(1, stockName);
                ResultSet set = pt.executeQuery();
                if(set.next()){
                    String comapnyName = set.getString(1);
                    float price = set.getFloat(2);
                    int yearLow = set.getInt(3);
                    int yearHigh = set.getInt(4);
                    String description = set.getString(5);
                    String nseCode = set.getString(6);
                    String bseCode = set.getString(7);
                    System.out.println("Company Name : "+comapnyName);
                    System.out.println("Price : "+price);
                    System.out.println("Year Low : "+yearLow);
                    System.out.println("Year High : "+yearHigh);
                    System.out.println("Description : "+description);
                    System.out.println("NSE Code : "+nseCode);
                    System.out.println("BSE Code : "+bseCode);
                    System.out.println("------------------------------------------------");
                    if(logout==true){
                        System.out.print("Press any key to go back : ");
                        String input = sc.next();
                        if(input=="1"){
                             displayMenu();
                         }
                         else {
                             displayMenu();
                         }
                    }
                    else{
                        System.out.print("Press any key to go back : ");
                        String input = sc.next();
                        if(input=="1"){
                            dashboard(con, sc, comapnyName, input);
                         }
                         else {
                             dashboard(con, sc, comapnyName, input);
                            }
                    }
                } 
                else{
                    System.out.println("Stock not found");
                    searchStock(con, sc);
                }
        }catch (Exception e){
            System.out.println("Cannot found stock");
            searchStock(con, sc);
        }
    }

    //portfolio

    static void portfolio(Connection con,Scanner sc,String username){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("                 PORTFOLIO                           ");
        System.out.println("------------------------------------------------");
        float totalInvestment=0;
        try {
            String query = "SELECT sum(investment) FROM portfolio WHERE username = ?";
            PreparedStatement pt = con.prepareStatement(query);
            pt.setString(1, username);
            ResultSet set = pt.executeQuery();
            
            while(set.next()){
                totalInvestment=set.getFloat(1);
            }
           
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            String query = "SELECT * FROM portfolio WHERE username = ?";
            PreparedStatement pt = con.prepareStatement(query);
            pt.setString(1, username);
            ResultSet set = pt.executeQuery();
            while(set.next()){
                int id = set.getInt(1);
                String stockName = set.getString(2);
                float avgPrice = set.getFloat(3);
                int qty = set.getInt(4);
                String user = set.getString(5);
                float investment=set.getFloat(6);
                System.out.println("Username : "+username);
                System.out.println("Invesetment "+id);
                System.out.println("Stock Name : "+stockName);
                System.out.println("Average Price : "+avgPrice);
                System.out.println("Quantity : "+qty);
                System.out.println("Investment : "+investment);
                System.out.println("--------------------------------------------------");
            }

            System.out.println("Total Investment : "+totalInvestment);
           
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Press any key to go back");
        String input = sc.next();
        if(input=="1"){
            dashboard(con, sc, username, username);
        }
        else {
           dashboard(con, sc, username, username);
        }
    }
//profile
        static void profile(Connection con,Scanner sc,String username){
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("         PROFILE            ");
            System.out.println("______________________________________");
            try {
                String query = "SELECT * FROM user INNER JOIN wallet ON user.username = wallet.username WHERE user.username = ?";
              
                PreparedStatement pt = con.prepareStatement(query);
                pt.setString(1, username);
               
                ResultSet rs = pt.executeQuery();
                while(rs.next()){
                    System.out.println("Name : "+rs.getString(1)+"\n");
                    System.out.println("username : "+rs.getString(8)+"\n");
                    System.out.println("Phone number : "+rs.getLong(3));
                    System.out.println("\nEmail : "+rs.getString(2));
                    System.out.println("\nWallet balance : "+rs.getInt(11)+"\n");
                    System.out.println("Aadhar number : "+rs.getLong(5));
                    System.out.println("\nPAN number : "+rs.getString(6));
                    System.out.println("\nAge : "+rs.getInt(4));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Press any key to go back");
            String input = sc.next();
            if(input=="1"){
                dashboard(con, sc, username, username);
            }
            else {
               dashboard(con, sc, username, username);
            }
        }

    //BUY STOCK
        static void buyStock(Connection con,Scanner sc,String username,String name){
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("BUY STOCK");
            System.out.println("______________________________________");
            System.out.print("Enter Stock name : ");
            String stockName = sc.next();
            sc.nextLine();
            try {
                String query = "SELECT stockPrice FROM company WHERE LOWER(nseCode)=LOWER(?)";
                PreparedStatement pt = con.prepareStatement(query);
                pt.setString(1, stockName);
                ResultSet set = pt.executeQuery();
                float price=0;
                if(set.next()){
                    price = set.getFloat(1);
                    System.out.println(price);
                }  
                else{
                    System.out.println("Stock not found");
                    buyStock(con, sc, username, stockName);
                }
                System.out.println("Enter quantity : ");
                int qty =sc.nextInt();
                float amount = price*qty;
                System.out.println("Amount to be paid : "+amount);
                float availablebalance = checkBalance(con,username);
                System.out.println("Balance available = "+availablebalance);
                if(amount <= availablebalance){
                    try {
                        
                        String query1 = "SELECT stockName ,quantity ,investment FROM portfolio WHERE stockName= ? and username=?";
                        PreparedStatement pt1 = con.prepareStatement(query1);
                        pt1.setString(1,stockName);
                        pt1.setString(2,username);
                        ResultSet set1 = pt1.executeQuery();
                        String stock="";
                        int fetchedQty1=0;
                        float investment1=0;
                        while(set1.next()){
                            stock=set1.getString(1);
                            fetchedQty1=set1.getInt(2);
                            investment1=set1.getFloat(3);
                        }
                        if(stock.equalsIgnoreCase(stockName)){
                            int qtyToIncrease = fetchedQty1+qty;
                            float investmentToIncrease= investment1+(price*qty);
                            updatePortfolio(con, sc, username, stockName, qtyToIncrease, investmentToIncrease);
                            float amountToUpdate = availablebalance-amount;
                            updateBalance(con,sc,username,amountToUpdate);
                            System.out.println("Stock Buy successful ");
                            dashboard(con, sc, stockName, username);
                        }
                        else{
                            insertPortfolio(con,sc,username,stockName,qty,amount,price);
                            float amountToUpdate = availablebalance-amount;
                            updateBalance(con,sc,username,amountToUpdate);
                            System.out.println("Stock Buy successful ");
                            dashboard(con, sc, stockName, username);
                        }
                    }catch (Exception e) {
                        System.out.println("Internet Issue Cannot Buy stock ");
                        dashboard(con, sc, stockName, username);
                    }
                    
                }
                else{
                    System.out.println("Insufficient balance");
                    dashboard(con, sc, name,username);
                }

            } catch (Exception e) {
                System.out.println("Stock not found ");
                buyStock(con, sc,username,name);
            }
            System.out.println("Press any key to go back");
            String input = sc.next();
            if(input=="1"){
                dashboard(con, sc, username, username);
            }
            else {
               dashboard(con, sc, username, username);
            }
        }

//SELL STOCK
        static void sellStock(Connection con,Scanner sc,String username,String name){
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("SELL STOCK");
            System.out.println("_____________________________________________________");
            System.out.print("Enter Stock name : ");
            String stockName = sc.next();
            sc.nextLine();
            try {
                String query = "SELECT stockName FROM portfolio WHERE LOWER(stockName)=LOWER(?) and username=?";
                PreparedStatement pt = con.prepareStatement(query);
                pt.setString(1,stockName);
                pt.setString(2,username);

                ResultSet set = pt.executeQuery();
                String stock="";
                if(set.next()){
                    stock=set.getString(1);
                }
                if(stock.equalsIgnoreCase(stockName)){
                    System.out.println("Enter quantity : ");
                    int qty = sc.nextInt();
                    try {
                        String query1 = "SELECT SUM(quantity), investment, averagePrice FROM portfolio WHERE stockName = ? AND username = ? GROUP BY investment, averagePrice";
                        PreparedStatement pt1 = con.prepareStatement(query1);
                        pt1.setString(1,stockName);
                        pt1.setString(2,username);
                        ResultSet set1 = pt1.executeQuery();
                        int fetchedQty = 0;
                        float invst = 0;
                        float price=0;
                        while(set1.next()){
                            fetchedQty= set1.getInt(1);
                            invst = set1.getFloat(2);
                            price=set1.getFloat(3);
                           
                        }
                        if(qty<=fetchedQty){
                            int quantityToDecrease = fetchedQty-qty;
                            float investmentToDecrease =invst - (price*qty);
                            updatePortfolio(con, sc, username, stockName,quantityToDecrease,investmentToDecrease);
                            String query2="select amount from wallet where username=?";
                            PreparedStatement ptstmt = con.prepareStatement(query2);
                            ptstmt.setString(1,username);
                            ResultSet rs2 = ptstmt.executeQuery();
                            float amountToUpdate=0;
                            while(rs2.next()){
                                amountToUpdate=rs2.getFloat(1);
                            }
                            amountToUpdate+=+(price*qty);
                            updateBalance(con,sc,username,amountToUpdate);
                            System.out.println(qty+" Stock sold ");
                            dashboard(con, sc, stockName, username);
                        }
                        
                    } catch (Exception e) {
                        System.out.println("Internet Issue ");
                        sellStock(con, sc, username, stockName);
                    }
                }
                else{
                    System.out.println("You have not purchased any stock ");
                    sellStock(con, sc, username, stockName);
                }
            

            } catch (Exception e) {
                System.out.println("Stock not found ");
                sellStock(con, sc, username, stockName);
            }
        }

        //Update portfolio
        static void updatePortfolio(Connection con,Scanner sc,String username,String stockName,int quantityToupdate,float invst){
            try {
                String query = "update portfolio set quantity = ? , investment=? where username = ? and stockName = ?";
                PreparedStatement pt = con.prepareStatement(query);
                
                
                pt.setInt(1,quantityToupdate);
                pt.setFloat(2,invst);
                pt.setString(3,username);
                pt.setString(4,stockName);
                int rowsUpdated = pt.executeUpdate();
                if (rowsUpdated == 0) {
                    System.out.println("No rows updated.");
                } else {
                    System.out.println("Portfolio updated successfully.");
                }
        
            } catch (Exception e) {
                System.out.println("Error while Updating Portfolio ");
            }
        }


        //Update balance
        static void updateBalance(Connection con,Scanner sc,String username,float amountToUpdate){
            try {
                String query = "update wallet set amount= ? where username=?";
                PreparedStatement pt = con.prepareStatement(query);
                pt.setFloat(1, amountToUpdate);
                pt.setString(2,username);
                pt.executeUpdate();

            } catch (Exception e) {
                System.out.println(e);
                System.out.println("In update balanvce");
                // TODO: handle exception
            }
        }
        //insert portfolio 
        static void insertPortfolio(Connection con,Scanner sc,String username,String stockName,int qty,float amount,float price){
            try {
                String query = "insert into portfolio (stockName,averagePrice,quantity,username,investment)values(?,?,?,?,?)";
                PreparedStatement pt = con.prepareStatement(query);
                pt.setString(1,stockName);
                float avg = (qty*price)/qty;
                pt.setFloat(2, avg);
                pt.setInt(3,qty);
                pt.setString(4,username);
                pt.setFloat(5, amount);
                pt.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("In Update portfolio");
              
            }
        }
       
static float checkBalance(Connection con, String username) {
    System.out.println("CHECKING BALANCE");
    String query = "SELECT amount FROM wallet WHERE username = ?";
    try (PreparedStatement pt = con.prepareStatement(query)) {
        pt.setString(1, username);
        try (ResultSet rs = pt.executeQuery()) {
            if (rs.next()) {
                return rs.getFloat(1);
            } else {
                System.out.println("No balance found for user: " + username);
                return 0;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error checking balance: " + e.getMessage());
        return 0;
    }
}

//LOGOUT
static void logout(Connection con, Scanner sc,String username) {
    try{
        
        System.out.println("LOGGED OUT From "+username);
        username =null;
        displayMenu();

    }catch(Exception e){
        System.out.println(e);
    }
}

//MARKET PRICE
    static void  todaysMarketPrice(Scanner sc){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("MARKET PRICE");
        System.out.println("--------------------------------");
        System.out.println("NSE : 23171.35");

        System.out.println("BSE : 76569.44");
        System.out.println();
        System.out.println("Press any key to go back");
        String input = sc.next();
        if(input=="1"){
            displayMenu();
        }
        else {
           displayMenu();
        }
    }

    }


