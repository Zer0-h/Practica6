package model.processos;

import model.Idioma;
import model.ResultatComparacio;

/**
 * Classe encarregada de comparar lèxicament dos idiomes.
 * Implementa l'algorisme de Levenshtein ponderat per calcular
 * la distància entre diccionaris de paraules.
 *
 * El càlcul es fa de manera simètrica: d'A cap a B i de B cap a A,
 * i la distància final es calcula com la norma euclidiana de les dues mitjanes.
 *
 * Aquesta classe és utilitzada per processos de comparació.
 *
 * @author tonitorres
 */
public class ComparadorIdiomes {

    /** Si és cert, imprimeix informació addicional per consola. */
    private final boolean verbose;

    /**
     * Constructor que defineix si el mode verbose està activat.
     *
     * @param verbose true per mostrar detalls a la consola, false en cas
     *                contrari
     */
    public ComparadorIdiomes(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Compara dos idiomes utilitzant la distància de Levenshtein mínima
     * entre cada paraula del primer i totes les del segon, i viceversa.
     *
     * @param a idioma A
     * @param b idioma B
     *
     * @return resultat de la comparació amb la distància final
     */
    public ResultatComparacio comparar(Idioma a, Idioma b) {
        long start = System.nanoTime();

        if (verbose) {
            System.out.println("Comparant " + a.getNom() + " amb " + b.getNom());
        }

        Levenshtein lev = new Levenshtein();

        // Distància mitjana des de cada paraula d'A cap a B
        double sumaDistAB = a.getParaules().parallelStream()
                .mapToInt(pa -> b.getParaules().stream()
                .mapToInt(pb -> lev.distancia(pa, pb))
                .min()
                .orElse(Integer.MAX_VALUE))
                .sum();

        // Distància mitjana des de cada paraula de B cap a A
        double sumaDistBA = b.getParaules().parallelStream()
                .mapToInt(pb -> a.getParaules().stream()
                .mapToInt(pa -> lev.distancia(pb, pa))
                .min()
                .orElse(Integer.MAX_VALUE))
                .sum();

        double mitjaA = sumaDistAB / a.getParaules().size();
        double mitjaB = sumaDistBA / b.getParaules().size();

        // Càlcul de la distància final com a combinació de les dues mitjanes
        double dist = Math.sqrt(mitjaA * mitjaA + mitjaB * mitjaB);

        if (verbose) {
            long end = System.nanoTime();
            System.out.println("Temps: " + ((end - start) / 1_000_000) + " ms");
        }

        return new ResultatComparacio(a.getNom(), b.getNom(), dist);
    }
}
