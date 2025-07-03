package org.example.infrastructure.adapter.output.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, unique = true)
    private String id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private BigDecimal amount;
    private double interestRate;
    private int durationInMonths;
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private String status;
}
