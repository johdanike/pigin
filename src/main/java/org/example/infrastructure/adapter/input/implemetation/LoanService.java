package org.example.infrastructure.adapter.input.implemetation;

import org.example.application.port.output.LoanRepositoryUseCase;
import org.example.application.port.output.ProfileDataRepositoryUseCase;
import org.example.domain.model.Loan;
import org.example.domain.model.ProfileData;
import org.example.domain.model.enums.LoanStatus;
import org.example.domain.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepositoryUseCase loanRepository;

    @Autowired
    private ProfileDataRepositoryUseCase profileDataRepository;

    @Autowired
    private CreditScoringServiceImpl creditScoringService;
//    private CreditScoringServiceImpl creditScoringServiceImpl;

    @Autowired
    private WalletService walletService;

    public Loan applyForLoan(Long userId, BigDecimal amount, int durationWeeks) {
        Optional<ProfileData> profileDataOpt = profileDataRepository.findByUserId(userId);
        if (profileDataOpt.isEmpty()) {
            throw new IllegalStateException("User profile data not found. Please submit profile first.");
        }

        List<Loan> existingLoans = loanRepository.findByUserId(String.valueOf(userId));
        boolean hasActiveLoan = existingLoans.stream()
                .anyMatch(loan -> loan.getStatus() == LoanStatus.APPROVED ||
                        loan.getStatus() == LoanStatus.PENDING);

        if (hasActiveLoan) {
            throw new IllegalStateException("User already has an active loan application");
        }

        LocalDateTime dueDate = LocalDateTime.now().plusWeeks(durationWeeks);
        Loan loan = new Loan(String.valueOf(userId), amount, 0.15, durationWeeks * 4); // Convert weeks to months approximately

        ProfileData profileData = profileDataOpt.get();
        int creditScore = creditScoringService.calculateCreditScore(profileData);
        loan.setCreditScore(creditScore);

        if (creditScore >= 700) {
            loan.setStatus(LoanStatus.APPROVED);
            walletService.disburseLoan(userId, amount, loan.getId());
        } else if (creditScore >= 600) {
            loan.setStatus(LoanStatus.PENDING);
        } else {
            loan.setStatus(LoanStatus.REJECTED);
        }

        return loanRepository.save(loan);
    }

    public Optional<Loan> getLoanById(Long loanId) {
        return loanRepository.findById(String.valueOf(loanId));
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getDefaultedLoans() {
        return loanRepository.findByStatus(LoanStatus.DEFAULTED);
    }

    public void processRepayment(Long loanId, BigDecimal amount) {
        Optional<Loan> loanOpt = loanRepository.findById(String.valueOf(loanId));
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            if (loan.getStatus() == LoanStatus.APPROVED) {
                BigDecimal newRemainingAmount = loan.getRemainingAmount().subtract(amount);
                loan.setRemainingAmount(newRemainingAmount);

                if (newRemainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    loan.setStatus(LoanStatus.PAID);
                    loan.setRemainingAmount(BigDecimal.ZERO);
                }

                loanRepository.save(loan);
            }
        }
    }

    public void markLoanAsDefaulted(Long loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(String.valueOf(loanId));
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.setStatus(LoanStatus.DEFAULTED);
            loanRepository.save(loan);
        }
    }
}
