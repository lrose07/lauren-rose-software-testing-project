package com.shopping;

public enum ShippingOption {
    STANDARD(10, 50),
    NEXT_DAY(25, -1);

    private final int baseCost;
    private final int freeThreshold;

    ShippingOption(int baseCost, int freeThreshold) {
        this.baseCost = baseCost;
        this.freeThreshold = freeThreshold;
    }

    public int calculateCost(double subtotal) {
        if (freeThreshold > 0 && subtotal > freeThreshold) {
            return 0;
        }
        return baseCost;
    }
}
