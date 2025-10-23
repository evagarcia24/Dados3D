package dados3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

		panelDadoVisual.setPreferredSize(new Dimension(360, 360));
		panelDadoVisual.setAlignmentX(Component.CENTER_ALIGNMENT);

		lblResultado.setFont(new Font("Arial", Font.BOLD, 30));
		lblResultado.setForeground(Color.WHITE);
		lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblResultado.setOpaque(true);
		lblResultado.setBackground(new Color(255, 255, 255, 70));
		lblResultado.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));

		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(5));
		panel.add(panelDadoVisual);
		panel.add(Box.createVerticalStrut(10));
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
		Timer timer = new Timer(70, e -> tirarDado21());
		timer.setRepeats(false);
		timer.start();
	}

	private void animarDado(final PanelDado3D panelDado, final JLabel labelResultado, final Dado dado) {
		labelResultado.setText("Girando...");

		final int[] contador = { 0 };
		Timer animacion = new Timer(70, e -> {
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
