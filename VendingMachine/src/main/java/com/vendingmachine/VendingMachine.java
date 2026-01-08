package com.vendingmachine;

import com.vendingmachine.inventory.Inventory;
import com.vendingmachine.model.Coin;
import com.vendingmachine.model.ItemShelf;
import com.vendingmachine.model.Product;
import com.vendingmachine.state.DispensingState;
import com.vendingmachine.state.HasMoneyState;
import com.vendingmachine.state.IdleState;
import com.vendingmachine.state.VendingMachineState;

import java.util.ArrayList;
import java.util.List;

/**
 * Main VendingMachine class that coordinates the vending machine operations.
 * 
 * Design Patterns Used:
 * - State Pattern: For managing different states (Idle, HasMoney, Dispensing)
 * - Singleton Pattern: Optional - can be made singleton if needed
 * 
 * SOLID Principles Applied:
 * - Single Responsibility: Each class has one responsibility
 * - Open-Closed: New states/products can be added without modifying existing code
 * - Liskov Substitution: All states implement VendingMachineState interface
 * - Interface Segregation: VendingMachineState has minimal required methods
 * - Dependency Inversion: VendingMachine depends on abstractions (VendingMachineState)
 */
public class VendingMachine {
    
    private final Inventory inventory;
    private final List<Coin> insertedCoins;
    private String selectedShelfCode;
    
    // State Pattern - different states of the vending machine
    private final VendingMachineState idleState;
    private final VendingMachineState hasMoneyState;
    private final VendingMachineState dispensingState;
    private VendingMachineState currentState;

    public VendingMachine() {
        this.inventory = new Inventory();
        this.insertedCoins = new ArrayList<>();
        
        // Initialize states
        this.idleState = new IdleState(this);
        this.hasMoneyState = new HasMoneyState(this);
        this.dispensingState = new DispensingState(this);
        
        // Start in idle state
        this.currentState = idleState;
    }

    // ==================== Public API ====================

    /**
     * Inserts a coin into the vending machine.
     * @param coin the coin to insert
     */
    public void insertCoin(Coin coin) {
        currentState.insertCoin(coin);
    }

    /**
     * Selects a product from the specified shelf.
     * @param shelfCode the code of the shelf
     */
    public void selectProduct(String shelfCode) {
        currentState.selectProduct(shelfCode);
    }

    /**
     * Dispenses the selected product.
     * @return the dispensed product
     */
    public Product dispenseProduct() {
        return currentState.dispenseProduct();
    }

    /**
     * Cancels the current transaction and returns inserted coins.
     * @return list of coins returned to the user
     */
    public List<Coin> cancelTransaction() {
        return currentState.cancelTransaction();
    }

    /**
     * Gets the current state name.
     * @return name of current state
     */
    public String getCurrentStateName() {
        return currentState.getStateName();
    }

    // ==================== Inventory Management ====================

    /**
     * Stocks a product on a shelf.
     * @param shelfCode the shelf code
     * @param product the product to stock
     * @param quantity the quantity to stock
     */
    public void stockProduct(String shelfCode, Product product, int quantity) {
        inventory.stockProduct(shelfCode, product, quantity);
    }

    /**
     * Adds coins to the machine's coin inventory (for change).
     * @param coin type of coin
     * @param count number of coins
     */
    public void loadCoins(Coin coin, int count) {
        inventory.addCoins(coin, count);
    }

    /**
     * Displays all available products.
     */
    public void displayProducts() {
        System.out.println("\n========== AVAILABLE PRODUCTS ==========");
        for (ItemShelf shelf : inventory.getAllShelves()) {
            if (shelf.getProduct() != null) {
                System.out.printf("[%s] %s - %s (Stock: %d)%n",
                    shelf.getCode(),
                    shelf.getProduct().getName(),
                    shelf.getProduct().getFormattedPrice(),
                    shelf.getQuantity());
            }
        }
        System.out.println("=========================================\n");
    }

    /**
     * Displays the inventory status (for admin/debugging).
     */
    public void displayInventoryStatus() {
        inventory.displayInventory();
    }

    // ==================== State Management (Internal) ====================

    public void setState(VendingMachineState state) {
        this.currentState = state;
    }

    public VendingMachineState getIdleState() {
        return idleState;
    }

    public VendingMachineState getHasMoneyState() {
        return hasMoneyState;
    }

    public VendingMachineState getDispensingState() {
        return dispensingState;
    }

    // ==================== Balance Management (Internal) ====================

    public void addInsertedCoin(Coin coin) {
        insertedCoins.add(coin);
    }

    public int getCurrentBalance() {
        return insertedCoins.stream()
            .mapToInt(Coin::getValue)
            .sum();
    }

    public List<Coin> refundInsertedCoins() {
        List<Coin> refund = new ArrayList<>(insertedCoins);
        insertedCoins.clear();
        return refund;
    }

    public void clearInsertedCoins() {
        insertedCoins.clear();
    }

    public void addInsertedCoinsToInventory() {
        for (Coin coin : insertedCoins) {
            inventory.addCoins(coin, 1);
        }
    }

    // ==================== Selection Management (Internal) ====================

    public String getSelectedShelfCode() {
        return selectedShelfCode;
    }

    public void setSelectedShelfCode(String shelfCode) {
        this.selectedShelfCode = shelfCode;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
