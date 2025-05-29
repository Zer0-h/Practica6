package vista;

import controlador.Controlador;
import controlador.Notificacio;
import controlador.Notificar;
import java.awt.*;
import javax.swing.*;
import model.Model;

/**
 * Vista principal de l’aplicació TSP resolt amb Branch and Bound.
 *
 * Aquesta classe conté la interfície gràfica general, composta pels panells:
 * - Panell de configuració i accions (PanellBotonsTSP)
 * - Panell de visualització del graf (PanellGrafTSP)
 * - Panell d’estadístiques (PanellEstadistiques)
 *
 * Gestiona les actualitzacions visuals en resposta a les notificacions
 * rebudes del controlador, seguint el patró MVC amb comunicació desacoblada.
 *
 * @author tonitorres
 */
public class Vista extends JFrame implements Notificar {

    /** Referència al controlador del sistema (MVC). */
    private final Controlador controlador;

    /** Panell superior amb botons i selectors de configuració. */
    private final PanellBotons panellBotons;

    /** Panell central per dibuixar el graf i la ruta òptima. */
    private final PanellGraf panellGraf;

    /** Panell inferior per mostrar les estadístiques de l'execució. */
    private final PanellEstadistiques panellEstadistiques;

    /**
     * Constructor que inicialitza la finestra principal i els components de la
     * GUI.
     *
     * @param controlador controlador del sistema
     */
    public Vista(Controlador controlador) {
        super("TSP - Branch and Bound");
        this.controlador = controlador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -- Components de la GUI --
        panellBotons = new PanellBotons(controlador);
        add(panellBotons, BorderLayout.NORTH);

        panellGraf = new PanellGraf();
        add(panellGraf, BorderLayout.CENTER);

        panellEstadistiques = new PanellEstadistiques();
        add(panellEstadistiques, BorderLayout.SOUTH);

        // -- Configuració de la finestra --
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Mostra el graf generat a partir de la matriu actual del model.
     * També reinicia les estadístiques.
     */
    public void pintarGraf() {
        Model model = controlador.getModel();
        panellGraf.actualitzarGraf(model.getMatriuDistancies());
        panellEstadistiques.reiniciar();
    }

    /**
     * Mostra la ruta òptima trobada i les estadístiques del càlcul.
     */
    public void pintarResultat() {
        Model model = controlador.getModel();
        panellGraf.mostrarCami(model.getMillorRuta());
        panellEstadistiques.actualitzarEstadistiques(
                model.getCostRuta(),
                model.getNodesExplorats(),
                model.getNodesPresents(),
                model.getMillorRuta()
        );
    }

    /**
     * Habilita o deshabilita la visualització dels costos als arcs del graf.
     *
     * @param mostrar true per mostrar els valors, false per ocultar-los
     */
    public void setMostrarCostosArcs(boolean mostrar) {
        panellGraf.setMostrarCostos(mostrar);
    }

    /**
     * Gestor de notificacions per a la vista. Rep esdeveniments del controlador
     * i actualitza la GUI en conseqüència.
     *
     * @param notificacio tipus d’acció a realitzar
     */
    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case PINTAR_GRAF ->
                pintarGraf();
            case PINTAR_RESULTAT ->
                pintarResultat();
        }
    }
}
