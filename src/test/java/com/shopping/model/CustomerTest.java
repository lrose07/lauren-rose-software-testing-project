package com.shopping;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    @DisplayName("Should create customer with name and state")
    void shouldCreateCustomerWithNameAndState() {
        Customer customer = Customer.builder().name("John Doe").state("CA").build();
        
        assertAll("Customer creation verification",
            () -> assertEquals("John Doe", customer.getName()),
            () -> assertEquals("CA", customer.getState())
        );
    }

    @Test
    @DisplayName("Should default to standard shipping")
    void shouldDefaultToStandardShipping() {
        Customer customer = Customer.builder().name("John Doe").state("TX").build();
        
        assertEquals(ShippingOption.STANDARD, customer.getShippingOption());
    }

    @Test
    @DisplayName("Should set shipping option")
    void shouldSetShippingOption() {
        Customer customer = Customer.builder().name("John Doe").state("TX").build();
        
        customer.setShippingOption(ShippingOption.NEXT_DAY);
        
        assertEquals(ShippingOption.NEXT_DAY, customer.getShippingOption());
    }
}
