package fraud.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public final class FraudRing {
    private final Set<String> accounts;
    private final List<Transfer> transfers;
    private final BigDecimal totalAmount;
    private final RiskLevel riskLevel;
    private final Duration duration;

    public FraudRing(Set<String> accounts, List<Transfer> transfers,
                     BigDecimal totalAmount, RiskLevel riskLevel, Duration duration) {
        this.accounts = accounts;
        this.transfers = transfers;
        this.totalAmount = totalAmount;
        this.riskLevel = riskLevel;
        this.duration = duration;
    }

    public Set<String> getAccounts() { return accounts; }
    public List<Transfer> getTransfers() { return transfers; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public Duration getDuration() { return duration; }

    @Override
    public String toString() {
        return String.format(
            "FraudRing{accounts=%d, transfers=%d, totalAmount=%.2f, risk=%s, duration=%sh, ibans=%s}",
            accounts.size(), transfers.size(), totalAmount, riskLevel, 
            duration.toHours(), accounts
        );
    }
}
