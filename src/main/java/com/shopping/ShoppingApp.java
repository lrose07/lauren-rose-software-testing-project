package com.shopping;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

public class ShoppingApp {
    private final Scanner scanner;
    private final ShoppingService service;
    private final PrintStream out;

    public ShoppingApp() {
        this.scanner = new Scanner(System.in);
        this.service = new ShoppingService();
        this.out = System.out;
    }

    ShoppingApp(Scanner scanner, ShoppingService service, PrintStream out) {
        this.scanner = scanner;
        this.service = service;
        this.out = out;
    }

    public static void main(String[] args) {
        new ShoppingApp().run(System.out);
    }

    public void run(PrintStream out) {
        out.println("=== Shopping Application ===");
        setupCustomer();

        boolean running = true;
        while (running) {
            out.println("\n1. Add item to cart");
            out.println("2. Get current total");
            out.println("3. See cart contents");
            out.println("4. Edit item quantity");
            out.println("5. Remove item from cart");
            out.println("6. Checkout");
            out.println("7. Exit");
            out.print("\nSelect action: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addItem(); break;
                case "2": showTotal(); break;
                case "3": showCart(); break;
                case "4": editQuantity(); break;
                case "5": removeItem(); break;
                case "6": running = checkout(); break;
                case "7": running = false; break;
                default: out.println("Invalid option.");
            }
        }
        out.println("Goodbye!");
    }

    private void setupCustomer() {
        out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        out.print("Enter your state (e.g., IL, CA, NY): ");
        String state = scanner.nextLine().trim();
        service.createCustomer(name, state);
        setShippingOption();
    }

    private void setShippingOption() {
        out.println("Select shipping option:");
        out.println("1. Standard ($10, free over $50)");
        out.println("2. Next Day ($25)");
        out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("2")) {
            service.setShippingOption(ShippingOption.NEXT_DAY);
        }
    }

    private void addItem() {
        out.print("Enter item name: ");
        String itemName = scanner.nextLine().trim();
        ShoppingService.ValidationResult nameResult = service.validateItemName(itemName);
        if (!nameResult.isValid()) {
            out.println(nameResult.getErrorMessage());
            return;
        }

        out.print("Enter item price: ");
        String priceInput = scanner.nextLine().trim();
        ShoppingService.ValidationResult priceResult = service.validatePrice(priceInput);
        if (!priceResult.isValid()) {
            out.println(priceResult.getErrorMessage());
            return;
        }
        BigDecimal price = priceResult.getValue();

        out.print("Enter quantity: ");
        String quantityInput = scanner.nextLine().trim();
        ShoppingService.ValidationResult quantityResult = service.validateQuantity(quantityInput);
        if (!quantityResult.isValid()) {
            out.println(quantityResult.getErrorMessage());
            return;
        }
        int quantity = quantityResult.getValue();

        service.addItemToCart(itemName, price, quantity);
        out.println("Item added. Cart now has " + service.getCartItemCount() + " items.");
    }

    private void showTotal() {
        ShoppingService.OrderSummary summary = service.getOrderSummary();
        if (summary == null) {
            out.println("Cart is empty.");
            return;
        }

        out.println("\n--- Order Summary ---");
        out.println("Subtotal: $" + summary.getSubtotal());
        out.println("Tax: $" + summary.getTax());
        out.println("Shipping: $" + summary.getShippingCost());
        out.println("Total: $" + summary.getTotal());
    }

    private void showCart() {
        if (service.isCartEmpty()) {
            out.println("Cart is empty.");
            return;
        }

        out.println("\n--- Cart Contents ---");
        for (CartItem item : service.getCartItems().values()) {
            out.println(item.getName() + " - $" + item.getPrice() + " x " + item.getQuantity() + " = $" + item.getTotal());
        }
    }

    private void editQuantity() {
        if (service.isCartEmpty()) {
            out.println("Cart is empty.");
            return;
        }

        out.print("Enter item name: ");
        String itemName = scanner.nextLine().trim();
        if (!service.hasItemInCart(itemName)) {
            out.println("Item not found in cart.");
            return;
        }

        out.print("Enter new quantity: ");
        String quantityInput = scanner.nextLine().trim();
        ShoppingService.ValidationResult quantityResult = service.validateQuantity(quantityInput);
        if (!quantityResult.isValid()) {
            out.println(quantityResult.getErrorMessage());
            return;
        }
        int newQuantity = quantityResult.getValue();

        service.editCartItemQuantity(itemName, newQuantity);
        out.println("Quantity updated. Cart now has " + service.getCartItemCount() + " items.");
    }

    private void removeItem() {
        if (service.isCartEmpty()) {
            out.println("Cart is empty.");
            return;
        }

        out.print("Enter item name to remove: ");
        String itemName = scanner.nextLine().trim();
        if (!service.hasItemInCart(itemName)) {
            out.println("Item not found in cart.");
            return;
        }

        service.removeItemFromCart(itemName);
        out.println("Item removed. Cart now has " + service.getCartItemCount() + " items.");
    }

    private boolean checkout() {
        ShoppingService.CheckoutResult result = service.checkout();
        if (!result.isSuccess()) {
            out.println(result.getErrorMessage());
            return true;
        }

        out.println("\n--- Final Order ---");
        for (CartItem item : result.getItems().values()) {
            out.println(item.getName() + " - $" + item.getPrice() + " x " + item.getQuantity() + " = $" + item.getTotal());
        }
        out.println("Total: $" + result.getTotal());
        out.println("Transaction completed");
        return false;
    }
}
