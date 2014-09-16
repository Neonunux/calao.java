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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class AnswersPanel.
 *
 * @author Neonunux
 */
public class AnswersPanel extends JPanel  
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 448119751116801373L;
	
	
	private static final Logger logger =  LogManager.getLogger(AnswersPanel.class.getName());
	
	/** The show cursor and beats. */
	private boolean showCursorAndBeats = false;

	/** The curr height. */
	int currWidth = 0, currHeight = 0;
	
	/** The b image. */
	BufferedImage bImage = null;
	
	/** The right img. */
	Image rightImg = null;
	
	/** The wrong img. */
	Image wrongImg = null;
	
	/** The warn img. */
	Image warnImg = null;

	/** The cursor xpos. */
	int cursorXpos = -1;
	
	/** The cursor ypos. */
	int cursorYpos = -1;
	
	/**
	 * Instantiates a new answers panel.
	 */
	public AnswersPanel()
	{
		
		rightImg = new ImageIcon(getClass().getResource("correct.png")).getImage();
		wrongImg = new ImageIcon(getClass().getResource("wrong.png")).getImage();
		warnImg = new ImageIcon(getClass().getResource("warning.png")).getImage();
		checkSurface();
	}
	
	/**
	 * Check surface.
	 */
	private void checkSurface()
	{
		int tmpWidth = getWidth();
		int tmpHeight = getHeight();
		if (tmpWidth != currWidth || tmpHeight != currHeight)
		{
			//logger.debug("answersLayer size changed !!");
			currWidth = tmpWidth;
			currHeight = tmpHeight;
			bImage = new BufferedImage(currWidth, currHeight, BufferedImage.TYPE_INT_ARGB);
		}
	}
	
	/**
	 * Clear surface.
	 */
	public void clearSurface()
	{
		if (bImage == null)
			return;
		Graphics2D g2d = bImage.createGraphics();
		g2d.setBackground(new Color(255, 255, 255, 0));
		g2d.clearRect(0, 0, currWidth, currHeight);
		g2d.dispose();
		cursorXpos = -1;
		cursorYpos = -1;
	}

    /**
     * Enable cursor.
     *
     * @param on the on
     */
    public void enableCursor(boolean on)
    {
    	showCursorAndBeats = on;
    }

    /**
     * Draw cursor.
     *
     * @param x the x
     * @param y the y
     * @param clean the clean
     */
    public void drawCursor(int x, int y, boolean clean)
    {
    	if (showCursorAndBeats == false || x < 0 || y < 0)
    		return;
    	Graphics2D g2d = bImage.createGraphics();

    	if (clean == false)
    		g2d.setColor(Color.orange);
    	else
    		g2d.setColor(Color.white);
    	g2d.fillRect(0, y, x, 3);
    	g2d.dispose();
    	this.repaint();
    }

    /**
     * Draw metronome.
     *
     * @param x the x
     * @param y the y
     */
    public void drawMetronome(int x, int y)
    {
    	if (showCursorAndBeats == false)
    		return;
    	Graphics2D g2d = bImage.createGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(x + 2, y - 8, 5, 8);
		g2d.dispose();
    	this.repaint();
    }
    
    /**
     * Draw answer.
     *
     * @param type the type
     * @param x the x
     * @param y the y
     */
    public void drawAnswer(int type, int x, int y)
    {
    	Graphics2D g2d = bImage.createGraphics();
    	if (type == 0)
    		g2d.drawImage(wrongImg, x, y, null);
    	else if (type == 1)
    		g2d.drawImage(rightImg, x, y, null);
    	else if (type == 2)
    		g2d.drawImage(warnImg, x, y, null);
    	g2d.dispose();
    	this.repaint();
    }

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) 
 	{
		super.paintComponent(g);
		checkSurface();
		g.drawImage(bImage, 0, 0, null);		
 	}
}
