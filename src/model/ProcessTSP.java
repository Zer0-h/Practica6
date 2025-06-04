package model;

import controlador.Controlador;
import controlador.Notificacio;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

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

    /**
     * Constructor que rep el controlador principal del sistema.
     *
     * @param controlador controlador principal (MVC)
     */
    public ProcessTSP(Controlador controlador) {
        this.controlador = controlador;
    }

    /** Punt d’entrada del fil: resol el TSP i actualitza el model i la vista. */
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

    /**
     * Resol el TSP aplicant Branch and Bound amb matrius reduïdes
     * i generació de fills en paral·lel.
     *
     * @param matriuOriginal matriu de distàncies del graf original
     * @param model          referència al model per guardar els resultats
     */
    private void resoldreTSP(int[][] matriuOriginal, Model model) {
        int n = matriuOriginal.length;

        // Variables compartides per guardar l’estat global
        AtomicInteger millorCost = new AtomicInteger(INFINIT);
        List<Integer> millorCami = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger nodesExplorats = new AtomicInteger(0);
        AtomicInteger nodesDescartats = new AtomicInteger(0);
        AtomicInteger cotaMinima = new AtomicInteger(Integer.MAX_VALUE);
        AtomicInteger cotaMaxima = new AtomicInteger(Integer.MIN_VALUE);

        PriorityQueue<NodeTSP> cua = new PriorityQueue<>();
        List<Integer> camiInicial = new ArrayList<>();
        camiInicial.add(0);

        int[][] matReducida = reduirMatriu(copiarMatriu(matriuOriginal), n);
        int cotaInicial = calcularReduccio(matReducida, n);

        cua.add(new NodeTSP(camiInicial, matReducida, 0, cotaInicial, 0));

        ExecutorService executor = Executors.newFixedThreadPool(
                Math.max(2, Runtime.getRuntime().availableProcessors())
        );

        // Bucle principal: extreu nodes i genera fills en paral·lel
        while (!cua.isEmpty()) {
            NodeTSP node = cua.poll();

            if (node.getCami().size() == n) {
                int costFinal = node.getCost() + matriuOriginal[node.getCiutatActual()][0];
                if (costFinal < millorCost.get()) {
                    millorCost.set(costFinal);
                    millorCami.clear();
                    millorCami.addAll(node.getCami());
                    millorCami.add(0); // tornada a l’origen
                }
                continue;
            }

            List<Future<NodeTSP>> futurs = new ArrayList<>();

            for (int ciutat = 0; ciutat < n; ciutat++) {
                if (!node.getCami().contains(ciutat)) {
                    final int ciutatFinal = ciutat;
                    futurs.add(executor.submit(() -> {
                        int dist = node.getMatriuReduida()[node.getCiutatActual()][ciutatFinal];
                        if (dist >= INFINIT) {
                            return null;
                        }

                        List<Integer> nouCami = new ArrayList<>(node.getCami());
                        nouCami.add(ciutatFinal);

                        int[][] novaMatriu = copiarMatriu(node.getMatriuReduida());
                        bloquejar(novaMatriu, node.getCiutatActual(), ciutatFinal);
                        novaMatriu[ciutatFinal][0] = INFINIT;

                        int reduccio = calcularReduccio(novaMatriu, n);
                        int nouCost = node.getCost() + dist;
                        int novaCota = nouCost + reduccio;

                        cotaMinima.getAndUpdate(min -> Math.min(min, novaCota));
                        cotaMaxima.getAndUpdate(max -> Math.max(max, novaCota));

                        if (novaCota < millorCost.get()) {
                            nodesExplorats.incrementAndGet();
                            return new NodeTSP(nouCami, novaMatriu, nouCost, novaCota, ciutatFinal);
                        } else {
                            nodesDescartats.incrementAndGet();
                            return null;
                        }
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

        // Actualitza el model amb els resultats finals
        model.setMillorRuta(millorCami);
        model.setCostRuta(millorCost.get());
        model.setNodesExplorats(nodesExplorats.get());
        model.setNodesDescartats(nodesDescartats.get());
        model.setCotaMinima(cotaMinima.get());
        model.setCotaMaxima(cotaMaxima.get());
    }

    /**
     * Bloqueja una fila i una columna de la matriu per evitar tornar enrere.
     */
    private void bloquejar(int[][] matriu, int fila, int columna) {
        Arrays.fill(matriu[fila], INFINIT);
        for (int i = 0; i < matriu.length; i++) {
            matriu[i][columna] = INFINIT;
        }
    }

    /**
     * Fa una còpia profunda d’una matriu d’enters.
     */
    private int[][] copiarMatriu(int[][] original) {
        int n = original.length;
        int[][] copia = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, n);
        }
        return copia;
    }

    /**
     * Aplica una reducció per files i columnes i retorna la matriu modificada.
     */
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

    /**
     * Calcula el cost total de la reducció aplicada a la matriu.
     */
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
