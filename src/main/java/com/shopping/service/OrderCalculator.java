package com.shopping.service;

import com.shopping.model.ShippingOption;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class OrderCalculator {
    private OrderCalculator() {
        throw new AssertionError("Utility class - do not instantiate");
    }

    public static BigDecimal calculateTotal(BigDecimal subtotal, String state, ShippingOption shipping) {
        BigDecimal tax = TaxCalculator.calculateTax(subtotal, state);
        int shippingCost = shipping.calculateCost(subtotal.doubleValue());
        return subtotal.add(tax).add(BigDecimal.valueOf(shippingCost))
            .setScale(2, RoundingMode.HALF_UP);
    }
}
