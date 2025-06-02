package model;

import controlador.Controlador;
import controlador.Notificacio;
import java.util.*;
import java.util.concurrent.*;

/**
 * Fil d'execució que resol el problema del viatjant de comerç (TSP)
 * utilitzant l'algorisme de Branch and Bound amb poda i matrius reduïdes.
 *
 * Aquesta versió incorpora concurrència per a la generació de nodes fills,
 * aprofitant múltiples nuclis de CPU mitjançant un `ExecutorService`.
 *
 * Un cop trobada la millor ruta, s'actualitza el model i es notifica la vista.
 * També s’emmagatzemen la cota mínima i màxima trobades durant l’execució.
 *
 * @author tonitorres
 */
public class ProcessTSP extends Thread {

    /** Referència al controlador principal. */
    private final Controlador controlador;

    /** Valor utilitzat per representar connexions inexistents. */
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

        resoldreTSP(matriu, model);

        controlador.notificar(Notificacio.PINTAR_RESULTAT);
    }

    private void resoldreTSP(int[][] matriuOriginal, Model model) {
        int n = matriuOriginal.length;

        final int[] millorCost = {INFINIT};
        List<Integer> millorCami = Collections.synchronizedList(new ArrayList<>());
        int[] nodesExplorats = {0};
        int nodesPresents = 0;

        final int[] cotaMinima = {Integer.MAX_VALUE};
        final int[] cotaMaxima = {Integer.MIN_VALUE};

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
                            // Actualitzam cotes trobades
                            cotaMinima[0] = Math.min(cotaMinima[0], novaCota);
                            cotaMaxima[0] = Math.max(cotaMaxima[0], novaCota);

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

        // Guardam els resultats

        model.setMillorRuta(millorCami);
        model.setCostRuta(millorCost[0]);
        model.setNodesExplorats(nodesExplorats[0]);
        model.setNodesPresents(nodesPresents);
        model.setCotaMinima(cotaMinima[0]);
        model.setCotaMaxima(cotaMaxima[0]);
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
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copia[i][j] = original[i][j];
            }
        }
        return copia;
    }

    private int[][] reduirMatriu(int[][] matriu, int n) {
        for (int i = 0; i < n; i++) {
            int min = Arrays.stream(matriu[i]).min().orElse(INFINIT);
            if (min != INFINIT && min > 0) {
                for (int j = 0; j < n; j++) {
                    if (matriu[i][j] != INFINIT) {
                        matriu[i][j] -= min;
                    }
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
                    if (matriu[i][j] != INFINIT) {
                        matriu[i][j] -= min;
                    }
                }
            }
        }
        return matriu;
    }

    private int calcularReduccio(int[][] matriu, int n) {
        int suma = 0;
        for (int i = 0; i < n; i++) {
            int min = Arrays.stream(matriu[i]).min().orElse(INFINIT);
            if (min != INFINIT && min > 0) {
                suma += min;
            }
        }
        for (int j = 0; j < n; j++) {
            int min = INFINIT;
            for (int i = 0; i < n; i++) {
                min = Math.min(min, matriu[i][j]);
            }
            if (min != INFINIT && min > 0) {
                suma += min;
            }
        }
        return suma;
    }
}
