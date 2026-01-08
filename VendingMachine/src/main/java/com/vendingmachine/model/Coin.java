package com.vendingmachine.model;

/**
 * Enum representing different coin denominations accepted by the vending machine.
 * 
 * Follows Single Responsibility Principle - only handles coin denomination values.
 */
public enum Coin {
    PENNY(1),
    NICKEL(5),
    DIME(10),
    QUARTER(25),
    DOLLAR(100);

    private final int value;

    Coin(int value) {
        this.value = value;
    }

    /**
     * Gets the value of the coin in cents.
     * @return the coin value in cents
     */
    public int getValue() {
        return value;
    }
}
