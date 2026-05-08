package fraud.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class Transfer {
    private final String id;
    private final String sourceIban;
    private final String targetIban;
    private final BigDecimal amount;
    private final Instant timestamp;

    public Transfer(String sourceIban, String targetIban, BigDecimal amount, Instant timestamp) {
        this.id = UUID.randomUUID().toString();
        this.sourceIban = sourceIban;
        this.targetIban = targetIban;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getSourceIban() { return sourceIban; }
    public String getTargetIban() { return targetIban; }
    public BigDecimal getAmount() { return amount; }
    public Instant getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Transfer{%s -> %s, amount=%.2f, time=%s}",
            sourceIban, targetIban, amount, timestamp);
    }
}
