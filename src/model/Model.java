package model;

import java.util.List;

public class Model {

    private int[][] matriuDistancies;
    private List<Integer> millorRuta;
    private int costRuta;
    private int nodesExplorats;
    private int nodesPresents;
    private int nodesPresentsTotals;
    private int numCiutats;
    private int maxCost;

    public void setMaxCost(int value) {
        maxCost = value;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public void setNumCiutats(int value) {
        numCiutats = value;
    }

    public int getNumCiutats() {
        return numCiutats;
    }

    public void setMatriuDistancies(int[][] matriu) {
        this.matriuDistancies = matriu;
    }

    public int[][] getMatriuDistancies() {
        return matriuDistancies;
    }

    public void setMillorRuta(List<Integer> ruta) {
        this.millorRuta = ruta;
    }

    public List<Integer> getMillorRuta() {
        return millorRuta;
    }

    public void setCostRuta(int cost) {
        this.costRuta = cost;
    }

    public int getCostRuta() {
        return costRuta;
    }

    public void setNodesExplorats(int nodes) {
        this.nodesExplorats = nodes;
    }

    public int getNodesExplorats() {
        return nodesExplorats;
    }

    public void setNodesPresents(int nodes) {
        this.nodesPresents = nodes;
    }

    public int getNodesPresents() {
        return nodesPresents;
    }

    public void setNodesPresentsTotals(int nodes) {
        this.nodesPresentsTotals = nodes;
    }

    public int getNodesPresentsTotals() {
        return nodesPresentsTotals;
    }

    public void reset() {
        millorRuta = null;
        costRuta = -1;
        nodesExplorats = 0;
        nodesPresents = 0;
        nodesPresentsTotals = 0;
    }
}
