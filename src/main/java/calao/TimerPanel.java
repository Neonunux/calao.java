package calao;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TimerPanel is used to set a maximum time to answer.
 */
public class TimerPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 6396633037556341891L;

	private static final Logger logger = LogManager.getLogger(TimerPanel.class.getName());

	private int angle = 20;

	private int R = 70;
	private int startAngle = 90;

	private int xmiddle;
	private int ymiddle;
	private int pie;

	private Preferences appPrefs;

	private String color = "#000000";

	long chrono = 0;

	private Timer timer;

	public TimerPanel(Preferences p) {
		appPrefs = p;
		color = appPrefs.getProperty("colors.background");
		// startChrono();

		timer = new Timer(200, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				repaint();
				angle = angle + 2;
				if (angle > 360) {
					angle = 2;
					// checkChrono();
				}
			}
		});
		timer.setRepeats(true);
	}

	void startChrono() {
		chrono = java.lang.System.currentTimeMillis();
	}

	void checkChrono() {
		long chrono2 = java.lang.System.currentTimeMillis();
		long temps = chrono2 - chrono;
		logger.debug("Temps ecoule = " + temps + " ms");
	}

	protected void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.decode(color));
		g.fillRect(0, 0, getWidth(), getHeight());
		pie = (int) Math.round(((angle - 1) / 359.0) * 255.0);

		Color colorCircle = new Color(0 + pie, 0, 255 - pie);
		g.setColor(colorCircle);

		xmiddle = (getWidth() - R) / 2;
		ymiddle = (getHeight() - R) / 2;
		g.fillArc(xmiddle, ymiddle, R, R, startAngle, angle);
	}

	public void run() {

	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}
}