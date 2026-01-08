package com.vendingmachine.inventory;

import com.vendingmachine.model.Coin;
import com.vendingmachine.model.ItemShelf;
import com.vendingmachine.model.Product;
import com.vendingmachine.exception.ProductNotAvailableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages the inventory of products and coins in the vending machine.
 * 
 * Follows Single Responsibility Principle - only handles inventory management.
 * Follows Open-Closed Principle - can extend inventory operations without modifying existing code.
 */
public class Inventory {
    private final Map<String, ItemShelf> productShelves;
    private final Map<Coin, Integer> coinInventory;

    public Inventory() {
        this.productShelves = new HashMap<>();
        this.coinInventory = new HashMap<>();
        initializeCoinInventory();
    }

    private void initializeCoinInventory() {
        for (Coin coin : Coin.values()) {
            coinInventory.put(coin, 0);
        }
    }

    // ==================== Product Inventory ====================

    /**
     * Adds a new shelf to the inventory.
     * @param code unique shelf code
     */
    public void addShelf(String code) {
        if (!productShelves.containsKey(code)) {
            productShelves.put(code, new ItemShelf(code));
        }
    }

    /**
     * Stocks a product on a specific shelf.
     * @param code shelf code
     * @param product the product to stock
     * @param quantity number of items to stock
     */
    public void stockProduct(String code, Product product, int quantity) {
        ItemShelf shelf = productShelves.get(code);
        if (shelf == null) {
            shelf = new ItemShelf(code);
            productShelves.put(code, shelf);
        }
        shelf.setProduct(product);
        shelf.setQuantity(quantity);
    }

    /**
     * Adds more stock to an existing product shelf.
     * @param code shelf code
     * @param quantity additional quantity to add
     */
    public void addStock(String code, int quantity) {
        ItemShelf shelf = productShelves.get(code);
        if (shelf == null) {
            throw new IllegalArgumentException("Shelf " + code + " does not exist");
        }
        shelf.addStock(quantity);
    }

    /**
     * Checks if a product is available at a specific shelf.
     * @param code shelf code
     * @return true if product is available
     */
    public boolean isProductAvailable(String code) {
        ItemShelf shelf = productShelves.get(code);
        return shelf != null && shelf.isAvailable();
    }

    /**
     * Gets the product at a specific shelf.
     * @param code shelf code
     * @return the product
     * @throws ProductNotAvailableException if shelf doesn't exist or product not available
     */
    public Product getProduct(String code) {
        ItemShelf shelf = productShelves.get(code);
        if (shelf == null) {
            throw new ProductNotAvailableException("Shelf " + code + " does not exist");
        }
        if (shelf.getProduct() == null) {
            throw new ProductNotAvailableException("No product assigned to shelf " + code);
        }
        return shelf.getProduct();
    }

    /**
     * Dispenses a product from a specific shelf.
     * @param code shelf code
     * @return the dispensed product
     * @throws ProductNotAvailableException if product not available
     */
    public Product dispenseProduct(String code) {
        ItemShelf shelf = productShelves.get(code);
        if (shelf == null || !shelf.isAvailable()) {
            throw new ProductNotAvailableException("Product not available at shelf " + code);
        }
        return shelf.dispense();
    }

    /**
     * Gets all shelves in the inventory.
     * @return unmodifiable list of all shelves
     */
    public List<ItemShelf> getAllShelves() {
        return Collections.unmodifiableList(new ArrayList<>(productShelves.values()));
    }

    // ==================== Coin Inventory ====================

    /**
     * Adds coins to the coin inventory.
     * @param coin type of coin
     * @param count number of coins to add
     */
    public void addCoins(Coin coin, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Cannot add negative coins");
        }
        coinInventory.merge(coin, count, Integer::sum);
    }

    /**
     * Gets the count of a specific coin type.
     * @param coin type of coin
     * @return number of coins available
     */
    public int getCoinCount(Coin coin) {
        return coinInventory.getOrDefault(coin, 0);
    }

    /**
     * Removes coins from the inventory for change.
     * @param coin type of coin
     * @param count number of coins to remove
     * @return true if successful
     */
    public boolean removeCoins(Coin coin, int count) {
        int available = coinInventory.getOrDefault(coin, 0);
        if (available >= count) {
            coinInventory.put(coin, available - count);
            return true;
        }
        return false;
    }

    /**
     * Calculates and returns change for the given amount.
     * Uses greedy algorithm with largest coins first.
     * @param amountInCents amount to return as change
     * @return list of coins as change, or null if exact change cannot be made
     */
    public List<Coin> calculateChange(int amountInCents) {
        if (amountInCents == 0) {
            return new ArrayList<>();
        }
        if (amountInCents < 0) {
            throw new IllegalArgumentException("Cannot calculate negative change");
        }

        List<Coin> change = new ArrayList<>();
        Map<Coin, Integer> tempInventory = new HashMap<>(coinInventory);
        int remaining = amountInCents;

        // Sort coins by value in descending order (largest first)
        List<Coin> sortedCoins = java.util.Arrays.stream(Coin.values())
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .collect(java.util.stream.Collectors.toList());
        
        for (Coin coin : sortedCoins) {
            int available = tempInventory.getOrDefault(coin, 0);
            while (remaining >= coin.getValue() && available > 0) {
                change.add(coin);
                remaining -= coin.getValue();
                available--;
                tempInventory.put(coin, available);
            }
        }

        if (remaining == 0) {
            // Update actual inventory by decrementing count for each coin used
            for (Coin coin : change) {
                int currentCount = coinInventory.get(coin);
                coinInventory.put(coin, currentCount - 1);
            }
            return change;
        }
        
        // Cannot provide exact change
        return null;
    }

    /**
     * Gets the total value of coins in inventory.
     * @return total value in cents
     */
    public int getTotalCoinValue() {
        int total = 0;
        for (Map.Entry<Coin, Integer> entry : coinInventory.entrySet()) {
            total += entry.getKey().getValue() * entry.getValue();
        }
        return total;
    }

    /**
     * Displays the current inventory status.
     */
    public void displayInventory() {
        System.out.println("\n========== INVENTORY STATUS ==========");
        System.out.println("Products:");
        for (ItemShelf shelf : productShelves.values()) {
            System.out.println("  " + shelf);
        }
        System.out.println("\nCoins:");
        for (Coin coin : Coin.values()) {
            System.out.printf("  %s: %d coins (%d cents each)%n", 
                coin.name(), coinInventory.get(coin), coin.getValue());
        }
        System.out.printf("Total coin value: $%.2f%n", getTotalCoinValue() / 100.0);
        System.out.println("======================================\n");
    }
}
