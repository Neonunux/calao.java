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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Neonunux
 *
 */
public class Note 
{
	
	
	private static final Logger logger =  LogManager.getLogger(Note.class.getName());
	
	/** The xpos. */
	public int xpos;
	
	/** The ypos. */
	public int ypos;
	
	/** The type. */
	public int type; // 0 whole, 1 half, 2 quarter, 3 eighth, 4 triplet, 5 silence, 6 dotted half, 7 dotted quarter
	
	/** The duration. */
	public double duration;
	
	/** The timestamp. */
	public double timestamp;

	/** The clef. */
	public int clef; // note clef
	
	/** The alt type. */
	public int altType; // alteration to be displayed. Can be: -2 = double flat, -1 = flat, 0 = none, 1 = sharp, 2 = natural
	
	/** The second row. */
	public boolean secondRow; // indicates whether the note is on the first or second row
	
	/** The level. */
	public int level; // note level as handled in the ClefSelector
	
	/** The add lines number. */
	public int addLinesNumber; // number of additional lines (if present)
	
	/** The add lines ypos. */
	public int addLinesYpos; // Y position of the first additional line (if present)
	
	/** The pitch. */
	public int pitch; // MIDI note pitch
	
	/** The triplet value. */
	public int tripletValue = 0;
	
	/** The highlight. */
	public boolean highlight; // used when playing a rhtyhm or score sequence
	
	/**
	 * Instantiates a new note.
	 *
	 * @param xPos the x pos
	 * @param nClef the n clef
	 * @param nLevel the n level
	 * @param nPitch the n pitch
	 * @param nType the n type
	 * @param nSecondRow the n second row
	 * @param nAlt the n alt
	 */
	public Note(int xPos, int nClef, int nLevel, int nPitch, int nType, boolean nSecondRow, int nAlt)
	{
		xpos = xPos;
		clef = nClef;
		altType = nAlt;
		level = nLevel;
		pitch = nPitch;
		type = nType;
		secondRow = nSecondRow;
		addLinesNumber = 0;
		addLinesYpos = 0;
		highlight = false;
		timestamp = 0;

		switch(type)
		{
			case 0: duration = 4; break;
			case 1: duration = 2; break;
			case 2: duration = 1; break;
			case 3: duration = 0.5; break;
			case 4: duration = 1.0 / 3.0; break;
			case 5: duration = 0; level = 12; pitch = 71; break;
			case 6: duration = 3; break;
			case 7: duration = 1.5; break;
		}
		
		logger.debug("[Note] t: " + type + ", p: " + pitch + ", l: " + level + ", dur: " + duration + ", alt: " + altType);
		
		ypos = 0; // y positions are calculated by the NotesPanel
	}
	
	/**
	 * Gets the duration.
	 *
	 * @param type the type
	 * @return the duration
	 */
	public double getDuration(int type)
	{
		double dur = 1;
		switch(type)
		{
			case 0: dur = 4; break;
			case 1: dur = 2; break;
			case 2: dur = 1; break;
			case 3: dur = 0.5; break;
			case 4: dur = 1.0 / 3.0; break;
			case 5: dur = 0; break;
			case 6: dur = 3; break;
			case 7: dur = 1.5; break;
		}
		return dur;
	}
	
	/**
	 * Sets the triplet value.
	 *
	 * @param val the new triplet value
	 */
	public void setTripletValue(int val)
	{
		this.tripletValue = val;
	}
	
	/**
	 * Sets the time stamp.
	 *
	 * @param ts the new time stamp
	 */
	public void setTimeStamp(double ts)
	{
		this.timestamp = ts;
	}
}
