package com.vendingmachine;

import com.vendingmachine.model.Coin;
import com.vendingmachine.model.Product;
import com.vendingmachine.exception.InsufficientMoneyException;
import com.vendingmachine.exception.InvalidOperationException;
import com.vendingmachine.exception.ProductNotAvailableException;

/**
 * Demo class showcasing the Vending Machine functionality.
 * 
 * This demonstrates:
 * - State Pattern usage
 * - SOLID Principles in action
 * - Error handling
 * - Complete purchase flow
 */
public class VendingMachineDemo {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("    VENDING MACHINE - LOW LEVEL DESIGN DEMO   ");
        System.out.println("==============================================\n");

        // Create vending machine
        VendingMachine vendingMachine = new VendingMachine();

        // Setup Phase: Stock products
        setupProducts(vendingMachine);

        // Setup Phase: Load coins for change
        setupCoins(vendingMachine);

        // Display initial state
        vendingMachine.displayProducts();
        vendingMachine.displayInventoryStatus();

        // Demo 1: Successful purchase with exact money
        System.out.println(">>> DEMO 1: Successful purchase with exact money");
        demo1ExactMoneyPurchase(vendingMachine);

        // Demo 2: Purchase with change
        System.out.println("\n>>> DEMO 2: Purchase with change");
        demo2PurchaseWithChange(vendingMachine);

        // Demo 3: Cancel transaction
        System.out.println("\n>>> DEMO 3: Cancel transaction");
        demo3CancelTransaction(vendingMachine);

        // Demo 4: Insufficient money
        System.out.println("\n>>> DEMO 4: Insufficient money error handling");
        demo4InsufficientMoney(vendingMachine);

        // Demo 5: Product not available
        System.out.println("\n>>> DEMO 5: Product not available error handling");
        demo5ProductNotAvailable(vendingMachine);

        // Demo 6: Invalid operation
        System.out.println("\n>>> DEMO 6: Invalid operation error handling");
        demo6InvalidOperation(vendingMachine);

        // Final inventory status
        System.out.println("\n>>> FINAL INVENTORY STATUS:");
        vendingMachine.displayInventoryStatus();

        System.out.println("==============================================");
        System.out.println("           DEMO COMPLETED SUCCESSFULLY        ");
        System.out.println("==============================================");
    }

    private static void setupProducts(VendingMachine vm) {
        System.out.println("Setting up products...");
        
        // Create products
        Product coke = new Product("COKE", "Coca-Cola", 150);      // $1.50
        Product pepsi = new Product("PEPSI", "Pepsi", 150);        // $1.50
        Product water = new Product("WATER", "Spring Water", 100); // $1.00
        Product chips = new Product("CHIPS", "Potato Chips", 125); // $1.25
        Product candy = new Product("CANDY", "Chocolate Bar", 75); // $0.75
        Product juice = new Product("JUICE", "Orange Juice", 175); // $1.75

        // Stock products on shelves
        vm.stockProduct("A1", coke, 5);
        vm.stockProduct("A2", pepsi, 5);
        vm.stockProduct("A3", water, 10);
        vm.stockProduct("B1", chips, 8);
        vm.stockProduct("B2", candy, 12);
        vm.stockProduct("B3", juice, 4);
        
        System.out.println("Products stocked successfully!\n");
    }

    private static void setupCoins(VendingMachine vm) {
        System.out.println("Loading coins for change...");
        
        vm.loadCoins(Coin.DOLLAR, 10);
        vm.loadCoins(Coin.QUARTER, 20);
        vm.loadCoins(Coin.DIME, 30);
        vm.loadCoins(Coin.NICKEL, 30);
        vm.loadCoins(Coin.PENNY, 50);
        
        System.out.println("Coins loaded successfully!\n");
    }

    private static void demo1ExactMoneyPurchase(VendingMachine vm) {
        System.out.println("Current state: " + vm.getCurrentStateName());
        
        // Insert exact money for water ($1.00)
        vm.insertCoin(Coin.DOLLAR);
        
        System.out.println("Current state: " + vm.getCurrentStateName());
        
        // Select water at shelf A3
        vm.selectProduct("A3");
        
        System.out.println("Current state: " + vm.getCurrentStateName());
        
        // Dispense
        Product product = vm.dispenseProduct();
        
        System.out.println("Received: " + product);
        System.out.println("Current state: " + vm.getCurrentStateName());
    }

    private static void demo2PurchaseWithChange(VendingMachine vm) {
        System.out.println("Current state: " + vm.getCurrentStateName());
        
        // Insert $2.00 for candy that costs $0.75
        vm.insertCoin(Coin.DOLLAR);
        vm.insertCoin(Coin.DOLLAR);
        
        System.out.println("Current state: " + vm.getCurrentStateName());
        
        // Select candy at shelf B2
        vm.selectProduct("B2");
        
        System.out.println("Current state: " + vm.getCurrentStateName());
        
        // Dispense (should receive $1.25 change)
        Product product = vm.dispenseProduct();
        
        System.out.println("Received: " + product);
        System.out.println("Current state: " + vm.getCurrentStateName());
    }

    private static void demo3CancelTransaction(VendingMachine vm) {
        System.out.println("Current state: " + vm.getCurrentStateName());
        
        // Insert some money
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.DIME);
        
        System.out.println("Inserted 60 cents...");
        System.out.println("Current state: " + vm.getCurrentStateName());
        
        // Cancel and get refund
        var refund = vm.cancelTransaction();
        
        System.out.println("Refunded coins:");
        for (Coin coin : refund) {
            System.out.println("  - " + coin.name() + " (" + coin.getValue() + " cents)");
        }
        System.out.println("Current state: " + vm.getCurrentStateName());
    }

    private static void demo4InsufficientMoney(VendingMachine vm) {
        try {
            // Insert only 50 cents
            vm.insertCoin(Coin.QUARTER);
            vm.insertCoin(Coin.QUARTER);
            
            // Try to buy Coke ($1.50)
            vm.selectProduct("A1");
            
        } catch (InsufficientMoneyException e) {
            System.out.println("Error caught: " + e.getMessage());
            System.out.println("Need " + e.getShortage() + " more cents");
            
            // Cancel to get refund
            vm.cancelTransaction();
        }
    }

    private static void demo5ProductNotAvailable(VendingMachine vm) {
        try {
            vm.insertCoin(Coin.DOLLAR);
            
            // Try to select non-existent shelf
            vm.selectProduct("Z9");
            
        } catch (ProductNotAvailableException e) {
            System.out.println("Error caught: " + e.getMessage());
            
            // Cancel to get refund
            vm.cancelTransaction();
        }
    }

    private static void demo6InvalidOperation(VendingMachine vm) {
        try {
            // Try to select product without inserting money (machine is in Idle state)
            vm.selectProduct("A1");
            
        } catch (InvalidOperationException e) {
            System.out.println("Error caught: " + e.getMessage());
        }

        try {
            // Try to dispense without selecting (machine is in Idle state)
            vm.dispenseProduct();
            
        } catch (InvalidOperationException e) {
            System.out.println("Error caught: " + e.getMessage());
        }
    }
}
