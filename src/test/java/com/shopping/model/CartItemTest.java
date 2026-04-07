package com.shopping;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class CartItemTest {

    @Test
    @DisplayName("Should create cart item with correct values")
    void shouldCreateCartItemWithCorrectValues() {
        CartItem item = CartItem.builder().name("Apple").price(new BigDecimal("1.50")).quantity(3).build();
        
        assertAll("Cart item creation verification",
            () -> assertEquals("Apple", item.getName()),
            () -> assertEquals(new BigDecimal("1.50"), item.getPrice()),
            () -> assertEquals(3, item.getQuantity())
        );
    }

    @Test
    @DisplayName("Should calculate total correctly")
    void shouldCalculateTotalCorrectly() {
        CartItem item = CartItem.builder().name("Book").price(new BigDecimal("15.00")).quantity(2).build();
        
        assertEquals(new BigDecimal("30.00"), item.getTotal());
    }

    @Test
    @DisplayName("Should set quantity correctly")
    void shouldSetQuantityCorrectly() {
        CartItem item = CartItem.builder().name("Pen").price(new BigDecimal("2.00")).quantity(1).build();
        
        item.setQuantity(5);
        
        assertAll("Quantity set verification",
            () -> assertEquals(5, item.getQuantity()),
            () -> assertEquals(new BigDecimal("10.00"), item.getTotal())
        );
    }

    @Test
    @DisplayName("Should throw exception when setting quantity less than 1")
    void shouldThrowExceptionWhenSettingQuantityLessThanOne() {
        CartItem item = CartItem.builder().name("Pen").price(new BigDecimal("2.00")).quantity(1).build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> item.setQuantity(0));
        
        assertEquals("Quantity must be at least 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should add quantity correctly")
    void shouldAddQuantityCorrectly() {
        CartItem item = CartItem.builder().name("Notebook").price(new BigDecimal("5.00")).quantity(2).build();
        
        item.addQuantity(3);
        
        assertAll("Add quantity verification",
            () -> assertEquals(5, item.getQuantity()),
            () -> assertEquals(new BigDecimal("25.00"), item.getTotal())
        );
    }

    @Test
    @DisplayName("Should throw exception when setting quantity to zero boundary")
    void shouldThrowExceptionWhenSettingQuantityToZeroBoundary() {
        CartItem item = CartItem.builder().name("Pen").price(new BigDecimal("2.00")).quantity(1).build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> item.setQuantity(0));
        
        assertEquals("Quantity must be at least 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept quantity at minimum boundary of 1")
    void shouldAcceptQuantityAtMinimumBoundaryOfOne() {
        CartItem item = CartItem.builder().name("Pen").price(new BigDecimal("2.00")).quantity(5).build();
        
        item.setQuantity(1);
        
        assertEquals(1, item.getQuantity());
    }

    @Test
    @DisplayName("Should throw exception when adding zero quantity")
    void shouldThrowExceptionWhenAddingZeroQuantity() {
        CartItem item = CartItem.builder().name("Notebook").price(new BigDecimal("5.00")).quantity(2).build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> item.addQuantity(0));
        
        assertEquals("Cannot add less than 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept adding at minimum boundary of 1")
    void shouldAcceptAddingAtMinimumBoundaryOfOne() {
        CartItem item = CartItem.builder().name("Notebook").price(new BigDecimal("5.00")).quantity(2).build();
        
        item.addQuantity(1);
        
        assertEquals(3, item.getQuantity());
    }

    @Test
    @DisplayName("Should calculate total for large quantities")
    void shouldCalculateTotalForLargeQuantities() {
        CartItem item = CartItem.builder().name("Bulk").price(new BigDecimal("0.01")).quantity(1000).build();
        
        assertEquals(new BigDecimal("10.00"), item.getTotal());
    }
}
