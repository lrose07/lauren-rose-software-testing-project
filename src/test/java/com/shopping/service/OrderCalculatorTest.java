package com.shopping.service;

import com.shopping.model.ShippingOption;
import com.shopping.service.OrderCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class OrderCalculatorTest {

    @Test
    @DisplayName("Should calculate total with standard shipping and no tax")
    void shouldCalculateTotalWithStandardShippingAndNoTax() {
        BigDecimal subtotal = new BigDecimal("30.00");
        
        BigDecimal total = OrderCalculator.calculateTotal(subtotal, "TX", ShippingOption.STANDARD);
        
        // 30.00 + 0 tax + 10 shipping = 40.00
        assertEquals(new BigDecimal("40.00"), total);
    }

    @Test
    @DisplayName("Should calculate total with next day shipping and tax")
    void shouldCalculateTotalWithNextDayShippingAndTax() {
        BigDecimal subtotal = new BigDecimal("100.00");
        
        BigDecimal total = OrderCalculator.calculateTotal(subtotal, "CA", ShippingOption.NEXT_DAY);
        
        // 100.00 + 6.00 tax + 25 shipping = 131.00
        assertEquals(new BigDecimal("131.00"), total);
    }

    @Test
    @DisplayName("Should calculate total with free standard shipping over $50")
    void shouldCalculateTotalWithFreeStandardShippingOverFifty() {
        BigDecimal subtotal = new BigDecimal("60.00");
        
        BigDecimal total = OrderCalculator.calculateTotal(subtotal, "VA", ShippingOption.STANDARD);
        
        // 60.00 + 0 tax + 0 shipping (free over $50) = 60.00
        assertEquals(new BigDecimal("60.00"), total);
    }

    @Test
    @DisplayName("Should calculate total with tax and free shipping")
    void shouldCalculateTotalWithTaxAndFreeShipping() {
        BigDecimal subtotal = new BigDecimal("75.00");
        
        BigDecimal total = OrderCalculator.calculateTotal(subtotal, "NY", ShippingOption.STANDARD);
        
        // 75.00 + 4.50 tax + 0 shipping (free over $50) = 79.50
        assertEquals(new BigDecimal("79.50"), total);
    }

    @Test
    @DisplayName("Should calculate total exactly at $50 for standard shipping")
    void shouldCalculateTotalExactlyAtFiftyForStandardShipping() {
        BigDecimal subtotal = new BigDecimal("50.00");
        
        BigDecimal total = OrderCalculator.calculateTotal(subtotal, "TX", ShippingOption.STANDARD);
        
        // 50.00 + 0 tax + 10 shipping (not free, must be OVER $50) = 60.00
        assertEquals(new BigDecimal("60.00"), total);
    }

    @Test
    @DisplayName("Should calculate total with next day shipping no free threshold")
    void shouldCalculateTotalWithNextDayShippingNoFreeThreshold() {
        BigDecimal subtotal = new BigDecimal("100.00");
        
        BigDecimal total = OrderCalculator.calculateTotal(subtotal, "VA", ShippingOption.NEXT_DAY);
        
        // 100.00 + 0 tax + 25 shipping (no free shipping) = 125.00
        assertEquals(new BigDecimal("125.00"), total);
    }

    @Test
    @DisplayName("Should handle zero subtotal")
    void shouldHandleZeroSubtotal() {
        BigDecimal total = OrderCalculator.calculateTotal(BigDecimal.ZERO, "IL", ShippingOption.STANDARD);
        
        // 0 + 0 tax + 10 shipping = 10.00
        assertEquals(new BigDecimal("10.00"), total);
    }
}
