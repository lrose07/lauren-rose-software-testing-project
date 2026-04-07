package com.shopping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<String, CartItem> items = new HashMap<>();

    public void addItem(String itemName, BigDecimal price, int quantity) {
        items.merge(itemName, CartItem.builder().name(itemName).price(price).quantity(quantity).build(),
            (existing, newItem) -> {
                existing.addQuantity(quantity);
                return existing;
            });
    }

    public void editQuantity(String itemName, int newQuantity) {
        if (newQuantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        CartItem item = items.get(itemName);
        if (item == null) {
            throw new IllegalArgumentException("Item not found in cart: " + itemName);
        }
        item.setQuantity(newQuantity);
    }

    public void removeItem(String itemName) {
        items.remove(itemName);
    }

    public BigDecimal getSubtotal() {
        return items.values().stream()
            .map(CartItem::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getItemCount() {
        return items.values().stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }

    public Map<String, CartItem> getItems() {
        return new HashMap<>(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean hasItem(String itemName) {
        return items.containsKey(itemName);
    }
}
