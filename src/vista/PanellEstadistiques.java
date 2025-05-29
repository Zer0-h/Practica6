package vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * Panell inferior dedicat a mostrar les estadístiques del càlcul del TSP:
 * cost total de la ruta òptima, nombre de nodes explorats, nombre de nodes
 * que han estat presents a la cua i la ruta òptima trobada.
 *
 * Inclou una àrea de text amb scroll per mostrar la ruta òptima completa.
 *
 * Forma part de la vista del patró MVC.
 *
 * @author tonitorres
 */
public class PanellEstadistiques extends JPanel {

    /** Etiqueta per mostrar el cost total de la ruta òptima. */
    private final JLabel labelCost;

    /** Etiqueta per mostrar el nombre de nodes explorats. */
    private final JLabel labelExplorats;

    /** Etiqueta per mostrar el nombre de nodes presents a la cua. */
    private final JLabel labelPresents;

    /** Àrea de text que mostra la ruta òptima trobada. */
    private final JTextArea areaRuta;

    /**
     * Constructor que inicialitza el panell i tots els seus components visuals.
     */
    public PanellEstadistiques() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Estadístiques"));

        // Panell superior amb estadístiques resumides
        JPanel panellDades = new JPanel(new GridLayout(1, 3, 20, 10));
        labelCost = new JLabel("Cost total: -");
        labelExplorats = new JLabel("Nodes explorats: -");
        labelPresents = new JLabel("Nodes presents: -");
        panellDades.add(labelCost);
        panellDades.add(labelExplorats);
        panellDades.add(labelPresents);

        add(panellDades, BorderLayout.NORTH);

        // Àrea de text per mostrar la ruta òptima
        areaRuta = new JTextArea("Ruta òptima: -");
        areaRuta.setEditable(false);
        areaRuta.setLineWrap(false);
        areaRuta.setWrapStyleWord(false);
        areaRuta.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Scroll horitzontal si la ruta és molt llarga
        JScrollPane scroll = new JScrollPane(areaRuta);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(100, 40));

        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Actualitza el text de la ruta òptima amb una nova llista de ciutats.
     *
     * @param ruta llista d’índexs de ciutats que formen la ruta òptima
     */
    public void actualitzarRuta(List<Integer> ruta) {
        if (ruta == null || ruta.isEmpty()) {
            areaRuta.setText("Ruta òptima: -");
            return;
        }

        StringBuilder sb = new StringBuilder("Ruta òptima: ");
        for (int i = 0; i < ruta.size(); i++) {
            char lletra = (char) ('A' + ruta.get(i));
            sb.append(lletra);
            if (i < ruta.size() - 1) {
                sb.append(" → ");
            }
        }

        areaRuta.setText(sb.toString());
        areaRuta.setCaretPosition(0);
    }

    /**
     * Mostra totes les estadístiques del càlcul del TSP.
     *
     * @param cost      cost total de la ruta òptima
     * @param explorats nombre de nodes explorats
     * @param presents  nombre total de nodes presents a la cua
     * @param ruta      ruta òptima trobada
     */
    public void actualitzarEstadistiques(int cost, int explorats, int presents, List<Integer> ruta) {
        labelCost.setText("Cost total: " + (cost >= 0 ? cost : "-"));
        labelExplorats.setText("Nodes explorats: " + (explorats >= 0 ? explorats : "-"));
        labelPresents.setText("Nodes presents: " + (presents >= 0 ? presents : "-"));
        actualitzarRuta(ruta);
    }

    /**
     * Reinicia les estadístiques a l’estat inicial (guions).
     */
    public void reiniciar() {
        actualitzarEstadistiques(-1, -1, -1, null);
    }
}
