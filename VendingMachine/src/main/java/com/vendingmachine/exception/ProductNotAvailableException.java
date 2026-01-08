package com.vendingmachine.exception;

/**
 * Exception thrown when a requested product is not available in the vending machine.
 */
public class ProductNotAvailableException extends RuntimeException {
    
    public ProductNotAvailableException(String message) {
        super(message);
    }

    public ProductNotAvailableException(String shelfCode, String productName) {
        super(String.format("Product '%s' at shelf '%s' is not available", productName, shelfCode));
    }
}
