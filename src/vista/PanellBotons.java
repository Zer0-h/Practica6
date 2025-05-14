package vista;

import controlador.Controlador;
import controlador.Notificacio;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import model.Model;

/**
 * Panell superior que permet seleccionar idiomes i llançar accions de
 * comparació.
 * Forma part de la vista del patró MVC i emet notificacions al controlador.
 *
 * Aquest panell conté dos JComboBox per triar els idiomes i dos botons per
 * iniciar
 * una comparació entre dos o entre un i tots els altres idiomes disponibles.
 *
 * @author tonitorres
 */
public class PanellBotons extends JPanel {

    /** Selector desplegable per triar l'idioma origen. */
    private final JComboBox<String> comboOrigen;

    /** Selector desplegable per triar l'idioma destí. */
    private final JComboBox<String> comboDesti;

    /** Botó per iniciar la comparació entre dos idiomes. */
    private final JButton botoUnaComparacio;

    /** Botó per iniciar la comparació entre un idioma i tots els altres. */
    private final JButton botoCompararTots;

    /** Diccionari que associa el codi curt d’un idioma amb el seu nom complet. */
    private final Map<String, String> mapaIdiomes;

    /**
     * Constructor que inicialitza i organitza els components gràfics del
     * panell.
     *
     * @param controlador Referència al controlador principal de l’aplicació
     */
    public PanellBotons(Controlador controlador) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // Inicialitzem els idiomes disponibles (codi -> nom complet)
        mapaIdiomes = new LinkedHashMap<>();
        mapaIdiomes.put("ale", "Alemany");
        mapaIdiomes.put("cat", "Català");
        mapaIdiomes.put("eus", "Euskera");
        mapaIdiomes.put("fra", "Francès");
        mapaIdiomes.put("hol", "Holandès");
        mapaIdiomes.put("eng", "Anglès");
        mapaIdiomes.put("ita", "Italià");
        mapaIdiomes.put("nor", "Noruec");
        mapaIdiomes.put("por", "Portuguès");
        mapaIdiomes.put("esp", "Espanyol");
        mapaIdiomes.put("swe", "Suec");

        // Creem els models dels ComboBox
        DefaultComboBoxModel<String> modelOrigen = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> modelDesti = new DefaultComboBoxModel<>();

        for (String codi : mapaIdiomes.keySet()) {
            modelOrigen.addElement(mapaIdiomes.get(codi));
            modelDesti.addElement(mapaIdiomes.get(codi));
        }

        // ComboBox de selecció d'idioma origen
        comboOrigen = new JComboBox<>(modelOrigen);
        comboOrigen.setPreferredSize(new Dimension(150, 30));

        // ComboBox de selecció d'idioma destí
        comboDesti = new JComboBox<>(modelDesti);
        comboDesti.setPreferredSize(new Dimension(150, 30));

        // Botó per comparar dos idiomes seleccionats
        botoUnaComparacio = new JButton("Comparar amb un altre idioma");
        botoUnaComparacio.addActionListener(e -> {
            Model model = controlador.getModel();
            String origenCodi = obtenirCodi((String) comboOrigen.getSelectedItem());
            String destiCodi = obtenirCodi((String) comboDesti.getSelectedItem());
            model.setIdiomes(origenCodi, destiCodi);
            controlador.notificar(Notificacio.COMPARAR_DOS);
        });

        // Botó per comparar un idioma amb tots els altres
        botoCompararTots = new JButton("Comparar amb tots els altres");
        botoCompararTots.addActionListener(e -> {
            Model model = controlador.getModel();
            String origenCodi = obtenirCodi((String) comboOrigen.getSelectedItem());
            String destiCodi = obtenirCodi((String) comboDesti.getSelectedItem());
            model.setIdiomes(origenCodi, destiCodi);
            controlador.notificar(Notificacio.COMPARAR_TOTS);
        });

        // Afegim tots els components al panell (distribució horitzontal)
        add(new JLabel("Idioma Origen:"));
        add(comboOrigen);
        add(Box.createHorizontalStrut(10));
        add(new JLabel("Idioma Destí:"));
        add(comboDesti);
        add(Box.createHorizontalStrut(10));
        add(botoUnaComparacio);
        add(botoCompararTots);
    }

    /**
     * Obté el codi curt associat a un nom complet d’idioma.
     *
     * @param nomComplet nom complet seleccionat al JComboBox
     *
     * @return codi corresponent (ex: "cat" per "Català")
     */
    private String obtenirCodi(String nomComplet) {
        for (Map.Entry<String, String> entry : mapaIdiomes.entrySet()) {
            if (entry.getValue().equals(nomComplet)) {
                return entry.getKey();
            }
        }
        return nomComplet;
    }
}
