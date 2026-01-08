package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;
import com.vendingmachine.model.Coin;
import com.vendingmachine.model.Product;
import com.vendingmachine.exception.InsufficientMoneyException;
import com.vendingmachine.exception.InvalidOperationException;
import com.vendingmachine.exception.ProductNotAvailableException;

import java.util.List;

/**
 * State representing the vending machine when coins have been inserted.
 * User can insert more coins, select a product, or cancel.
 * 
 * Follows Single Responsibility Principle - handles only the state where money has been inserted.
 */
public class HasMoneyState implements VendingMachineState {
    
    private final VendingMachine vendingMachine;

    public HasMoneyState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        vendingMachine.addInsertedCoin(coin);
        System.out.println("Inserted: " + coin.name() + " (" + coin.getValue() + " cents)");
        System.out.println("Current balance: " + vendingMachine.getCurrentBalance() + " cents");
    }

    @Override
    public void selectProduct(String shelfCode) {
        // Validate product availability
        if (!vendingMachine.getInventory().isProductAvailable(shelfCode)) {
            throw new ProductNotAvailableException("Product at shelf " + shelfCode + " is not available");
        }

        Product product = vendingMachine.getInventory().getProduct(shelfCode);
        int balance = vendingMachine.getCurrentBalance();
        int price = product.getPriceInCents();

        // Check if enough money
        if (balance < price) {
            throw new InsufficientMoneyException(price, balance);
        }

        // Set selected product and transition to dispensing state
        vendingMachine.setSelectedShelfCode(shelfCode);
        System.out.println("Product selected: " + product.getName() + " (" + product.getFormattedPrice() + ")");
        
        // Transition to Dispensing state
        vendingMachine.setState(vendingMachine.getDispensingState());
    }

    @Override
    public Product dispenseProduct() {
        throw new InvalidOperationException("Please select a product first");
    }

    @Override
    public List<Coin> cancelTransaction() {
        List<Coin> refund = vendingMachine.refundInsertedCoins();
        System.out.println("Transaction cancelled. Returning " + refund.size() + " coin(s).");
        
        // Transition back to Idle state
        vendingMachine.setState(vendingMachine.getIdleState());
        return refund;
    }

    @Override
    public String getStateName() {
        return "HAS_MONEY";
    }
}
