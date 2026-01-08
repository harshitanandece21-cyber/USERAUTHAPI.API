package com.vendingmachine.exception;

/**
 * Exception thrown when an invalid operation is performed on the vending machine.
 */
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String operation, String currentState) {
        super(String.format("Cannot perform '%s' in current state: %s", operation, currentState));
    }
}
