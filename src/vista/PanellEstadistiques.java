package vista;

import java.awt.GridLayout;
import javax.swing.*;
import java.util.List;

/**
 * Panell inferior per mostrar estadístiques del càlcul del TSP.
 * S'actualitza quan es troba una solució.
 */
public class PanellEstadistiques extends JPanel {

    private final JLabel labelCost;
    private final JLabel labelExplorats;
    private final JLabel labelPresents;
    private final JLabel labelRuta;

    public PanellEstadistiques() {
        setLayout(new GridLayout(1, 3, 20, 10));
        setBorder(BorderFactory.createTitledBorder("Estadístiques"));

        labelCost = new JLabel("Cost total: -");
        labelExplorats = new JLabel("Nodes explorats: -");
        labelPresents = new JLabel("Nodes presents: -");
        labelRuta = new JLabel("Ruta òptima: -");

        add(labelCost);
        add(labelExplorats);
        add(labelPresents);
        add(labelRuta);
    }

    public void actualitzarRuta(List<Integer> ruta) {
        if (ruta == null || ruta.isEmpty()) {
            labelRuta.setText("Ruta òptima: -");
            return;
        }

        StringBuilder sb = new StringBuilder("Ruta òptima: ");
        for (int i = 0; i < ruta.size(); i++) {
            char lletra = (char) ('A' + ruta.get(i));
            sb.append(lletra);
            if (i < ruta.size() - 1) sb.append(" → ");
        }
        labelRuta.setText(sb.toString());
    }

    public void actualitzarEstadistiques(int cost, int explorats, int presents, List<Integer> ruta) {
        labelCost.setText("Cost total: " + cost);
        labelExplorats.setText("Nodes explorats: " + explorats);
        labelPresents.setText("Nodes presents: " + presents);
        actualitzarRuta(ruta);
    }

    public void reiniciar() {
        actualitzarEstadistiques(-1, -1, -1, null);
    }
}
