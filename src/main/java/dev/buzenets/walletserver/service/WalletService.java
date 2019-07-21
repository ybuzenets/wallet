package dev.buzenets.walletserver.service;

import dev.buzenets.walletserver.entity.Wallet;
import dev.buzenets.walletserver.exception.InsufficientFundsException;
import dev.buzenets.walletserver.model.Currency;
import dev.buzenets.walletserver.repository.WalletRepository;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;
import java.util.Set;


@Service
@NotThreadSafe
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void deposit(long userId, String curr, BigDecimal amount) {
        final Wallet wallet = walletRepository.findWalletByUserAndCurrency(userId,
            Currency.valueOf(curr)
        )
            .orElseGet(() -> {
                final Wallet w = new Wallet();
                w.setCurrency(Currency.valueOf(curr));
                w.setUser(userId);
                return w;
            });
        wallet.setAmount(wallet.getAmount()
            .add(amount));
        walletRepository.saveAndFlush(wallet);
    }

    public void withdraw(long userId, String curr, BigDecimal amount) throws InsufficientFundsException {
        final Wallet wallet = walletRepository.findWalletByUserAndCurrency(userId,
            Currency.valueOf(curr)
        )
            .orElseGet(() -> {
                final Wallet w = new Wallet();
                w.setCurrency(Currency.valueOf(curr));
                w.setUser(userId);
                return w;
            });
        final BigDecimal result = wallet.getAmount()
            .subtract(amount);
        if (result.compareTo(BigDecimal.ZERO) >= 0) {
            wallet.setAmount(result);
            walletRepository.saveAndFlush(wallet);
        } else {
            throw new InsufficientFundsException();
        }
    }

    public Set<Wallet> getBalance(long userId) {
        return walletRepository.findAllByUser(userId);
    }
}
