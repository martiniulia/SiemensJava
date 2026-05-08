package fraud;

import fraud.detection.FraudDetector;
import fraud.model.FraudRing;
import fraud.model.RiskLevel;
import fraud.model.Transfer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FraudDetectorTest {

    private final FraudDetector detector = new FraudDetector();

    @Test
    void simpleTriangle_detectsOneRing() {
        List<Transfer> transfers = List.of(
            t("A", "B", 5000, Instant.now()), 
            t("B", "C", 4800, Instant.now()), 
            t("C", "A", 4500, Instant.now())
        );
        List<FraudRing> rings = detector.detect(transfers);
        assertEquals(1, rings.size());
    }

    @Test
    void temporalWindow_respected() {
        Instant now = Instant.now();
        List<Transfer> transfers = List.of(
            t("A", "B", 5000, now),
            t("B", "A", 5000, now.plus(48, ChronoUnit.HOURS))
        );

        FraudDetector strict = new FraudDetector(BigDecimal.ONE, Duration.ofHours(24));
        assertTrue(strict.detect(transfers).isEmpty());

        FraudDetector relaxed = new FraudDetector(BigDecimal.ONE, Duration.ofHours(72));
        assertEquals(1, relaxed.detect(transfers).size());
    }

    private Transfer t(String src, String tgt, double amount, Instant time) {
        return new Transfer(src, tgt, BigDecimal.valueOf(amount), time);
    }
}
