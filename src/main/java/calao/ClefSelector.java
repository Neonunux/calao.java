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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class ClefSelector.
 *
 * @author Neonunux
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ClefSelector extends JPanel implements MouseListener
{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3872352788125977616L;
	
	private static final Logger logger =  LogManager.getLogger(ClefSelector.class.getName());

	/** The app bundle. */
	ResourceBundle appBundle;
	
	/** The clef symbol. */
	String clefSymbol;
	
	/** The choosen. */
	String choosen;
	
	/** The selector up. */
	JLabel selectorUp;
	
	/** The selector down. */
	JLabel selectorDown;
	
	/** The clef text. */
	JLabel clefText;
	
	/** The disabled text. */
	JLabel disabledText;
	
	/** The enabled. */
	boolean enabled = false;
	
	/** The lower level. */
	int lowerLevel = 0;
	
	/** The higher level. */
	int higherLevel = 0;
	
	/** The key list. */
	private ArrayList keyList = new ArrayList();
	
	/** The key iter. */
	private ListIterator<String> keyIter;

	/**
	 * Instantiates a new clef selector.
	 *
	 * @param b the b
	 * @param s the s
	 */
	public ClefSelector(ResourceBundle b, String s)
	{
		appBundle = b;
		clefSymbol = s;
		choosen = s;
		setLayout(null);
		Font arial = new Font("Arial", Font.BOLD, 15);

		keyList.add("G2");
		keyList.add("C1");
		keyList.add("C2");
		keyList.add("C3");
		keyList.add("C4");
		keyList.add("C5");
		keyList.add("F4");
		keyIter = keyList.listIterator();

		if (clefSymbol == "G2") {
			clefText = new JLabel(appBundle.getString("_clef.g2"), null, JLabel.CENTER);
		} else if (clefSymbol == "F4") {
			clefText = new JLabel(appBundle.getString("_clef.f4"), null, JLabel.CENTER);
		} else if (clefSymbol == "C3") {
			clefText = new JLabel(appBundle.getString("_clef.c3"), null, JLabel.CENTER);
		} else if (clefSymbol == "C4") {
			clefText = new JLabel(appBundle.getString("_clef.c4"), null, JLabel.CENTER);
		}

		selectorUp = new JLabel("\u25B2"); // \u25B2: triangle UP
		selectorUp.setFont(arial.deriveFont((float) 22.0));
		selectorUp.setForeground(Color.black);
		selectorUp.setBounds(25, 5, 140, 40);
		selectorUp.setVisible(enabled);

		selectorDown = new JLabel("\u25BC"); // \u25BC: triangle DOWN
		selectorDown.setFont(arial.deriveFont((float) 22.0));
		selectorDown.setForeground(Color.black);
		selectorDown.setBounds(25, 160, 140, 40);
		selectorDown.setVisible(enabled);

		clefText.setFont(arial);
		clefText.setForeground(Color.lightGray);
		clefText.setPreferredSize(new Dimension(140, 40));
		clefText.setBounds(15, 10, 140, 40);

		disabledText = new JLabel(appBundle.getString("_clefDisabled"));
		disabledText.setFont(arial);
		disabledText.setForeground(Color.lightGray);
		disabledText.setPreferredSize(new Dimension(140, 140));
		disabledText.setBounds(65, 25, 140, 140);

		add(selectorUp);
		add(selectorDown);
		add(disabledText);

		addMouseListener(this);
	}

	public void setClef(String key)
	{
		this.choosen = key;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean set)
	{
		this.enabled = set;
		this.clefText.setVisible(!enabled);
		this.disabledText.setVisible(!enabled);
		this.selectorDown.setVisible(enabled);
		this.selectorUp.setVisible(enabled);
		repaint();
	}

	/**
	 * Sets the levels.
	 *
	 * @param low the low
	 * @param high the high
	 */
	public void setLevels(int low, int high)
	{
		this.lowerLevel = low;
		this.higherLevel = high;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#isEnabled()
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Gets the lower level.
	 *
	 * @return the lower level
	 */
	public int getLowerLevel()
	{
		return lowerLevel;
	}

	/**
	 * Gets the higher level.
	 *
	 * @return the higher level
	 */
	public int getHigherLevel()
	{
		return higherLevel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) 
	{
		logger.debug("Mouse clicked (# of clicks: " + e.getClickCount() + ")");
		logger.debug("X pos: " + e.getX() + ", Y pos: " + e.getY());

		if (e.getX() < 50 )
		{
			// Key selector UP
			if (e.getY() < 50)
			{
				if (keyIter.hasNext()) 
				{
					clefSymbol = keyIter.next();
				}
				else // iterator on end's keyList
				{ 
					keyIter=keyList.listIterator();
					clefSymbol = keyIter.next();
				}
				choosen = clefSymbol;
				logger.debug("Clef up : "+ choosen + "\n");
			}
			// Key selector DOWN
			if (e.getY() > 164)
			{
				if (keyIter.hasPrevious())
				{
					clefSymbol = keyIter.previous();
				}
				else // iterator on list's begining
				{
					keyIter = keyList.listIterator(keyList.size());
					clefSymbol = keyIter.previous();
				}
				choosen = clefSymbol;
				logger.debug("Clef down : "+ choosen + "\n");
			}
			// Key un/activation 
			if (e.getY() > 49 && e.getY() < 165)
			{
				enabled = !enabled;
				clefText.setVisible(!enabled);
				disabledText.setVisible(!enabled);
				selectorDown.setVisible(enabled);
				selectorUp.setVisible(enabled);

				repaint();
				return;
			}
		}
		else
		{
			if (enabled == false)
				return;
		}
		// levels management
		if (e.getX() > 50 && e.getY() > 9 && e.getY() < 189)
		{
			int relYpos = e.getY() - 14;
			int level = (relYpos / 7);
			logger.debug("[ClefSelector] New level = " + level);

			if (e.getX() < 90)
			{
				if (level < higherLevel)
					higherLevel = level;
				else
					lowerLevel = level;
			}
			else
			{
				if (level > lowerLevel)
					lowerLevel = level;
				else
					higherLevel = level;
			}
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) 
	{
		//logger.debug("Mouse pressed; # of clicks: " + e.getClickCount());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) 
	{
		//logger.debug("Mouse released; # of clicks: " + e.getClickCount());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) 
	{
		//logger.debug("Mouse entered");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) 
	{
		//logger.debug("Mouse exited");
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) 
	{
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color fc;
		if (enabled == false) {
			fc = Color.lightGray;
		} else {
			fc = Color.black;
		}
		g.setColor(fc);
		g.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
		g.setColor(Color.white);
		g.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
		g.setColor(fc);

		if (clefSymbol == "C1")
		{
			g.setFont(getFont().deriveFont(73f));
			g.drawString("" + (char)0xBF, 15, 159);
		}
		else if (clefSymbol == "C2")
		{
			g.setFont(getFont().deriveFont(73f));
			g.drawString("" + (char)0xBF, 15, 145);
		}
		else if (clefSymbol == "C3")
		{
			g.setFont(getFont().deriveFont(73f));
			g.drawString("" + (char)0xBF, 15, 132);
		}
		else if (clefSymbol == "C4")
		{
			g.setFont(getFont().deriveFont(73f));
			g.drawString("" + (char)0xBF, 15, 118);
		}
		else if (clefSymbol == "C5")
		{
			g.setFont(getFont().deriveFont(73f));
			g.drawString("" + (char)0xBF, 15, 104);
		}
		if (clefSymbol == "G2")
		{
			g.setFont(getFont().deriveFont(80f));
			g.drawString("G", 15, 130);
		}
		if (clefSymbol == "F4")
		{
			g.setFont(getFont().deriveFont(80f));
			g.drawString("?", 15, 130);			
		}
		if (enabled == true)
		{
			g.setFont(getFont().deriveFont(68f));
			String ss = "" + (char)0xA9 + (char)0xA9 + (char)0xA9 + (char)0xA9; // staff symbol
			g.drawString(ss, 15, 128);

			int ypos = 143;
			for (int i = 0; i < 4; i++, ypos+=14) // draw 3 additional lines below
				g.fillRect(70, ypos, 32, 2);
			ypos = 59;
			for (int i = 0; i < 4; i++, ypos-=14) // draw 3 additional lines above
				g.fillRect(100, ypos, 32, 2);

			g.drawString("w", 75, 25 + (lowerLevel * 7));
			g.drawString("w", 105, 25 + (higherLevel * 7));
		}
	}
}
