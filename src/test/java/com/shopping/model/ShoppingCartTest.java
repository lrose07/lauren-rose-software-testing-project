package com.shopping.model;

import com.shopping.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    @Test
    @DisplayName("Should be empty when created")
    void shouldBeEmptyWhenCreated() {
        assertAll("Empty cart verification",
                () -> assertTrue(cart.isEmpty()),
                () -> assertEquals(0, cart.getItemCount()),
                () -> assertEquals(BigDecimal.ZERO, cart.getSubtotal())
        );
    }

    @Test
    @DisplayName("Should add item to cart")
    void shouldAddItemToCart() {
        cart.addItem("Apple", new BigDecimal("1.50"), 3);

        assertAll("Item added verification",
                () -> assertFalse(cart.isEmpty()),
                () -> assertEquals(3, cart.getItemCount()),
                () -> assertTrue(cart.hasItem("Apple"))
        );
    }

    @Test
    @DisplayName("Should merge quantities when adding same item")
    void shouldMergeQuantitiesWhenAddingSameItem() {
        cart.addItem("Book", new BigDecimal("10.00"), 2);
        cart.addItem("Book", new BigDecimal("10.00"), 3);

        assertAll("Quantity merge verification",
                () -> assertEquals(5, cart.getItemCount()),
                () -> assertEquals(new BigDecimal("50.00"), cart.getSubtotal())
        );
    }

    @Test
    @DisplayName("Should calculate subtotal correctly with multiple items")
    void shouldCalculateSubtotalCorrectlyWithMultipleItems() {
        cart.addItem("Apple", new BigDecimal("1.50"), 2);  // 3.00
        cart.addItem("Book", new BigDecimal("10.00"), 1); // 10.00
        cart.addItem("Pen", new BigDecimal("2.00"), 5);    // 10.00

        assertEquals(new BigDecimal("23.00"), cart.getSubtotal());
    }

    @Test
    @DisplayName("Should edit quantity of existing item")
    void shouldEditQuantityOfExistingItem() {
        cart.addItem("Notebook", new BigDecimal("5.00"), 2);

        cart.editQuantity("Notebook", 5);

        assertAll("Quantity edit verification",
                () -> assertEquals(5, cart.getItemCount()),
                () -> assertEquals(new BigDecimal("25.00"), cart.getSubtotal())
        );
    }

    @Test
    @DisplayName("Should throw exception when editing quantity of non-existent item")
    void shouldThrowExceptionWhenEditingQuantityOfNonExistentItem() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cart.editQuantity("NonExistent", 5));

        assertEquals("Item not found in cart: NonExistent", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting quantity less than 1")
    void shouldThrowExceptionWhenSettingQuantityLessThanOne() {
        cart.addItem("Item", new BigDecimal("5.00"), 2);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cart.editQuantity("Item", 0));

        assertEquals("Quantity must be at least 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should allow editing quantity to 1")
    void shouldAllowEditingQuantityToOne() {
        cart.addItem("Item", new BigDecimal("5.00"), 2);

        cart.editQuantity("Item", 1);

        assertAll("Quantity edit to 1 verification",
                () -> assertEquals(1, cart.getItemCount()),
                () -> assertEquals(new BigDecimal("5.00"), cart.getSubtotal())
        );
    }

    @Test
    @DisplayName("Should remove item from cart")
    void shouldRemoveItemFromCart() {
        cart.addItem("Apple", new BigDecimal("1.50"), 3);
        cart.addItem("Book", new BigDecimal("10.00"), 1);

        cart.removeItem("Apple");

        assertAll("Item removal verification",
                () -> assertFalse(cart.hasItem("Apple")),
                () -> assertTrue(cart.hasItem("Book")),
                () -> assertEquals(1, cart.getItemCount())
        );
    }

    @Test
    @DisplayName("Should handle removing non-existent item gracefully")
    void shouldHandleRemovingNonExistentItemGracefully() {
        cart.addItem("Apple", new BigDecimal("1.50"), 3);

        cart.removeItem("NonExistent"); // Should not throw

        assertEquals(3, cart.getItemCount());
    }

    @Test
    @DisplayName("Should return defensive copy of items")
    void shouldReturnDefensiveCopyOfItems() {
        cart.addItem("Apple", new BigDecimal("1.50"), 3);

        var items = cart.getItems();
        items.clear(); // Modify the returned map

        assertAll("Defensive copy verification",
                () -> assertFalse(cart.isEmpty()),
                () -> assertEquals(3, cart.getItemCount())
        );
    }
}