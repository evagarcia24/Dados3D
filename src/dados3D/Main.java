package dados3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

// ===================================================================
// CLASE PRINCIPAL
// ===================================================================
public class Main extends JFrame {
    private Dado dado3;
    private Dado dado21;
    private JLabel labelResultado3;
    private JLabel labelResultado21;
    private PanelDado3D panelDado3Visual;
    private PanelDado3D panelDado21Visual;

    public Main() {
        dado3 = new Dado(3);
        dado21 = new Dado(21);

        setTitle("Juego de Rol - Tirar Dados");
        setSize(1150, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = crearPanelConFondo();
        panelPrincipal.setLayout(new BorderLayout(30, 30));

        JLabel titulo = new JLabel("JUEGO DE ROL - TIRADA DE DADOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 50));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelDados = new JPanel(new GridLayout(1, 2, 70, 0));
        panelDados.setOpaque(false);
        panelDados.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        // Prisma triangular
        panelDado3Visual = new PanelDado3D("prisma");
        labelResultado3 = new JLabel("...");
        JPanel panelDado3 = crearPanelDado("Dado de 3 caras", panelDado3Visual, labelResultado3);
        panelDados.add(panelDado3);

        // Icosaedro
        panelDado21Visual = new PanelDado3D("icosaedro");
        labelResultado21 = new JLabel("...");
        JPanel panelDado21 = crearPanelDado("Dado de 21 caras", panelDado21Visual, labelResultado21);
        panelDados.add(panelDado21);

        panelPrincipal.add(panelDados, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 20));

        JButton btnDado3 = crearBoton("Tirar Dado 3 Caras");
        btnDado3.addActionListener(e -> tirarDado3());

        JButton btnDado21 = crearBoton("Tirar Dado 21 Caras");
        btnDado21.addActionListener(e -> tirarDado21());

        JButton btnAmbos = crearBoton("Tirar Ambos Dados");
        btnAmbos.addActionListener(e -> tirarAmbos());

        panelBotones.add(btnDado3);
        panelBotones.add(btnDado21);
        panelBotones.add(btnAmbos);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);
    }

    private JPanel crearPanelConFondo() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, Color.BLACK, 0, getHeight(), new Color(20, 20, 20));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private JPanel crearPanelDado(String titulo, PanelDado3D panelDadoVisual, JLabel lblResultado) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        panelDadoVisual.setPreferredSize(new Dimension(320, 320));
        panelDadoVisual.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblResultado.setFont(new Font("Arial", Font.BOLD, 28));
        lblResultado.setForeground(Color.WHITE);
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblResultado.setOpaque(true);
        lblResultado.setBackground(new Color(255, 255, 255, 70));
        lblResultado.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(panelDadoVisual);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblResultado);

        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(Color.BLACK);
        boton.setBackground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                boton.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(MouseEvent evt) {
                boton.setBackground(Color.WHITE);
            }
        });
        return boton;
    }

    private void tirarDado3() {
        animarDado(panelDado3Visual, labelResultado3, dado3);
    }

    private void tirarDado21() {
        animarDado(panelDado21Visual, labelResultado21, dado21);
    }

    private void tirarAmbos() {
        tirarDado3();
        Timer timer = new Timer(100, e -> tirarDado21());
        timer.setRepeats(false);
        timer.start();
    }

    private void animarDado(final PanelDado3D panelDado, final JLabel labelResultado, final Dado dado) {
        labelResultado.setText("Girando...");
        final int[] contador = {0};
        Timer animacion = new Timer(80, e -> {
            panelDado.rotar();
            contador[0]++;
            if (contador[0] >= 40) {
                ((Timer) e.getSource()).stop();
                int resultado = dado.tirar();
                labelResultado.setText("Resultado: " + resultado);
            }
        });
        animacion.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main ventana = new Main();
            ventana.setVisible(true);
        });
    }
}

// ===================================================================
// PanelDado3D: prisma triangular para D3, icosaedro para D21
// ===================================================================
class PanelDado3D extends JPanel {
    private String tipo; // "prisma" o "icosaedro"
    private double rotX, rotY, rotZ;

    public PanelDado3D(String tipo) {
        this.tipo = tipo;
        this.rotX = 0.4;
        this.rotY = 0.5;
        this.rotZ = 0.2;
        setOpaque(false);
    }

    public void rotar() {
        rotX += 0.15;
        rotY += 0.18;
        rotZ += 0.12;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if ("icosaedro".equals(tipo)) dibujarIcosaedro(g2d);
        else dibujarPrismaTriangular(g2d);
    }

    // ============================================================
    // 🔺 PRISMA TRIANGULAR → dado de 3 caras
    // ============================================================
    private void dibujarPrismaTriangular(Graphics2D g2d) {
        int ancho = getWidth();
        int alto = getHeight();
        int centroX = ancho / 2;
        int centroY = alto / 2;
        double escala = Math.min(ancho, alto) / 3.5;

        double h = 0.7;
        double[][] vertices = {
            {0, 1, -h}, {-Math.sqrt(3)/2, -0.5, -h}, {Math.sqrt(3)/2, -0.5, -h},
            {0, 1,  h}, {-Math.sqrt(3)/2, -0.5,  h}, {Math.sqrt(3)/2, -0.5,  h}
        };

        double cosY = Math.cos(rotY), sinY = Math.sin(rotY);
        double cosX = Math.cos(rotX), sinX = Math.sin(rotX);

        int[][] proyectados = new int[6][2];
        double[] zDepth = new double[6];

        for (int i = 0; i < 6; i++) {
            double x = vertices[i][0];
            double y = vertices[i][1];
            double z = vertices[i][2];

            double x1 = x * cosY - z * sinY;
            double z1 = x * sinY + z * cosY;
            double y2 = y * cosX - z1 * sinX;
            double z2 = y * sinX + z1 * cosX;

            proyectados[i][0] = centroX + (int) (x1 * escala);
            proyectados[i][1] = centroY - (int) (y2 * escala);
            zDepth[i] = z2;
        }

        int[][] caras = {
            //{0, 1, 2}, {3, 4, 5}, Esto lo he comentado para que solo haya 3 caras, seueda un poco raro pero no me ha salido otra forma
            {0, 1, 4, 3}, {1, 2, 5, 4}, {2, 0, 3, 5} 
        };

        double[] profundidad = new double[caras.length];
        for (int i = 0; i < caras.length; i++) {
            double sum = 0;
            for (int v : caras[i]) sum += zDepth[v];
            profundidad[i] = sum / caras[i].length;
        }

        Integer[] orden = new Integer[caras.length];
        for (int i = 0; i < caras.length; i++) orden[i] = i;
        java.util.Arrays.sort(orden, (a, b) -> Double.compare(profundidad[a], profundidad[b]));

        for (int idx : orden) {
            Polygon p = new Polygon();
            for (int v : caras[idx]) p.addPoint(proyectados[v][0], proyectados[v][1]);
            float brillo = (float)(0.7 + 0.3 * (profundidad[idx] + 2) / 4);
            brillo = Math.max(0.6f, Math.min(1.0f, brillo));
            g2d.setColor(new Color((int)(255*brillo), (int)(255*brillo), (int)(255*brillo)));
            g2d.fillPolygon(p);
            g2d.setColor(new Color(40, 40, 40));
            g2d.setStroke(new BasicStroke(1.8f));
            g2d.drawPolygon(p);
        }
    }

    // ============================================================
    // ⚪ ICOSAEDRO → dado de 20 caras Para simular vale
    // ============================================================
    private void dibujarIcosaedro(Graphics2D g2d) {
        double phi = (1 + Math.sqrt(5)) / 2;
        double[][] vertices3D = {
            {-1,  phi, 0}, { 1,  phi, 0}, {-1, -phi, 0}, { 1, -phi, 0},
            {0, -1,  phi}, {0,  1,  phi}, {0, -1, -phi}, {0,  1, -phi},
            { phi, 0, -1}, { phi, 0,  1}, {-phi, 0, -1}, {-phi, 0,  1}
        };

        int[][] faces = {
            {0,11,5}, {0,5,1}, {0,1,7}, {0,7,10}, {0,10,11},
            {1,5,9}, {5,11,4}, {11,10,2}, {10,7,6}, {7,1,8},
            {3,9,4}, {3,4,2}, {3,2,6}, {3,6,8}, {3,8,9},
            {4,9,5}, {2,4,11}, {6,2,10}, {8,6,7}, {9,8,1}
        };

        Point2D[] projected = new Point2D[12];
        double[] zDepth = new double[12];

        for (int i = 0; i < 12; i++) {
            double x = vertices3D[i][0], y = vertices3D[i][1], z = vertices3D[i][2];
            double x1 = x * Math.cos(rotY) - z * Math.sin(rotY);
            double z1 = x * Math.sin(rotY) + z * Math.cos(rotY);
            double y2 = y * Math.cos(rotX) - z1 * Math.sin(rotX);
            zDepth[i] = z1 * Math.cos(rotX) + y * Math.sin(rotX);
            double scale = Math.min(getWidth(), getHeight()) / 6.0;
            projected[i] = new Point2D.Double(getWidth()/2 + x1 * scale, getHeight()/2 - y2 * scale);
        }

        double[] faceDepth = new double[20];
        for (int i = 0; i < 20; i++) {
            double sumZ = 0;
            for (int v : faces[i]) sumZ += zDepth[v];
            faceDepth[i] = sumZ / 3.0;
        }

        Integer[] order = new Integer[20];
        for (int i = 0; i < 20; i++) order[i] = i;
        java.util.Arrays.sort(order, (a, b) -> Double.compare(faceDepth[a], faceDepth[b]));

        for (int i : order) {
            Polygon p = new Polygon();
            for (int v : faces[i]) p.addPoint((int)projected[v].getX(), (int)projected[v].getY());
            float brightness = (float)(0.7 + 0.3 * (faceDepth[i] + 2) / 4);
            brightness = Math.max(0.7f, Math.min(1.0f, brightness));
            g2d.setColor(new Color((int)(255*brightness), (int)(255*brightness), (int)(255*brightness)));
            g2d.fillPolygon(p);
            g2d.setColor(new Color(40, 40, 40));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawPolygon(p);
        }
    }
}
