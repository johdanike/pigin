package org.example.infrastructure.adapter.input.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.service.LoanUseCaseApplicationService;
import org.example.domain.service.UserService;
import org.example.infrastructure.adapter.input.dto.responses.AdminLoanSummaryDto;
import org.example.infrastructure.adapter.input.dto.responses.DefaultedLoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/v1")
@RequiredArgsConstructor
public class AdminController {

    private final LoanUseCaseApplicationService loanService;
    private final UserService userService;

    @GetMapping("/loans")
    public ResponseEntity<List<AdminLoanSummaryDto>> getAllLoans(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Admin fetching loans with status: {}, page: {}, size: {}", status, page, size);
        
        List<AdminLoanSummaryDto> loans = loanService.getAllLoansForAdmin(status, page, size);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/defaults")
    public ResponseEntity<List<DefaultedLoanDto>> getDefaultedLoans() {
        log.info("Admin fetching defaulted loans");
        
        List<DefaultedLoanDto> defaultedLoans = loanService.getDefaultedLoans();
        return ResponseEntity.ok(defaultedLoans);
    }

    @GetMapping("/users/{userId}/loans")
    public ResponseEntity<?> getUserLoanHistory(@PathVariable String userId) {
        log.info("Admin fetching loan history for user: {}", userId);
        
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }

    @PutMapping("/loans/{loanId}/status")
    public ResponseEntity<?> updateLoanStatus(
            @PathVariable String loanId,
            @RequestParam String status,
            @RequestParam(required = false) String reason) {
        log.info("Admin updating loan {} status to: {} with reason: {}", loanId, status, reason);
        
        return ResponseEntity.ok(loanService.updateLoanStatus(loanId, status, reason));
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<?> getDashboardSummary() {
        log.info("Admin fetching dashboard summary");
        
        return ResponseEntity.ok(loanService.getAdminDashboardSummary());
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Admin fetching all users, page: {}, size: {}", page, size);
        
        return ResponseEntity.ok(userService.getAllUsersForAdmin(page, size));
    }
}