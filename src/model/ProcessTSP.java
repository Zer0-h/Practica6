package model;

import controlador.Controlador;
import controlador.Notificacio;

public class ProcessTSP extends Thread {

    private final Controlador controlador;

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

        BranchAndBoundTSP solver = new BranchAndBoundTSP(matriu);
        ResultatTSP resultat = solver.resoldre();

        // Actualitzar el model amb la solució trobada
        model.setMillorRuta(resultat.getRuta());
        model.setCostRuta(resultat.getCost());
        model.setNodesExplorats(resultat.getNodesExplorats());
        model.setNodesPresents(resultat.getNodesPresents());

        // Notificar la vista per actualitzar resultats
        controlador.notificar(Notificacio.PINTAR_RESULTAT);
    }
}
