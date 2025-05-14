package model;

import java.util.List;

public class ResultatTSP {
    private final List<Integer> ruta;
    private final int cost;
    private final int nodesExplorats;
    private final int nodesPresents;

    public ResultatTSP(List<Integer> ruta, int cost, int explorats, int presents) {
        this.ruta = ruta;
        this.cost = cost;
        this.nodesExplorats = explorats;
        this.nodesPresents = presents;
    }

    public List<Integer> getRuta() {
        return ruta;
    }

    public int getCost() {
        return cost;
    }

    public int getNodesExplorats() {
        return nodesExplorats;
    }

    public int getNodesPresents() {
        return nodesPresents;
    }
}
