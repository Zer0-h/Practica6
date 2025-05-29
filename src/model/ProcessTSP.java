package model;

import controlador.Controlador;
import controlador.Notificacio;

import java.util.*;
import java.util.concurrent.*;

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

        ResultatTSP resultat = resoldreTSP(matriu);

        model.setMillorRuta(resultat.getRuta());
        model.setCostRuta(resultat.getCost());
        model.setNodesExplorats(resultat.getNodesExplorats());
        model.setNodesPresents(resultat.getNodesPresents());

        controlador.notificar(Notificacio.PINTAR_RESULTAT);
    }

    private ResultatTSP resoldreTSP(int[][] matriuOriginal) {
        int n = matriuOriginal.length;
        final int[] millorCost = {INFINIT};
        List<Integer> millorCami = Collections.synchronizedList(new ArrayList<>());
        int[] nodesExplorats = {0};
        int nodesPresents = 0;

        PriorityQueue<NodeTSP> cua = new PriorityQueue<>();
        List<Integer> camiInicial = new ArrayList<>();
        camiInicial.add(0);

        int[][] matReducida = reduirMatriu(copiarMatriu(matriuOriginal), n);
        int cotaInicial = calcularReduccio(matReducida, n);

        cua.add(new NodeTSP(camiInicial, matReducida, 0, cotaInicial, 0));

        ExecutorService executor = Executors.newFixedThreadPool(
                Math.max(2, Runtime.getRuntime().availableProcessors())
        );

        while (!cua.isEmpty()) {
            NodeTSP node = cua.poll();
            nodesPresents++;

            if (node.getCami().size() == n) {
                int costFinal = node.getCost() + matriuOriginal[node.getCiutatActual()][0];
                synchronized (millorCost) {
                    if (costFinal < millorCost[0]) {
                        millorCost[0] = costFinal;
                        millorCami.clear();
                        millorCami.addAll(node.getCami());
                        millorCami.add(0);
                    }
                }
                continue;
            }

            List<Future<NodeTSP>> futurs = new ArrayList<>();

            for (int ciutat = 0; ciutat < n; ciutat++) {
                if (!node.getCami().contains(ciutat)) {
                    final int ciutatFinal = ciutat;
                    futurs.add(executor.submit(() -> {
                        List<Integer> nouCami = new ArrayList<>(node.getCami());
                        nouCami.add(ciutatFinal);

                        int[][] novaMatriu = copiarMatriu(node.getMatriuReduida());
                        bloquejar(novaMatriu, node.getCiutatActual(), ciutatFinal);
                        novaMatriu[ciutatFinal][0] = INFINIT;

                        int reduccio = calcularReduccio(novaMatriu, n);
                        int nouCost = node.getCost() + node.getMatriuReduida()[node.getCiutatActual()][ciutatFinal];
                        int novaCota = nouCost + reduccio;

                        synchronized (millorCost) {
                            if (novaCota < millorCost[0]) {
                                nodesExplorats[0]++;
                                return new NodeTSP(nouCami, novaMatriu, nouCost, novaCota, ciutatFinal);
                            }
                        }
                        return null;
                    }));
                }
            }

            for (Future<NodeTSP> future : futurs) {
                try {
                    NodeTSP fill = future.get();
                    if (fill != null) {
                        cua.add(fill);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        executor.shutdown();

        return new ResultatTSP(millorCami, millorCost[0], nodesExplorats[0], nodesPresents);
    }

    private void bloquejar(int[][] matriu, int fila, int columna) {
        Arrays.fill(matriu[fila], INFINIT);
        for (int i = 0; i < matriu.length; i++) {
            matriu[i][columna] = INFINIT;
        }
    }

    private int[][] copiarMatriu(int[][] original) {
        int n = original.length;
        int[][] copia = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                copia[i][j] = original[i][j];
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
