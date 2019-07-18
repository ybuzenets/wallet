package dev.buzenets.walletserver.entity;

import dev.buzenets.walletserver.model.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"USER", "CURRENCY"})})
public class Wallet {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Getter
    @Setter
    //TODO: make another entity for user and use a foreign key.
    // Left out for the simplicity purpose
    private int user;

    @Version
    private int version;

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

        if (user != wallet.user) {
            return false;
        }
        if (!id.equals(wallet.id)) {
            return false;
        }
        return currency == wallet.currency;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + user;
        return result;
    }
}
