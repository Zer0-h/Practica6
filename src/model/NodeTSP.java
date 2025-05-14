package model;

import java.util.List;

public class NodeTSP implements Comparable<NodeTSP> {

    private final List<Integer> cami;
    private final int[][] matriuReduida;
    private final int cost;
    private final int cotaInferior;
    private final int ciutatActual;

    public NodeTSP(List<Integer> cami, int[][] matriuReduida, int cost, int cotaInferior, int ciutatActual) {
        this.cami = cami;
        this.matriuReduida = matriuReduida;
        this.cost = cost;
        this.cotaInferior = cotaInferior;
        this.ciutatActual = ciutatActual;
    }

    public List<Integer> getCami() {
        return cami;
    }

    public int[][] getMatriuReduida() {
        return matriuReduida;
    }

    public int getCost() {
        return cost;
    }

    public int getCotaInferior() {
        return cotaInferior;
    }

    public int getCiutatActual() {
        return ciutatActual;
    }

    @Override
    public int compareTo(NodeTSP altre) {
        return Integer.compare(this.cotaInferior, altre.cotaInferior);
    }
}
