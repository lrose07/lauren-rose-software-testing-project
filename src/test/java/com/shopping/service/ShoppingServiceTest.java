package com.shopping.service;

import com.shopping.model.ShippingOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class ShoppingServiceTest {

    private ShoppingService service;

    @BeforeEach
    void setUp() {
        service = new ShoppingService();
    }

    @Test
    @DisplayName("Should create customer successfully")
    void shouldCreateCustomerSuccessfully() {
        service.createCustomer("John Doe", "CA");

        assertAll("Customer creation verification",
            () -> assertTrue(service.isCustomerCreated()),
            () -> assertEquals("John Doe", service.getCustomer().getName()),
            () -> assertEquals("CA", service.getCustomer().getState())
        );
    }

    @Test
    @DisplayName("Should return false for isCustomerCreated when no customer")
    void shouldReturnFalseForIsCustomerCreatedWhenNoCustomer() {
        assertFalse(service.isCustomerCreated());
    }

    @Test
    @DisplayName("Should uppercase state when creating customer")
    void shouldUppercaseStateWhenCreatingCustomer() {
        service.createCustomer("John Doe", "ca");

        assertEquals("CA", service.getCustomer().getState());
    }

    @Test
    @DisplayName("Should default to standard shipping")
    void shouldDefaultToStandardShipping() {
        service.createCustomer("John Doe", "TX");

        assertEquals(ShippingOption.STANDARD, service.getShippingOption());
    }

    @Test
    @DisplayName("Should set shipping option")
    void shouldSetShippingOption() {
        service.createCustomer("John Doe", "TX");

        service.setShippingOption(ShippingOption.NEXT_DAY);

        assertEquals(ShippingOption.NEXT_DAY, service.getShippingOption());
    }

    @Test
    @DisplayName("Should throw exception when setting shipping option without customer")
    void shouldThrowExceptionWhenSettingShippingOptionWithoutCustomer() {
        assertThrows(IllegalStateException.class, () -> service.setShippingOption(ShippingOption.NEXT_DAY));
    }

    @Test
    @DisplayName("Should throw exception when getting shipping option without customer")
    void shouldThrowExceptionWhenGettingShippingOptionWithoutCustomer() {
        assertThrows(IllegalStateException.class, () -> service.getShippingOption());
    }

    @Test
    @DisplayName("Should validate item name - empty string")
    void shouldValidateItemNameEmptyString() {
        ShoppingService.ValidationResult result = service.validateItemName("");

        assertAll("Invalid item name validation",
            () -> assertFalse(result.isValid()),
            () -> assertEquals("Item name cannot be empty.", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should validate item name - null")
    void shouldValidateItemNameNull() {
        ShoppingService.ValidationResult result = service.validateItemName(null);

        assertAll("Invalid item name validation",
            () -> assertFalse(result.isValid()),
            () -> assertEquals("Item name cannot be empty.", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should validate item name - valid")
    void shouldValidateItemNameValid() {
        ShoppingService.ValidationResult result = service.validateItemName("Apple");

        assertTrue(result.isValid());
    }

    @Test
    @DisplayName("Should validate price - invalid format")
    void shouldValidatePriceInvalidFormat() {
        ShoppingService.ValidationResult result = service.validatePrice("abc");

        assertAll("Invalid price format validation",
            () -> assertFalse(result.isValid()),
            () -> assertEquals("Invalid price.", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should validate price - zero or negative")
    void shouldValidatePriceZeroOrNegative() {
        assertAll("Invalid price boundary validation",
            () -> assertFalse(service.validatePrice("0").isValid()),
            () -> assertFalse(service.validatePrice("-5").isValid())
        );
    }

    @Test
    @DisplayName("Should validate price - valid")
    void shouldValidatePriceValid() {
        ShoppingService.ValidationResult result = service.validatePrice("15.99");

        assertAll("Valid price validation",
            () -> assertTrue(result.isValid()),
            () -> assertEquals(new BigDecimal("15.99"), result.getValue())
        );
    }

    @Test
    @DisplayName("Should validate quantity - invalid format")
    void shouldValidateQuantityInvalidFormat() {
        ShoppingService.ValidationResult result = service.validateQuantity("abc");

        assertAll("Invalid quantity format validation",
            () -> assertFalse(result.isValid()),
            () -> assertEquals("Quantity must be an integer.", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should validate quantity - less than 1")
    void shouldValidateQuantityLessThanOne() {
        ShoppingService.ValidationResult result = service.validateQuantity("0");

        assertAll("Invalid quantity boundary validation",
            () -> assertFalse(result.isValid()),
            () -> assertEquals("Quantity must be at least 1.", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should validate quantity - valid")
    void shouldValidateQuantityValid() {
        ShoppingService.ValidationResult result = service.validateQuantity("5");

        assertAll("Valid quantity validation",
            () -> assertTrue(result.isValid()),
            () -> assertEquals(5, (Integer) result.getValue())
        );
    }

    @Test
    @DisplayName("Should add item to cart")
    void shouldAddItemToCart() {
        service.addItemToCart("Apple", new BigDecimal("1.50"), 3);

        assertAll("Item added to cart verification",
            () -> assertEquals(3, service.getCartItemCount()),
            () -> assertTrue(service.hasItemInCart("Apple"))
        );
    }

    @Test
    @DisplayName("Should check if cart is empty")
    void shouldCheckIfCartIsEmpty() {
        assertTrue(service.isCartEmpty());

        service.addItemToCart("Apple", new BigDecimal("1.50"), 3);

        assertFalse(service.isCartEmpty());
    }

    @Test
    @DisplayName("Should get cart items")
    void shouldGetCartItems() {
        service.addItemToCart("Apple", new BigDecimal("1.50"), 3);

        var items = service.getCartItems();

        assertAll("Cart items retrieval verification",
            () -> assertEquals(1, items.size()),
            () -> assertTrue(items.containsKey("Apple"))
        );
    }

    @Test
    @DisplayName("Should edit cart item quantity")
    void shouldEditCartItemQuantity() {
        service.addItemToCart("Apple", new BigDecimal("1.50"), 3);

        service.editCartItemQuantity("Apple", 5);

        assertEquals(5, service.getCartItemCount());
    }

    @Test
    @DisplayName("Should remove item from cart")
    void shouldRemoveItemFromCart() {
        service.addItemToCart("Apple", new BigDecimal("1.50"), 3);
        service.addItemToCart("Banana", new BigDecimal("0.99"), 2);

        service.removeItemFromCart("Apple");

        assertAll("Item removal verification",
            () -> assertFalse(service.hasItemInCart("Apple")),
            () -> assertTrue(service.hasItemInCart("Banana")),
            () -> assertEquals(2, service.getCartItemCount())
        );
    }

    @Test
    @DisplayName("Should return null order summary when cart is empty")
    void shouldReturnNullOrderSummaryWhenCartIsEmpty() {
        service.createCustomer("John Doe", "CA");

        ShoppingService.OrderSummary summary = service.getOrderSummary();

        assertNull(summary);
    }

    @Test
    @DisplayName("Should throw exception when getting order summary without customer")
    void shouldThrowExceptionWhenGettingOrderSummaryWithoutCustomer() {
        service.addItemToCart("Apple", new BigDecimal("1.50"), 3);

        assertThrows(IllegalStateException.class, () -> service.getOrderSummary());
    }

    @Test
    @DisplayName("Should get order summary with calculations")
    void shouldGetOrderSummaryWithCalculations() {
        service.createCustomer("John Doe", "CA");  // 6% tax
        service.addItemToCart("Apple", new BigDecimal("10.00"), 1);  // $10 subtotal

        ShoppingService.OrderSummary summary = service.getOrderSummary();

        assertAll("Order summary calculation verification",
            () -> assertNotNull(summary),
            () -> assertEquals(new BigDecimal("10.00"), summary.subtotal()),
            () -> assertEquals(new BigDecimal("0.60"), summary.tax()),
            () -> assertEquals(10, summary.shippingCost()),
            () -> assertEquals(new BigDecimal("20.60"), summary.total())
        );
    }

    @Test
    @DisplayName("Should checkout successfully")
    void shouldCheckoutSuccessfully() {
        service.createCustomer("John Doe", "TX");  // No tax
        service.addItemToCart("Apple", new BigDecimal("50.01"), 1);  // Over $50 for free shipping
        service.setShippingOption(ShippingOption.STANDARD);  // Free over $50

        ShoppingService.CheckoutResult result = service.checkout();

        assertAll("Successful checkout verification",
            () -> assertTrue(result.isSuccess()),
            () -> assertEquals(new BigDecimal("50.01"), result.getTotal()),
            () -> assertNotNull(result.getItems())
        );
    }

    @Test
    @DisplayName("Should fail checkout when cart is empty")
    void shouldFailCheckoutWhenCartIsEmpty() {
        service.createCustomer("John Doe", "TX");

        ShoppingService.CheckoutResult result = service.checkout();

        assertAll("Failed checkout verification",
            () -> assertFalse(result.isSuccess()),
            () -> assertEquals("Cart is empty. Add items before checkout.", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should fail checkout when below minimum purchase")
    void shouldFailCheckoutWhenBelowMinimumPurchase() {
        service.createCustomer("John Doe", "TX");
        service.addItemToCart("Pen", new BigDecimal("0.50"), 1);  // $0.50 < $1.00

        ShoppingService.CheckoutResult result = service.checkout();

        assertAll("Below minimum purchase verification",
            () -> assertFalse(result.isSuccess()),
            () -> assertEquals("Minimum purchase is $1.00", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should fail checkout when above maximum purchase")
    void shouldFailCheckoutWhenAboveMaximumPurchase() {
        service.createCustomer("John Doe", "TX");
        service.addItemToCart("Car", new BigDecimal("100000.00"), 1);  // > $99,999.99

        ShoppingService.CheckoutResult result = service.checkout();

        assertAll("Above maximum purchase verification",
            () -> assertFalse(result.isSuccess()),
            () -> assertEquals("Maximum purchase is $99999.99", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should checkout successfully at exactly minimum purchase")
    void shouldCheckoutSuccessfullyAtExactlyMinimumPurchase() {
        service.createCustomer("John Doe", "TX");
        service.addItemToCart("Pen", new BigDecimal("1.00"), 1);  // Exactly $1.00

        ShoppingService.CheckoutResult result = service.checkout();

        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("Should checkout successfully at exactly maximum purchase")
    void shouldCheckoutSuccessfullyAtExactlyMaximumPurchase() {
        service.createCustomer("John Doe", "TX");
        service.addItemToCart("Car", new BigDecimal("99999.99"), 1);  // Exactly $99,999.99

        ShoppingService.CheckoutResult result = service.checkout();

        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("Should fail checkout when customer not created")
    void shouldFailCheckoutWhenCustomerNotCreated() {
        service.addItemToCart("Apple", new BigDecimal("10.00"), 1);

        ShoppingService.CheckoutResult result = service.checkout();

        assertAll("Customer not created verification",
            () -> assertFalse(result.isSuccess()),
            () -> assertEquals("Customer must be created first.", result.getErrorMessage())
        );
    }

    @Test
    @DisplayName("Should return null when getValue called on success without value")
    void shouldReturnNullWhenGetValueCalledOnSuccessWithoutValue() {
        ShoppingService.ValidationResult result = ShoppingService.ValidationResult.success();

        assertAll("Success without value verification",
            () -> assertTrue(result.isValid()),
            () -> assertNull(result.getValue())
        );
    }
}
