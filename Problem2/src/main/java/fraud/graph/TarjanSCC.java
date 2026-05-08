package fraud.graph;

import java.util.*;

public class TarjanSCC {

    private final TransferGraph graph;

    private int timer;
    private final int[] disc;
    private final int[] low;
    private final boolean[] onStack;
    private final Deque<Integer> stack;

    private final List<List<Integer>> sccs;

    public TarjanSCC(TransferGraph graph) {
        this.graph = graph;
        int n = graph.size();
        this.disc = new int[n];
        this.low = new int[n];
        this.onStack = new boolean[n];
        this.stack = new ArrayDeque<>();
        this.sccs = new ArrayList<>();
        Arrays.fill(disc, -1);
    }

    public List<List<Integer>> findSCCs() {
        for (int i = 0; i < graph.size(); i++) {
            if (disc[i] == -1) {
                dfs(i);
            }
        }
        return Collections.unmodifiableList(sccs);
    }

    private void dfs(int u) {
        disc[u] = low[u] = timer++;
        stack.push(u);
        onStack[u] = true;

        for (int v : graph.neighbors(u)) {
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int w = stack.pop();
                onStack[w] = false;
                scc.add(w);
                if (w == u) break;
            }
            sccs.add(scc);
        }
    }
}
