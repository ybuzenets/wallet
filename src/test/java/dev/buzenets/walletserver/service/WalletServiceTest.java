package dev.buzenets.walletserver.service;

import dev.buzenets.walletserver.entity.Wallet;
import dev.buzenets.walletserver.model.Currency;
import dev.buzenets.walletserver.model.WalletDTO;
import dev.buzenets.walletserver.repository.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class WalletServiceTest {

    @Autowired ThreadSafeWalletService walletService;

    @Autowired WalletRepository walletRepository;


    @AfterEach
    void tearDown() {
        walletRepository.deleteAll();
    }

    @Test
    void shouldAddAllDeposits() {
        Runnable task = () -> walletService.deposit(1, Currency.EUR.name(), BigDecimal.TEN);

        Stream.generate(() -> task)
            .limit(10)
            .parallel()
            .forEach(Runnable::run);
        final BigDecimal amount = walletRepository.findWalletByUserAndCurrency(1, Currency.EUR)
            .map(Wallet::getAmount)
            .orElseThrow();
        assertEquals(100, amount.intValue());
    }

    @Test
    void shouldWithdrawConcurrently() {
        walletService.deposit(1, Currency.EUR.name(), BigDecimal.TEN);
        Runnable task = () -> walletService.withdraw(1, Currency.EUR.name(), BigDecimal.ONE);

        Stream.generate(() -> task)
            .limit(10)
            .parallel()
            .forEach(Runnable::run);
        final BigDecimal amount = walletRepository.findWalletByUserAndCurrency(1, Currency.EUR)
            .map(Wallet::getAmount)
            .orElseThrow();
        assertEquals(0, amount.intValue());
    }

    @Test
    void shouldThrowExceptionOnUnknownCurrency() {
        assertThrows(IllegalArgumentException.class,
            () -> walletService.deposit(1, "unknown", BigDecimal.TEN)
        );
    }

    @Test
    void shouldThrowExceptionOnWithdrawingFromEmptyWallet() {
        assertThrows(IllegalArgumentException.class,
            () -> walletService.withdraw(1, Currency.EUR.name(), BigDecimal.TEN)
        );
    }

    @Test
    void shouldNotChangeBalanceIfEqualAmountOfWithdrawAndDeposit() {
        walletService.deposit(1, Currency.EUR.name(), BigDecimal.TEN);
        Runnable withdrawTask = () -> walletService.withdraw(1,
            Currency.EUR.name(),
            BigDecimal.ONE
        );
        Runnable depositTask = () -> walletService.deposit(1, Currency.EUR.name(), BigDecimal.ONE);
        Stream.concat(
            Stream.generate(() -> withdrawTask)
                .limit(5),
            Stream.generate(() -> depositTask)
                .limit(5)
        )
            .parallel()
            .forEach(Runnable::run);
        final BigDecimal amount = walletRepository.findWalletByUserAndCurrency(1, Currency.EUR)
            .map(Wallet::getAmount)
            .orElseThrow();
        assertEquals(10, amount.intValue());
    }

    @Test
    void shouldReturnEmptyWallets() {
        final Collection<WalletDTO> balance = walletService.getBalance(1);
        assertEquals(balance.size(), 3);
        assertTrue(balance.stream()
            .allMatch(walletDTO -> walletDTO.getAmount()
                .equals(BigDecimal.ZERO)));
    }
}