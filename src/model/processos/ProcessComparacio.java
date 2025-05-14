package model.processos;

import controlador.Controlador;
import controlador.Notificacio;
import java.util.List;
import model.Idioma;
import model.Model;
import model.ResultatComparacio;

/**
 * Fil de procés que s'encarrega de calcular la distància lèxica
 * entre dos idiomes seleccionats pel model.
 *
 * Un cop completada la comparació, el resultat es guarda al model
 * i es notifica la vista perquè actualitzi el panell gràfic.
 *
 * Aquest procés s'executa en segon pla per no bloquejar la interfície gràfica.
 *
 * @author tonitorres
 */
public class ProcessComparacio extends Thread {

    /** Referència al controlador per accedir al model i notificar. */
    private final Controlador controlador;

    /**
     * Constructor que rep el controlador principal del sistema.
     *
     * @param controlador instància que permet accedir al model i generar
     *                    notificacions
     */
    public ProcessComparacio(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Executa el fil. Carrega els idiomes seleccionats i realitza la
     * comparació.
     */
    @Override
    public void run() {
        Model model = controlador.getModel();

        // Carregam els dos idiomes seleccionats pel model
        List<Idioma> diccionaris = model.carregarDiccionaris(
                List.of(model.getIdiomaOrigen(), model.getIdiomaDesti())
        );

        // Assignam l'origen i el destí (si només n'hi ha un, es compara amb ell mateix)
        Idioma origen = diccionaris.get(0);
        Idioma desti = diccionaris.size() > 1 ? diccionaris.get(1) : origen;

        if (origen != null && desti != null) {
            // Realitzam la comparació
            ComparadorIdiomes comparador = new ComparadorIdiomes(true);
            ResultatComparacio resultat = comparador.comparar(origen, desti);

            // Guardam el resultat i actualitzam la vista
            model.setResultats(List.of(resultat));
            controlador.notificar(Notificacio.PINTAR_GRAF);

        } else {
            System.out.println("Error: no s'han pogut carregar els idiomes.");
        }
    }
}
