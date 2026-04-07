package com.shopping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class ShoppingService {
    private static final BigDecimal MIN_PURCHASE = new BigDecimal("1.00");
    private static final BigDecimal MAX_PURCHASE = new BigDecimal("99999.99");

    private Customer customer;
    private final ShoppingCart cart = new ShoppingCart();

    public void createCustomer(String name, String state) {
        this.customer = Customer.builder()
            .name(name)
            .state(state.toUpperCase())
            .build();
    }

    public void setShippingOption(ShippingOption option) {
        if (customer == null) {
            throw new IllegalStateException("Customer must be created first");
        }
        customer.setShippingOption(option);
    }

    public ShippingOption getShippingOption() {
        if (customer == null) {
            throw new IllegalStateException("Customer must be created first");
        }
        return customer.getShippingOption();
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isCustomerCreated() {
        return customer != null;
    }

    public ValidationResult validateItemName(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            return ValidationResult.error("Item name cannot be empty.");
        }
        return ValidationResult.success();
    }

    public ValidationResult validatePrice(String priceStr) {
        try {
            BigDecimal price = new BigDecimal(priceStr.trim());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                return ValidationResult.error("Price must be positive.");
            }
            return ValidationResult.successWithValue(price);
        } catch (NumberFormatException e) {
            return ValidationResult.error("Invalid price.");
        }
    }

    public ValidationResult validateQuantity(String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr.trim());
            if (quantity < 1) {
                return ValidationResult.error("Quantity must be at least 1.");
            }
            return ValidationResult.successWithValue(quantity);
        } catch (NumberFormatException e) {
            return ValidationResult.error("Quantity must be an integer.");
        }
    }

    public void addItemToCart(String itemName, BigDecimal price, int quantity) {
        cart.addItem(itemName, price, quantity);
    }

    public int getCartItemCount() {
        return cart.getItemCount();
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public Map<String, CartItem> getCartItems() {
        return cart.getItems();
    }

    public boolean hasItemInCart(String itemName) {
        return cart.hasItem(itemName);
    }

    public void editCartItemQuantity(String itemName, int newQuantity) {
        cart.editQuantity(itemName, newQuantity);
    }

    public void removeItemFromCart(String itemName) {
        cart.removeItem(itemName);
    }

    public OrderSummary getOrderSummary() {
        if (customer == null) {
            throw new IllegalStateException("Customer must be created first");
        }
        if (cart.isEmpty()) {
            return null;
        }

        BigDecimal subtotal = cart.getSubtotal();
        BigDecimal tax = TaxCalculator.calculateTax(subtotal, customer.getState());
        int shippingCost = customer.getShippingOption().calculateCost(subtotal.doubleValue());
        BigDecimal total = OrderCalculator.calculateTotal(subtotal, customer.getState(), customer.getShippingOption());

        return new OrderSummary(subtotal, tax, shippingCost, total);
    }

    public CheckoutResult checkout() {
        if (customer == null) {
            return CheckoutResult.error("Customer must be created first.");
        }
        if (cart.isEmpty()) {
            return CheckoutResult.error("Cart is empty. Add items before checkout.");
        }

        BigDecimal subtotal = cart.getSubtotal();
        if (subtotal.compareTo(MIN_PURCHASE) < 0) {
            return CheckoutResult.error("Minimum purchase is $" + MIN_PURCHASE);
        }
        if (subtotal.compareTo(MAX_PURCHASE) > 0) {
            return CheckoutResult.error("Maximum purchase is $" + MAX_PURCHASE);
        }

        BigDecimal total = OrderCalculator.calculateTotal(subtotal, customer.getState(), customer.getShippingOption());
        return CheckoutResult.success(total, cart.getItems());
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final Object value;

        private ValidationResult(boolean valid, String errorMessage, Object value) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.value = value;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, null, null);
        }

        public static ValidationResult successWithValue(Object value) {
            return new ValidationResult(true, null, value);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message, null);
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        @SuppressWarnings("unchecked")
        public <T> T getValue() {
            return (T) value;
        }
    }

    public static class OrderSummary {
        private final BigDecimal subtotal;
        private final BigDecimal tax;
        private final int shippingCost;
        private final BigDecimal total;

        public OrderSummary(BigDecimal subtotal, BigDecimal tax, int shippingCost, BigDecimal total) {
            this.subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
            this.tax = tax;
            this.shippingCost = shippingCost;
            this.total = total;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public BigDecimal getTax() {
            return tax;
        }

        public int getShippingCost() {
            return shippingCost;
        }

        public BigDecimal getTotal() {
            return total;
        }
    }

    public static class CheckoutResult {
        private final boolean success;
        private final String errorMessage;
        private final BigDecimal total;
        private final Map<String, CartItem> items;

        private CheckoutResult(boolean success, String errorMessage, BigDecimal total, Map<String, CartItem> items) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.total = total;
            this.items = items;
        }

        public static CheckoutResult success(BigDecimal total, Map<String, CartItem> items) {
            return new CheckoutResult(true, null, total, items);
        }

        public static CheckoutResult error(String message) {
            return new CheckoutResult(false, message, null, null);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public Map<String, CartItem> getItems() {
            return items;
        }
    }
}
