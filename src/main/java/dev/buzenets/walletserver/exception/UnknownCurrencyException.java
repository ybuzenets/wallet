package dev.buzenets.walletserver.exception;

public class UnknownCurrencyException extends IllegalArgumentException {
    public UnknownCurrencyException(String currency, Throwable cause) {
        super("Unknown currency: " +currency, cause);
    }
}
