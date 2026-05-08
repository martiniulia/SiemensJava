package fraud.graph;

import fraud.model.Transfer;

import java.util.*;

public class TransferGraph {

    private final List<List<Integer>> adjacency;

    private final Map<String, Integer> ibanToIndex;
    private final List<String> indexToIban;

    private final Map<Integer, List<Transfer>> transfersBySource;

    public TransferGraph(List<Transfer> transfers) {
        this.ibanToIndex = new HashMap<>();
        this.indexToIban = new ArrayList<>();
        this.adjacency = new ArrayList<>();
        this.transfersBySource = new HashMap<>();

        for (Transfer t : transfers) {
            registerNode(t.getSourceIban());
            registerNode(t.getTargetIban());
        }

        for (Transfer t : transfers) {
            int src = ibanToIndex.get(t.getSourceIban());
            int tgt = ibanToIndex.get(t.getTargetIban());
            adjacency.get(src).add(tgt);
            transfersBySource.computeIfAbsent(src, k -> new ArrayList<>()).add(t);
        }
    }

    private void registerNode(String iban) {
        if (!ibanToIndex.containsKey(iban)) {
            int idx = indexToIban.size();
            ibanToIndex.put(iban, idx);
            indexToIban.add(iban);
            adjacency.add(new ArrayList<>());
        }
    }

    public int size() { return indexToIban.size(); }
    public List<Integer> neighbors(int node) { return adjacency.get(node); }
    public String ibanOf(int index) { return indexToIban.get(index); }
    public int indexOf(String iban) { return ibanToIndex.getOrDefault(iban, -1); }
    public List<Transfer> transfersFrom(int node) {
        return transfersBySource.getOrDefault(node, Collections.emptyList());
    }
}
