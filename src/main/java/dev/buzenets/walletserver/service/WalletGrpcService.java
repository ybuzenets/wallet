package dev.buzenets.walletserver.service;

import com.google.protobuf.Empty;
import dev.buzenets.walletserver.exception.InsufficientFundsException;
import dev.buzenets.walletserver.exception.UnknownCurrencyException;
import dev.buzenets.walletserver.grpc.BalanceSummary;
import dev.buzenets.walletserver.grpc.WalletGrpc;
import dev.buzenets.walletserver.grpc.WalletRequest;
import dev.buzenets.walletserver.model.WalletDTO;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

@GRpcService
public class WalletGrpcService extends WalletGrpc.WalletImplBase {
    private final ThreadSafeWalletService walletService;

    public WalletGrpcService(ThreadSafeWalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public void deposit(WalletRequest request, StreamObserver<Empty> responseObserver) {
        try {
            responseObserver.onNext(makeDeposit(request));
            responseObserver.onCompleted();
        } catch (UnknownCurrencyException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Unknown Currency")
                .asRuntimeException());
        }
    }


    @Override
    public void withdraw(WalletRequest request, StreamObserver<Empty> responseObserver) {
        try {
            responseObserver.onNext(makeWithdrawal(request));
            responseObserver.onCompleted();
        } catch (UnknownCurrencyException | InsufficientFundsException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage())
                .asRuntimeException());
        }
    }

    private Empty makeWithdrawal(WalletRequest request) {
        final long user = request.getUser();
        final String currency = request.getCurrency()
            .toString();
        final BigDecimal amount = new BigDecimal(request.getAmount());
        walletService.withdraw(user, currency, amount);

        return Empty.getDefaultInstance();
    }

    @Override
    public void balance(WalletRequest request, StreamObserver<BalanceSummary> responseObserver) {
        responseObserver.onNext(getBalance(request));
        responseObserver.onCompleted();
    }

    private BalanceSummary getBalance(WalletRequest request) {
        final Collection<WalletDTO> wallets = walletService.getBalance(request.getUser());

        return BalanceSummary.newBuilder()
            .putAllAmount(wallets.stream()
                .collect(Collectors.toMap(
                    wallet -> wallet.getCurrency()
                        .name(),
                    walletDTO -> walletDTO.getAmount()
                        .toPlainString()
                )))
            .build();

    }

    private Empty makeDeposit(
        WalletRequest request
    ) {
        final long user = request.getUser();
        final String currency = request.getCurrency()
            .toString();
        final BigDecimal amount = new BigDecimal(request.getAmount());
        walletService.deposit(user, currency, amount);

        return Empty.getDefaultInstance();
    }

}