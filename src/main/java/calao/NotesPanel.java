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
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class NotesPanel instantiates Staff area where
 * notes appears. 
 *
 * @author Neonunux
 */
public class NotesPanel extends JPanel implements MouseListener
{
	
	
	private static final Logger logger =  LogManager.getLogger(NotesPanel.class.getName());
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1735923156425027329L;
	
	/** The app font. */
	Font appFont;
	
	
	Preferences appPrefs;
	
	/** The notes. */
	private Vector<Note> notes; // first clef notes
	
	/** The notes2. */
	private Vector<Note> notes2; // second clef notes

    /** The clef mask. */
    private int clefMask = 1;
    
    /** The clefs. */
    private Vector<Integer> clefs = new Vector<Integer>();    
	
	/** The rows distance. */
	private int rowsDistance = 90; // distance in pixel between staff rows
	
	/** The note distance. */
	private int noteDistance = 72; // distance in pixel between 1/4 notes
	
	/** The first note x pos. */
	private int firstNoteXPos = 50;

	/** The staff width. */
	private int staffWidth;
	// kinda dirty variables used by setNotesPosition
	/** The tmp y. */
	int tmpY = 0;
	
	/** The tmp x. */
	int tmpX = 0;

	/** The inline mode. */
	private boolean inlineMode = false;
	
	/** The single note index. */
	private int singleNoteIndex = -1; // force the painting of a single note (first clef)
	
	/** The single note2 index. */
	private int singleNote2Index = -1; // force the painting of a single note (second clef)
	
	/** The selected clef. */
	private int selectedClef = 1; 

	/** The learning text. */
	private JLabel learningText;

	// edit mode, activated from the exercise panel
	/** The edit mode. */
	boolean editMode = false;
	
	/** The edit mode rhythm. */
	boolean editModeRhythm = false;
	
	/** The edit note index. */
	int editNoteIndex = -1;
	
	/** The edit note sel h. */
	int editNoteSelX = -1, editNoteSelY = -1, editNoteSelW = -1, editNoteSelH = -1;
	
	/** The edit ng. */
	NoteGenerator editNG;

	/** The global scale. */
	private double globalScale = 1.0;

	/**
	 * Instantiates a new notes panel.
	 *
	 * @param f the f
	 * @param p the p
	 * @param n the n
	 * @param n2 the n2
	 * @param inline the inline
	 */
	public NotesPanel(Font f, Preferences p, Vector<Note> n, Vector<Note> n2, boolean inline)
	{
		appFont = f;
		appPrefs = p;
		notes = n;
		notes2 = n2;
		inlineMode = inline;
		globalScale = 1.0;
		
		learningText = new JLabel("", null, JLabel.CENTER);
		learningText.setForeground(Color.decode("0x869EBA"));
		learningText.setVisible(false);
		
		setLayout(null);
		
		add(learningText);
		//setDoubleBuffered(false);
    	//setBackground(Color.blue);
		addMouseListener(this);
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
     * Gets the rows distance.
     *
     * @return the rows distance
     */
    public int getRowsDistance()
    {
    	return rowsDistance;
    }

    /**
     * Sets the clefs.
     *
     * @param type the new clefs
     */
    void setClefs(String clef) {
    	//this.clefMask = type; TODO REGIS ORIGINAL LINE
    	
    	clefs.clear();
    	
//    	if ((clefMask & appPrefs.CLEF_G2) > 0) clefs.add(appPrefs.CLEF_G2); // OLD
//    	if ((clefMask & appPrefs.CLEF_F4) > 0) clefs.add(appPrefs.CLEF_F4); // OLD
//    	if ((clefMask & appPrefs.CLEF_C3) > 0) clefs.add(appPrefs.CLEF_C3); // OLD
//    	if ((clefMask & appPrefs.CLEF_C4) > 0) clefs.add(appPrefs.CLEF_C4); // OLD
    	
//    	clefs.add("G2"); // NEW 
//    	clefs.add("C1"); // NEW
//    	clefs.add("C2"); // NEW
//    	clefs.add("C3"); // NEW
//    	clefs.add("C5"); // NEW
//    	clefs.add("C4"); // NEW
    	
    	Font ltf = new Font("Arial", Font.BOLD, 30);
    	learningText.setPreferredSize( new Dimension(200, 50));
    	learningText.setBounds((getWidth() / 2) - 150, getHeight() - 60, 300, 50);
    	learningText.setText("");
    	learningText.setFont(ltf);
    	
    	repaint();
    }
    
    /**
     * Gets the clef.
     *
     * @param idx the idx
     * @return the clef
     */
    public int getClef(int idx)
    {
    	if (idx < 0 || idx >= clefs.size())
    		return 0;
    	return clefs.get(idx);
    }
    
    /**
     * Sets the first note x position.
     *
     * @param xpos the new first note x position
     */
    public void setFirstNoteXPosition(int xpos)
    {
    	this.firstNoteXPos = xpos;
    }
    
    /**
     * Sets the staff width.
     *
     * @param w the new staff width
     */
    public void setStaffWidth(int w)
    {
    	logger.debug("[NP] staff width: " + w);
    	this.staffWidth = w;
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
     * Sets the edit mode.
     *
     * @param active the active
     * @param isRhythm the is rhythm
     */
    public void setEditMode(boolean active, boolean isRhythm)
    {
    	this.editMode = active;
    	this.editModeRhythm = isRhythm;
    }
    
    /**
     * Sets the edits the note index.
     *
     * @param idx the new edits the note index
     */
    public void setEditNoteIndex(int idx)
    {
    	this.editNoteIndex = idx;
    }
    
    /**
     * Gets the edits the note index.
     *
     * @return the edits the note index
     */
    public int getEditNoteIndex()
    {
    	return editNoteIndex;
    }

    /**
     * Sets the edits the note generator.
     *
     * @param ng the new edits the note generator
     */
    public void setEditNoteGenerator(NoteGenerator ng)
    {
    	this.editNG = ng;
    }

    /**
     * Sets the notes sequence.
     *
     * @param n the n
     * @param n2 the n2
     */
    public void setNotesSequence(Vector<Note> n, Vector<Note> n2)
    {
    	int minLev = 9;
    	int maxLev = 17;
    	int row1H = 0;
    	int row2H = 0;
    	if (notes != null) notes.clear();
    	if (notes2 != null) notes2.clear();
    	if (n != null)
    	{
    		notes = n;
    		row1H = 90;
    		for (int i = 0; i < n.size(); i++)
    		{
    			int lev = n.get(i).level;
    			if (lev < minLev) minLev = lev;
    			if (lev > maxLev) maxLev = lev;
    		}
    		logger.debug("Clef 1: minLev: " + minLev + ", maxLev: " + maxLev);
    		if (maxLev > 17) row1H += (maxLev - 17) * 5;
    		if (minLev < 9) row1H += (9 - minLev) * 5;
    	}
    	minLev = 9;
    	maxLev = 17;
    	if (n2 != null && clefs.size() == 2)
    	{
    		notes2 = n2;
    		row2H = 90;
  		    for (int i = 0; i < n2.size(); i++)
    		{
  		    	int lev = n2.get(i).level;
    			if (lev < minLev) minLev = lev;
    			if (lev > maxLev) maxLev = lev;
    		}
    		logger.debug("Clef 2: minLev: " + minLev + ", maxLev: " + maxLev);
    		if (maxLev > 17) row2H += (maxLev - 17) * 5;
    		if (minLev < 9) row2H += (9 - minLev) * 5;
    	}
    	rowsDistance = row1H + row2H;
    	logger.debug("[setNotesSequence] rowsDistance set to: " + rowsDistance);
    }

    /**
     * Sets the notes positions.
     */
    public void setNotesPositions()
    {
    	tmpX = firstNoteXPos;
    	tmpY = 0;

    	if (notes == null)
    		return;

    	for (int i = 0; i < notes.size(); i++)
    	{
    		setSingleNotePosition(notes.get(i), true);
    		//logger.debug("[Note(1): #" + i + "] type: " + notes.get(i).type + ", xpos: " + notes.get(i).xpos + ", ypos: " + notes.get(i).ypos);
    	}
    	
    	if (notes2 == null)
    		return;

    	tmpX = firstNoteXPos;
    	tmpY = 0;

    	for (int i = 0; i < notes2.size(); i++)
    	{
    		setSingleNotePosition(notes2.get(i), true);
    		//logger.debug("[Note(2): #" + i + "] type: " + notes.get(i).type + ", xpos: " + notes.get(i).xpos + ", ypos: " + notes.get(i).ypos);
    	}
    }

    /**
     * Sets the single note position.
     *
     * @param note the note
     * @param setXpos the set xpos
     */
    public void setSingleNotePosition(Note note, boolean setXpos)
    {
   		int type = note.type;
   		int ypos = (note.level * 5) + 11;
   		int yOffset = 0;

		if (tmpX >= staffWidth)
		{
			tmpX = firstNoteXPos;
			tmpY += rowsDistance;
		}

		if (note.secondRow == true)
			yOffset += (rowsDistance / 2);

		note.addLinesNumber = 0;

		if (note.level < 7)
		{
			note.addLinesNumber = 4 - (note.level / 2);
			note.addLinesYpos = ypos + tmpY - 6 + ((note.level%2) * 5);
			if (note.secondRow == true)
				note.addLinesYpos+=rowsDistance/2;
		}
		else if  (note.level > 17)
		{
			note.addLinesNumber = (note.level / 2) - 8;
			note.addLinesYpos = ypos + tmpY - 6 - ((note.level - 18) * 5);
			if (note.secondRow == true)
				note.addLinesYpos+=rowsDistance/2;
		}

		if (type == 0) // whole note
			ypos++;
		else if (type == 2 || type == 7) // quarter or dotted quarter note
		{
			if (note.level < 12)
				ypos += 41;
		}
		else if (type == 3) // eighth note
		{
			if (note.level < 12) 
				ypos += 30;
		}
		else if (type == 4) // triplets
		{
			if (note.tripletValue < 0)
				ypos += 41;
		}    		
		else if (type == 5) // silence
		{
			if (note.duration == 4)
				ypos -= 16;
			else if (note.duration == 2)
				ypos -= 12;
			else if (note.duration == 1)
				ypos += 13;
			else if (note.duration == 0.5)
				ypos += 13;
		}

		note.ypos = ypos + tmpY + yOffset;
		if (inlineMode == false && setXpos == true) // the inline game controls X position itself
		{
			note.xpos = tmpX;
			tmpX += (note.duration * noteDistance);
		}
    }

    /**
     * Sets the learning tips.
     *
     * @param tip the tip
     * @param enable the enable
     */
    public void setLearningTips(String tip, boolean enable)
    {
    	if (enable == true)
    		learningText.setText(tip);

   		learningText.setVisible(enable); 	
    }

    /**
     * Highlight note.
     *
     * @param index the index
     * @param clef the clef
     * @param enable the enable
     */
    public void highlightNote(int index, int clef, boolean enable)
    {
    	if (clef == 1)
    	{
    		if (notes.size() == 0) return;
    		singleNoteIndex = index;
    		notes.get(index).highlight = enable;
    		repaint();
    		singleNoteIndex = -1;
    	}
    	else if (clef == 2)
    	{
    		if (notes2.size() == 0) return;
    		singleNote2Index = index;
    		notes2.get(index).highlight = enable;
    		repaint();
    		singleNote2Index = -1;
    	}
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) 
	{
		//logger.debug("Mouse clicked (# of clicks: " + e.getClickCount() + ")");
    	int mouseX = e.getX();
    	int mouseY = e.getY();
		logger.debug("[Edit mode] clicked X pos: " + mouseX + ", Y pos: " + mouseY);
		//logger.debug("editNoteSelX: " + editNoteSelX + ",editNoteSelY: " + editNoteSelY + ", editNoteSelW: " + editNoteSelW + ", editNoteSelH: "+ editNoteSelH);

		if (editMode == false || editModeRhythm == true)
			return;

		if (clefs.size() > 1)
		{
			int selY = editNoteSelY;
			int selH = editNoteSelH;

			if (notes.size() == 0 && notes2.size() == 0)
			{
				selY = (selectedClef - 1) * (rowsDistance / 2);
				selH = rowsDistance / 2;
				int newClef = selectedClef;
				if (selectedClef == 1 && mouseY >= selY + selH && mouseY < selY + (selH * 2))
					newClef = 2;
				else if (selectedClef == 2 && mouseY < selY && mouseY >= selY - selH)
					newClef = 1;
				if (newClef != selectedClef)
				{
					this.firePropertyChange("newSelectedClef", selectedClef, newClef);
					selectedClef = newClef;
					repaint();
				}
				return;
			}
		}


		// if mouse clicked inside the current selection, then manage a pitch change
		if (editNoteIndex != -1 && mouseX >= editNoteSelX && mouseX < editNoteSelX + editNoteSelW && 
			mouseY >= editNoteSelY && mouseY < editNoteSelY + editNoteSelH)
		{
			Note tmpNote = null;
			if (selectedClef == 1)
				tmpNote = notes.get(editNoteIndex);
			else if (selectedClef == 2)
				tmpNote = notes2.get(editNoteIndex);
			int origLevel = tmpNote.level;
			int newLevel = (mouseY - editNoteSelY - 4) / 5;
			if (newLevel != origLevel)
			{
				tmpNote.level = (mouseY - editNoteSelY - 4) / 5;
				tmpX = tmpNote.xpos; // must 'rewind' xpos to avoid wrong check for second line
				if (selectedClef == 1)
					tmpY = editNoteSelY;
				else if (selectedClef == 2)
					tmpY = editNoteSelY - (rowsDistance/2);
				setSingleNotePosition(tmpNote, false); // do not touch X position !
				tmpNote.pitch = editNG.getPitchFromClefAndLevel(clefs.get(selectedClef - 1), tmpNote.level); // retrieve the base pitch of this level and clef
				tmpNote.pitch = editNG.getAlteredFromBase(tmpNote.pitch); // retrieve a new pitch if it is altered
				if (tmpNote.altType != 0)
					this.firePropertyChange("levelWasAltered", origLevel, newLevel);
				tmpNote.altType = 0;

				logger.debug("[Edit mode] note level: " + tmpNote.level + ", pitch = " + tmpNote.pitch);
				this.firePropertyChange("levelChanged", origLevel, newLevel);
				repaint();
			}
		}
		else
		{
			logger.debug("[Edit mode] look for a note to select...");
			// look for a note to select
			int lookupY = 0, tmpClef = 1, selH = rowsDistance;
			Vector<Note>tmpNotes = notes;
			if (clefs.size() > 1) selH = rowsDistance / 2;

			for (int c = 0; c < clefs.size(); c++)
			{
			  int lookupX = firstNoteXPos;
			  for (int i = 0; i < tmpNotes.size(); i++)
			  {
				Note tmpNote = tmpNotes.get(i);

				//logger.debug("Clef: " + tmpClef + " - #" + i + ": nX: " + (lookupX - 5) + ", nY: " + lookupY + ", nX1: " + (int)(lookupX + (tmpNote.duration * noteDistance)) + ", nY1: " + (tmpY + selH));
				if (mouseX >= lookupX - 5 && mouseX < (int)(lookupX + (tmpNote.duration * noteDistance)) && 
					mouseY >= lookupY && mouseY < lookupY + selH)
				{
					if (tmpClef != selectedClef)
					{
						this.firePropertyChange("newSelectedClef", selectedClef, tmpClef);
						selectedClef = tmpClef;
					}
					logger.debug("[Edit mode] selected note #" + i + ", pitch = " + tmpNote.pitch);
					this.firePropertyChange("selectionChanged", editNoteIndex, i);
					setEditNoteIndex(i);
					repaint();
					return;
				}
				lookupX += (tmpNote.duration * noteDistance);
				if (lookupX >= staffWidth)
				{
					lookupX = firstNoteXPos;
					lookupY += rowsDistance;
				}
			  }
			  // haven't found anything ? Look if we clicked on an empty clef
			  if (clefs.size() > 1)
			  {
				int newClef = selectedClef;
				if (tmpClef == 1 && notes2 != null && notes2.size() == 0)
					newClef = 2;
				else if (tmpClef == 2 && notes != null && notes.size() == 0)
					newClef = 1;
				if (newClef != selectedClef)
				{
					this.firePropertyChange("newSelectedClef", selectedClef, newClef);
					selectedClef = newClef;
					setEditNoteIndex(-1);
					repaint();
					return;
				}
			  }
			  lookupY = rowsDistance / 2;
			  tmpClef++;
			  tmpNotes = notes2;
			}
		}
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

    /**
     * Draw note.
     *
     * @param g the g
     * @param index the index
     * @param clef the clef
     */
    private void drawNote(Graphics g, int index, int clef) 
    {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	String symbol = "";
		Note note = null;
		if (clef == 1) note = notes.get(index);
		else if (clef == 2) note = notes2.get(index);
		int type = note.type;

		if (editMode == true && clef == selectedClef && index == editNoteIndex)
		{
			// gotta find the original note Y base position :'(
			Vector<Note>tmpNotes = notes;
			if (clef == 2)
				tmpNotes = notes2;

			int lookupX = firstNoteXPos;
			int lookupY = (clef - 1) * (rowsDistance / 2);
			for (int i = 0; i < editNoteIndex; i++)
			{
				lookupX += (tmpNotes.get(i).duration * noteDistance);
				if (lookupX >= staffWidth)
				{
					lookupX = firstNoteXPos;
					lookupY += rowsDistance;
				}
			}
			g.setColor(new Color(0xA2, 0xDD, 0xFF, 0x7F));
			editNoteSelX = note.xpos - 5;
			editNoteSelY = lookupY;
			editNoteSelW = (int)(note.duration * noteDistance);
			editNoteSelH = 130;
			g.fillRoundRect(editNoteSelX, editNoteSelY, editNoteSelW, editNoteSelH, 10, 10);
			if (clefs.size() > 1)
			{
				// highlight the current clef
				g.setColor(new Color(0x00, 0xFF, 0x00, 0x1F));
				g.fillRoundRect(0, editNoteSelY + 5, firstNoteXPos - 5, editNoteSelH - 10, 10, 10);
			}
		}

		if (note.highlight == true)
	    	g.setColor(Color.blue);
		else 
			g.setColor(Color.black);

		// draw additional lines if needed
		if (note.addLinesNumber > 0)
		{
			int addLineWidth = (note.type == 0)?23:16;
			for (int j = 0; j < note.addLinesNumber; j++)
				g.drawLine(note.xpos - 5, note.addLinesYpos + (j * 10), note.xpos + addLineWidth, note.addLinesYpos + (j * 10));
		}

    	g.setFont(appFont.deriveFont(57f));
		if (type == 0) symbol = "w"; // whole note
		else if (type == 1 || type == 6)
		{
			symbol = "h"; // half note
			if (type == 6) // dotted half
				g.fillOval(note.xpos + 15, note.ypos - 8, 5, 5);
		}
		else if (type == 2 || type == 7)
		{
			if (note.level >= 12) symbol = "q"; // quarter note upward
			else symbol = "" + (char)0xF6; // quarter note downward
			if (type == 7) // dotted quarter
			{
				if (note.level >= 12)
					g.fillOval(note.xpos + 15, note.ypos - 8, 5, 5);
				else
					g.fillOval(note.xpos + 15, note.ypos - 49, 5, 5);
			}
		}
		else if (type == 3)
		{
			if (note.level >= 12) symbol = "" + (char)0xC8; // eighth note upward 
			else symbol = "" + (char)0xCA; // eighth note downward
		}
		else if (type == 4)
		{
			if (note.tripletValue > 0)
				symbol = "q"; // triplet note upward
			else symbol = "" + (char)0xF6; // triplet note downward
		}
		else if (type == 5) // silence
		{
			if (note.duration == 4)
				g.fillRect(note.xpos + (int)(noteDistance * 1.55), note.ypos, 14, 6);
			else if (note.duration == 2)
				g.fillRect(note.xpos, note.ypos, 14, 6);
			else if (note.duration == 1)
				symbol = "Q";
			else if (note.duration == 0.5)
			{
				g.setFont(appFont.deriveFont(50f));
				symbol = "E";
			}
		}

		g.drawString(symbol, note.xpos, note.ypos);

		// draw alteration symbol if required
		if (note.altType != 0)
		{
			int altYOff = 0;
			g.setFont(appFont.deriveFont(50f));
			if (note.level < 12)
			{
				if (note.type == 2 || note.type == 7) 
					altYOff = -41;
				else if (note.type == 3)
					altYOff = -30;
			}
			if (note.altType == -2)
			{
				g.drawString("b", note.xpos - 19, note.ypos + altYOff);
				g.drawString("b", note.xpos - 12, note.ypos + altYOff);
			}
			else if (note.altType == -1)
				g.drawString("b", note.xpos - 12, note.ypos + altYOff);
			else if (note.altType == 1)
				g.drawString("B", note.xpos - 12, note.ypos + altYOff);
			else if (note.altType == 2)
				g.drawString("" + (char)0xBD, note.xpos - 14, note.ypos + altYOff);
		}

		// draw triplets special graphics
    	if (note.tripletValue != 0)
    	{
    		int tsub = 0; 
    		if (note.tripletValue < 0) // notes downward
    		{
    			if (note.tripletValue <= -1000) tsub = 1000;
    			int tripletBarYPos = note.ypos + (((Math.abs(note.tripletValue) - tsub) - note.level) * 5);
    			g.drawLine(note.xpos, note.ypos - 15, note.xpos, tripletBarYPos - 15);
    			if (note.tripletValue > -1000)
    			{
    				g.setFont(new Font("Arial", Font.BOLD, 15));
            		g.drawString("3", note.xpos + 22, tripletBarYPos + 3);
    				g.fillRect(note.xpos, tripletBarYPos - 15, 49, 5);
    			}
    		}
    		else // notes upward
    		{
    			if (note.tripletValue >= 1000) tsub = 1000;
    			int tripletBarYPos = note.ypos - ((note.level - (note.tripletValue - tsub)) * 5);
    			g.drawLine(note.xpos + 11, note.ypos - 40, note.xpos + 11, tripletBarYPos - 40);
    			if (note.tripletValue < 1000)
    			{
    				g.setFont(new Font("Arial", Font.BOLD, 15));
            		g.drawString("3", note.xpos + 32, tripletBarYPos - 42);
    				g.fillRect(note.xpos + 11, tripletBarYPos - 40, 49, 5);
    			}
    		}	
    	}
    }

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) 
 	{
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (globalScale != 1.0)
			((Graphics2D) g).scale(globalScale, globalScale);

    	if (singleNoteIndex == -1 && singleNote2Index == -1)
    	{
        	g.setColor(Color.black);
        	if (notes != null)
        	{
        		for (int i = 0; i < notes.size(); i++)
        			drawNote(g, i, 1);
        	}
        	if (notes2 != null)
        	{
        		for (int i = 0; i < notes2.size(); i++)
        			drawNote(g, i, 2);
        	}
    		if (editMode == true && editNoteIndex == -1 && clefs.size() > 1)
    		{
    			g.setColor(new Color(0x00, 0xFF, 0x00, 0x1F));
    			if (selectedClef == 1)
    				g.fillRoundRect(0, 5, firstNoteXPos - 5, 120, 10, 10);
    			else if (selectedClef == 2)
    				g.fillRoundRect(0, rowsDistance/2, firstNoteXPos - 5, 120, 10, 10);
    		}
    	}
    	else
    	{
    		if (singleNote2Index == -1)
    			drawNote(g, singleNoteIndex, 1);
    		else if (singleNoteIndex != -1 && singleNoteIndex < notes2.size())
    			drawNote(g, singleNoteIndex, 2);
    	}
 	}
}
