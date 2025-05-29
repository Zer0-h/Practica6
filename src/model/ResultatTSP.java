package model;

import java.util.List;

/**
 * Classe que encapsula el resultat de l'execució de l'algorisme
 * Branch and Bound per al problema del viatjant de comerç (TSP).
 *
 * Emmagatzema la ruta òptima trobada, el seu cost total i
 * estadístiques del procés com el nombre de nodes explorats i els
 * que han estat presents a la cua de prioritats.
 *
 * Aquesta classe és immutable.
 *
 * @author tonitorres
 */
public class ResultatTSP {

    /** Ruta òptima trobada (ordre dels nodes, acaba a l’origen). */
    private final List<Integer> ruta;

    /** Cost total de la ruta òptima. */
    private final int cost;

    /** Nombre de nodes que han estat realment explorats. */
    private final int nodesExplorats;

    /** Nombre total de nodes presents a la cua (inclosos descartats). */
    private final int nodesPresents;

    /**
     * Constructor que inicialitza tots els camps.
     *
     * @param ruta      ruta òptima trobada
     * @param cost      cost total del recorregut
     * @param explorats nombre de nodes explorats
     * @param presents  nombre de nodes presents a la cua
     */
    public ResultatTSP(List<Integer> ruta, int cost, int explorats, int presents) {
        this.ruta = ruta;
        this.cost = cost;
        this.nodesExplorats = explorats;
        this.nodesPresents = presents;
    }

    /** Retorna la ruta òptima. */
    public List<Integer> getRuta() {
        return ruta;
    }

    /** Retorna el cost total de la ruta. */
    public int getCost() {
        return cost;
    }

    /** Retorna el nombre de nodes explorats. */
    public int getNodesExplorats() {
        return nodesExplorats;
    }

    /** Retorna el nombre total de nodes presents a la cua. */
    public int getNodesPresents() {
        return nodesPresents;
    }
}
