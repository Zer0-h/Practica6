package model;

import java.util.List;

/**
 * Classe Model del patró MVC.
 * Emmagatzema les dades de l’estat de l’aplicació, com la configuració
 * del graf, la matriu de distàncies, la millor ruta trobada i les estadístiques
 * del procés de càlcul.
 *
 * No conté lògica d'algorismes; actua com a contenidor d'informació compartida
 * entre la Vista i el Controlador.
 *
 * @author tonitorres
 */
public class Model {

    // --- Configuració de l’usuari ---
    /** Nombre de ciutats del graf. */
    private int numCiutats;

    /** Cost màxim per aresta (valor aleatori màxim). */
    private int maxCost;

    /** Densitat de connexions addicionals (valor entre 0 i 1). */
    private double densitat;

    // --- Matriu de distàncies ---
    /** Matriu d’adjacència dirigida amb els costos entre ciutats. */
    private int[][] matriuDistancies;

    // --- Resultats del càlcul del TSP ---
    /** Ruta òptima trobada (ordre dels nodes). */
    private List<Integer> millorRuta;

    /** Cost total de la millor ruta trobada. */
    private int costRuta;

    /** Nombre de nodes realment explorats (fills generats). */
    private int nodesExplorats;

    /** Nombre de nodes presents a la cua de prioritats durant l’execució. */
    private int nodesPresents;

    /** Nombre total de nodes gestionats (si es vol controlar més
     * detalladament). */
    private int nodesPresentsTotals;

    // --- Getters i Setters de configuració ---
    public int getNumCiutats() {
        return numCiutats;
    }

    public void setNumCiutats(int value) {
        this.numCiutats = value;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(int value) {
        this.maxCost = value;
    }

    public double getDensitat() {
        return densitat;
    }

    public void setDensitat(double densitat) {
        this.densitat = densitat;
    }

    // --- Getters i Setters de la matriu ---
    public int[][] getMatriuDistancies() {
        return matriuDistancies;
    }

    public void setMatriuDistancies(int[][] matriu) {
        this.matriuDistancies = matriu;
    }

    // --- Getters i Setters de resultats ---
    public List<Integer> getMillorRuta() {
        return millorRuta;
    }

    public void setMillorRuta(List<Integer> ruta) {
        this.millorRuta = ruta;
    }

    public int getCostRuta() {
        return costRuta;
    }

    public void setCostRuta(int cost) {
        this.costRuta = cost;
    }

    public int getNodesExplorats() {
        return nodesExplorats;
    }

    public void setNodesExplorats(int nodes) {
        this.nodesExplorats = nodes;
    }

    public int getNodesPresents() {
        return nodesPresents;
    }

    public void setNodesPresents(int nodes) {
        this.nodesPresents = nodes;
    }

    public int getNodesPresentsTotals() {
        return nodesPresentsTotals;
    }

    public void setNodesPresentsTotals(int nodes) {
        this.nodesPresentsTotals = nodes;
    }

    /**
     * Reinicia les dades de resultat abans d’un nou càlcul.
     * Es manté la configuració (nombre de ciutats, cost màxim i densitat).
     */
    public void reset() {
        millorRuta = null;
        costRuta = -1;
        nodesExplorats = 0;
        nodesPresents = 0;
        nodesPresentsTotals = 0;
    }
}
