package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panell inferior per mostrar estadístiques del càlcul del TSP.
 * S'actualitza quan es troba una solució.
 */
public class PanellEstadistiques extends JPanel {

    private final JLabel labelCost;
    private final JLabel labelExplorats;
    private final JLabel labelPresents;

    public PanellEstadistiques() {
        setLayout(new GridLayout(1, 3, 20, 10));
        setBorder(BorderFactory.createTitledBorder("Estadístiques"));

        labelCost = new JLabel("Cost total: -");
        labelExplorats = new JLabel("Nodes explorats: -");
        labelPresents = new JLabel("Nodes presents: -");

        add(labelCost);
        add(labelExplorats);
        add(labelPresents);
    }

    public void actualitzarEstadistiques(int cost, int explorats, int presents) {
        labelCost.setText("Cost total: " + cost);
        labelExplorats.setText("Nodes explorats: " + explorats);
        labelPresents.setText("Nodes presents: " + presents);
    }

    public void reiniciar() {
        actualitzarEstadistiques(-1, -1, -1);
    }
}
