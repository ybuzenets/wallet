package dev.buzenets.walletserver.service;

import dev.buzenets.walletserver.entity.Wallet;
import dev.buzenets.walletserver.exception.InsufficientFundsException;
import dev.buzenets.walletserver.exception.UnknownCurrencyException;
import dev.buzenets.walletserver.model.Currency;
import dev.buzenets.walletserver.model.WalletDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ThreadSafeWalletService {

    private final Map<WalletDTO, Object> locker = new ConcurrentHashMap<>();
    private final WalletService walletService;

    public ThreadSafeWalletService(WalletService walletService) {
        this.walletService = walletService;
    }

    public void deposit(long userId, String curr, BigDecimal amount) {
        try {
            final Currency currency = Currency.valueOf(curr);
            //instead of explicit locking, use ConcurrentHashMap internal synchronization
            locker.computeIfAbsent(new WalletDTO(userId, currency), walletDTO -> {
                walletService.deposit(userId, curr, amount);
                return null;
            });
        } catch (IllegalArgumentException e) {
            throw new UnknownCurrencyException(curr, e);
        }
    }

    public void withdraw(long userId, String curr, BigDecimal amount) {
        try {
            final Currency currency = Currency.valueOf(curr);
            locker.computeIfAbsent(new WalletDTO(userId, currency), walletDTO -> {
                walletService.withdraw(userId, curr, amount);
                return null;
            });
        } catch (InsufficientFundsException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new UnknownCurrencyException(curr, e);
        }
    }

    public Collection<WalletDTO> getBalance(long userId) {

        final Set<Wallet> existingWallets = walletService.getBalance(userId);
        final Set<WalletDTO> result = existingWallets.stream()
            .map(WalletDTO::new)
            .collect(Collectors.toSet());
        for (Currency currency : Currency.values()) {
            result.add(new WalletDTO(userId, currency));
        }
        return result;
    }
}
