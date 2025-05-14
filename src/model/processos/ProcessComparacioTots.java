package model.processos;

import controlador.Controlador;
import controlador.Notificacio;
import java.util.List;
import model.Idioma;
import model.Model;
import model.ResultatComparacio;

/**
 * Fil de procés que compara un idioma origen amb tots els altres idiomes
 * disponibles.
 *
 * Per cada idioma comparat, es genera un resultat que s’afegeix al model i
 * s’actualitza la visualització mitjançant notificacions successives.
 *
 * Aquest procés es fa en paral·lel a la interfície gràfica per no bloquejar-la.
 *
 * @author tonitorres
 */
public class ProcessComparacioTots extends Thread {

    /** Referència al controlador principal per accedir al model i notificar. */
    private final Controlador controlador;

    /**
     * Constructor que inicialitza el procés amb el controlador del sistema.
     *
     * @param controlador controlador MVC per accedir al model i enviar
     *                    notificacions
     */
    public ProcessComparacioTots(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Executa el fil de comparació per a tots els idiomes.
     * Compara l'idioma origen amb la resta i notifica la vista progressivament.
     */
    @Override
    public void run() {
        Model model = controlador.getModel();

        // Llista fixa d'idiomes disponibles (fitxers corresponents han d'existir)
        List<Idioma> diccionaris = model.carregarDiccionaris(
                List.of("ale", "cat", "eng", "esp", "eus", "fra", "hol", "ita", "nor", "por", "swe")
        );

        // Cercam l'idioma origen seleccionat al model
        Idioma origen = diccionaris.stream()
                .filter(id -> id.getNom().equals(model.getIdiomaOrigen()))
                .findFirst()
                .orElse(null);

        if (origen == null) {
            System.out.println("No s'ha trobat l'idioma origen: " + model.getIdiomaOrigen());
            return;
        }

        // Cream el comparador (amb informació de temps si verbose = true)
        ComparadorIdiomes comparador = new ComparadorIdiomes(true);

        // Comparam l'idioma origen amb la resta i anem notificant la vista
        for (Idioma altre : diccionaris) {
            if (!altre.getNom().equals(model.getIdiomaOrigen())) {
                ResultatComparacio resultat = comparador.comparar(origen, altre);
                model.afegirResultats(resultat);
                controlador.notificar(Notificacio.PINTAR_GRAF);
            }
        }
    }
}
