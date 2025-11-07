package services;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class StockServiceAPI {

    private static final String API_KEY = "12DAU1BL5RJR18ZN";

    // ✅ MAIN SEARCH USED IN DASHBOARD + BUY + SELL
    public static float getPrice(String symbol) {
        try {
            symbol = symbol.toUpperCase() + ".BO"; // ✅ Force BSE India

            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                    + symbol + "&apikey=" + API_KEY;

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) sb.append(line);

            JSONObject json = new JSONObject(sb.toString());
            JSONObject data = json.getJSONObject("Global Quote");

            return Float.parseFloat(data.getString("05. price"));

        } catch (Exception e) {
            System.out.println("Error getting price: " + e.getMessage());
            return 0;
        }
    }
    public static void showTodaysMarketIndex() {
    try {
        System.out.println("\n--- TODAY'S MARKET INDEX ---");

        // ✅ NIFTY 50
        String niftyUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=NIFTY_50&apikey=" + API_KEY;
        String niftyData = getRawJSON(niftyUrl);
        JSONObject niftyJson = new JSONObject(niftyData).getJSONObject("Global Quote");

        System.out.println("\nNIFTY 50:");
        System.out.println("Price  : " + niftyJson.getString("05. price"));
        System.out.println("Change : " + niftyJson.getString("09. change"));
        System.out.println("Change%: " + niftyJson.getString("10. change percent"));

        // ✅ SENSEX
        String sensexUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=BSE&apikey=" + API_KEY;
        String sensexData = getRawJSON(sensexUrl);
        JSONObject sensexJson = new JSONObject(sensexData).getJSONObject("Global Quote");

        System.out.println("\nSENSEX:");
        System.out.println("Price  : " + sensexJson.getString("05. price"));
        System.out.println("Change : " + sensexJson.getString("09. change"));
        System.out.println("Change%: " + sensexJson.getString("10. change percent"));

    } catch (Exception e) {
        System.out.println("Error fetching index: " + e.getMessage());
    }
}
private static String getRawJSON(String urlString) throws Exception {
    HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
    conn.setRequestMethod("GET");

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String line;

    while ((line = br.readLine()) != null) sb.append(line);

    return sb.toString();
}
public static float getIndexPrice(String symbol) {
    try {
        String urlStr = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;
        
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        JSONObject json = new JSONObject(sb.toString());
        JSONObject result = json.getJSONObject("chart")
                                .getJSONArray("result")
                                .getJSONObject(0);

        JSONObject meta = result.getJSONObject("meta");
        double lastPrice = meta.getDouble("regularMarketPrice");

        return (float) lastPrice;

    } catch (Exception e) {
        System.out.println("Error fetching index: " + e.getMessage());
        return -1;
    }
}

    // ✅ SEARCH from Dashboard Menu
    public static void search(Scanner sc) {
        System.out.print("Enter stock symbol (e.g. TCS, INFY, RELIANCE): ");
        String stock = sc.next();

        float price = getPrice(stock);

        System.out.println("\nStock: " + stock.toUpperCase());
        System.out.println("Price: " + price);
        System.out.println("------------------------------");
    }
}
