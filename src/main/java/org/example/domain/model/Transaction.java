package org.example.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;
    private  TransactionType transactionType;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String description;

    private Long loanId;

    // Default constructor
    public Transaction() {}

    // Constructor with parameters
    public Transaction(Long userId, TransactionType type, BigDecimal amount, String description) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }



}

