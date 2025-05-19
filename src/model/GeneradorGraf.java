package model;

import java.util.*;

public class GeneradorGraf {

    private static final int INFINIT = Integer.MAX_VALUE / 2;
    private static final Random random = new Random();

    /**
     * Genera una matriu de distàncies amb una ruta vàlida garantida.
     *
     * @param n nombre de ciutats
     * @param maxCost cost màxim entre ciutats
     * @param densitat probabilitat (entre 0 i 1) d’afegir connexions aleatòries
     * @return matriu d’adjacència dirigida amb valors > 0 o INFINIT
     */
    public static int[][] generarMatriu(int n, int maxCost, double densitat) {
        int[][] matriu = new int[n][n];

        // Inicialitzem tot a INFINIT (no connectat)
        for (int i = 0; i < n; i++) {
            Arrays.fill(matriu[i], INFINIT);
        }

        // Ruta Hamiltoniana base
        List<Integer> ruta = new ArrayList<>();
        for (int i = 0; i < n; i++) ruta.add(i);
        Collections.shuffle(ruta);

        for (int i = 0; i < n - 1; i++) {
            int from = ruta.get(i);
            int to = ruta.get(i + 1);
            matriu[from][to] = costAleatori(maxCost);
        }

        // Tancar el cicle (últim → primer)
        matriu[ruta.get(n - 1)][ruta.get(0)] = costAleatori(maxCost);

        // Afegir connexions aleatòries segons la densitat
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && matriu[i][j] == INFINIT && random.nextDouble() < densitat) {
                    matriu[i][j] = costAleatori(maxCost);
                }
            }
        }

        return matriu;
    }

    private static int costAleatori(int max) {
        return random.nextInt(max - 1) + 1; // entre 1 i max-1
    }

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
