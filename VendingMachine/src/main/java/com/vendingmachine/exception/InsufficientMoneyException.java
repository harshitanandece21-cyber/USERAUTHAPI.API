package com.vendingmachine.exception;

/**
 * Exception thrown when there is insufficient money for a purchase.
 */
public class InsufficientMoneyException extends RuntimeException {
    private final int required;
    private final int provided;

    public InsufficientMoneyException(int required, int provided) {
        super(String.format("Insufficient money. Required: %d cents, Provided: %d cents", required, provided));
        this.required = required;
        this.provided = provided;
    }

    public int getRequired() {
        return required;
    }

    public int getProvided() {
        return provided;
    }

    public int getShortage() {
        return required - provided;
    }
}
