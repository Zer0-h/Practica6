package vista;

import controlador.Controlador;
import controlador.Notificacio;
import controlador.Notificar;

import javax.swing.*;
import java.awt.*;
import model.Model;

/**
 * Vista principal de l'aplicació TSP amb Branch and Bound.
 * Conté panell de botons i gràfica (que implementarem després).
 */
public class Vista extends JFrame implements Notificar {

    private final Controlador controlador;
    private final PanellBotonsTSP panellBotons;
    private final PanellGrafTSP panellGraf;
    private final PanellEstadistiques panellEstadistiques;

    public Vista(Controlador controlador) {
        super("TSP - Branch and Bound");
        this.controlador = controlador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panellBotons = new PanellBotonsTSP(controlador);
        add(panellBotons, BorderLayout.NORTH);

        panellGraf = new PanellGrafTSP();
        add(panellGraf, BorderLayout.CENTER);

        panellEstadistiques = new PanellEstadistiques();
        add(panellEstadistiques, BorderLayout.SOUTH);

        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void pintarGraf() {
        Model model = controlador.getModel();
        panellGraf.actualitzarGraf(model.getMatriuDistancies());
    }

    public void pintarResultat() {
        Model model = controlador.getModel();
        panellGraf.mostrarCami(controlador.getModel().getMillorRuta());
        panellEstadistiques.actualitzarEstadistiques(
            model.getCostRuta(),
            model.getNodesExplorats(),
            model.getNodesPresents(),
            model.getMillorRuta()
        );

    }

    public void setMostrarCostosArcs(boolean mostrar) {
        panellGraf.setMostrarCostos(mostrar);
    }

    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case PINTAR_GRAF -> pintarGraf();
            case PINTAR_RESULTAT -> pintarResultat();
        }
    }
}
