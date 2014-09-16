/**
 * Calao is an educational platform to get started with musical
 * reading and solfege.
 * Copyright (C) 2012-2014 R. Leloup (http://github.com/Neonunux/Calao)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package calao;

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
	 * @param String t 
	 *            Type of Accidentals: # or b
	 * @param Integer count 
	 *            Amount of accidentals: 1 to 7 
	 * @param Preferences p 
	 *            The preferences configuration
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
		
		int clefOffset;
		
		Integer xPos2;
		Integer yPos2;
		
		clefOffset = getClefOffset(clefMask);

		String alteration = null;
		
		if (type.equals("#")) {
			alteration  = sharp;
		}

		if (type.equals("b")) {
			alteration = flat;
		}
		
		for (int i = 0; i < amount; i++) {
			xPos2 = xPos + getXYAlterations(clefMask).get(2 * i);
			yPos2 = yPos + getXYAlterations(clefMask).get(2 * i + 1) + clefOffset;
			drawAlteration(g, f, xPos2, yPos2, alteration);
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

	Vector<Integer> getXYAlterations(int clefMask) {
		Vector<Integer> alt = new Vector<Integer>();
		if (type.equals("b")) {
			if (amount >= 1) {
				alt.add(0); // SIB
				alt.add(5);
			}
			if (amount >= 2) {
				alt.add(9); // MIB
				alt.add(-10);
			}
			if (amount >= 3) {
				alt.add(18); // LAB
				alt.add(10);
			}
			if (amount >= 4) {
				alt.add(27); // REB
				alt.add(-5);
			}
			if (amount >= 5) {
				alt.add(36); // SOLB
				alt.add(15);
			}
			if (amount >= 6) {
				alt.add(45); // DOB
				alt.add(0);
			}
			if (amount >= 7) {
				alt.add(54); // FAB
				alt.add(20);
			}
		}

		if (type.equals("#")) {
			if (amount >= 1) {
				alt.add(0); // SOL
				alt.add(-15);
			}
			if (amount >= 2) {
				alt.add(10); // RE
				alt.add(0);
			}
			if (amount >= 3) {
				if (clefMask == appPrefs.CLEF_C4) {
					alt.add(20); // LA
					alt.add(15);
				} else {
					alt.add(20);
					alt.add(-20);
				}
			}
			if (amount >= 4) {
				alt.add(30); // MI
				alt.add(-5);
			}
			if (amount >= 5) {
				alt.add(40); // SI
				alt.add(10);
			}
			if (amount >= 6) {
				if (clefMask == appPrefs.CLEF_C4) {
					alt.add(50); // FA#
					alt.add(25);
				} else {
					alt.add(50);
					alt.add(-10);
				}
			}
			if (amount >= 7) {
				alt.add(60); // DO#
				alt.add(5);
			}
		}
		return alt;
	}
}
