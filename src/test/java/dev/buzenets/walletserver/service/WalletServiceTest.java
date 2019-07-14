package dev.buzenets.walletserver.service;

import dev.buzenets.walletserver.entity.User;
import dev.buzenets.walletserver.entity.Wallet;
import dev.buzenets.walletserver.model.Currency;
import dev.buzenets.walletserver.repository.UserRepository;
import dev.buzenets.walletserver.repository.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
class WalletServiceTest {

    @Autowired WalletService walletService;

    @Autowired WalletRepository walletRepository;

    @Autowired UserRepository userRepository;

    @AfterEach
    void tearDown() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveUserInDatabase() {
        final User user = new User();
        user.setName("name");
        final User saved = userRepository.save(user);
        assertNotNull(saved);
    }

    @Test
    void shouldSaveWalletInDatabase() {
        final User user = new User();
        user.setName("name");
        userRepository.save(user);
        final Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setCurrency(Currency.EUR);
        final Wallet saved = walletRepository.save(wallet);
        assertNotNull(saved);
    }

    @Test
    void shouldGetWalletByUserIdAndCurrenct() {
        final User user = new User();
        user.setName("name");
        final User savedUser = userRepository.save(user);
        final Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setCurrency(Currency.EUR);
        final Wallet saved = walletRepository.save(wallet);

        final Wallet actual = walletService.getWallet(savedUser.getId(), Currency.EUR.name());
        assertEquals(saved, actual);
    }

    @Test
    void shouldCreateNewWalletIfNoWalletFound() {
        final User user = new User();
        user.setName("name");
        final User savedUser = userRepository.save(user);
        final Wallet wallet = walletService.getWallet(savedUser.getId(), Currency.EUR.name());
        assertNotNull(wallet);
    }

    @Test
    void shouldNotCreateDuplicateWallets() {
        final User user = new User();
        user.setName("name");
        final User savedUser = userRepository.save(user);
        Runnable task = () -> walletService.getWallet(savedUser.getId(), Currency.EUR.name());
        Stream.generate(() -> task)
            .limit(10)
            .parallel()
            .forEach(Runnable::run);
        final int size = walletRepository.findAll()
            .size();
        assertEquals(1, size);
    }

    @Test
    void shouldAddAllDeposits() {
        final User user = new User();
        user.setName("name");
        final User savedUser = userRepository.save(user);
        Runnable task = () -> walletService.deposit(
            savedUser.getId(),
            Currency.EUR.name(),
            BigDecimal.TEN
        );

        Stream.generate(() -> task)
            .limit(10)
            .parallel()
            .forEach(Runnable::run);
        final BigDecimal amount = walletService.getWallet(savedUser.getId(), Currency.EUR.name())
            .getAmount();
        assertEquals(100, amount.intValue());
    }

    @Test
    void shouldWithdrawConcurrently() {
        final User user = new User();
        user.setName("name");
        final User savedUser = userRepository.save(user);
        walletService.deposit(savedUser.getId(), Currency.EUR.name(), BigDecimal.TEN);
        Runnable task = () -> walletService.withdraw(savedUser.getId(),
            Currency.EUR.name(),
            BigDecimal.ONE
        );

        Stream.generate(() -> task)
            .limit(10)
            .parallel()
            .forEach(Runnable::run);
        final BigDecimal amount = walletService.getWallet(savedUser.getId(), Currency.EUR.name())
            .getAmount();
        assertEquals(0, amount.intValue());
    }

    @Test
    void shouldThrowExceptionOnUnknownCurrency() {
        final User user = new User();
        user.setName("name");
        final User savedUser = userRepository.save(user);
        assertThrows(IllegalArgumentException.class,
            () -> walletService.deposit(savedUser.getId(), "unknown", BigDecimal.TEN)
        );
    }

    @Test
    void shouldThrowExceptionOnWithdrawingFromEmptyWallet() {
        final User user = new User();
        user.setName("name");
        final User savedUser = userRepository.save(user);
        assertThrows(IllegalArgumentException.class,
            () -> walletService.withdraw(savedUser.getId(), Currency.EUR.name(), BigDecimal.TEN)
        );
    }
}