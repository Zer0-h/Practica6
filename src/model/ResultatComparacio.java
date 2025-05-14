package model;

/**
 * Classe que encapsula el resultat de la comparació entre dos idiomes.
 * Inclou els codis dels idiomes comparats i la distància lèxica obtinguda.
 *
 * Aquesta distància s'ha calculat mitjançant l'algorisme de Levenshtein
 * ponderat, i és emprada per construir el graf visual i respondre consultes.
 *
 * @author tonitorres
 */
public class ResultatComparacio {

    /** Codi de l'idioma origen. */
    private final String idiomaA;

    /** Codi de l'idioma destí. */
    private final String idiomaB;

    /** Distància lèxica entre els dos idiomes. */
    private final double distancia;

    /**
     * Constructor que inicialitza un resultat amb els dos idiomes i la
     * distància.
     *
     * @param a codi de l'idioma A (origen)
     * @param b codi de l'idioma B (destí)
     * @param d valor numèric de la distància entre els dos idiomes
     */
    public ResultatComparacio(String a, String b, double d) {
        this.idiomaA = a;
        this.idiomaB = b;
        this.distancia = d;
    }

    /**
     * Retorna el codi de l'idioma A (origen).
     */
    public String getIdiomaA() {
        return idiomaA;
    }

    /**
     * Retorna el codi de l'idioma B (destí).
     */
    public String getIdiomaB() {
        return idiomaB;
    }

    /**
     * Retorna la distància lèxica entre els dos idiomes.
     */
    public double getDistancia() {
        return distancia;
    }

    /**
     * Retorna una representació textual del resultat per a debugs o consola.
     *
     * @return cadena en format "idiomaA - idiomaB: valor"
     */
    @Override
    public String toString() {
        return idiomaA + " - " + idiomaB + ": " + String.format("%.3f", distancia);
    }
}
