package com.vendingmachine.state;

import com.vendingmachine.model.Coin;
import com.vendingmachine.model.Product;

import java.util.List;

/**
 * Interface defining the contract for vending machine states.
 * 
 * Implements State Pattern following:
 * - Single Responsibility Principle: Each state handles its specific behavior
 * - Open-Closed Principle: New states can be added without modifying existing code
 * - Interface Segregation Principle: Defines only necessary operations for states
 * - Dependency Inversion Principle: VendingMachine depends on abstraction, not concrete states
 */
public interface VendingMachineState {

    /**
     * Handles inserting a coin into the machine.
     * @param coin the coin being inserted
     */
    void insertCoin(Coin coin);

    /**
     * Handles selecting a product from a specific shelf.
     * @param shelfCode the code of the shelf containing the desired product
     */
    void selectProduct(String shelfCode);

    /**
     * Dispenses the selected product and returns change.
     * @return the dispensed product
     */
    Product dispenseProduct();

    /**
     * Cancels the current transaction and returns inserted money.
     * @return list of coins to return
     */
    List<Coin> cancelTransaction();

    /**
     * Gets the name of the current state.
     * @return state name
     */
    String getStateName();
}
