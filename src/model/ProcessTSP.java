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
     * Constructor que rep el controlador per accedir al model i emetre
     * notificacions.
     *
     * @param controlador controlador principal
     */
    public ProcessTSP(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Punt d’entrada del fil. Obté la matriu de distàncies,
     * executa la resolució i actualitza el model amb els resultats.
     */
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
     * Resol el TSP aplicant Branch and Bound amb matrius reduïdes i execució
     * paral·lela.
     *
     * @param matriuOriginal matriu de distàncies original
     * @param model          referència al model per actualitzar els resultats
     */
    private void resoldreTSP(int[][] matriuOriginal, Model model) {
        int n = matriuOriginal.length;

        AtomicInteger millorCost = new AtomicInteger(INFINIT);
        AtomicReference<List<Integer>> millorCami = new AtomicReference<>(new ArrayList<>());
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

        // Bucle principal: extreure nodes i generar fills en paral·lel
        while (!cua.isEmpty()) {
            NodeTSP node = cua.poll();

            if (node.getCami().size() == n) {
                int costFinal = node.getCost() + matriuOriginal[node.getCiutatActual()][0];
                if (costFinal < millorCost.get()) {
                    millorCost.set(costFinal);
                    List<Integer> ruta = new ArrayList<>(node.getCami());
                    ruta.add(0);
                    millorCami.set(ruta);
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

                        cotaMinima.updateAndGet(min -> Math.min(min, novaCota));
                        cotaMaxima.updateAndGet(max -> Math.max(max, novaCota));

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

        // Guardam els resultats dins el model
        model.setMillorRuta(millorCami.get());
        model.setCostRuta(millorCost.get());
        model.setNodesExplorats(nodesExplorats.get());
        model.setNodesDescartats(nodesDescartats.get());
        model.setCotaMinima(cotaMinima.get());
        model.setCotaMaxima(cotaMaxima.get());
    }

    /**
     * Bloqueja una fila i una columna de la matriu, evitant que es repeteixi
     * una ciutat.
     *
     * @param matriu  matriu a modificar
     * @param fila    fila a bloquejar (ciutat d’origen)
     * @param columna columna a bloquejar (ciutat de destí)
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
     * Aplica reducció per files i columnes a la matriu (pas de minimització).
     *
     * @param matriu matriu a reduir
     * @param n      dimensió de la matriu
     *
     * @return matriu amb valors reduïts
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
     * Calcula la suma mínima de files i columnes per obtenir la reducció.
     *
     * @param matriu matriu reduïda
     * @param n      mida del problema
     *
     * @return valor total de la reducció aplicada
     */
    private int calcularReduccio(int[][] matriu, int n) {
        int suma = 0;
        for (int i = 0; i < n; i++) {
            int minFila = Arrays.stream(matriu[i]).min().orElse(INFINIT);
            if (minFila != INFINIT && minFila > 0) suma += minFila;
        }
        for (int j = 0; j < n; j++) {
            int minCol = INFINIT;
            for (int i = 0; i < n; i++) {
                minCol = Math.min(minCol, matriu[i][j]);
            }
            if (minCol != INFINIT && minCol > 0) suma += minCol;
        }
        return suma;
    }
}
