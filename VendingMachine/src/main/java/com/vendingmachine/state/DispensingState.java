package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;
import com.vendingmachine.model.Coin;
import com.vendingmachine.model.Product;
import com.vendingmachine.exception.InsufficientChangeException;
import com.vendingmachine.exception.InvalidOperationException;

import java.util.List;

/**
 * State representing the vending machine when dispensing a product.
 * Product has been selected and payment verified.
 * 
 * Follows Single Responsibility Principle - handles only the dispensing behavior.
 */
public class DispensingState implements VendingMachineState {
    
    private final VendingMachine vendingMachine;

    public DispensingState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        throw new InvalidOperationException("Cannot insert coins while dispensing. Please wait.");
    }

    @Override
    public void selectProduct(String shelfCode) {
        throw new InvalidOperationException("Product already selected. Please wait for dispensing.");
    }

    @Override
    public Product dispenseProduct() {
        String shelfCode = vendingMachine.getSelectedShelfCode();
        Product product = vendingMachine.getInventory().getProduct(shelfCode);
        int price = product.getPriceInCents();
        int balance = vendingMachine.getCurrentBalance();
        int changeAmount = balance - price;

        // Calculate change before dispensing
        List<Coin> change = null;
        if (changeAmount > 0) {
            change = vendingMachine.getInventory().calculateChange(changeAmount);
            if (change == null) {
                // Cannot provide exact change, refund all money
                List<Coin> refund = vendingMachine.refundInsertedCoins();
                vendingMachine.setSelectedShelfCode(null);
                vendingMachine.setState(vendingMachine.getIdleState());
                throw new InsufficientChangeException(changeAmount);
            }
        }

        // Add inserted coins to inventory
        vendingMachine.addInsertedCoinsToInventory();

        // Dispense the product
        Product dispensedProduct = vendingMachine.getInventory().dispenseProduct(shelfCode);
        System.out.println("\n*** DISPENSING: " + dispensedProduct.getName() + " ***");

        // Return change if any
        if (change != null && !change.isEmpty()) {
            System.out.println("Change returned: ");
            int totalChange = 0;
            for (Coin coin : change) {
                System.out.println("  - " + coin.name() + " (" + coin.getValue() + " cents)");
                totalChange += coin.getValue();
            }
            System.out.printf("Total change: %d cents ($%.2f)%n", totalChange, totalChange / 100.0);
        } else {
            System.out.println("No change due.");
        }

        // Clear selection and reset state
        vendingMachine.setSelectedShelfCode(null);
        vendingMachine.clearInsertedCoins();
        vendingMachine.setState(vendingMachine.getIdleState());

        System.out.println("Thank you for your purchase!\n");
        return dispensedProduct;
    }

    @Override
    public List<Coin> cancelTransaction() {
        throw new InvalidOperationException("Cannot cancel during dispensing");
    }

    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}
