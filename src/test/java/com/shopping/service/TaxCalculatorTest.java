package com.shopping.service;

import com.shopping.service.TaxCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

class TaxCalculatorTest {

    @ParameterizedTest
    @CsvSource({
        "IL, 0.06",
        "CA, 0.06",
        "NY, 0.06",
        "il, 0.06",
        "ca, 0.06",
        "ny, 0.06"
    })
    @DisplayName("Should calculate 6% tax for taxable states")
    void shouldCalculateSixPercentTaxForTaxableStates(String state, String expectedPercent) {
        BigDecimal subtotal = new BigDecimal("100.00");
        BigDecimal expectedTax = subtotal.multiply(new BigDecimal(expectedPercent));
        
        BigDecimal tax = TaxCalculator.calculateTax(subtotal, state);
        
        assertEquals(expectedTax.setScale(2, RoundingMode.HALF_UP), tax);
    }

    @ParameterizedTest
    @ValueSource(strings = {"TX", "FL", "VA", "WA", "OH", "", "XX"})
    @DisplayName("Should return zero tax for non-taxable states")
    void shouldReturnZeroTaxForNonTaxableStates(String state) {
        BigDecimal subtotal = new BigDecimal("100.00");
        
        BigDecimal tax = TaxCalculator.calculateTax(subtotal, state);
        
        assertEquals(BigDecimal.ZERO, tax);
    }

    @Test
    @DisplayName("Should calculate tax with correct rounding")
    void shouldCalculateTaxWithCorrectRounding() {
        BigDecimal subtotal = new BigDecimal("99.99");
        
        BigDecimal tax = TaxCalculator.calculateTax(subtotal, "IL");
        
        // 99.99 * 0.06 = 5.9994 -> rounded to 6.00
        assertEquals(new BigDecimal("6.00"), tax);
    }

    @Test
    @DisplayName("Should handle zero subtotal")
    void shouldHandleZeroSubtotal() {
        BigDecimal tax = TaxCalculator.calculateTax(BigDecimal.ZERO, "CA");

        assertEquals(new BigDecimal("0.00"), tax);
    }

    @Test
    @DisplayName("Should return zero tax for null state")
    void shouldReturnZeroTaxForNullState() {
        BigDecimal tax = TaxCalculator.calculateTax(new BigDecimal("100.00"), null);

        assertEquals(BigDecimal.ZERO, tax);
    }
}
