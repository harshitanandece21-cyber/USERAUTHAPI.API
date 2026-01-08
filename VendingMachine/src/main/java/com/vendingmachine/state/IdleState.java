package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;
import com.vendingmachine.model.Coin;
import com.vendingmachine.model.Product;
import com.vendingmachine.exception.InvalidOperationException;

import java.util.ArrayList;
import java.util.List;

/**
 * State representing the vending machine when no coins have been inserted.
 * This is the initial/default state.
 * 
 * Follows Single Responsibility Principle - handles only idle state behavior.
 */
public class IdleState implements VendingMachineState {
    
    private final VendingMachine vendingMachine;

    public IdleState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        vendingMachine.addInsertedCoin(coin);
        System.out.println("Inserted: " + coin.name() + " (" + coin.getValue() + " cents)");
        System.out.println("Current balance: " + vendingMachine.getCurrentBalance() + " cents");
        
        // Transition to HasMoney state
        vendingMachine.setState(vendingMachine.getHasMoneyState());
    }

    @Override
    public void selectProduct(String shelfCode) {
        throw new InvalidOperationException("Please insert coins first before selecting a product");
    }

    @Override
    public Product dispenseProduct() {
        throw new InvalidOperationException("dispense", getStateName());
    }

    @Override
    public List<Coin> cancelTransaction() {
        System.out.println("No transaction to cancel.");
        return new ArrayList<>();
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }
}
