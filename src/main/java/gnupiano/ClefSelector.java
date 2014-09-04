package gnupiano;
/***********************************************
This file is part of the GnuLecture project (https://github.com/Neonunux/gnulecture/wiki).

GnuLecture is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

GnuLecture is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GnuLecture.  If not, see <http://www.gnu.org/licenses/>.

 **********************************************/

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

@SuppressWarnings({"unchecked","rawtypes"})
public class ClefSelector extends JPanel implements MouseListener
{
	private static final long serialVersionUID = -3872352788125977616L;

	ResourceBundle appBundle;
	String clefSymbol;
	String choosen;
	JLabel selectorUp;
	JLabel selectorDown;
	JLabel clefText;
	JLabel disabledText;
	boolean enabled = false;
	int lowerLevel = 0;
	int higherLevel = 0;
	private ArrayList keyList = new ArrayList();
	private ListIterator<String> keyIter;

	public ClefSelector(ResourceBundle b, String s)
	{
		appBundle = b;
		clefSymbol = s;
		choosen = s;
		setLayout(null);
		Font arial = new Font("Arial", Font.BOLD, 15);

		keyList.add("G2");	// G2
		keyList.add("C1");	// C1
		keyList.add("C2");	// C2
		keyList.add("C3");	// C3
		keyList.add("C4");	// C4
		keyList.add("C5");	// C5
		keyList.add("F4");	// F4
		keyIter = keyList.listIterator();

		if (clefSymbol == "G2")
			clefText = new JLabel(appBundle.getString("_clef.g2"), null, JLabel.CENTER);
		else if (clefSymbol == "F4")
			clefText = new JLabel(appBundle.getString("_clef.f4"), null, JLabel.CENTER);
		else if (clefSymbol == "C3")
			clefText = new JLabel(appBundle.getString("_clef.c3"), null, JLabel.CENTER);
		else if (clefSymbol == "C4")
			clefText = new JLabel(appBundle.getString("_clef.c4"), null, JLabel.CENTER);

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

	public void setEnabled(boolean set)
	{
		this.enabled = set;
		clefText.setVisible(!enabled);
		disabledText.setVisible(!enabled);
		selectorDown.setVisible(enabled);
		selectorUp.setVisible(enabled);
		repaint();
	}

	public void setLevels(int low, int high)
	{
		this.lowerLevel = low;
		this.higherLevel = high;
		repaint();
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public int getLowerLevel()
	{
		return lowerLevel;
	}

	public int getHigherLevel()
	{
		return higherLevel;
	}

	public void mouseClicked(MouseEvent e) 
	{
		//System.out.println("Mouse clicked (# of clicks: " + e.getClickCount() + ")");
		System.out.println("X pos: " + e.getX() + ", Y pos: " + e.getY());

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
				System.out.print("Clef up : "+ choosen + "\n");
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
				System.out.print("Clef down : "+ choosen + "\n");
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
			System.out.println("[ClefSelector] New level = " + level);

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

	public void mousePressed(MouseEvent e) 
	{
		//System.out.println("Mouse pressed; # of clicks: " + e.getClickCount());
	}

	public void mouseReleased(MouseEvent e) 
	{
		//System.out.println("Mouse released; # of clicks: " + e.getClickCount());
	}

	public void mouseEntered(MouseEvent e) 
	{
		//System.out.println("Mouse entered");
	}

	public void mouseExited(MouseEvent e) 
	{
		//System.out.println("Mouse exited");
	}

	protected void paintComponent(Graphics g) 
	{
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color fc;
		if (enabled == false)
			fc = Color.lightGray;
		else
			fc = Color.black;
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
