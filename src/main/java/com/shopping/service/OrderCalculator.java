package com.shopping;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderCalculator {
    public static BigDecimal calculateTotal(BigDecimal subtotal, String state, ShippingOption shipping) {
        BigDecimal tax = TaxCalculator.calculateTax(subtotal, state);
        int shippingCost = shipping.calculateCost(subtotal.doubleValue());
        return subtotal.add(tax).add(BigDecimal.valueOf(shippingCost))
            .setScale(2, RoundingMode.HALF_UP);
    }
}
