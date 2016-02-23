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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/*
 *  Preferences map:
 *  
 *  language         | the UI global language
 *  
 *  clefsMask        |  (TREBLE_CLEF|BASS_CLEF|ALTO_CLEF|TENOR_CLEF); REMOVED
 *  voices			 | Configuration clef of the 4 score voices
 *  ClefG2Lower  	 | pitch of the lowest note used in exercises
 *  ClefG2Upper  	 | pitch of the highest note used in exercises
 *  ClefF4Lower    	 | pitch of the lowest note used in exercises
 *  ClefF4Upper    	 | pitch of the highest note used in exercises
 *  ClefC3Lower    	 | pitch of the lowest note used in exercises
 *  ClefC3Upper    	 | pitch of the highest note used in exercises
 *  ClefC4Lower   	 | pitch of the lowest note used in exercises
 *  ClefC4Upper   	 | pitch of the highest note used in exercises
 *  
 *  accidentals      | index of accidentals related to ClefNotesOptionDialog combo box list
 *  timeSignature    | index of time signature related to ClefNotesOptionDialog radio button group
 *  wholeNote        | 0: whole notes disabled, 1: whole notes enabled
 *  halfNote         | 0: half notes disabled, 1: half notes enabled
 *  quarterNote      | 0: quarter notes disabled, 1: quarter notes enabled
 *  eighthNote       | 0: eighth notes disabled, 1: eighth notes enabled
 *  tripletNote      | 0: triplets disabled, 1: triplets enabled
 *  silenceNote      | 0: silence disabled, 1: silence enabled
 *  3_4_Note		 | 0: 3/4 notes disabled, 1: 3/4 notes enabled
 *  3_8_Note		 | 0: 3/8 notes disabled, 1: 3/8 notes enabled
 *  
 *  voice0			 | First voice
 *  voice1			 | Second voice
 *  voice2			 | Third voice
 *  voice4			 | Forth voice
 *  
 *  metromome	     | metronome - 0: disabled, 1: enabled 
 *  showBeats		 | show metronome beats - 0: disabled, 1: enabled
 *  clickAccents     | metronome accents - 0: disabled, 1: enabled
 *  
 *  keyboardlenght   | number of piano keys. Can be 63 or 73
 *  mididevice       | index of the MIDI device to use. 0 is always "no device"
 *  sound            | playback sound enabled
 *  keyboardsound    | exercises sound enabled 
 *  instrument       | index of instrument taken from instrument list
 *  latency          | latency between MIDI in and synthesizer playback
 *  transposition    | number of octaves used to transpose sound
 *  
 *  audiodevice      | index of the audio input device to use for capture
 *  defaultInput     | default input to capture notes. 0: MIDI, 1: Microphone
 *  audiosensitivity | audio sensitivity threshold
 *  
 *  synthDriver      | synthesizer system to be used (Java or Fluidsynth)
 *  fluidDevice		 | audio device that Fluidsynth will use to output sounds     // TODO: not used yet
 *  soundfontPath    | path of the Soundfont to use when Fluidsynth is active
 *  
 *  inputDevice      | input device to use to acquire notes [MIDI | Audio],index
 *  outputDevice     | outputDevice to reproduce notes [Java | Fluidsynth],index
 */
/**
 * The Class Preferences.
 *
 * @author Neonunux
 */
public class Preferences 
{
	
	
	private static final Logger logger =  LogManager.getLogger(Preferences.class.getName());
	
	/** The CLE f_ g2. */
	//public int CLEF_G2 = 0x0001; // OLD
	
	/** The CLE f_ f4. */
	//public int CLEF_F4 = 0x0002; // OLD
	
	/** The CLE f_ c3. */
	//public int CLEF_C3 = 0x0004; // OLD
	
	/** The CLE f_ c4. */
	//public int CLEF_C4 = 0x0008; // OLD
	
	public int G2_CLEF = 0x0001; // ADDED
	public int C1_CLEF = 0x0002; // ADDED
	public int C2_CLEF = 0x0004; // ADDED
	public int C3_CLEF = 0x0008; // ADDED
	public int C4_CLEF = 0x0016; // ADDED
	public int C5_CLEF = 0x0032; // ADDED
	public int F4_CLEF = 0x0064; // ADDED
	
	/** The game stopped. */
	public int GAME_STOPPED        = 0;
	
	/** The inline single notes. */
	public int INLINE_SINGLE_NOTES = 1;
	
	/** The inline more notes. */
	public int INLINE_MORE_NOTES   = 2;
	
	/** The inline learn notes. */
	public int INLINE_LEARN_NOTES  = 3;
	
	/** The score game listen. */
	public int SCORE_GAME_LISTEN   = 4;
	
	/** The rhtyhm game user. */
	public int RHTYHM_GAME_USER    = 5;
	
	/** The score game user. */
	public int SCORE_GAME_USER     = 6;
	
	/** The ear training. */
	public int EAR_TRAINING		   = 7;
	
	/** The inline normal mode. */
	public int INLINE_NORMAL_MODE  = 8;
	
	/** The note normal. */
	public int NOTE_NORMAL		   = 0;
	
	/** The note accidentals. */
	public int NOTE_ACCIDENTALS	   = 1;
	
	/** The note intervals. */
	public int NOTE_INTERVALS      = 2;
	
	/** The note chords. */
	public int NOTE_CHORDS   	   = 3;

	/** The prefs. */
	Properties prefs = new Properties();
	
	/** The global exercise mode. */
	boolean globalExerciseMode = false;
	
	/** The current exercise. */
	Exercise currentExercise;

	/**
	 * Instantiates a new preferences.
	 */
	public Preferences()
	{
	  try
	  {
		prefs.load(new FileInputStream("calao.properties"));
 	    logger.trace("language = " + prefs.getProperty("language"));
		//prefs.list(System.out);
  	  }
  	  catch (Exception e) 
  	  {
 	      logger.debug(e);
  	  }
	}

	/**
	 * Gets the property.
	 *
	 * @param prop the prop
	 * @return the property
	 */
	public String getProperty(String prop)
	{
		if (prop == "language")
			return prefs.getProperty(prop, "");
		else
			return prefs.getProperty(prop, "-1");
	}

	/**
	 * Sets the property.
	 *
	 * @param prop the prop
	 * @param value the value
	 */
	public void setProperty(String prop, String value)
	{
		this.prefs.setProperty(prop, value);
	}

	/**
	 * Store properties.
	 */
	public void storeProperties()
	{
		try 
		{ 
			prefs.store(new FileOutputStream("calao.properties"), null); 
			//prefs.list(System.out);
        } 
        catch (IOException e) { }
	}
	
	/**
	 * Sets the exercise mode.
	 *
	 * @param enable the enable
	 * @param e the e
	 */
	public void setExerciseMode(boolean enable, Exercise e)
	{
		this.globalExerciseMode = enable;
		this.currentExercise = e;
	}
}
