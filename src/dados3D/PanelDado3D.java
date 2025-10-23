package dados3D;

// PanelDado3D: prisma triangular para D3, icosaedro para D21

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

class PanelDado3D extends JPanel {
	private String tipo; // "prisma" o "Icosamonoedro (21 caras)"
	private double rotX, rotY, rotZ;

	public PanelDado3D(String tipo) {
		this.tipo = tipo;
		this.rotX = 0.4;
		this.rotY = 0.5;
		this.rotZ = 0.3;
		setOpaque(false);
	}

	public void rotar() {
		rotX += 0.15;
		rotY += 0.18;
		rotZ += 0.14;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		if ("icosaedro".equals(tipo))
			dibujarIcosaedro(g2d);
		else
			dibujarPrismaTriangular(g2d);
	}

	// 🔺 PRISMA TRIANGULAR → dado de 3 caras
	private void dibujarPrismaTriangular(Graphics2D g2d) {
		int ancho = getWidth();
		int alto = getHeight();
		int centroX = ancho / 2;
		int centroY = alto / 2;
		double escala = Math.min(ancho, alto) / 4.0;

		double h = 0.8;
		double[][] vertices = { { 0, 1, -h }, { -Math.sqrt(3) / 2, -0.5, -h }, { Math.sqrt(3) / 2, -0.5, -h },
				{ 0, 1, h }, { -Math.sqrt(3) / 2, -0.5, h }, { Math.sqrt(3) / 2, -0.5, h } };

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
				// {0, 1, 2}, {3, 4, 5}, Esto lo he comentado para que solo haya 3 caras, se queda
				// un poco raro pero no me ha salido otra forma
				{ 0, 1, 4, 3 }, { 1, 2, 5, 4 }, { 2, 0, 3, 5 } };

		double[] profundidad = new double[caras.length];
		for (int i = 0; i < caras.length; i++) {
			double sum = 0;
			for (int v : caras[i])
				sum += zDepth[v];
			profundidad[i] = sum / caras[i].length;
		}

		Integer[] orden = new Integer[caras.length];
		for (int i = 0; i < caras.length; i++)
			orden[i] = i;
		java.util.Arrays.sort(orden, (a, b) -> Double.compare(profundidad[a], profundidad[b]));

		for (int idx : orden) {
			Polygon p = new Polygon();
			for (int v : caras[idx])
				p.addPoint(proyectados[v][0], proyectados[v][1]);
			float brillo = (float) (0.7 + 0.3 * (profundidad[idx] + 2) / 4);
			brillo = Math.max(0.7f, Math.min(1.0f, brillo));
			g2d.setColor(new Color((int) (255 * brillo), (int) (255 * brillo), (int) (255 * brillo)));
			g2d.fillPolygon(p);
			g2d.setColor(new Color(40, 40, 40));
			g2d.setStroke(new BasicStroke(1.8f));
			g2d.drawPolygon(p);
		}
	}

	// ⚪ Icosamonoedro → el dibujo son 20 caras (icosaedro) porque es mal facil de crear que uno de 21 
	private void dibujarIcosaedro(Graphics2D g2d) {
		double phi = (1 + Math.sqrt(5)) / 2;
		double[][] vertices3D = { { -1, phi, 0 }, { 1, phi, 0 }, { -1, -phi, 0 }, { 1, -phi, 0 }, { 0, -1, phi },
				{ 0, 1, phi }, { 0, -1, -phi }, { 0, 1, -phi }, { phi, 0, -1 }, { phi, 0, 1 }, { -phi, 0, -1 },
				{ -phi, 0, 1 } };

		int[][] faces = { { 0, 11, 5 }, { 0, 5, 1 }, { 0, 1, 7 }, { 0, 7, 10 }, { 0, 10, 11 }, { 1, 5, 9 },
				{ 5, 11, 4 }, { 11, 10, 2 }, { 10, 7, 6 }, { 7, 1, 8 }, { 3, 9, 4 }, { 3, 4, 2 }, { 3, 2, 6 },
				{ 3, 6, 8 }, { 3, 8, 9 }, { 4, 9, 5 }, { 2, 4, 11 }, { 6, 2, 10 }, { 8, 6, 7 }, { 9, 8, 1 } };

		Point2D[] projected = new Point2D[12];
		double[] zDepth = new double[12];

		for (int i = 0; i < 12; i++) {
			double x = vertices3D[i][0], y = vertices3D[i][1], z = vertices3D[i][2];
			double x1 = x * Math.cos(rotY) - z * Math.sin(rotY);
			double z1 = x * Math.sin(rotY) + z * Math.cos(rotY);
			double y2 = y * Math.cos(rotX) - z1 * Math.sin(rotX);
			zDepth[i] = z1 * Math.cos(rotX) + y * Math.sin(rotX);
			double scale = Math.min(getWidth(), getHeight()) / 6.0;
			projected[i] = new Point2D.Double(getWidth() / 2 + x1 * scale, getHeight() / 2 - y2 * scale);
		}

		double[] faceDepth = new double[20];
		for (int i = 0; i < 20; i++) {
			double sumZ = 0;
			for (int v : faces[i])
				sumZ += zDepth[v];
			faceDepth[i] = sumZ / 3.0;
		}

		Integer[] order = new Integer[20];
		for (int i = 0; i < 20; i++)
			order[i] = i;
		java.util.Arrays.sort(order, (a, b) -> Double.compare(faceDepth[a], faceDepth[b]));

		for (int i : order) {
			Polygon p = new Polygon();
			for (int v : faces[i])
				p.addPoint((int) projected[v].getX(), (int) projected[v].getY());
			float brightness = (float) (0.7 + 0.3 * (faceDepth[i] + 2) / 4);
			brightness = Math.max(0.7f, Math.min(1.0f, brightness));
			g2d.setColor(new Color((int) (255 * brightness), (int) (255 * brightness), (int) (255 * brightness)));
			g2d.fillPolygon(p);
			g2d.setColor(new Color(40, 40, 40));
			g2d.setStroke(new BasicStroke(1.5f));
			g2d.drawPolygon(p);
		}
	}
}
