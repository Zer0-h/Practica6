package controlador;

import model.GeneradorGraf;
import model.Model;
import model.ProcessTSP;
import vista.Vista;

/**
 * Classe principal del controlador de l'aplicació (patró MVC).
 * Gestiona la comunicació entre la Vista i el Model mitjançant notificacions.
 * S'encarrega de generar grafs i d'iniciar el procés de resolució del TSP.
 *
 * @author tonitorres
 */
public class Controlador implements Notificar {

    /** Model de dades de l'aplicació. */
    private Model model;

    /** Vista gràfica de l'aplicació. */
    private Vista vista;

    /**
     * Punt d'entrada principal de l'aplicació.
     * Crea i inicialitza el controlador.
     *
     * @param args arguments d'execució (no utilitzats)
     */
    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    /**
     * Inicialitza el model i la vista, establint la comunicació entre ambdues.
     */
    private void inicialitzar() {
        model = new Model();
        vista = new Vista(this);
    }

    /**
     * Genera un nou graf aleatori segons els paràmetres del model
     * i actualitza la visualització.
     */
    private void generarGraf() {
        model.reset();
        int[][] matriu = GeneradorGraf.generarMatriu(
                model.getNumCiutats(),
                model.getMaxCost(),
                model.getDensitat()
        );
        model.setMatriuDistancies(matriu);
        notificar(Notificacio.PINTAR_GRAF);
    }

    /**
     * Inicia el procés de resolució del problema del viatjant de comerç (TSP)
     * amb l'algorisme de Branch and Bound, en un fil separat.
     */
    private void resoldreTSP() {
        model.reset();
        vista.notificar(Notificacio.PINTAR_GRAF); // opcional: redibuixa el graf abans de calcular
        new ProcessTSP(this).start();
    }

    /**
     * Retorna el model de l'aplicació.
     *
     * @return instància del model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Gestor d'esdeveniments del sistema. Rep notificacions de la vista
     * i activa l'acció corresponent segons el tipus de notificació.
     *
     * @param notificacio tipus de notificació rebuda
     */
    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case GENERAR_GRAF ->
                generarGraf();
            case RESOLDRE_TSP ->
                resoldreTSP();
            case PINTAR_RESULTAT ->
                vista.notificar(Notificacio.PINTAR_RESULTAT);
            case PINTAR_GRAF ->
                vista.notificar(Notificacio.PINTAR_GRAF);
        }
    }
}
