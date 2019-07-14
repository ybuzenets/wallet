package dev.buzenets.walletserver.exception;

public class InsufficientFundsException extends IllegalArgumentException {
    public InsufficientFundsException() {
        super("Insufficient funds");
    }
}
