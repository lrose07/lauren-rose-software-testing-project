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
    @DisplayName("Standard shipping edge case at exactly $50")
    void standardShippingEdgeCaseAtExactlyFifty() {
        // Must be OVER $50 for free shipping, so at exactly $50 it's $10
        assertEquals(10, ShippingOption.STANDARD.calculateCost(50.00));
    }
}
