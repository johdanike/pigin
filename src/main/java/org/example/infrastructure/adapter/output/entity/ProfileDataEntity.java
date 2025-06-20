package org.example.infrastructure.adapter.output.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.infrastructure.adapter.input.dto.requests.WalletInflow;

@Setter
@Getter
@Entity
@ToString
@EqualsAndHashCode
public class ProfileDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, unique = true)
    private String id;

    @Version
    private Long version;

    @Column(nullable = false)
    private String userId;

    @Embedded
    private WalletInflow monthlyWalletInflow;
    private int airtimeTopUpsPerMonth;
    private int utilityPaymentScore;
    private int finTechActivityScore;
    private int trainingProgramScore;
    private int totalScore;
}
