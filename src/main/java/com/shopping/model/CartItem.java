package com.shopping.model;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CartItem {
    private final String name;
    private final BigDecimal price;
    private int quantity;

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = quantity;
    }

    public void addQuantity(int enteredQuantity) {
        if (enteredQuantity < 1) {
            throw new IllegalArgumentException("Cannot add less than 1");
        }
        setQuantity(this.quantity + enteredQuantity);
    }

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
