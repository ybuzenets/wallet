package dev.buzenets.walletserver.entity;

import dev.buzenets.walletserver.model.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Wallet {
    @Id
    @Getter
    @Setter
    //could use a composite id on User/Currency pair but not entirely sure how to do it properly
    private Long id;

    @Getter
    @Setter
    private BigDecimal amount;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    @Getter
    @Setter
    private User user;

    @Override
    public String toString() {
        return "Wallet{" + "id=" + id + ", amount=" + amount + ", currency=" + currency + ", user="
            + user + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Wallet wallet = (Wallet) o;

        if (currency != wallet.currency) {
            return false;
        }
        return user.equals(wallet.user);
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
