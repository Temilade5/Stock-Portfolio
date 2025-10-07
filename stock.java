import java.util.*;

class Stock {
    String ticker;
    double priceBought;
    int quantity;

    public Stock(String ticker, double priceBought, int quantity) {
        this.ticker = ticker;
        this.priceBought = priceBought;
        this.quantity = quantity;
    }

    // method to calculate present value of stock
    public double getTotalValue(double currentPrice) {
        return currentPrice * quantity;
    }

    // method to calculate how much was originally spent
    public double getTotalCost() {
        return priceBought * quantity;
    }

    // method to calculate profit or loss
    public double getProfit(double currentPrice) {
        return getTotalValue(currentPrice) - getTotalCost();
    }
}

public class StockPortfolioSimulator {
    static Scanner input = new Scanner(System.in);
    static Map<String, Stock> portfolio = new HashMap<>();   // this stores the current stocks you own
    static Map<String, Double> mockStockPrices = new HashMap<>(); // this stores the current mock stock prices

    public static void main(String[] args) {
        System.out.println("--------------Welcome to the Stock Portfolio Simulator-------------");

        mockStockPrices.put("AAPL", 190.55);
        mockStockPrices.put("GOOGL", 2800.00);
        mockStockPrices.put("AMZN", 3400.00);

        while (true) {
            System.out.println("\n1. Buy Stock\n2. Sell Stock\n3. View Portfolio\n4. Exit\nEnter your choice:");
            int option = -1;
            try {
                option = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (option) {
                case 1:
                    buyStock();
                    break;
                case 2:
                    sellStock();
                    break;
                case 3:
                    viewPortfolio();
                    break;
                case 4:
                    System.out.println("Exiting the simulator. Thank you!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    // method to buy stock
    public static void buyStock() {
        System.out.println("Enter the ticker of the stock you would like to buy:");
        String ticker = input.nextLine().toUpperCase();
        if (!mockStockPrices.containsKey(ticker)) {
            System.out.println("Invalid ticker. Please try again.");
            return;
        }
        double price = mockStockPrices.get(ticker);
        System.out.println("Current price of " + ticker + " is $" + price);
        System.out.println("Enter the quantity you want to buy:");
        int qty = -1;
        try {
            qty = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid quantity. Please enter a positive integer.");
            return;
        }
        if (qty <= 0) {
            System.out.println("Invalid quantity. Please enter a positive value.");
            return;
        }
        if (portfolio.containsKey(ticker)) {
            Stock existingStock = portfolio.get(ticker);
            existingStock.quantity += qty; // update the quantity if the stock already exists
            // Optionally, you could average the priceBought, but here we keep the last price
        } else {
            portfolio.put(ticker, new Stock(ticker, price, qty)); // adds the stock to the portfolio
        }
        System.out.println("You have successfully bought " + qty + " shares of " + ticker + " at $" + price + " each.");
    }

    // method to sell stock
    public static void sellStock() {
        System.out.println("Enter the ticker of the stock you would like to sell:");
        String ticker = input.nextLine().toUpperCase();
        if (!portfolio.containsKey(ticker)) {
            System.out.println("You do not own any shares of " + ticker + ".");
            return;
        }
        Stock stock = portfolio.get(ticker);
        if (stock.quantity == 0) {
            System.out.println("You have no shares of " + ticker + " to sell.");
            return;
        }
        double currentPrice = mockStockPrices.getOrDefault(ticker, 0.0);
        System.out.println("You own " + stock.quantity + " shares of " + ticker + " bought at $" + stock.priceBought + " each.");
        System.out.println("Current price of " + ticker + " is $" + currentPrice);
        System.out.println("Do you want to sell all shares? (yes/no)");
        String response = input.nextLine().toLowerCase();
        if (response.equals("yes")) {
            int qty = stock.quantity;
            double profit = stock.getProfit(currentPrice);
            portfolio.remove(ticker);
            System.out.println("You have successfully sold all " + qty + " shares of " + ticker + " at $" + currentPrice + " each.");
            System.out.println("You bought them at $" + stock.priceBought + " each.");
            System.out.printf("Your profit from this transaction is: $%.2f\n", profit);
            return;
        }
        System.out.println("Enter the quantity you want to sell:");
        int qty = -1;
        try {
            qty = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid quantity. Please enter a positive integer.");
            return;
        }
        if (qty <= 0 || qty > stock.quantity) {
            System.out.println("Invalid quantity. Please enter a positive value less than or equal to " + stock.quantity);
            return;
        }
        double profit = (currentPrice - stock.priceBought) * qty;
        stock.quantity -= qty;
        if (stock.quantity == 0) {
            portfolio.remove(ticker);
        }
        System.out.println("You have successfully sold " + qty + " shares of " + ticker + " at $" + currentPrice + " each.");
        System.out.println("You bought them at $" + stock.priceBought + " each.");
        System.out.printf("Your profit from this transaction is: $%.2f\n", profit);
    }

    // method to view portfolio
    public static void viewPortfolio() {
        if (portfolio.isEmpty()) {
            System.out.println("Your portfolio is empty.");
            return;
        }
        System.out.println("Your current portfolio:");
        System.out.printf("%-10s %-12s %-10s %-12s\n", "Ticker", "Price Bought", "Quantity", "Total Value");
        double totalCost = 0;
        double totalValue = 0;
        for (Stock stock : portfolio.values()) {
            double currentPrice = mockStockPrices.getOrDefault(stock.ticker, 0.0);
            double value = stock.getTotalValue(currentPrice);
            totalCost += stock.getTotalCost();
            totalValue += value;
            System.out.printf("%-10s $%-11.2f %-10d $%-11.2f\n", stock.ticker, stock.priceBought, stock.quantity, value);
        }
        System.out.printf("Total Invested: $%.2f\n", totalCost);
        System.out.printf("Total Value of Portfolio: $%.2f\n", totalValue);
        double finalValue = totalValue - totalCost;
        if (finalValue > 0) {
            System.out.printf("Total Profit: $%.2f\n", finalValue);
        } else if (finalValue < 0) {
            System.out.printf("Total Loss: $%.2f\n", Math.abs(finalValue));
        } else {
            System.out.println("No profit or loss.");
        }
    }
}
