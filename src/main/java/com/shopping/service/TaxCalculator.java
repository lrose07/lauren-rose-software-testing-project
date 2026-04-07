package com.shopping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class TaxCalculator {
    private static final BigDecimal TAX_RATE = new BigDecimal("0.06");
    private static final Set<String> TAXABLE_STATES = Set.of("IL", "CA", "NY");

    public static BigDecimal calculateTax(BigDecimal subtotal, String state) {
        if (!TAXABLE_STATES.contains(state.toUpperCase())) {
            return BigDecimal.ZERO;
        }
        return subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
