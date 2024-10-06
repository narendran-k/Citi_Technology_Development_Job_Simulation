package <Application>;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class Apps extends Application {
    private static final String STOCK_SYMBOL = "^DJI"; // DJIA
    private static Queue<Double> stockQueue = new LinkedList<>();
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Set up the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (ticks)");
        yAxis.setLabel("Stock Price (USD)");

        // Create the line chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("DJIA Stock Price Over Time");
        lineChart.getData().add(series);

        // Create a stack pane and add the line chart
        StackPane root = new StackPane();
        root.getChildren().add(lineChart);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Stock Price Tracker");
        stage.show();

        // Timer for querying stock price every 5 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private int timeTick = 0;

            @Override
            public void run() {
                try {
                    Stock stock = YahooFinance.get(STOCK_SYMBOL);
                    double price = stock.getQuote().getPrice().doubleValue();
                    stockQueue.add(price);
                    System.out.println("Price: " + price + ", Timestamp: " + System.currentTimeMillis());

                    // Update the line graph
                    series.getData().add(new XYChart.Data<>(timeTick++, price));
                } catch (IOException e) {
                    System.err.println("Error fetching stock price: " + e.getMessage());
                }
            }
        }, 0, 5000); // Schedule task to run every 5 seconds
    }
}
