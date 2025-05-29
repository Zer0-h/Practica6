package model;

import java.util.*;

/**
 * Classe utilitzada per generar matrius d’adjacència per al problema del TSP.
 * Garanteix una ruta Hamiltoniana vàlida i permet afegir connexions aleatòries
 * segons una densitat definida.
 *
 * La matriu resultant és dirigida, amb costos positius o valors infinits
 * per indicar absència de connexió.
 *
 * @author tonitorres
 */
public class GeneradorGraf {

    /** Valor que representa una connexió inexistent (cost infinit). */
    private static final int INFINIT = Integer.MAX_VALUE / 2;

    /** Generador aleatori per crear costos i estructures. */
    private static final Random random = new Random();

    /**
     * Genera una matriu de distàncies entre ciutats amb una ruta vàlida
     * garantida.
     * Es construeix una ruta Hamiltoniana inicial (n nodes connectats en cicle)
     * i s’hi afegeixen connexions addicionals aleatòries segons la densitat.
     *
     * @param n        nombre de ciutats (nodes)
     * @param maxCost  cost màxim possible entre dues ciutats
     * @param densitat valor entre 0 i 1 que indica la probabilitat d’afegir
     *                 arestes extra
     *
     * @return matriu d’adjacència dirigida amb costos > 0 o INFINIT
     */
    public static int[][] generarMatriu(int n, int maxCost, double densitat) {
        int[][] matriu = new int[n][n];

        // Inicialitzar la matriu amb INFINIT (cap connexió)
        for (int i = 0; i < n; i++) {
            Arrays.fill(matriu[i], INFINIT);
        }

        // Crear una ruta Hamiltoniana aleatòria
        List<Integer> ruta = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ruta.add(i);
        }
        Collections.shuffle(ruta);

        // Connectar seqüencialment la ruta
        for (int i = 0; i < n - 1; i++) {
            int from = ruta.get(i);
            int to = ruta.get(i + 1);
            matriu[from][to] = costAleatori(maxCost);
        }

        // Tancar el cicle tornant al node inicial
        matriu[ruta.get(n - 1)][ruta.get(0)] = costAleatori(maxCost);

        // Afegir connexions aleatòries segons la densitat especificada
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && matriu[i][j] == INFINIT && random.nextDouble() < densitat) {
                    matriu[i][j] = costAleatori(maxCost);
                }
            }
        }

        return matriu;
    }

    /**
     * Genera un cost aleatori positiu entre 1 i maxCost - 1.
     *
     * @param max valor màxim de cost permès
     *
     * @return valor aleatori entre 1 i max - 1
     */
    private static int costAleatori(int max) {
        return random.nextInt(max - 1) + 1;
    }

    /**
     * Imprimeix la matriu de distàncies per consola, mostrant els valors
     * de cost o el símbol ∞ per les connexions inaccessibles.
     *
     * @param matriu matriu d’adjacència a imprimir
     */
    public static void imprimirMatriu(int[][] matriu) {
        int n = matriu.length;
        System.out.println("Matriu de distàncies:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matriu[i][j] == INFINIT) {
                    System.out.print("  ∞  ");
                } else {
                    System.out.printf("%4d ", matriu[i][j]);
                }
            }
            System.out.println();
        }
    }
}
