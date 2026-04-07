package com.shopping;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    private final String name;
    private final String state;
    @Builder.Default
    private ShippingOption shippingOption = ShippingOption.STANDARD;
}
