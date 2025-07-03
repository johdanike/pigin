package org.example.domain.service;

import org.example.domain.model.Transaction;
import org.example.application.port.output.WalletRepositoryUseCase;
import org.example.application.port.output.TransactionRepositoryUseCase;
import org.example.domain.model.Wallet;
import org.example.domain.model.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepositoryUseCase walletRepository;

    @Autowired
    private TransactionRepositoryUseCase transactionRepository;

    public Wallet createWallet(Long userId) {
        Wallet wallet = new Wallet(userId);
        return walletRepository.save(wallet);
    }

    public Optional<Wallet> getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    @Transactional
    public void disburseLoan(Long userId, BigDecimal amount, String loanId) {
        Optional<Wallet> walletOpt = walletRepository.findByUserId(userId);
        Wallet wallet;

        if (walletOpt.isPresent()) {
            wallet = walletOpt.get();
        } else {
            wallet = createWallet(userId);
        }

        wallet.credit(amount);
        walletRepository.save(wallet);

        // Record transaction
        Transaction transaction = new Transaction(userId, TransactionType.CREDIT,
                amount, "Loan disbursement");
        transaction.setLoanId(Long.valueOf(loanId));
        transactionRepository.save(transaction);
    }

    @Transactional
    public boolean deductRepayment(Long userId, BigDecimal amount, Long loanId) {
        Optional<Wallet> walletOpt = walletRepository.findByUserId(userId);
        if (walletOpt.isPresent()) {
            Wallet wallet = walletOpt.get();
            if (wallet.debit(amount)) {
                walletRepository.save(wallet);

                // Record transaction
                Transaction transaction = new Transaction(userId, TransactionType.DEBIT,
                        amount, "Loan repayment");
                transaction.setLoanId(loanId);
                transactionRepository.save(transaction);
                return true;
            }
        }
        return false;
    }
}
