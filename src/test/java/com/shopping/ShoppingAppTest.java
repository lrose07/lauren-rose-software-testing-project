package com.shopping;

import com.shopping.model.ShippingOption;
import com.shopping.service.ShoppingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

class ShoppingAppTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    @DisplayName("Should run full checkout flow and exit")
    void shouldRunFullCheckoutFlowAndExit() {
        String input = String.join("\n",
            "John Doe",      // name
            "CA",            // state
            "1",             // standard shipping
            "1",             // add item
            "Apple",         // item name
            "10.00",         // price
            "2",             // quantity
            "2",             // show total
            "6",             // checkout
            "7"              // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Checkout flow output verification",
            () -> assertTrue(output.contains("Shopping Application")),
            () -> assertTrue(output.contains("Item added")),
            () -> assertTrue(output.contains("Transaction completed")),
            () -> assertTrue(output.contains("Goodbye!"))
        );
    }

    @Test
    @DisplayName("Should handle invalid menu option")
    void shouldHandleInvalidMenuOption() {
        String input = String.join("\n",
            "John", "TX", "1",
            "invalid",  // invalid option
            "7"         // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Invalid option"));
    }

    @Test
    @DisplayName("Should handle empty item name validation")
    void shouldHandleEmptyItemNameValidation() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "",           // empty name
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Item name cannot be empty"));
    }

    @Test
    @DisplayName("Should handle invalid price")
    void shouldHandleInvalidPrice() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",      // name
            "abc",        // invalid price
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Invalid price"));
    }

    @Test
    @DisplayName("Should handle negative price")
    void shouldHandleNegativePrice() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",      // name
            "-5",         // negative price
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Price must be positive"));
    }

    @Test
    @DisplayName("Should handle invalid quantity")
    void shouldHandleInvalidQuantity() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",      // name
            "10.00",      // price
            "abc",        // invalid quantity
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Quantity must be an integer"));
    }

    @Test
    @DisplayName("Should handle quantity less than 1")
    void shouldHandleQuantityLessThanOne() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",      // name
            "10.00",      // price
            "0",          // quantity < 1
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Quantity must be at least 1"));
    }

    @Test
    @DisplayName("Should show empty cart message")
    void shouldShowEmptyCartMessage() {
        String input = String.join("\n",
            "John", "TX", "1",
            "3",          // show cart
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Cart is empty"));
    }

    @Test
    @DisplayName("Should show cart is empty when getting total with empty cart")
    void shouldShowCartIsEmptyWhenGettingTotalWithEmptyCart() {
        String input = String.join("\n",
            "John", "TX", "1",
            "2",          // get current total (showTotal)
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Cart is empty"));
    }

    @Test
    @DisplayName("Should show cart contents")
    void shouldShowCartContents() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "5.00",
            "2",
            "3",          // show cart
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertAll("Cart contents verification",
            () -> assertTrue(outContent.toString().contains("Cart Contents")),
            () -> assertTrue(outContent.toString().contains("Apple"))
        );
    }

    @Test
    @DisplayName("Should handle edit quantity on empty cart")
    void shouldHandleEditQuantityOnEmptyCart() {
        String input = String.join("\n",
            "John", "TX", "1",
            "4",          // edit quantity
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Cart is empty"));
    }

    @Test
    @DisplayName("Should handle edit non-existent item")
    void shouldHandleEditNonExistentItem() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "5.00",
            "1",
            "4",          // edit quantity
            "Banana",     // non-existent item
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Item not found in cart"));
    }

    @Test
    @DisplayName("Should handle remove on empty cart")
    void shouldHandleRemoveOnEmptyCart() {
        String input = String.join("\n",
            "John", "TX", "1",
            "5",          // remove item
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Cart is empty"));
    }

    @Test
    @DisplayName("Should handle checkout on empty cart")
    void shouldHandleCheckoutOnEmptyCart() {
        String input = String.join("\n",
            "John", "TX", "1",
            "6",          // checkout
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Cart is empty"));
    }

    @Test
    @DisplayName("Should handle checkout below minimum")
    void shouldHandleCheckoutBelowMinimum() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Pen",
            "0.50",       // $0.50
            "1",
            "6",          // checkout
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Minimum purchase"));
    }

    @Test
    @DisplayName("Should edit item quantity successfully")
    void shouldEditItemQuantitySuccessfully() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "5.00",
            "2",
            "4",          // edit quantity
            "Apple",
            "5",          // new quantity
            "3",          // show cart
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("Quantity updated"));
    }

    @Test
    @DisplayName("Should handle invalid quantity when editing")
    void shouldHandleInvalidQuantityWhenEditing() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "5.00",
            "2",
            "4",          // edit quantity
            "Apple",
            "0",          // invalid quantity
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Invalid quantity edit verification",
            () -> assertTrue(output.contains("Enter new quantity:")),
            () -> assertTrue(output.contains("Quantity must be at least 1"))
        );
    }

    @Test
    @DisplayName("Should verify edit quantity prompt appears")
    void shouldVerifyEditQuantityPromptAppears() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "5.00",
            "1",
            "4",          // edit quantity (now cart has item, passes empty check)
            "Banana",     // non-existent item - triggers "Item not found" and returns
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));
        app.run(new PrintStream(outContent));

        String output = outContent.toString();

        // Find the "Item added" message first (marks end of add flow)
        int itemAddedIndex = output.indexOf("Item added");
        assertTrue(itemAddedIndex > 0, "Should show item added message");

        // Then find "Enter item name:" that appears AFTER "Item added"
        // This ensures we're checking the prompt from editQuantity, not addItem
        int itemNamePromptIndex = output.indexOf("Enter item name:", itemAddedIndex);

        // And verify it's followed by "Item not found" (which only happens in edit flow)
        int itemNotFoundIndex = output.indexOf("Item not found in cart.", itemNamePromptIndex);

        assertTrue(itemNamePromptIndex > itemAddedIndex,
            "Enter item name prompt should appear after item added. itemAddedIndex=" + itemAddedIndex + ", itemNamePromptIndex=" + itemNamePromptIndex);
        assertTrue(itemNotFoundIndex > itemNamePromptIndex,
            "Item not found should appear after the prompt, proving edit flow was executed");
    }

    @Test
    @DisplayName("Should remove item successfully")
    void shouldRemoveItemSuccessfully() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "5.00",
            "2",
            "5",          // remove item
            "Apple",
            "3",          // show cart
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("Item removed"));
    }

    @Test
    @DisplayName("Should handle remove non-existent item")
    void shouldHandleRemoveNonExistentItem() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "5.00",
            "1",
            "5",          // remove item
            "Banana",     // non-existent item
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertTrue(outContent.toString().contains("Item not found in cart"));
    }

    @Test
    @DisplayName("Should verify edit quantity changes state")
    void shouldVerifyEditQuantityChangesState() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "5.00",
            "2",          // initial quantity
            "4",          // edit quantity
            "Apple",
            "5",          // new quantity
            "2",          // show total
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        // Verify the cart now has 5 items, not 2
        String output = outContent.toString();
        assertTrue(output.contains("Quantity updated. Cart now has 5 items"));
    }

    @Test
    @DisplayName("Should verify remove item changes state")
    void shouldVerifyRemoveItemChangesState() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add Apple
            "Apple",
            "5.00",
            "2",
            "1",          // add Banana
            "Banana",
            "3.00",
            "3",
            "5",          // remove Apple
            "Apple",
            "2",          // show total - should only show Banana
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        // Verify the cart now has 3 items (only Banana)
        String output = outContent.toString();
        assertTrue(output.contains("Item removed. Cart now has 3 items"));
    }

    @Test
    @DisplayName("Should continue after failed checkout")
    void shouldContinueAfterFailedCheckout() {
        String input = String.join("\n",
            "John", "TX", "1",
            "6",          // checkout (empty cart - fails)
            "3",          // show cart - proves app continued (didn't exit)
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));
        app.run(new PrintStream(outContent));

        String output = outContent.toString();

        // If checkout returned false (mutation), app would exit and not process option 3
        // We should see "Cart is empty." from the showCart() call after failed checkout
        // Check that it appears AFTER the checkout error message
        int checkoutErrorIndex = output.indexOf("Cart is empty. Add items before checkout.");
        int cartEmptyAfterCheckout = output.indexOf("Cart is empty.", checkoutErrorIndex + 1);

        assertTrue(checkoutErrorIndex > 0, "Should show checkout error");
        assertTrue(cartEmptyAfterCheckout > checkoutErrorIndex,
            "Should show cart after failed checkout - proves app continued. checkoutErrorIndex=" + checkoutErrorIndex + ", cartEmptyAfterCheckout=" + cartEmptyAfterCheckout);

        // Count menu appearances - should be 2 (initial + after failed checkout)
        // If mutation changed return true to false, app exits and menu only appears once
        int firstMenu = output.indexOf("1. Add item to cart");
        int secondMenu = output.indexOf("1. Add item to cart", firstMenu + 1);
        assertTrue(secondMenu > firstMenu,
            "Menu should appear twice - proves checkout returned true and loop continued");
    }

    @Test
    @DisplayName("Should verify checkout success output")
    void shouldVerifyCheckoutSuccessOutput() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1",          // add item
            "Apple",
            "50.00",
            "1",
            "6"           // checkout - success
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Checkout success output verification",
            () -> assertTrue(output.contains("--- Final Order ---")),
            () -> assertTrue(output.contains("Apple")),
            () -> assertTrue(output.contains("50.00")),
            () -> assertTrue(output.contains("Total: $")),
            () -> assertTrue(output.contains("Transaction completed"))
        );
    }

    @Test
    @DisplayName("Should select next day shipping")
    void shouldSelectNextDayShipping() {
        String input = String.join("\n",
            "John", "TX", "2",  // next day shipping
            "7"                 // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        assertEquals(ShippingOption.NEXT_DAY, service.getShippingOption());
    }

    @Test
    @DisplayName("Should show order summary")
    void shouldShowOrderSummary() {
        String input = String.join("\n",
            "John", "CA", "1",  // CA has 6% tax
            "1",          // add item
            "Apple",
            "100.00",     // $100 item
            "1",
            "2",          // show total
            "7"           // exit
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Order summary verification",
            () -> assertTrue(output.contains("Order Summary")),
            () -> assertTrue(output.contains("Subtotal:")),
            () -> assertTrue(output.contains("Tax:"))
        );
    }

    @Test
    @DisplayName("Should verify welcome message format")
    void shouldVerifyWelcomeMessageFormat() {
        String input = String.join("\n", "John", "TX", "1", "7");
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("=== Shopping Application ==="));
    }

    @Test
    @DisplayName("Should verify menu display with all options")
    void shouldVerifyMenuDisplayWithAllOptions() {
        String input = String.join("\n", "John", "TX", "1", "7");
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Menu display verification",
            () -> assertTrue(output.contains("1. Add item to cart")),
            () -> assertTrue(output.contains("2. Get current total")),
            () -> assertTrue(output.contains("3. See cart contents")),
            () -> assertTrue(output.contains("4. Edit item quantity")),
            () -> assertTrue(output.contains("5. Remove item from cart")),
            () -> assertTrue(output.contains("6. Checkout")),
            () -> assertTrue(output.contains("7. Exit")),
            () -> assertTrue(output.contains("Select action:"))
        );
    }

    @Test
    @DisplayName("Should verify shipping option selection display")
    void shouldVerifyShippingOptionSelectionDisplay() {
        String input = String.join("\n", "John", "TX", "1", "7");
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Shipping options display verification",
            () -> assertTrue(output.contains("Select shipping option:")),
            () -> assertTrue(output.contains("Standard ($10, free over $50)")),
            () -> assertTrue(output.contains("Next Day ($25)")),
            () -> assertTrue(output.contains("Choice:"))
        );
    }

    @Test
    @DisplayName("Should verify customer prompts")
    void shouldVerifyCustomerPrompts() {
        String input = String.join("\n", "John Doe", "CA", "1", "7");
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Customer prompts verification",
            () -> assertTrue(output.contains("Enter your name:")),
            () -> assertTrue(output.contains("Enter your state"))
        );
    }

    @Test
    @DisplayName("Should verify item added message includes count")
    void shouldVerifyItemAddedMessageIncludesCount() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "5.00", "2",
            "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("Item added. Cart now has"));
    }

    @Test
    @DisplayName("Should verify cart contents header")
    void shouldVerifyCartContentsHeader() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "5.00", "1",
            "3", "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("--- Cart Contents ---"));
    }

    @Test
    @DisplayName("Should verify quantity updated message includes count")
    void shouldVerifyQuantityUpdatedMessageIncludesCount() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "5.00", "1",
            "4", "Apple", "3",
            "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("Quantity updated. Cart now has"));
    }

    @Test
    @DisplayName("Should verify item removed message includes count")
    void shouldVerifyItemRemovedMessageIncludesCount() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "5.00", "1",
            "5", "Apple",
            "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("Item removed. Cart now has"));
    }

    @Test
    @DisplayName("Should verify final order header on checkout")
    void shouldVerifyFinalOrderHeaderOnCheckout() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "50.00", "1",
            "6"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("--- Final Order ---"));
    }

    @Test
    @DisplayName("Should verify transaction completed message")
    void shouldVerifyTransactionCompletedMessage() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "50.00", "1",
            "6"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("Transaction completed"));
    }

    @Test
    @DisplayName("Should verify add item prompts")
    void shouldVerifyAddItemPrompts() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "5.00", "2",
            "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Add item prompts verification",
            () -> assertTrue(output.contains("Enter item name:")),
            () -> assertTrue(output.contains("Enter item price:")),
            () -> assertTrue(output.contains("Enter quantity:"))
        );
    }

    @Test
    @DisplayName("Should verify edit quantity prompts")
    void shouldVerifyEditQuantityPrompts() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "5.00", "1",
            "4", "Apple", "5",
            "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Edit quantity prompts verification",
            () -> assertTrue(output.contains("Enter item name:")),
            () -> assertTrue(output.contains("Enter new quantity:"))
        );
    }

    @Test
    @DisplayName("Should verify remove item prompts")
    void shouldVerifyRemoveItemPrompts() {
        String input = String.join("\n",
            "John", "TX", "1",
            "1", "Apple", "5.00", "1",
            "5", "Apple",
            "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertTrue(output.contains("Enter item name to remove:"));
    }

    @Test
    @DisplayName("Should verify order summary displays all components")
    void shouldVerifyOrderSummaryDisplaysAllComponents() {
        String input = String.join("\n",
            "John", "CA", "1",
            "1", "Apple", "100.00", "1",
            "2", "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        assertAll("Order summary components verification",
            () -> assertTrue(output.contains("--- Order Summary ---")),
            () -> assertTrue(output.contains("Subtotal: $")),
            () -> assertTrue(output.contains("Tax: $")),
            () -> assertTrue(output.contains("Shipping: $")),
            () -> assertTrue(output.contains("Total: $"))
        );
    }

    @Test
    @DisplayName("Should verify multiple invalid options handled")
    void shouldVerifyMultipleInvalidOptionsHandled() {
        String input = String.join("\n",
            "John", "TX", "1",
            "abc", "xyz", "7"
        );
        Scanner scanner = new Scanner(input);
        ShoppingService service = new ShoppingService();
        ShoppingApp app = new ShoppingApp(scanner, service, new PrintStream(outContent));

        app.run(new PrintStream(outContent));

        String output = outContent.toString();
        long count = output.split("Invalid option\\.").length - 1;
        assertTrue(count >= 2, "Should show invalid option message at least twice");
    }

    @Test
    @DisplayName("Should create app with default constructor")
    void shouldCreateAppWithDefaultConstructor() {
        // This test ensures the default constructor is covered
        assertDoesNotThrow(() -> new ShoppingApp());
    }

    @Test
    @DisplayName("Should run main method successfully")
    void shouldRunMainMethodSuccessfully() {
        // Provide input for main() to complete one loop and exit
        String input = String.join("\n",
            "John",      // name
            "TX",        // state
            "1",         // standard shipping
            "7"          // exit
        );

        // Redirect System.in and System.out
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream originalOut = System.out;
        System.setIn(in);
        System.setOut(new PrintStream(out));

        try {
            // Create app and run it through main flow
            ShoppingApp app = new ShoppingApp();
            app.run(System.out);

            String output = out.toString();
            assertTrue(output.contains("Shopping Application"));
            assertTrue(output.contains("Goodbye!"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Should execute main method without error")
    void shouldExecuteMainMethodWithoutError() {
        String input = String.join("\n",
            "John",      // name
            "TX",        // state
            "1",         // standard shipping
            "7"          // exit
        );

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream originalOut = System.out;
        System.setIn(in);
        System.setOut(new PrintStream(out));

        try {
            ShoppingApp.main(new String[]{});

            String output = out.toString();
            assertTrue(output.contains("Shopping Application"));
        } finally {
            System.setOut(originalOut);
        }
    }
}
