package com.vendingmachine.exception;

/**
 * Exception thrown when the vending machine cannot provide exact change.
 */
public class InsufficientChangeException extends RuntimeException {

    public InsufficientChangeException(String message) {
        super(message);
    }

    public InsufficientChangeException(int changeRequired) {
        super(String.format("Cannot provide exact change of %d cents", changeRequired));
    }
}
