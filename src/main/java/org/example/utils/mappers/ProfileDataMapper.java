package org.example.utils.mappers;


import org.example.domain.model.ProfileData;
import org.example.infrastructure.adapter.output.entity.ProfileDataEntity;

public class ProfileDataMapper {

    public static ProfileData toDomain(ProfileDataEntity entity) {
        ProfileData profile = new ProfileData();
        profile.setId(entity.getId());
        profile.setUserId(entity.getUserId());
        profile.setMonthlyWalletInflow(entity.getMonthlyWalletInflow().getMonthlySalary()
                .add(entity.getMonthlyWalletInflow().getBusinessRevenue())
                .add(entity.getMonthlyWalletInflow().getRemittances()));
        profile.setAirtimeTopUpsPerMonth(entity.getAirtimeTopUpsPerMonth());
        profile.setUtilityPaymentScore(entity.getUtilityPaymentScore());
        profile.setFinTechActivityScore(entity.getFinTechActivityScore());
        profile.setTrainingProgramScore(entity.getTrainingProgramScore());
        profile.setTotalScore(entity.getTotalScore());
        return profile;
    }

    public static ProfileDataEntity toEntity(ProfileData domain) {
        ProfileDataEntity entity = new ProfileDataEntity();
        entity.setUserId(domain.getUserId());
        entity.setAirtimeTopUpsPerMonth(domain.getAirtimeTopUpsPerMonth());
        entity.setUtilityPaymentScore(domain.getUtilityPaymentScore());
        entity.setFinTechActivityScore(domain.getFinTechActivityScore());
        entity.setTrainingProgramScore(domain.getTrainingProgramScore());
        entity.setTotalScore(domain.getTotalScore());
        // Wallet inflow set externally (already done via DTO request)
        return entity;
    }
}