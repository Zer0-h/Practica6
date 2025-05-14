package model.processos;

/**
 * Classe que implementa l'algorisme de Levenshtein per calcular
 * la distància mínima d'edició entre dues cadenes.
 *
 * Utilitza una implementació optimitzada amb només dues files,
 * aconseguint un cost en espai de O(n).
 *
 * Aquesta distància representa el mínim nombre d'operacions
 * (inserció, supressió, substitució) per transformar una cadena en una altra.
 *
 * @author tonitorres
 */
public class Levenshtein {

    /**
     * Calcula la distància de Levenshtein entre dues cadenes.
     *
     * @param str1 primera cadena
     * @param str2 segona cadena
     *
     * @return distància mínima d'edició entre str1 i str2
     */
    public int distancia(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();

        // Casos trivials
        if (m == 0) {
            return n;
        }
        if (n == 0) {
            return m;
        }

        // Vectors reutilitzables per a les files
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        // Inicialitzar la primera fila (transformació d'str1 a cadena buida)
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        // Iteració fila per fila
        for (int i = 1; i <= m; i++) {
            curr[0] = i;

            for (int j = 1; j <= n; j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;

                // Mínim entre inserció, supressió i substitució
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, // Inserció
                                prev[j] + 1), // Supressió
                        prev[j - 1] + cost);           // Substitució
            }

            // Intercanvi de files per la següent iteració
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[n];
    }
}
