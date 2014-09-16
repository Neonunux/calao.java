/***********************************************
This file is part of the Calao project (https://github.com/Neonunux/calao/wiki).

Calao is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Calao is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Calao.  If not, see <http://www.gnu.org/licenses/>.

**********************************************/
package calao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class Key.
 *
 * @author Neonunux
 */
public class Key extends JButton {

	private static final Logger logger = LogManager.getLogger(Key.class
			.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8886689336934022704L;

	/** The is_black. */
	boolean is_black = false;

	/** The is_highlighted. */
	boolean is_highlighted = false;

	/** The pitch. */
	int pitch;

	/** The octave. */
	int octave;

	/** The note index. */
	int noteIndex; // holds the index of the seven note scale (C, D, E, F, G, A,
					// B)

	/** The offx. */
	int offx; // just for black keys since they have an irregular pattern

	/**
	 * Instantiates a new key.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 * @param kpitch
	 *            the kpitch
	 * @param koctave
	 *            the koctave
	 * @param idx
	 *            the idx
	 * @param offset
	 *            the offset
	 * @param black
	 *            the black
	 */
	public Key(int x, int y, int w, int h, int kpitch, int koctave, int idx,
			int offset, boolean black) {
		pitch = kpitch;
		octave = koctave;
		is_black = black;
		is_highlighted = false;
		noteIndex = idx;
		offx = offset;
		setBounds(x + offx, y, w, h);
		setPreferredSize(new Dimension(w, h));
		if (black == true) {
			setBackground(Color.black);
		} else {
			setBackground(Color.white);
		}
	}

	/**
	 * Highlight.
	 *
	 * @param on
	 *            the on
	 * @param learning
	 *            the learning
	 */
	public void highlight(boolean on, boolean learning) {
		is_highlighted = on;
		if (on == true) {
			if (learning == false)
				setBackground(Color.decode("0x98FB98"));
			else {
				setBackground(Color.decode("0xE7A935"));
				is_highlighted = false;
			}
		} else if (is_black == true)
			setBackground(Color.black);
		else
			setBackground(Color.white);
	}

	/**
	 * Sets the pressed.
	 *
	 * @param on
	 *            the new pressed
	 */
	public void setPressed(boolean on) {
		if (on == true) {
			setBackground(Color.decode("0xB8D8FF"));
		} else {
			if (is_highlighted == true) {
				setBackground(Color.decode("0x98FB98"));
			} else if (is_black == true) {
				setBackground(Color.black);
			} else {
				setBackground(Color.white);
			}
		}
	}

	/**
	 * Sets the xpos.
	 *
	 * @param newx
	 *            the new xpos
	 */
	public void setXpos(int newx) {
		Rectangle b = getBounds();
		b.x = newx;
		setBounds(b);
	}

	/**
	 * Gets the xoffset.
	 *
	 * @return the xoffset
	 */
	public int getXoffset() {
		return this.offx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// logger.debug("Draw Key: size = " + getSize().width);

		g.setColor(Color.black);
		if (is_black == false)
			g.drawRoundRect(0, -7, getSize().width - 1, getSize().height, 7, 7);
		else
			g.drawRect(0, -1, getSize().width - 1, getSize().height - 6);

		if (getModel().isArmed()) // is button being clicked ?
			g.setColor(Color.decode("0x869EBA"));
		else if (getModel().isRollover()) // rollover effect
			g.setColor(Color.decode("0xB8D8FF"));
		else
			g.setColor(getBackground()); // normal state

		if (is_black == false)
			g.fillRoundRect(1, -6, getSize().width - 2, getSize().height - 1,
					6, 6);
		else
			g.fillRect(1, 0, getSize().width - 2, getSize().height - 7);
		g.setColor(Color.black);
		g.drawLine(0, 0, getSize().width, 0);
	}
}
