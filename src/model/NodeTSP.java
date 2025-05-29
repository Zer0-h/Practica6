package model;

import java.util.List;

/**
 * Classe que representa un node dins l’algorisme de Branch and Bound
 * per al problema del viatjant de comerç (TSP).
 *
 * Cada node conté la informació de l'estat actual del recorregut:
 * - el camí recorregut fins al moment,
 * - la matriu de distàncies reduïda amb les restriccions aplicades,
 * - el cost acumulat del recorregut,
 * - la cota inferior estimada (cost + reducció),
 * - i la ciutat on es troba actualment.
 *
 * Aquesta classe implementa l’interfície `Comparable` per tal de poder
 * ordenar els nodes segons la seva cota inferior dins una cua de prioritat.
 *
 * @author tonitorres
 */
public class NodeTSP implements Comparable<NodeTSP> {

    /** Camí recorregut fins ara (seqüència d’indrets visitats). */
    private final List<Integer> cami;

    /** Matriu de distàncies modificada amb files/columnes bloquejades. */
    private final int[][] matriuReduida;

    /** Cost acumulat real del camí fins aquest node. */
    private final int cost;

    /** Cota inferior estimada (cost acumulat + reducció mínima restant). */
    private final int cotaInferior;

    /** Índex de la ciutat actual (última del camí). */
    private final int ciutatActual;

    /**
     * Constructor del node.
     *
     * @param cami          camí recorregut fins ara
     * @param matriuReduida matriu amb restriccions aplicades per aquest estat
     * @param cost          cost acumulat fins aquest punt
     * @param cotaInferior  cota inferior estimada
     * @param ciutatActual  índex de la ciutat actual
     */
    public NodeTSP(List<Integer> cami, int[][] matriuReduida, int cost, int cotaInferior, int ciutatActual) {
        this.cami = cami;
        this.matriuReduida = matriuReduida;
        this.cost = cost;
        this.cotaInferior = cotaInferior;
        this.ciutatActual = ciutatActual;
    }

    /** Retorna el camí recorregut fins ara. */
    public List<Integer> getCami() {
        return cami;
    }

    /** Retorna la matriu reduïda associada a aquest node. */
    public int[][] getMatriuReduida() {
        return matriuReduida;
    }

    /** Retorna el cost acumulat del camí. */
    public int getCost() {
        return cost;
    }

    /** Retorna la cota inferior estimada per aquest node. */
    public int getCotaInferior() {
        return cotaInferior;
    }

    /** Retorna la ciutat actual (última ciutat visitada). */
    public int getCiutatActual() {
        return ciutatActual;
    }

    /**
     * Compara dos nodes en funció de la seva cota inferior.
     * Això permet ordenar-los dins una PriorityQueue perquè
     * els nodes amb millor (menor) cota s’explorin primer.
     *
     * @param altre un altre node a comparar
     *
     * @return valor negatiu si aquest té millor cota, positiu si pitjor
     */
    @Override
    public int compareTo(NodeTSP altre) {
        return Integer.compare(this.cotaInferior, altre.cotaInferior);
    }
}
