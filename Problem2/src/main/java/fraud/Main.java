package fraud;

import fraud.detection.FraudDetector;
import fraud.model.FraudRing;
import fraud.model.Transfer;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Fraud Ring Detection Demo ===\n");

        scenario1_simpleTriangle();
        scenario2_nestedRings();
        scenario3_noFraud();
        scenario4_largeCriticalRing();
        scenario5_falsePositiveFiltered();
        scenario6_temporalRing_detected();
        scenario7_temporalRing_filtered();
    }

    static void scenario1_simpleTriangle() {
        System.out.println("--- Scenario 1: Simple Triangle ---");
        Instant now = Instant.now();
        List<Transfer> transfers = List.of(
            new Transfer("RO11AAAA0000000000000001", "RO22BBBB0000000000000002", new BigDecimal("5000.00"), now),
            new Transfer("RO22BBBB0000000000000002", "RO33CCCC0000000000000003", new BigDecimal("4800.00"), now.plusSeconds(60)),
            new Transfer("RO33CCCC0000000000000003", "RO11AAAA0000000000000001", new BigDecimal("4500.00"), now.plusSeconds(120))
        );
        printResults(new FraudDetector().detect(transfers));
    }

    static void scenario2_nestedRings() {
        System.out.println("--- Scenario 2: Nested Rings ---");
        Instant now = Instant.now();
        List<Transfer> transfers = new ArrayList<>(List.of(
            new Transfer("RO11AAAA0000000000000001", "RO22BBBB0000000000000002", new BigDecimal("8000.00"), now),
            new Transfer("RO22BBBB0000000000000002", "RO33CCCC0000000000000003", new BigDecimal("7500.00"), now.plusSeconds(30)),
            new Transfer("RO33CCCC0000000000000003", "RO11AAAA0000000000000001", new BigDecimal("7000.00"), now.plusSeconds(60)),
            new Transfer("RO33CCCC0000000000000003", "RO44DDDD0000000000000004", new BigDecimal("6000.00"), now.plusSeconds(90)),
            new Transfer("RO44DDDD0000000000000004", "RO55EEEE0000000000000005", new BigDecimal("5500.00"), now.plusSeconds(120)),
            new Transfer("RO55EEEE0000000000000005", "RO33CCCC0000000000000003", new BigDecimal("5000.00"), now.plusSeconds(150))
        ));
        printResults(new FraudDetector().detect(transfers));
    }

    static void scenario3_noFraud() {
        System.out.println("--- Scenario 3: No Fraud (Linear Chain) ---");
        Instant now = Instant.now();
        List<Transfer> transfers = List.of(
            new Transfer("RO11AAAA0000000000000001", "RO22BBBB0000000000000002", new BigDecimal("10000.00"), now),
            new Transfer("RO22BBBB0000000000000002", "RO33CCCC0000000000000003", new BigDecimal("9500.00"), now.plusSeconds(30)),
            new Transfer("RO33CCCC0000000000000003", "RO44DDDD0000000000000004", new BigDecimal("9000.00"), now.plusSeconds(60))
        );
        printResults(new FraudDetector().detect(transfers));
    }

    static void scenario4_largeCriticalRing() {
        System.out.println("--- Scenario 4: Large Critical Ring (6 accounts) ---");
        Instant now = Instant.now();
        String[] ibans = {
            "RO11AAAA0000000000000001",
            "RO22BBBB0000000000000002",
            "RO33CCCC0000000000000003",
            "RO44DDDD0000000000000004",
            "RO55EEEE0000000000000005",
            "RO66FFFF0000000000000006"
        };
        List<Transfer> transfers = new ArrayList<>();
        BigDecimal amount = new BigDecimal("15000.00");
        for (int i = 0; i < ibans.length; i++) {
            transfers.add(new Transfer(
                ibans[i],
                ibans[(i + 1) % ibans.length],
                amount,
                now.plus(i * 10L, ChronoUnit.MINUTES)
            ));
        }
        printResults(new FraudDetector().detect(transfers));
    }

    static void scenario5_falsePositiveFiltered() {
        System.out.println("--- Scenario 5: Ring Below Threshold (filtered) ---");
        Instant now = Instant.now();
        List<Transfer> transfers = List.of(
            new Transfer("RO11AAAA0000000000000001", "RO22BBBB0000000000000002", new BigDecimal("200.00"), now),
            new Transfer("RO22BBBB0000000000000002", "RO11AAAA0000000000000001", new BigDecimal("190.00"), now.plusSeconds(30))
        );
        printResults(new FraudDetector().detect(transfers));
    }

    static void scenario6_temporalRing_detected() {
        System.out.println("--- Scenario 6: Temporal Ring (within 24h window) ---");
        Instant now = Instant.now();
        List<Transfer> transfers = List.of(
            new Transfer("ACC1", "ACC2", new BigDecimal("5000.00"), now),
            new Transfer("ACC2", "ACC1", new BigDecimal("4900.00"), now.plus(2, ChronoUnit.HOURS))
        );
        FraudDetector detector = new FraudDetector(new BigDecimal("1000.00"), Duration.ofHours(24));
        printResults(detector.detect(transfers));
    }

    static void scenario7_temporalRing_filtered() {
        System.out.println("--- Scenario 7: Temporal Ring (outside 24h window - filtered) ---");
        Instant now = Instant.now();
        List<Transfer> transfers = List.of(
            new Transfer("ACC1", "ACC2", new BigDecimal("5000.00"), now),
            new Transfer("ACC2", "ACC1", new BigDecimal("4900.00"), now.plus(48, ChronoUnit.HOURS))
        );
        FraudDetector detector = new FraudDetector(new BigDecimal("1000.00"), Duration.ofHours(24));
        printResults(detector.detect(transfers));
    }

    static void printResults(List<FraudRing> rings) {
        if (rings.isEmpty()) {
            System.out.println("  No fraud rings detected.\n");
            return;
        }
        for (FraudRing ring : rings) {
            System.out.println("  " + ring);
            for (var t : ring.getTransfers()) {
                System.out.println("    " + t);
            }
        }
        System.out.println();
    }
}
