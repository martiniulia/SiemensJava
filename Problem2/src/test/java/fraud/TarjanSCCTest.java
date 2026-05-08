package fraud;

import fraud.graph.TarjanSCC;
import fraud.graph.TransferGraph;
import fraud.model.Transfer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TarjanSCCTest {

    @Test
    void simpleTriangle_shouldFindOneSCC() {
        List<Transfer> transfers = List.of(
            t("A", "B"), t("B", "C"), t("C", "A")
        );
        var sccs = new TarjanSCC(new TransferGraph(transfers)).findSCCs();
        long cyclicSccs = sccs.stream().filter(s -> s.size() >= 2).count();
        assertEquals(1, cyclicSccs);
    }

    @Test
    void linearChain_shouldFindNoSCC() {
        List<Transfer> transfers = List.of(
            t("A", "B"), t("B", "C"), t("C", "D")
        );
        var sccs = new TarjanSCC(new TransferGraph(transfers)).findSCCs();
        long cyclicSccs = sccs.stream().filter(s -> s.size() >= 2).count();
        assertEquals(0, cyclicSccs);
    }

    private Transfer t(String src, String tgt) {
        return new Transfer(src, tgt, new BigDecimal("1000.00"), Instant.now());
    }
}
