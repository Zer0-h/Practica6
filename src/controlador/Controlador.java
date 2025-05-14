package controlador;

import model.Model;
import model.GeneradorGraf;
import model.ProcessTSP;
import vista.Vista;

public class Controlador implements Notificar {

    private Model model;
    private Vista vista;

    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    private void inicialitzar() {
        model = new Model();
        vista = new Vista(this);
    }

    private void generarGraf() {
        model.reset();
        int[][] matriu = GeneradorGraf.generarMatriu(model.getNumCiutats(), model.getMaxCost());
        model.setMatriuDistancies(matriu);
        notificar(Notificacio.PINTAR_GRAF);
    }

    private void resoldreTSP() {
        model.reset();
        vista.notificar(Notificacio.PINTAR_GRAF); // opcional: esborra graf anterior
        new ProcessTSP(this).start();
    }

    public Model getModel() {
        return model;
    }

    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case GENERAR_GRAF -> generarGraf();
            case RESOLDRE_TSP -> resoldreTSP();
            case PINTAR_RESULTAT -> vista.notificar(Notificacio.PINTAR_RESULTAT);
            case PINTAR_GRAF -> vista.notificar(Notificacio.PINTAR_GRAF);
        }
    }
}
