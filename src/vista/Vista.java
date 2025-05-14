package vista;

import controlador.Controlador;
import controlador.Notificacio;
import controlador.Notificar;
import java.awt.*;
import javax.swing.*;
import model.Model;

/**
 * Classe principal de la interfície gràfica de l’aplicació.
 * Implementa la part visual del patró MVC i rep notificacions del controlador.
 *
 * La finestra principal conté:
 * - Un panell superior amb selectors i botons per iniciar comparacions.
 * - Un panell central per representar gràficament les distàncies entre idiomes.
 *
 * @author tonitorres
 */
public class Vista extends JFrame implements Notificar {

    /** Referència al controlador MVC. */
    private final Controlador controlador;

    /** Panell superior amb selecció d'idiomes i accions. */
    private final PanellBotons panellBotons;

    /** Panell central que mostra el graf de comparacions. */
    private final PanellGraf panellGraf;

    /**
     * Constructor de la finestra principal.
     *
     * @param controlador controlador associat al patró MVC
     */
    public Vista(Controlador controlador) {
        super("Diferenciador d'idiomes");
        this.controlador = controlador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panell de botons i selectors a la part superior
        panellBotons = new PanellBotons(controlador);
        add(panellBotons, BorderLayout.NORTH);

        // Panell del graf al centre
        panellGraf = new PanellGraf();
        add(panellGraf, BorderLayout.CENTER);

        // Configuració de la finestra
        setSize(1400, 1200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Pinta el graf de distàncies a partir dels resultats actuals del model.
     */
    private void pintarResultats() {
        Model model = controlador.getModel();
        panellGraf.pintarResultats(model.getResultatsMultiples(), model.getIdiomaOrigen());
    }

    /**
     * Rep notificacions del controlador i actua en conseqüència.
     *
     * @param notificacio tipus d’esdeveniment rebut
     */
    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case PINTAR_GRAF ->
                pintarResultats();
        }
    }
}
