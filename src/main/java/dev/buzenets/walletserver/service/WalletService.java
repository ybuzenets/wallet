package dev.buzenets.walletserver.service;

import dev.buzenets.walletserver.entity.User;
import dev.buzenets.walletserver.entity.Wallet;
import dev.buzenets.walletserver.exception.InsufficientFundsException;
import dev.buzenets.walletserver.exception.UnknownCurrencyException;
import dev.buzenets.walletserver.model.Currency;
import dev.buzenets.walletserver.repository.UserRepository;
import dev.buzenets.walletserver.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;


@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Retryable(value = {DataIntegrityViolationException.class, CannotAcquireLockException.class})
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deposit(Long userId, String curr, BigDecimal amount) {
        final Wallet wallet = getWallet(userId, curr);

        final BigDecimal total = wallet.getAmount()
            .add(amount);
        wallet.setAmount(total);
        walletRepository.save(wallet);
    }

    @Retryable(value = {DataIntegrityViolationException.class, CannotAcquireLockException.class})
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void withdraw(Long userId, String curr, BigDecimal amount) {
        final Wallet wallet = getWallet(userId, curr);

        final BigDecimal total = wallet.getAmount()
            .subtract(amount);
        if (total.compareTo(BigDecimal.ZERO) >= 0) {
            wallet.setAmount(total);
            walletRepository.save(wallet);
        } else {
            throw new InsufficientFundsException();
        }
    }

    public Set<Wallet> getBalance(Long userId) {
        return userRepository.findById(userId)
            .map(User::getWallets)
            .orElse(Collections.emptySet());
    }

    @Retryable(DataIntegrityViolationException.class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Wallet getWallet(Long userId, String curr) {
        try {
            final Currency currency = Currency.valueOf(curr);
            return walletRepository.findWalletByUser_IdAndCurrency(userId, currency)
                .orElseGet(() -> {
                    final Wallet tmp = new Wallet();
                    tmp.setUser(userRepository.getOne(userId));
                    tmp.setCurrency(currency);
                    return walletRepository.save(tmp);
                });
        } catch (IllegalArgumentException e) {
            throw new UnknownCurrencyException(curr, e);
        }
    }
}
