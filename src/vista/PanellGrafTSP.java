package vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanellGrafTSP extends JPanel {

    private int[][] matriu;
    private List<Integer> camiOptim;
    private static final int RADIUS = 200;

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

        // Dibuixar totes les arestes (en gris clar)
        g2.setColor(new Color(200, 200, 200));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && matriu[i][j] < Integer.MAX_VALUE / 2) {
                    g2.drawLine(posicions[i].x, posicions[i].y, posicions[j].x, posicions[j].y);
                }
            }
        }

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
            g2.drawString("C" + i, p.x - 8, p.y + 5);
        }
    }
}
