package dev.buzenets.walletserver.entity;

import dev.buzenets.walletserver.model.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

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
    private long user;

    @Version private int version;

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

        return Objects.equals(id, wallet.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
