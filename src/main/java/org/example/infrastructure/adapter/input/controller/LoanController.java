package org.example.infrastructure.adapter.input.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.port.input.CreateNewLoanUseCase;
import org.example.domain.service.LoanUseCaseApplicationService;
import org.example.infrastructure.adapter.input.dto.requests.LoanRequestDTO;
import org.example.infrastructure.adapter.input.dto.responses.LoanApplicationResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.LoanStatusResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/loan/v1")
@RequiredArgsConstructor
public class LoanController {

    private final CreateNewLoanUseCase createNewLoanUseCase;
    private final LoanUseCaseApplicationService loanService;

    @PostMapping("/apply")
    public ResponseEntity<LoanApplicationResponseDto> applyForLoan(
            @RequestBody LoanRequestDTO loanRequest,
            Principal principal) {
        log.info("Loan application received for user: {}", principal != null ? principal.getName() : "anonymous");
        
        LoanApplicationResponseDto response = createNewLoanUseCase.createLoan(loanRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{loanId}")
    public ResponseEntity<LoanStatusResponseDto> getLoanStatus(@PathVariable String loanId) {
        log.info("Fetching loan status for loan ID: {}", loanId);
        
        LoanStatusResponseDto response = loanService.getLoanStatus(loanId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserLoans(@PathVariable String userId) {
        log.info("Fetching loans for user: {}", userId);
        
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }

    @PostMapping("/{loanId}/repay")
    public ResponseEntity<?> makeRepayment(
            @PathVariable String loanId,
            @RequestParam Double amount) {
        log.info("Processing repayment for loan: {} amount: {}", loanId, amount);
        
        return ResponseEntity.ok(loanService.processRepayment(loanId, amount));
    }
}