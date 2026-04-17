# Shopping App

A console-based shopping cart application built with Java and Maven.

## Features

- Add, edit, and remove items from cart
- Automatic tax calculation for IL, CA, and NY (6%)
- Shipping options: Standard (\$10, free over \$50) and Next Day ($25)
- Order validation with min/max purchase limits
- Checkout with order summary

## Quick Start

### Prerequisites

- Java 25 or later
- Maven 3.9+

### Run the Application

```bash
# Compile and package
mvn clean package

# Run the application
java -jar target/shopping-app-1.0.jar
```

## User Instructions

1. **Start the app** - Enter your name and state when prompted
2. **Select shipping** - Choose Standard or Next Day
3. **Add items** - Enter item name, price, and quantity
4. **View cart** - Check current items and totals anytime
5. **Checkout** - Complete your purchase when ready

### Menu Options

| Option | Action |
|--------|--------|
| 1 | Add item to cart |
| 2 | Get current total |
| 3 | See cart contents |
| 4 | Edit item quantity |
| 5 | Remove item from cart |
| 6 | Checkout |
| 7 | Exit |

### Taxable States

Sales tax (6%) applies to: **IL, CA, NY**

### Purchase Limits

- Minimum: $1.00
- Maximum: $99,999.99

## Developer Instructions

### Build

```bash
# Compile
mvn compile

# Run tests
mvn test

# Package with coverage
mvn verify
```

### Test Coverage

The project uses JaCoCo with minimum thresholds:
- Line coverage: 90%
- Branch coverage: 90%
- Method coverage: 90%

### Mutation Testing

```bash
mvn pitest:mutationCoverage
```

Threshold: 90% mutation coverage

### Project Structure

```
src/
├── main/java/com/shopping/
│   ├── ShoppingApp.java          # Console UI
│   ├── model/
│   │   ├── CartItem.java         # Item with price/qty
│   │   ├── Customer.java         # Customer info
│   │   ├── ShippingOption.java   # Shipping enum
│   │   └── ShoppingCart.java     # Cart operations
│   └── service/
│       ├── OrderCalculator.java  # Total calculation
│       ├── ShoppingService.java  # Business logic
│       └── TaxCalculator.java    # Tax calculation
└── test/java/com/shopping/      # Unit tests
```

### Key Design Decisions

- **BigDecimal** for all monetary calculations to avoid floating-point errors
- **Java Records** for immutable data transfer objects (OrderSummary)
- **ValidationResult pattern** for input validation with error messages
- **Builder pattern** (Lombok) for object construction

### Dependencies

- JUnit 6 (testing)
- Mockito (mocking)
- Lombok (boilerplate reduction)
- JaCoCo (coverage)
- PIT (mutation testing)
