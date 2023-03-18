package src.views.formatter;

public class StockView {
    public static String formatStockTrade(int amount, double price) {
        return String.format("$ %.2f = %d x $ %.2f", amount*price, amount, price);
    }
}
