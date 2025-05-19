package vista;

import controlador.Controlador;
import controlador.Notificacio;

import javax.swing.*;
import java.awt.*;
import model.Model;

/**
 * Panell superior amb botons per generar un graf i resoldre el TSP.
 * Forma part de la vista del patró MVC.
 *
 * @author tonitorres
 */
public class PanellBotonsTSP extends JPanel {

    public PanellBotonsTSP(Controlador controlador) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        add(new JLabel("Nombre de ciutats:"));
        JSpinner spinnerCiutats = new JSpinner(new SpinnerNumberModel(6, 3, 20, 1));
        add(spinnerCiutats);

        add(new JLabel("Cost màxim:"));
        JSpinner spinnerCost = new JSpinner(new SpinnerNumberModel(100, 10, 999, 10));
        add(spinnerCost);

        add(new JLabel("Densitat:"));

        JSlider sliderDensitat = new JSlider(0, 100, 40);
        sliderDensitat.setMajorTickSpacing(25);
        sliderDensitat.setMinorTickSpacing(5);
        sliderDensitat.setPaintTicks(true);
        sliderDensitat.setPaintLabels(true);
        add(sliderDensitat);

        JLabel labelValorDensitat = new JLabel("40%");
        add(labelValorDensitat);

        sliderDensitat.addChangeListener(e -> {
            int valor = sliderDensitat.getValue();
            labelValorDensitat.setText(valor + "%");
        });

        JButton botoGenerar = new JButton("Generar graf");
        botoGenerar.addActionListener(e -> {
            Model model = controlador.getModel();

            model.setNumCiutats((int) spinnerCiutats.getValue());
            model.setMaxCost((int) spinnerCost.getValue());
            model.setDensitat(sliderDensitat.getValue() / 100.0);
            controlador.notificar(Notificacio.GENERAR_GRAF);
        });

        JButton botoResoldre = new JButton("Resoldre TSP");
        botoResoldre.addActionListener(e -> controlador.notificar(Notificacio.RESOLDRE_TSP));

        add(botoGenerar);
        add(botoResoldre);
    }
}
