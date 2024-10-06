package <project_name>;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    private static final String STOCK_SYMBOL = "^DJI"; // DJIA
    private static Queue<String> stockQueue = new LinkedList<>();

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Stock stock = YahooFinance.get(STOCK_SYMBOL);
                    double price = stock.getQuote().getPrice().doubleValue();
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    stockQueue.add("Price: " + price + ", Timestamp: " + timestamp);
                    System.out.println("Price: " + price + ", Timestamp: " + timestamp);
                } catch (IOException e) {
                    System.err.println("Error fetching stock price: " + e.getMessage());
                }
            }
        }, 0, 5000); // Schedule task to run every 5 seconds
    }
}
