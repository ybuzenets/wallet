package dev.buzenets.walletserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;

@SpringBootApplication
@EnableRetry
public class WalletServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletServerApplication.class, args);
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }
}
