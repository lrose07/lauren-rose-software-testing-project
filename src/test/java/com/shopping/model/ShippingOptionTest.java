package com.shopping.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ShippingOptionTest {

    @Test
    @DisplayName("Standard shipping should cost $10 for subtotal $50 or less")
    void standardShippingShouldCostTenForSubtotalFiftyOrLess() {
        assertAll("Standard shipping cost verification",
            () -> assertEquals(10, ShippingOption.STANDARD.calculateCost(50.00)),
            () -> assertEquals(10, ShippingOption.STANDARD.calculateCost(30.00)),
            () -> assertEquals(10, ShippingOption.STANDARD.calculateCost(1.00))
        );
    }

    @Test
    @DisplayName("Standard shipping should be free for subtotal over $50")
    void standardShippingShouldBeFreeForSubtotalOverFifty() {
        assertAll("Standard shipping free over $50 verification",
            () -> assertEquals(0, ShippingOption.STANDARD.calculateCost(50.01)),
            () -> assertEquals(0, ShippingOption.STANDARD.calculateCost(100.00)),
            () -> assertEquals(0, ShippingOption.STANDARD.calculateCost(99999.99))
        );
    }

    @Test
    @DisplayName("Next day shipping should always cost $25")
    void nextDayShippingShouldAlwaysCostTwentyFive() {
        assertAll("Next day shipping cost verification",
            () -> assertEquals(25, ShippingOption.NEXT_DAY.calculateCost(10.00)),
            () -> assertEquals(25, ShippingOption.NEXT_DAY.calculateCost(50.00)),
            () -> assertEquals(25, ShippingOption.NEXT_DAY.calculateCost(100.00)),
            () -> assertEquals(25, ShippingOption.NEXT_DAY.calculateCost(0.00))
        );
    }

    @Test
    @DisplayName("Standard shipping is $10 at $50 and free at $50.01")
    void standardShippingIsTenAtFiftyAndFreeAtFiftyOne() {
        assertAll("Boundary test",
            () -> assertEquals(10, ShippingOption.STANDARD.calculateCost(50.0)),
            () -> assertEquals(0, ShippingOption.STANDARD.calculateCost(50.01))
        );
    }

    @Test
    @DisplayName("Next day never free even at high subtotal")
    void nextDayNeverFreeEvenAtHighSubtotal() {
        assertEquals(25, ShippingOption.NEXT_DAY.calculateCost(1000.00));
    }
}
