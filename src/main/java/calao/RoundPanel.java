package calao;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

class RoundPanel extends JPanel {
	private static final long serialVersionUID = 2133404549466988014L;
	boolean gradientBack = false;
	Color startColor;
	Color endColor;
	Color borderColor = Color.decode("0x5F8DD3");
	private boolean isBorder = false;
	private int borderWidth = 3;

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public RoundPanel() {
	}

	public RoundPanel(Color startCol, Color endCol) {
		gradientBack = false;
		isBorder = true;
		startColor = startCol;
		endColor = endCol;
	}

	public boolean isBorder() {
		return isBorder;
	}

	public void setBorder(boolean isBorder) {
		this.isBorder = isBorder;
	}

	public void setBorderColor(Color bc) {
		this.borderColor = bc;
	}

	protected void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int bWidth = getBorderWidth();
		if (isBorder) {
			g.setColor(borderColor);
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
		}
		if (gradientBack == false)
			g.setColor(getBackground());
		else {
			GradientPaint vertGrad = new GradientPaint(0, 0, startColor, 0,
					getHeight(), endColor);
			((Graphics2D) g).setPaint(vertGrad);
		}
		g.fillRoundRect(bWidth, bWidth, getWidth() - 2 * bWidth, getHeight()
				- 2 * bWidth, 15, 15);
	}
}
