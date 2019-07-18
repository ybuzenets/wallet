package dev.buzenets.walletserver.repository;

import dev.buzenets.walletserver.entity.Wallet;
import dev.buzenets.walletserver.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findWalletByUserAndCurrency(int userId, Currency currency);

    Set<Wallet> findAllByUser(int user);
}
