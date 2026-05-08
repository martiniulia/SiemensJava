package fraud.scoring;

import fraud.model.RiskLevel;

import java.math.BigDecimal;

public class RiskScorer {

    private static final BigDecimal THRESHOLD_MEDIUM = new BigDecimal("10000.00");
    private static final BigDecimal THRESHOLD_CRITICAL = new BigDecimal("50000.00");

    public RiskLevel score(int ringSize, BigDecimal totalAmount) {
        int amountTier = amountTier(totalAmount);
        int sizeTier = sizeTier(ringSize);

        int combined = sizeTier * 3 + amountTier;
        return switch (combined) {
            case 0 -> RiskLevel.LOW;
            case 1 -> RiskLevel.MEDIUM;
            case 2 -> RiskLevel.HIGH;
            case 3 -> RiskLevel.MEDIUM;
            case 4 -> RiskLevel.HIGH;
            case 5 -> RiskLevel.CRITICAL;
            case 6 -> RiskLevel.HIGH;
            case 7 -> RiskLevel.CRITICAL;
            case 8 -> RiskLevel.CRITICAL;
            default -> RiskLevel.LOW;
        };
    }

    private int amountTier(BigDecimal amount) {
        if (amount.compareTo(THRESHOLD_MEDIUM) < 0) return 0;
        if (amount.compareTo(THRESHOLD_CRITICAL) < 0) return 1;
        return 2;
    }

    private int sizeTier(int size) {
        if (size <= 3) return 0;
        if (size <= 5) return 1;
        return 2;
    }
}
