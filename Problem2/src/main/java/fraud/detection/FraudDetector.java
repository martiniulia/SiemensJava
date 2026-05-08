package fraud.detection;

import fraud.graph.TarjanSCC;
import fraud.graph.TransferGraph;
import fraud.model.FraudRing;
import fraud.model.RiskLevel;
import fraud.model.Transfer;
import fraud.scoring.RiskScorer;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class FraudDetector {

    private static final BigDecimal DEFAULT_MIN_AMOUNT = new BigDecimal("1000.00");

    private final RiskScorer riskScorer;
    private final BigDecimal minimumRingAmount;
    private final Duration maxWindow;

    public FraudDetector() {
        this(DEFAULT_MIN_AMOUNT, null);
    }

    public FraudDetector(BigDecimal minimumRingAmount) {
        this(minimumRingAmount, null);
    }

    public FraudDetector(BigDecimal minimumRingAmount, Duration maxWindow) {
        this.riskScorer = new RiskScorer();
        this.minimumRingAmount = minimumRingAmount;
        this.maxWindow = maxWindow;
    }

    public List<FraudRing> detect(List<Transfer> transfers) {
        if (transfers == null || transfers.isEmpty()) {
            return Collections.emptyList();
        }

        TransferGraph graph = new TransferGraph(transfers);
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();

        List<FraudRing> rings = new ArrayList<>();

        for (List<Integer> scc : sccs) {
            if (scc.size() < 2) continue;

            Set<String> ibanSet = scc.stream()
                .map(graph::ibanOf)
                .collect(Collectors.toSet());

            List<Transfer> ringTransfers = transfers.stream()
                .filter(t -> ibanSet.contains(t.getSourceIban())
                          && ibanSet.contains(t.getTargetIban()))
                .collect(Collectors.toList());

            if (ringTransfers.isEmpty()) continue;

            Instant earliest = ringTransfers.stream().map(Transfer::getTimestamp).min(Instant::compareTo).get();
            Instant latest = ringTransfers.stream().map(Transfer::getTimestamp).max(Instant::compareTo).get();
            Duration duration = Duration.between(earliest, latest);

            if (maxWindow != null && duration.compareTo(maxWindow) > 0) {
                continue;
            }

            BigDecimal totalAmount = ringTransfers.stream()
                .map(Transfer::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalAmount.compareTo(minimumRingAmount) < 0) continue;

            RiskLevel risk = riskScorer.score(scc.size(), totalAmount);
            rings.add(new FraudRing(ibanSet, ringTransfers, totalAmount, risk, duration));
        }

        rings.sort(Comparator.comparing(FraudRing::getTotalAmount).reversed());
        return Collections.unmodifiableList(rings);
    }
}
