package vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * Panell central de la GUI que representa visualment el graf de ciutats
 * i la ruta òptima trobada amb Branch and Bound per al problema del TSP.
 *
 * Els nodes es dibuixen en disposició circular i les arestes poden mostrar
 * els costos si l'opció està activada. També es destaca la ruta òptima en
 * vermell.
 *
 * Forma part de la vista del patró MVC.
 *
 * @author tonitorres
 */
public class PanellGraf extends JPanel {

    /** Matriu d’adjacència amb les distàncies entre ciutats. */
    private int[][] matriu;

    /** Ruta òptima trobada (ordre dels nodes a recórrer). */
    private List<Integer> camiOptim;

    /** Radi del cercle on es distribueixen les ciutats. */
    private static final int RADIUS = 200;

    /** Indica si s’han de mostrar els costos de les arestes. */
    private boolean mostrarCostos = false;

    /** Constructor que inicialitza el fons blanc del panell. */
    public PanellGraf() {
        setBackground(Color.WHITE);
    }

    /**
     * Actualitza el graf mostrant una nova matriu de distàncies.
     *
     * @param matriu nova matriu d’adjacència del graf
     */
    public void actualitzarGraf(int[][] matriu) {
        this.matriu = matriu;
        this.camiOptim = null;
        repaint();
    }

    /**
     * Mostra la ruta òptima sobre el graf actual.
     *
     * @param cami llista d’índexs que representen la ruta òptima
     */
    public void mostrarCami(List<Integer> cami) {
        this.camiOptim = cami;
        repaint();
    }

    /**
     * Dibuixa tot el graf i la ruta òptima, si està disponible.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (matriu == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int n = matriu.length;
        Point[] posicions = new Point[n];

        // -- Posició dels nodes en cercle --
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (cx + RADIUS * Math.cos(angle));
            int y = (int) (cy + RADIUS * Math.sin(angle));
            posicions[i] = new Point(x, y);
        }

        Font fontOriginal = g2.getFont();

        // -- Dibuix de totes les arestes --
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && matriu[i][j] < Integer.MAX_VALUE / 2) {
                    Point p1 = posicions[i];
                    Point p2 = posicions[j];

                    g2.setColor(new Color(200, 200, 200));
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                    // -- Cost opcional de l’arc --
                    if (mostrarCostos) {
                        int mx = (p1.x + p2.x) / 2;
                        int my = (p1.y + p2.y) / 2;

                        int dx = p2.x - p1.x;
                        int dy = p2.y - p1.y;
                        double length = Math.sqrt(dx * dx + dy * dy);
                        int offsetX = (int) (-dy / length * 10);
                        int offsetY = (int) (dx / length * 10);

                        String costText = String.valueOf(matriu[i][j]);
                        g2.setColor(Color.DARK_GRAY);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                        g2.drawString(costText, mx + offsetX, my + offsetY);
                    }
                }
            }
        }

        g2.setFont(fontOriginal);

        // -- Dibuix de la ruta òptima --
        if (camiOptim != null && camiOptim.size() > 1) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2.5f));
            for (int i = 0; i < camiOptim.size() - 1; i++) {
                int from = camiOptim.get(i);
                int to = camiOptim.get(i + 1);
                g2.drawLine(posicions[from].x, posicions[from].y, posicions[to].x, posicions[to].y);
            }
        }

        // -- Dibuix dels nodes (ciutats) --
        for (int i = 0; i < n; i++) {
            Point p = posicions[i];

            g2.setColor(Color.WHITE);
            g2.fillOval(p.x - 20, p.y - 20, 40, 40);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 20, p.y - 20, 40, 40);

            // Etiqueta amb la lletra corresponent
            String nom = String.valueOf((char) ('A' + i));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(nom);
            int textHeight = fm.getAscent();
            g2.drawString(nom, p.x - textWidth / 2, p.y + textHeight / 2 - 2);
        }
    }

    /**
     * Habilita o deshabilita la visualització dels costos de les arestes.
     *
     * @param mostrar true per mostrar els valors, false per ocultar-los
     */
    public void setMostrarCostos(boolean mostrar) {
        this.mostrarCostos = mostrar;
        repaint();
    }
}
