package dev.buzenets.walletserver.model;

import dev.buzenets.walletserver.entity.Wallet;
import lombok.Getter;

import java.math.BigDecimal;

public class WalletDTO {

    @Getter private final Long id;
    @Getter private final int user;
    @Getter private final Currency currency;
    @Getter private final BigDecimal amount;

    public WalletDTO(Wallet wallet) {
        id = wallet.getId();
        user = wallet.getUser();
        currency = wallet.getCurrency();
        amount = wallet.getAmount();
    }

    public WalletDTO(int user, Currency currency) {
        id = null;
        this.user = user;
        this.currency = currency;
        this.amount = BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final WalletDTO walletDTO = (WalletDTO) o;

        if (user != walletDTO.user) {
            return false;
        }
        return currency == walletDTO.currency;
    }

    @Override
    public int hashCode() {
        int result = user;
        result = 31 * result + currency.hashCode();
        return result;
    }
}
