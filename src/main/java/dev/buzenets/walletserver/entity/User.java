package dev.buzenets.walletserver.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @OneToMany
    private Set<Wallet> wallets;

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
