package calao;

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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class Accidentals.
 *
 * @author Neonunux
 * @author Massimo Callegari
 */
public class Accidentals {

	private static final Logger logger = LogManager.getLogger(Accidentals.class
			.getName());

	Preferences appPrefs;

	/** The type. */
	private String type;

	/** The amount. */
	private int amount;

	/**
	 * Instantiates a new accidentals.
	 *
	 * @param t
	 *            the t
	 * @param count
	 *            the count
	 * @param p
	 *            the p
	 */
	public Accidentals(String t, int count, Preferences p) {
		appPrefs = p;
		type = t;
		amount = count;
	}

	/**
	 * Sets the type and count.
	 *
	 * @param t
	 *            the t
	 * @param count
	 *            the count
	 */
	public void setTypeAndCount(String t, int count) {
		this.type = t;
		this.amount = count;
		logger.debug("[Accidentals - setTypeAndCount] type: " + type
				+ ", count: " + count);
	}

	/**
	 * Gets the number.
	 *
	 * @return the number
	 */
	public int getNumber() {
		return amount;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the tonality.
	 *
	 * @param bundle
	 *            the bundle
	 * @return the tonality
	 */
	public String getTonality(ResourceBundle bundle) {
		String tStr = "";

		String DO = bundle.getString("_do");
		String RE = bundle.getString("_re");
		String MI = bundle.getString("_mi");
		String FA = bundle.getString("_fa");
		String SOL = bundle.getString("_sol");
		String LA = bundle.getString("_la");
		String SI = bundle.getString("_si");

		if (amount == 0) {
			tStr = DO + " Maj | " + LA + " min";
		} else if (amount == 1) {
			if (type.equals("#")) {
				tStr = SOL + " Maj | " + MI + " min";
			} else {
				tStr = FA + " Maj | " + RE + " min";
			}
		}
		if (amount == 2) {
			if (type.equals("#")) {
				tStr = RE + " Maj | " + SI + " min";
			} else {
				tStr = SI + "b Maj | " + SOL + " min";
			}
		}
		if (amount == 3) {
			if (type.equals("#")) {
				tStr = LA + " Maj | " + FA + "# min";
			} else {
				tStr = MI + "b Maj | " + DO + " min";
			}
		}
		if (amount == 4) {
			if (type.equals("#")) {
				tStr = MI + " Maj | " + DO + "# min";
			} else {
				tStr = LA + "b Maj | " + FA + " min";
			}
		}
		if (amount == 5) {
			if (type.equals("#")) {
				tStr = SI + " Maj | " + SOL + "# min";
			} else {
				tStr = RE + "b Maj | " + SI + "b min";
			}
		}
		if (amount == 6) {
			if (type.equals("#")) {
				tStr = FA + "# Maj | " + RE + "# min";
			} else {
				tStr = SOL + "b Maj | " + MI + "b min";
			}
		}
		if (amount == 7) {
			if (type.equals("#")) {
				tStr = DO + "# Maj | " + LA + "# min";
			} else {
				tStr = DO + "b Maj | " + LA + "b min";
			}
		}
		return tStr;
	}

	/**
	 * Draw alteration.
	 *
	 * @param g
	 *            the g
	 * @param f
	 *            the f
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param altType
	 *            the alt type
	 */
	private void drawAlteration(Graphics g, Font f, int x, int y, String altType) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);
		g.setFont(f.deriveFont(54f));
		if (altType == "B") {
			g.drawString(altType, x + 2, y + 21);
		} else {
			g.drawString(altType, x + 2, y + 22);
		}
	}

	/**
	 * Paint.
	 *
	 * @param g
	 *            the g
	 * @param f
	 *            the f
	 * @param xPos
	 *            the x pos
	 * @param yPos
	 *            the y pos
	 * @param clefMask
	 *            the clef mask
	 */
	public void paint(Graphics g, Font f, int xPos, int yPos, int clefMask) {
		String sharp = "B"; // # alteration
		String flat = "b"; // b alteration
		int clefOffset = 0;

		clefOffset = getClefOffset(clefMask);

		if (type.equals("#")) {
			// if (type.equals("B")) {
			// xPos = getXYFlatAlteration().get(0);
			// yPos = getXYFlatAlteration().get(1) + clefOffset;
			// drawAlteration(g, f, xPos, yPos, sharp);
			// }
			if (amount >= 1) // FA#
				drawAlteration(g, f, xPos, yPos - 15 + clefOffset, sharp);
			if (amount >= 2) // DO#
				drawAlteration(g, f, xPos + 10, yPos + clefOffset, sharp);
			if (amount >= 3) // SOL#
			{
				if (clefMask == appPrefs.CLEF_C4) {
					drawAlteration(g, f, xPos + 20, yPos + clefOffset + 15,
							sharp); // shift an octave down
				} else {
					drawAlteration(g, f, xPos + 20, yPos - 20 + clefOffset,
							sharp);
				}
			}
			if (amount >= 4) // RE#
				drawAlteration(g, f, xPos + 30, yPos - 5 + clefOffset, sharp);
			if (amount >= 5) // LA#
				drawAlteration(g, f, xPos + 40, yPos + 10 + clefOffset, sharp);
			if (amount >= 6) // MI#
			{
				if (clefMask == appPrefs.CLEF_C4) {
					drawAlteration(g, f, xPos + 50, yPos + 25 + clefOffset,
							sharp);
				} else {
					drawAlteration(g, f, xPos + 50, yPos - 10 + clefOffset,
							sharp);
				}
			}
			if (amount >= 7) // SI#
				drawAlteration(g, f, xPos + 60, yPos + 5 + clefOffset, sharp);
		}

		if (type.equals("b")) {
			// if (type.equals("b")) {
			// xPos = getXYFlatAlteration().get(0);
			// yPos = getXYFlatAlteration().get(1) + clefOffset;
			// drawAlteration(g, f, xPos, yPos, flat);
			// }
			if (amount >= 1) // SIb
				drawAlteration(g, f, xPos, yPos + 5 + clefOffset, flat);
			if (amount >= 2) // MIb
				drawAlteration(g, f, xPos + 9, yPos - 10 + clefOffset, flat);
			if (amount >= 3) // LAb
				drawAlteration(g, f, xPos + 18, yPos + 10 + clefOffset, flat);
			if (amount >= 4) // REb
				drawAlteration(g, f, xPos + 27, yPos - 5 + clefOffset, flat);
			if (amount >= 5) // SOLb
				drawAlteration(g, f, xPos + 36, yPos + 15 + clefOffset, flat);
			if (amount >= 6) // DOb
				drawAlteration(g, f, xPos + 45, yPos + clefOffset, flat);
			if (amount >= 7) // FAb
				drawAlteration(g, f, xPos + 54, yPos + 20 + clefOffset, flat);
		}
	}

	/**
	 * @param clefMask
	 * @param clefOffset
	 * @return
	 */
	public int getClefOffset(int clefMask) {
		int clefOffset = 0; // G2 ?
		if (clefMask == appPrefs.CLEF_F4) {
			clefOffset = 10;
		}
		// else if (clefMask == appPrefs.CLEF_C1) {
		// clefOffset = ??;
		// } else if (clefMask == appPrefs.CLEF_C2) {
		// clefOffset = ??;
		// }
		else if (clefMask == appPrefs.CLEF_C3) {
			clefOffset = 5;
		} else if (clefMask == appPrefs.CLEF_C4) {
			clefOffset = -5;
		}
		// else if (clefMask == appPrefs.CLEF_C5) {
		// clefOffset = ??;
		// }
		return clefOffset;
	}

	Vector<Integer> getXYFlatAlteration() {

		Vector<Integer> alt = new Vector<Integer>();
		if (amount >= 1) {
			alt.add(0);
			alt.add(5);
		}
		if (amount >= 2) {
			alt.add(9);
			alt.add(-10);
		}
		if (amount >= 3) {
			alt.add(18);
			alt.add(10);
		}
		if (amount >= 4) {
			alt.add(27);
			alt.add(-5);
		}
		if (amount >= 5) {
			alt.add(36);
			alt.add(15);
		}
		if (amount >= 6) {
			alt.add(45);
			alt.add(0);
		}
		if (amount >= 7) {
			alt.add(54);
			alt.add(20);
		}
		return alt;
	}

	Vector<Integer> getXYSharpAlteration(int clefMask) {
		// IN main method :
		// if (type.equals("b")) {
		// xPos = getXYFlatAlteration().get(0);
		// yPos = getXYFlatAlteration().get(1) + clefOffset;
		// drawAlteration(g, f, xPos, yPos, flat);
		// }

		Vector<Integer> alt = new Vector<Integer>();
		if (amount >= 1) {
			alt.add(0);
			alt.add(-15);
		}
		if (amount >= 2) {
			alt.add(10);
			alt.add(0);
		}
		if (amount >= 3) {
			alt.add(18);
			alt.add(10);
			if (clefMask == appPrefs.CLEF_C4) {
				alt.add(20);
				alt.add(15);
			} else {
				alt.add(20);
				alt.add(-20);
			}
		}
		if (amount >= 4) {
			alt.add(30);
			alt.add(-5);
		}
		if (amount >= 5) {
			alt.add(40);
			alt.add(10);
		}
		if (amount >= 6) {
			if (clefMask == appPrefs.CLEF_C4) {
				alt.add(50);
				alt.add(25);
			} else {
				alt.add(50);
				alt.add(-10);
			}
		}
		if (amount >= 7) {
			alt.add(60);
			alt.add(5);
		}
		return alt;
	}
}
