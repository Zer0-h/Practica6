package model;

import java.util.*;

public class BranchAndBoundTSP {

    private static final int INFINIT = Integer.MAX_VALUE / 2;

    private int[][] matriuOriginal;
    private int n;
    private int millorCost;
    private List<Integer> millorCami;

    private int nodesExplorats;
    private int nodesPresents;

    public BranchAndBoundTSP(int[][] matriu) {
        this.matriuOriginal = matriu;
        this.n = matriu.length;
        this.millorCost = INFINIT;
        this.millorCami = new ArrayList<>();
    }

    public ResultatTSP resoldre() {
        PriorityQueue<NodeTSP> cua = new PriorityQueue<>();
        List<Integer> camiInicial = new ArrayList<>();
        camiInicial.add(0); // comencem des de la ciutat 0

        int[][] matReducida = reduirMatriu(copiarMatriu(matriuOriginal));
        int cotaInicial = calcularReduccio(matReducida);

        cua.add(new NodeTSP(camiInicial, matReducida, 0, cotaInicial, 0));

        while (!cua.isEmpty()) {
            NodeTSP node = cua.poll();
            nodesPresents++;
            List<Integer> camiActual = node.getCami();

            if (camiActual.size() == n) {
                int costFinal = node.getCost() + matriuOriginal[node.getCiutatActual()][0];
                if (costFinal < millorCost) {
                    millorCost = costFinal;
                    millorCami = new ArrayList<>(camiActual);
                    millorCami.add(0); // tornem a l'inici
                }
                continue;
            }

            for (int ciutat = 0; ciutat < n; ciutat++) {
                if (!camiActual.contains(ciutat)) {
                    List<Integer> nouCami = new ArrayList<>(camiActual);
                    nouCami.add(ciutat);

                    int[][] novaMatriu = copiarMatriu(node.getMatriuReduida());

                    // Poda: bloqueja fila i columna
                    bloquejar(novaMatriu, node.getCiutatActual(), ciutat);
                    novaMatriu[ciutat][0] = INFINIT; // evitar tornar massa aviat

                    int reduccio = calcularReduccio(novaMatriu);
                    int nouCost = node.getCost() + node.getMatriuReduida()[node.getCiutatActual()][ciutat];

                    int novaCota = nouCost + reduccio;

                    if (novaCota < millorCost) {
                        NodeTSP fill = new NodeTSP(nouCami, novaMatriu, nouCost, novaCota, ciutat);
                        cua.add(fill);
                        nodesExplorats++;
                    }
                }
            }
        }

        return new ResultatTSP(millorCami, millorCost, nodesExplorats, nodesPresents);
    }

    // -- Mètodes auxiliars --

    private void bloquejar(int[][] matriu, int fila, int columna) {
        Arrays.fill(matriu[fila], INFINIT);
        for (int i = 0; i < matriu.length; i++) {
            matriu[i][columna] = INFINIT;
        }
    }

    private int[][] copiarMatriu(int[][] original) {
        int[][] copia = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copia;
    }

    private int[][] reduirMatriu(int[][] matriu) {
        int reduccioTotal = 0;

        // Reducció de files
        for (int i = 0; i < n; i++) {
            int min = Arrays.stream(matriu[i]).min().orElse(INFINIT);
            if (min != INFINIT && min > 0) {
                for (int j = 0; j < n; j++) {
                    if (matriu[i][j] != INFINIT) matriu[i][j] -= min;
                }
            }
        }

        // Reducció de columnes
        for (int j = 0; j < n; j++) {
            int min = INFINIT;
            for (int i = 0; i < n; i++) {
                min = Math.min(min, matriu[i][j]);
            }
            if (min != INFINIT && min > 0) {
                for (int i = 0; i < n; i++) {
                    if (matriu[i][j] != INFINIT) matriu[i][j] -= min;
                }
            }
        }

        return matriu;
    }

    private int calcularReduccio(int[][] matriu) {
        int suma = 0;

        // Reducció de files
        for (int i = 0; i < n; i++) {
            int min = Arrays.stream(matriu[i]).min().orElse(INFINIT);
            if (min != INFINIT && min > 0) suma += min;
        }

        // Reducció de columnes
        for (int j = 0; j < n; j++) {
            int min = INFINIT;
            for (int i = 0; i < n; i++) {
                min = Math.min(min, matriu[i][j]);
            }
            if (min != INFINIT && min > 0) suma += min;
        }

        return suma;
    }
}
