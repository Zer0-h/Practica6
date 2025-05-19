package vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanellGrafTSP extends JPanel {

    private int[][] matriu;
    private List<Integer> camiOptim;
    private static final int RADIUS = 200;
    private boolean mostrarCostos = false;

    public PanellGrafTSP() {
        setBackground(Color.WHITE);
    }

    public void actualitzarGraf(int[][] matriu) {
        this.matriu = matriu;
        this.camiOptim = null;
        repaint();
    }

    public void mostrarCami(List<Integer> cami) {
        this.camiOptim = cami;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (matriu == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int n = matriu.length;
        Point[] posicions = new Point[n];

        // Dibuixam les ciutats en cercle
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (cx + RADIUS * Math.cos(angle));
            int y = (int) (cy + RADIUS * Math.sin(angle));
            posicions[i] = new Point(x, y);
        }

        Font fontOriginal = g2.getFont();

        // Dibuixar totes les arestes amb el cost visible
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && matriu[i][j] < Integer.MAX_VALUE / 2) {
                    Point p1 = posicions[i];
                    Point p2 = posicions[j];

                    // Dibuixa línia de connexió
                    g2.setColor(new Color(200, 200, 200));
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                    // Dibuixa el valor del cost prop del centre de la línia
                    int mx = (p1.x + p2.x) / 2;
                    int my = (p1.y + p2.y) / 2;

                    int dx = p2.x - p1.x;
                    int dy = p2.y - p1.y;
                    double length = Math.sqrt(dx * dx + dy * dy);

                    // Desplaçament perpendicular per no solapar la línia
                    int offsetX = (int) (-dy / length * 10);
                    int offsetY = (int) (dx / length * 10);

                    if (mostrarCostos) {
                        String costText = String.valueOf(matriu[i][j]);
                        g2.setColor(Color.DARK_GRAY);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                        g2.drawString(costText, mx + offsetX, my + offsetY);
                    }
                }
            }
        }

        g2.setFont(fontOriginal);
        // Dibuixar camí òptim si hi és
        if (camiOptim != null && camiOptim.size() > 1) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2.5f));
            for (int i = 0; i < camiOptim.size() - 1; i++) {
                int from = camiOptim.get(i);
                int to = camiOptim.get(i + 1);
                g2.drawLine(posicions[from].x, posicions[from].y, posicions[to].x, posicions[to].y);
            }
        }

        // Dibuixar els nodes
        for (int i = 0; i < n; i++) {
            Point p = posicions[i];

            g2.setColor(Color.WHITE);
            g2.fillOval(p.x - 20, p.y - 20, 40, 40);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 20, p.y - 20, 40, 40);

            // Etiqueta: 'A', 'B', 'C', ...
            String nom = String.valueOf((char) ('A' + i));

            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(nom);
            int textHeight = fm.getAscent();
            g2.drawString(nom, p.x - textWidth / 2, p.y + textHeight / 2 - 2);
        }
    }

    public void setMostrarCostos(boolean mostrar) {
        this.mostrarCostos = mostrar;
        repaint();
    }
}
