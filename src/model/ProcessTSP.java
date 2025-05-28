package model;

import controlador.Controlador;
import controlador.Notificacio;

import java.util.*;

public class ProcessTSP extends Thread {

    private final Controlador controlador;
    private static final int INFINIT = Integer.MAX_VALUE / 2;

    public ProcessTSP(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void run() {
        Model model = controlador.getModel();
        int[][] matriu = model.getMatriuDistancies();

        if (matriu == null || matriu.length == 0) {
            System.err.println("Error: matriu de distàncies no inicialitzada.");
            return;
        }

        // Inici del càlcul
        ResultatTSP resultat = resoldreTSP(matriu);

        model.setMillorRuta(resultat.getRuta());
        model.setCostRuta(resultat.getCost());
        model.setNodesExplorats(resultat.getNodesExplorats());
        model.setNodesPresents(resultat.getNodesPresents());

        controlador.notificar(Notificacio.PINTAR_RESULTAT);
    }

    private ResultatTSP resoldreTSP(int[][] matriuOriginal) {
        int n = matriuOriginal.length;
        int millorCost = INFINIT;
        List<Integer> millorCami = new ArrayList<>();
        int nodesExplorats = 0;
        int nodesPresents = 0;

        PriorityQueue<NodeTSP> cua = new PriorityQueue<>();
        List<Integer> camiInicial = new ArrayList<>();
        camiInicial.add(0);

        int[][] matReducida = reduirMatriu(copiarMatriu(matriuOriginal), n);
        int cotaInicial = calcularReduccio(matReducida, n);

        cua.add(new NodeTSP(camiInicial, matReducida, 0, cotaInicial, 0));

        while (!cua.isEmpty()) {
            NodeTSP node = cua.poll();
            nodesPresents++;

            if (node.getCami().size() == n) {
                int costFinal = node.getCost() + matriuOriginal[node.getCiutatActual()][0];
                if (costFinal < millorCost) {
                    millorCost = costFinal;
                    millorCami = new ArrayList<>(node.getCami());
                    millorCami.add(0);
                }
                continue;
            }

            for (int ciutat = 0; ciutat < n; ciutat++) {
                if (!node.getCami().contains(ciutat)) {
                    List<Integer> nouCami = new ArrayList<>(node.getCami());
                    nouCami.add(ciutat);

                    int[][] novaMatriu = copiarMatriu(node.getMatriuReduida());
                    bloquejar(novaMatriu, node.getCiutatActual(), ciutat);
                    novaMatriu[ciutat][0] = INFINIT;

                    int reduccio = calcularReduccio(novaMatriu, n);
                    int nouCost = node.getCost() + node.getMatriuReduida()[node.getCiutatActual()][ciutat];
                    int novaCota = nouCost + reduccio;

                    if (novaCota < millorCost) {
                        cua.add(new NodeTSP(nouCami, novaMatriu, nouCost, novaCota, ciutat));
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
        for (int[] matriu1 : matriu) {
            matriu1[columna] = INFINIT;
        }
    }

    private int[][] copiarMatriu(int[][] original) {
        int[][] copia = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, original[i].length);
        }
        return copia;
    }

    private int[][] reduirMatriu(int[][] matriu, int n) {
        for (int i = 0; i < n; i++) {
            int min = Arrays.stream(matriu[i]).min().orElse(INFINIT);
            if (min != INFINIT && min > 0) {
                for (int j = 0; j < n; j++) {
                    if (matriu[i][j] != INFINIT) matriu[i][j] -= min;
                }
            }
        }
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

    private int calcularReduccio(int[][] matriu, int n) {
        int suma = 0;
        for (int i = 0; i < n; i++) {
            int min = Arrays.stream(matriu[i]).min().orElse(INFINIT);
            if (min != INFINIT && min > 0) suma += min;
        }
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
