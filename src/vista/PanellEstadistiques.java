package vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanellEstadistiques extends JPanel {

    private final JLabel labelCost;
    private final JLabel labelExplorats;
    private final JLabel labelPresents;
    private final JTextArea areaRuta;

    public PanellEstadistiques() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Estadístiques"));

        JPanel panellDades = new JPanel(new GridLayout(1, 3, 20, 10));
        labelCost = new JLabel("Cost total: -");
        labelExplorats = new JLabel("Nodes explorats: -");
        labelPresents = new JLabel("Nodes presents: -");
        panellDades.add(labelCost);
        panellDades.add(labelExplorats);
        panellDades.add(labelPresents);

        add(panellDades, BorderLayout.NORTH);

        areaRuta = new JTextArea("Ruta òptima: -");
        areaRuta.setEditable(false);
        areaRuta.setLineWrap(false);
        areaRuta.setWrapStyleWord(false);
        areaRuta.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(areaRuta);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(100, 40));

        add(scroll, BorderLayout.CENTER);
    }

    public void actualitzarRuta(List<Integer> ruta) {
        if (ruta == null || ruta.isEmpty()) {
            areaRuta.setText("Ruta òptima: -");
            return;
        }

        StringBuilder sb = new StringBuilder("Ruta òptima: ");
        for (int i = 0; i < ruta.size(); i++) {
            char lletra = (char) ('A' + ruta.get(i));
            sb.append(lletra);
            if (i < ruta.size() - 1) sb.append(" → ");
        }
        areaRuta.setText(sb.toString());
        areaRuta.setCaretPosition(0); // mou el scroll al principi
    }

    public void actualitzarEstadistiques(int cost, int explorats, int presents, List<Integer> ruta) {
        labelCost.setText("Cost total: " + (cost >= 0 ? cost : "-"));
        labelExplorats.setText("Nodes explorats: " + (explorats >= 0 ? explorats : "-"));
        labelPresents.setText("Nodes presents: " + (presents >= 0 ? presents : "-"));
        actualitzarRuta(ruta);
    }

    public void reiniciar() {
        actualitzarEstadistiques(-1, -1, -1, null);
    }
}
