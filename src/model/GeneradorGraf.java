package model;

import java.util.Random;

/**
 * Classe encarregada de generar matrius d'adjacència aleatòries
 * per al problema del viatjant de comerç (TSP).
 *
 * La matriu generada representa un graf complet, dirigit i amb pesos positius.
 * Els arcs (i,i) es marquen com a ∞ (valor molt alt).
 *
 * @author tonitorres
 */
public class GeneradorGraf {

    private static final int INFINIT = Integer.MAX_VALUE / 2;

    /**
     * Genera una matriu de distàncies aleatòria.
     *
     * @param n nombre de ciutats
     * @param maxCost valor màxim de les distàncies
     * @return matriu de distàncies n x n
     */
    public static int[][] generarMatriu(int n, int maxCost) {
        int[][] matriu = new int[n][n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matriu[i][j] = INFINIT;
                } else {
                    matriu[i][j] = rand.nextInt(maxCost - 1) + 1;
                }
            }
        }

        return matriu;
    }

    /**
     * Mostra la matriu per pantalla (per debug o proves).
     *
     * @param matriu la matriu de distàncies a imprimir
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
