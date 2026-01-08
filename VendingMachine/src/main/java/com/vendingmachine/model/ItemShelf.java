package com.vendingmachine.model;

/**
 * Represents a shelf/slot in the vending machine that holds products.
 * 
 * Follows Single Responsibility Principle - manages a single shelf's product and quantity.
 */
public class ItemShelf {
    private final String code;
    private Product product;
    private int quantity;

    public ItemShelf(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Shelf code cannot be null or empty");
        }
        this.code = code;
        this.quantity = 0;
    }

    public String getCode() {
        return code;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    /**
     * Checks if the shelf has products available.
     * @return true if quantity > 0, false otherwise
     */
    public boolean isAvailable() {
        return product != null && quantity > 0;
    }

    /**
     * Dispenses one product from the shelf.
     * @return the dispensed product
     * @throws IllegalStateException if no products available
     */
    public Product dispense() {
        if (!isAvailable()) {
            throw new IllegalStateException("No products available on shelf " + code);
        }
        quantity--;
        return product;
    }

    /**
     * Adds products to the shelf.
     * @param amount number of products to add
     */
    public void addStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative amount");
        }
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return String.format("Shelf[%s]: %s (Qty: %d)", 
            code, 
            product != null ? product.getName() : "Empty", 
            quantity);
    }
}
