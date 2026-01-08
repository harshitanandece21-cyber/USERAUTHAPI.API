# Vending Machine - Low Level Design (Java)

A comprehensive Low-Level Design implementation of a Vending Machine in Java, demonstrating Object-Oriented Programming (OOP) principles and SOLID principles.

## Table of Contents
- [Overview](#overview)
- [Design Patterns Used](#design-patterns-used)
- [SOLID Principles Applied](#solid-principles-applied)
- [Project Structure](#project-structure)
- [Class Diagram](#class-diagram)
- [How to Run](#how-to-run)
- [Features](#features)
- [Usage Examples](#usage-examples)

## Overview

This vending machine implementation supports:
- Multiple product types with different prices
- Multiple coin denominations (Penny, Nickel, Dime, Quarter, Dollar)
- Product inventory management
- Coin change calculation
- Transaction cancellation with refund
- Comprehensive error handling

## Design Patterns Used

### 1. State Pattern
The vending machine uses the State Pattern to manage different operational states:
- **IdleState**: No coins inserted, waiting for user interaction
- **HasMoneyState**: Coins inserted, waiting for product selection
- **DispensingState**: Product selected, dispensing product and returning change

This pattern allows the machine to change its behavior based on its internal state without complex conditional logic.

### 2. Factory Pattern (implicit)
States are created once during VendingMachine initialization and reused, following a flyweight-like pattern.

## SOLID Principles Applied

### 1. Single Responsibility Principle (SRP)
Each class has one clear responsibility:
- `Product`: Holds product information
- `Coin`: Represents coin denominations
- `ItemShelf`: Manages a single shelf's inventory
- `Inventory`: Manages overall product and coin inventory
- Each State class: Handles behavior for its specific state

### 2. Open-Closed Principle (OCP)
- New states can be added without modifying existing code
- New coin types can be added to the Coin enum
- New products can be created without changing the Product class

### 3. Liskov Substitution Principle (LSP)
- All state implementations (IdleState, HasMoneyState, DispensingState) can be used interchangeably through the VendingMachineState interface
- The vending machine works with any state that implements the interface

### 4. Interface Segregation Principle (ISP)
- `VendingMachineState` interface contains only essential methods
- No class is forced to implement methods it doesn't need

### 5. Dependency Inversion Principle (DIP)
- `VendingMachine` depends on the `VendingMachineState` abstraction, not concrete implementations
- High-level modules don't depend on low-level modules; both depend on abstractions

## Project Structure

```
VendingMachine/
├── src/main/java/com/vendingmachine/
│   ├── VendingMachine.java          # Main vending machine class
│   ├── VendingMachineDemo.java      # Demo/Main class
│   ├── model/
│   │   ├── Coin.java                # Coin enum
│   │   ├── Product.java             # Product class
│   │   └── ItemShelf.java           # Shelf class
│   ├── state/
│   │   ├── VendingMachineState.java # State interface
│   │   ├── IdleState.java           # Idle state implementation
│   │   ├── HasMoneyState.java       # Has money state implementation
│   │   └── DispensingState.java     # Dispensing state implementation
│   ├── inventory/
│   │   └── Inventory.java           # Inventory management
│   └── exception/
│       ├── ProductNotAvailableException.java
│       ├── InsufficientMoneyException.java
│       ├── InsufficientChangeException.java
│       └── InvalidOperationException.java
└── README.md
```

## Class Diagram

```
                    +-------------------+
                    | VendingMachine    |
                    +-------------------+
                    | - inventory       |
                    | - insertedCoins   |
                    | - currentState    |
                    +-------------------+
                    | + insertCoin()    |
                    | + selectProduct() |
                    | + dispenseProduct()|
                    | + cancelTransaction()|
                    +-------------------+
                            |
                            | uses
                            v
            +---------------------------+
            | <<interface>>             |
            | VendingMachineState       |
            +---------------------------+
            | + insertCoin()            |
            | + selectProduct()         |
            | + dispenseProduct()       |
            | + cancelTransaction()     |
            +---------------------------+
                    ^   ^   ^
        +-----------+   |   +-----------+
        |               |               |
+---------------+ +---------------+ +----------------+
| IdleState     | | HasMoneyState | | DispensingState|
+---------------+ +---------------+ +----------------+

+-------------------+       +-------------------+
| Inventory         |       | ItemShelf         |
+-------------------+       +-------------------+
| - productShelves  |<>-----| - code            |
| - coinInventory   |       | - product         |
+-------------------+       | - quantity        |
                            +-------------------+

+-------------------+       +-------------------+
| Product           |       | Coin (enum)       |
+-------------------+       +-------------------+
| - id              |       | PENNY(1)          |
| - name            |       | NICKEL(5)         |
| - priceInCents    |       | DIME(10)          |
+-------------------+       | QUARTER(25)       |
                            | DOLLAR(100)       |
                            +-------------------+
```

## How to Run

### Prerequisites
- Java 11 or higher
- Java compiler (javac)

### Compile and Run
```bash
# Navigate to the VendingMachine directory
cd VendingMachine

# Compile all Java files
javac -d out src/main/java/com/vendingmachine/**/*.java src/main/java/com/vendingmachine/*.java

# Run the demo
java -cp out com.vendingmachine.VendingMachineDemo
```

### Alternative: Single Command
```bash
# Compile and run in one step
cd VendingMachine/src/main/java
javac com/vendingmachine/**/*.java com/vendingmachine/*.java
java com.vendingmachine.VendingMachineDemo
```

## Features

1. **Product Management**
   - Add products to specific shelves
   - Track inventory quantities
   - Display available products

2. **Coin Handling**
   - Accept multiple coin denominations
   - Calculate and return change
   - Maintain coin inventory for change

3. **Purchase Flow**
   - Insert coins
   - Select product
   - Dispense product with change

4. **Transaction Management**
   - Cancel transaction and get refund
   - Handle insufficient funds
   - Handle out-of-stock products

5. **Error Handling**
   - ProductNotAvailableException
   - InsufficientMoneyException
   - InsufficientChangeException
   - InvalidOperationException

## Usage Examples

### Basic Purchase
```java
VendingMachine vm = new VendingMachine();

// Stock product
Product coke = new Product("COKE", "Coca-Cola", 150);
vm.stockProduct("A1", coke, 10);

// Load coins for change
vm.loadCoins(Coin.QUARTER, 20);

// Make purchase
vm.insertCoin(Coin.DOLLAR);
vm.insertCoin(Coin.DOLLAR);
vm.selectProduct("A1");
Product purchased = vm.dispenseProduct();
// Returns Coca-Cola with $0.50 change
```

### Cancel Transaction
```java
vm.insertCoin(Coin.QUARTER);
vm.insertCoin(Coin.QUARTER);
List<Coin> refund = vm.cancelTransaction();
// Returns the two quarters
```

### Error Handling
```java
try {
    vm.insertCoin(Coin.QUARTER);
    vm.selectProduct("A1"); // Price: $1.50
} catch (InsufficientMoneyException e) {
    System.out.println("Need more money: " + e.getShortage() + " cents");
    vm.cancelTransaction();
}
```

## State Transitions

```
     +--------+
     | IDLE   |<------------------------+
     +--------+                         |
         |                              |
         | insertCoin()                 |
         v                              |
     +-----------+                      |
     | HAS_MONEY |                      |
     +-----------+                      |
         |    |                         |
         |    | cancelTransaction()     |
         |    +-------------------------+
         |                              |
         | selectProduct()              |
         v                              |
     +------------+                     |
     | DISPENSING |                     |
     +------------+                     |
         |                              |
         | dispenseProduct()            |
         +------------------------------+
```

## License

This is a demonstration project for educational purposes, showcasing Low-Level Design principles in Java.
