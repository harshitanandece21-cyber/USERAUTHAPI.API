package com.vendingmachine.model;

/**
 * Represents a product available in the vending machine.
 * 
 * Follows Single Responsibility Principle - only contains product information.
 * Immutable design for thread safety and data integrity.
 */
public class Product {
    private final String id;
    private final String name;
    private final int priceInCents;

    public Product(String id, String name, int priceInCents) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (priceInCents < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    /**
     * Gets the price formatted as dollars and cents.
     * @return formatted price string (e.g., "$1.50")
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", priceInCents / 100.0);
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=%s}", id, name, getFormattedPrice());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
