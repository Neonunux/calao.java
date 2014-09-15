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

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class Staff.
 *
 * @author Neonunux
 * @author Massimo Callegari
 */
public class Staff extends JPanel 
{
	
	
	private static final Logger logger =  LogManager.getLogger(Staff.class.getName());
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7759085255881441116L;

	/** The app font. */
	Font appFont;
	
	/** The app bundle. */
	private ResourceBundle appBundle; // reference to languages bundle just to display tonality
	
	
	Preferences appPrefs;

	/*
	 *  ************************************* SCORE LAYOUT ***************************************
	 * 
	    | window |clefWidth|alteration|timeSign|  noteDistance  
	    | Margin |         |  Width   | Width  |    /------\    
	    |        |         |          |        |    |      |    
	    |         ----GG------#---------------------|------|--------------------------------------
	    |         -----G----------#-------4---------|-----O---------------------------------------
	    |         ---GG---------#---------4--------O----------------------------------------------
	    |         ---G-G--------------------------------------------------------------------------
	    |         ----G---------------------------------------------------------------------------
	 *
	 *  ******************************************************************************************
	 *	
	*/

    /** The clef width. */
	private int clefWidth = 32; // width of score clefs
    
    /** The alteration width. */
    private int alterationWidth = 0; // width of alterations symbols. None by default
    
    /** The time sign width. */
    private int timeSignWidth = 30; // width of current score time signature symbol. This includes also the first note margin
    
    /** The time sign numerator. */
    private int timeSignNumerator = 4;
    
    /** The time sign denominator. */
    private int timeSignDenominator = 4;
    
    /** The time division. */
    private int timeDivision = 1; // ratio between time signature denominator and quarters 
    
    /** The score ypos. */
    private int scoreYpos = 45; // Y coordinate of the first row of the score
    
    /** The rows distance. */
    private int rowsDistance = 90; // distance in pixel between staff rows
    
    /** The number of measures per row. */
    private int numberOfMeasuresPerRow = 2; // number of measures in a single row
    
    /** The number of rows. */
    private int numberOfRows = 4; // number of score rows
    
    /** The notes shift. */
    private int notesShift = 10; // space in pixel to align notes to the score layout
    
    /** The note distance. */
    private int noteDistance = 72; // distance in pixel between 1/4 notes
    
    /** The first note x pos. */
    private int firstNoteXPos = clefWidth + alterationWidth + alterationWidth + timeSignWidth + notesShift;
    
    /** The score line width. */
    private int scoreLineWidth;
    
    /** The inline mode. */
    private boolean inlineMode = false;
    
    /** The forced number of measures. */
    private int forcedNumberOfMeasures = -1;
    
    /** The clef mask. */
    private int clefMask = 1;
    
    /** The clefs. */
    private Vector<Integer> clefs = new Vector<Integer>();
    
    /** The acc. */
    private Accidentals accidentals; // accidentals reference used for drawing
    
    /** The global scale. */
    private double globalScale = 1.0;
    
    /**
     * Instantiates a new staff.
     *
     * @param f the f
     * @param b the b
     * @param p the p
     * @param a the a
     * @param inline the inline
     * @param singlePage the single page
     */
    public Staff(Font f, ResourceBundle b, Preferences p, Accidentals a, boolean inline, boolean singlePage)
    {
    	appFont = f;
    	appBundle = b;
    	appPrefs = p;
    	accidentals = a;
    	globalScale = 1.0;

    	if (inline == true)
    	{
    		inlineMode = true;
    		numberOfRows = 1;
    		numberOfMeasuresPerRow = 0;
    	}
    	setBackground(Color.white);
    }
    
    /**
     * Sets the rows distance.
     *
     * @param dist the new rows distance
     */
    public void setRowsDistance(int dist)
    {
    	this.rowsDistance = dist;
    }
    
    /**
     * Sets the clefs.
     *
     * @param type the new clefs
     */
    public void setClefs(int type)
    {
    	this.clefMask = type;
    	this.clefs.clear();

    	if ((clefMask & appPrefs.CLEF_G2) > 0) this.clefs.add(appPrefs.CLEF_G2);
    	if ((clefMask & appPrefs.CLEF_F4) > 0) this.clefs.add(appPrefs.CLEF_F4);
    	if ((clefMask & appPrefs.CLEF_C3) > 0) this.clefs.add(appPrefs.CLEF_C3);
    	if ((clefMask & appPrefs.CLEF_C4) > 0) this.clefs.add(appPrefs.CLEF_C4);

    	repaint();
    }
 
    /**
     * Sets the accidentals.
     *
     * @param a the new accidentals
     */
    public void setAccidentals(Accidentals a)
    {
    	this.accidentals = new Accidentals(a.getType(), a.getNumber(), appPrefs);
    }
    
    /**
     * Sets the time signature.
     *
     * @param num the num
     * @param denom the denom
     */
    public void setTimeSignature(int num, int denom)
    {
    	this.timeSignNumerator = num;
    	this.timeSignDenominator = denom;
    	this.timeDivision = timeSignDenominator / 4;
    	repaint();
    }
    
    /**
     * Gets the measures number.
     *
     * @return the measures number
     */
    public int getMeasuresNumber()
    {
    	if (forcedNumberOfMeasures == -1)
    	{
    		if (accidentals != null)
    			alterationWidth = accidentals.getNumber() * 12;
    		int scoreLineWidth = clefWidth + alterationWidth + timeSignWidth;
    		int tmpMeas =  (getWidth() - scoreLineWidth) / ((int)((double)timeSignNumerator/timeDivision) * noteDistance);
    		int tmpRows = getHeight() / rowsDistance;
       	
    		return tmpMeas * tmpRows;
    	}
    	else
    		return forcedNumberOfMeasures;
    }
    
    /**
     * Sets the measures number.
     *
     * @param num the new measures number
     */
    public void setMeasuresNumber(int num)
    {
    	this.forcedNumberOfMeasures = num;
    }
    
    /**
     * Gets the notes distance.
     *
     * @return the notes distance
     */
    public int getNotesDistance()
    {
    	return noteDistance;
    }
    
    /**
     * Gets the rows number.
     *
     * @return the rows number
     */
    public int getRowsNumber()
    {
    	return numberOfRows;
    }
    
    /**
     * Gets the first note x position.
     *
     * @return the first note x position
     */
    public int getFirstNoteXPosition()
    {
    	if (accidentals != null)
    		alterationWidth = accidentals.getNumber() * 12;
    	firstNoteXPos = clefWidth + alterationWidth + timeSignWidth + notesShift;
    	return firstNoteXPos;
    }
    
    /**
     * Gets the staff width.
     *
     * @return the staff width
     */
    public int getStaffWidth()
    {
    	if (inlineMode == false)
    	{
    		if (accidentals != null)
    			alterationWidth = accidentals.getNumber() * 12;
    		scoreLineWidth = clefWidth + alterationWidth + timeSignWidth;
    		int beatW = (int)(((double)timeSignNumerator/timeDivision) * noteDistance);
    		numberOfMeasuresPerRow = (getWidth() - scoreLineWidth) / beatW;
    		scoreLineWidth += (numberOfMeasuresPerRow * beatW);
    	}
    	else 
    		scoreLineWidth = getWidth();

    	//logger.debug("[getStaffWidth] staff width: " + scoreLineWidth);
    	return scoreLineWidth;
    }
    
    /**
     * Gets the staff height.
     *
     * @return the staff height
     */
    public int getStaffHeight()
    {
    	calculateSize();
    	return (numberOfRows * rowsDistance) + scoreYpos;
    }

    /**
     * Sets the scale.
     *
     * @param factor the new scale
     */
    public void setScale(double factor)
    {
    	this.globalScale = factor;
    }
    
    /**
     * Calculate size.
     *
     * @return the int
     */
    private int calculateSize()
    {
		if ( accidentals != null) {
			alterationWidth = accidentals.getNumber() * 12;
		} else {
			alterationWidth = 0;
		}
        firstNoteXPos = clefWidth + alterationWidth + alterationWidth + timeSignWidth + notesShift;

        scoreLineWidth = clefWidth + alterationWidth + timeSignWidth;

        int beatW = (int)(((double)timeSignNumerator/timeDivision) * noteDistance);
        int vxPos = scoreLineWidth + beatW;

        if (inlineMode == false)
        {
        	if (forcedNumberOfMeasures == -1)
        	{
        		numberOfMeasuresPerRow = (getWidth() - scoreLineWidth) / beatW;
        		numberOfRows = getHeight() / rowsDistance;
        	}
        	else
        	{
        		numberOfMeasuresPerRow = (getWidth() - scoreLineWidth) / beatW;
        		numberOfRows = (int)Math.ceil((double)forcedNumberOfMeasures / (double)numberOfMeasuresPerRow);
        		logger.debug("[Staff] numberOfMeasuresPerRow: " + numberOfMeasuresPerRow + ", numberOfRows: " + numberOfRows);
        	}
        	
        	scoreLineWidth += (numberOfMeasuresPerRow * beatW);
        }
        else
        {
        	scoreLineWidth = getWidth();
        }
        return vxPos;
    }
    
    // Draw staff. Includes clefs, alterations, time signature
 	/* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g) 
 	{
 		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 		super.paintComponent(g);
 		if (globalScale != 1.0)
			((Graphics2D) g).scale(globalScale, globalScale);
 		
 		g.setColor(Color.white);
 		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		
		logger.debug("[Staff - paintComponent] w = " + getWidth());

        int yPos = scoreYpos;
        int vXPos = calculateSize();
        
        for (int r = 0; r < numberOfRows; r++) 
        {
        	// draw vertical separators first
        	for (int v = 0; v < numberOfMeasuresPerRow; v++)
        		g.drawLine(vXPos + v * (int)(((double)timeSignNumerator/timeDivision) * noteDistance), yPos, vXPos + v * (int)(((double)timeSignNumerator/timeDivision) * noteDistance), yPos+40);
        	// draw the staff 5 lines 
        	for (int l = 0; l < 5; l++)
        		g.drawLine(0, yPos + (l * 10), scoreLineWidth, yPos + (l * 10));
        	
        	// 1 - Draw clef
        	if ((clefMask & appPrefs.CLEF_G2) > 0)
        	{
        		g.setFont(appFont.deriveFont(70f));
        		g.drawString("G", 0, yPos + 42);
        	}
        	else if ((clefMask & appPrefs.CLEF_F4) > 0)
        	{
        		g.setFont(appFont.deriveFont(60f));
        		g.drawString("?", 0, yPos + 40);
        	}
        	else if ((clefMask & appPrefs.CLEF_C3) > 0)
        	{
        		g.setFont(appFont.deriveFont(55f));
        		g.drawString("" + (char)0xBF, 0, yPos + 43);
        	}
        	else if ((clefMask & appPrefs.CLEF_C4) > 0)
        	{
        		g.setFont(appFont.deriveFont(55f));
        		g.drawString("" + (char)0xBF, 0, yPos + 33);
        	}
    		
    		// 2 - Draw accidentals
        	if (accidentals != null && clefs.size() > 0) {
				accidentals.paint(g, appFont, clefWidth, yPos, clefs.get(0));
			}
        	
        	// 3 - Draw tonality (only on the first row)
        	if (r == 0 && accidentals != null)
        	{
        		g.setColor(Color.gray);
        		g.setFont(new Font("LucidaSans", Font.PLAIN, 11));
        		g.drawString(accidentals.getTonality(appBundle), 0, yPos - 20);
        		g.setColor(Color.black);
        	}

        	// 4 - Draw time signature
    		String t = "";
        	if (inlineMode == false)
        	{
        		if (timeSignNumerator == 4 && timeSignDenominator == 4) {
					t = "$";
				}
        		if (timeSignNumerator == 3 && timeSignDenominator == 4) {
					t = "#";
				}
        		if (timeSignNumerator == 2 && timeSignDenominator == 4) {
					t = "@";
				}
        		if (timeSignNumerator == 6 && timeSignDenominator == 8) {
					t = "P";
				}
        		if (timeSignNumerator == 6 && timeSignDenominator == 4) {
					t = "^";
				}
        		if (timeSignNumerator == 3 && timeSignDenominator == 8) {
					t = ")";
				}

	    		g.setFont(appFont.deriveFont(58f));
	    		g.drawString(t, clefWidth + alterationWidth, yPos+41);
        	}

    		// 5 - Draw double clef elements
        	if (clefs.size() > 1)
        	{
            	for (int v = 0; v < numberOfMeasuresPerRow; v++) {
					g.drawLine(vXPos + v * (int)(((double)timeSignNumerator/timeDivision) * noteDistance), yPos + (rowsDistance / 2), vXPos + v * (int)(((double)timeSignNumerator/timeDivision) * noteDistance), yPos + (rowsDistance / 2) + 40);
				}
        		for (int l = 0; l < 5; l++) {
					g.drawLine(0, yPos + (rowsDistance / 2) + (l * 10), scoreLineWidth, yPos + (rowsDistance / 2) + (l * 10));
				}

        		// draw second clef
            	if (clefs.get(1) == appPrefs.CLEF_G2)
            	{
            		g.setFont(appFont.deriveFont(70f));
            		g.drawString("G", 0, yPos + (rowsDistance / 2) + 42);
            	}
            	else if (clefs.get(1) == appPrefs.CLEF_F4)
            	{
            		g.setFont(appFont.deriveFont(60f));
            		g.drawString("?", 0, yPos + (rowsDistance / 2) + 40);
            	}
            	else if (clefs.get(1) == appPrefs.CLEF_C3)
            	{
            		g.setFont(appFont.deriveFont(55f));
            		g.drawString("" + (char)0xBF, 0, yPos + (rowsDistance / 2) + 43);
            	}
            	else if (clefs.get(1) == appPrefs.CLEF_C4)
            	{
            		g.setFont(appFont.deriveFont(55f));
            		g.drawString("" + (char)0xBF, 0, yPos + (rowsDistance / 2) + 33);
            	}        		
        		
        		// draw accidentals
        		accidentals.paint(g, appFont, clefWidth, yPos + (rowsDistance / 2), clefs.get(1));

        		// draw tonality
            	g.setColor(Color.gray);
           	    g.setFont(new Font("LucidaSans", Font.PLAIN, 11));
            	g.drawString(accidentals.getTonality(appBundle), 0, yPos + (rowsDistance / 2) - 20);
            	g.setColor(Color.black);
        		// draw time signature
        		if (inlineMode == false)
            	{
        			g.setFont(appFont.deriveFont(58f));
        			g.drawString(t, clefWidth + alterationWidth, yPos+ + (rowsDistance / 2) + 41);
            	}
        	}
        	
        	yPos += rowsDistance;
        }
 	}
}
