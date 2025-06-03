package vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * Panell inferior dedicat a mostrar les estadístiques del càlcul del TSP:
 * cost total de la ruta òptima, nombre de nodes explorats, nodes descartats
 * per poda, cotes mínima i màxima trobades, i la ruta òptima completa.
 *
 * Les estadístiques es mostren en un panell de dues files i tres columnes
 * per millorar la llegibilitat. La ruta es mostra en una àrea de text amb
 * scroll horitzontal en cas que sigui molt llarga.
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

    /** Etiqueta per mostrar el nombre de nodes descartats per poda. */
    private final JLabel labelDescartats;

    /** Etiqueta per mostrar la cota mínima trobada durant l’execució. */
    private final JLabel labelCotaMinima;

    /** Etiqueta per mostrar la cota màxima trobada durant l’execució. */
    private final JLabel labelCotaMaxima;

    /** Àrea de text que mostra la ruta òptima trobada. */
    private final JTextArea areaRuta;

    /**
     * Constructor que inicialitza el panell i tots els seus components visuals.
     */
    public PanellEstadistiques() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Estadístiques"));

        // Panell superior amb estadístiques distribuïdes en dues files
        JPanel panellDades = new JPanel(new GridLayout(2, 3, 20, 10));
        labelCost = new JLabel("Cost total: -");
        labelExplorats = new JLabel("Nodes explorats: -");
        labelDescartats = new JLabel("Nodes descartats: -");
        labelCotaMinima = new JLabel("Cota mínima: -");
        labelCotaMaxima = new JLabel("Cota màxima: -");

        panellDades.add(labelCost);
        panellDades.add(labelExplorats);
        panellDades.add(labelDescartats);
        panellDades.add(labelCotaMinima);
        panellDades.add(labelCotaMaxima);

        add(panellDades, BorderLayout.NORTH);

        // Àrea de text per mostrar la ruta òptima
        areaRuta = new JTextArea("Ruta òptima: -");
        areaRuta.setEditable(false);
        areaRuta.setLineWrap(false);
        areaRuta.setWrapStyleWord(false);
        areaRuta.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Scroll horitzontal per rutes llargues
        JScrollPane scroll = new JScrollPane(areaRuta);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(100, 50));

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
        areaRuta.setCaretPosition(0); // Moure scroll al principi
    }

    /**
     * Mostra totes les estadístiques del càlcul del TSP.
     *
     * @param cost       cost total de la ruta òptima
     * @param explorats  nombre de nodes realment explorats
     * @param descartats nombre de nodes descartats per poda
     * @param cotaMin    cota mínima trobada
     * @param cotaMax    cota màxima trobada
     * @param ruta       ruta òptima trobada
     */
    public void actualitzarEstadistiques(int cost, int explorats, int descartats,
                                          int cotaMin, int cotaMax, List<Integer> ruta) {
        labelCost.setText("Cost total: " + (cost >= 0 ? cost : "-"));
        labelExplorats.setText("Nodes explorats: " + (explorats >= 0 ? explorats : "-"));
        labelDescartats.setText("Nodes descartats: " + (descartats >= 0 ? descartats : "-"));
        labelCotaMinima.setText("Cota mínima: " + (cotaMin >= 0 ? cotaMin : "-"));
        labelCotaMaxima.setText("Cota màxima: " + (cotaMax >= 0 ? cotaMax : "-"));

        actualitzarRuta(ruta);
    }

    /**
     * Reinicia les estadístiques a l’estat inicial (guions).
     */
    public void reiniciar() {
        actualitzarEstadistiques(-1, -1, -1, -1, -1, null);
    }
}
