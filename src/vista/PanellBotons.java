package vista;

import controlador.Controlador;
import controlador.Notificacio;
import java.awt.*;
import javax.swing.*;
import model.Model;

/**
 * Panell superior que conté els controls per generar un graf aleatori
 * i resoldre el problema del viatjant de comerç (TSP).
 *
 * Inclou opcions per ajustar el nombre de ciutats, el cost màxim entre ciutats,
 * la densitat de connexions i un checkbox per mostrar o ocultar els costos als
 * arcs.
 *
 * Forma part de la vista del patró MVC i emet notificacions al controlador.
 *
 * @author tonitorres
 */
public class PanellBotons extends JPanel {

    /**
     * Constructor que inicialitza els components del panell i configura els
     * esdeveniments que emeten notificacions al controlador.
     *
     * @param controlador instància principal del controlador MVC
     */
    public PanellBotons(Controlador controlador) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Spinner per seleccionar el nombre de ciutats
        add(new JLabel("Nombre de ciutats:"));
        JSpinner spinnerCiutats = new JSpinner(new SpinnerNumberModel(6, 3, 26, 1));
        add(spinnerCiutats);

        // Spinner per definir el cost màxim entre ciutats
        add(new JLabel("Cost màxim:"));
        JSpinner spinnerCost = new JSpinner(new SpinnerNumberModel(100, 10, 999, 10));
        add(spinnerCost);

        // Slider per controlar la densitat del graf (percentatge)
        add(new JLabel("Densitat:"));
        JSlider sliderDensitat = new JSlider(0, 100, 40);
        sliderDensitat.setMajorTickSpacing(25);
        sliderDensitat.setMinorTickSpacing(5);
        sliderDensitat.setPaintTicks(true);
        sliderDensitat.setPaintLabels(true);
        add(sliderDensitat);

        // Etiqueta que mostra el valor actual del slider de densitat
        JLabel labelValorDensitat = new JLabel("40%");
        add(labelValorDensitat);

        // Actualitzar el text quan es mou el slider
        sliderDensitat.addChangeListener(e -> {
            int valor = sliderDensitat.getValue();
            labelValorDensitat.setText(valor + "%");
        });

        // Botó per generar un nou graf aleatori amb els paràmetres definits
        JButton botoGenerar = new JButton("Generar graf");
        botoGenerar.addActionListener(e -> {
            Model model = controlador.getModel();
            model.setNumCiutats((int) spinnerCiutats.getValue());
            model.setMaxCost((int) spinnerCost.getValue());
            model.setDensitat(sliderDensitat.getValue() / 100.0);
            controlador.notificar(Notificacio.GENERAR_GRAF);
        });

        // Botó per iniciar la resolució del TSP
        JButton botoResoldre = new JButton("Resoldre TSP");
        botoResoldre.addActionListener(e -> controlador.notificar(Notificacio.RESOLDRE_TSP));

        // Checkbox per mostrar o ocultar els valors de cost a la visualització del graf
        JCheckBox checkMostrarCostos = new JCheckBox("Mostrar costos", false);
        add(checkMostrarCostos);

        checkMostrarCostos.addActionListener(e -> {
            boolean mostrar = checkMostrarCostos.isSelected();
            Vista vista = (Vista) SwingUtilities.getWindowAncestor(this);
            vista.setMostrarCostosArcs(mostrar);
        });

        // Afegim els botons a la barra
        add(botoGenerar);
        add(botoResoldre);
    }
}
