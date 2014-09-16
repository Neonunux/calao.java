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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequencer;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class ScorePanel.
 *
 * @author Neonunux
 */
public class ScorePanel extends JPanel implements ActionListener, KeyListener
{
	
	
	private static final Logger logger =  LogManager.getLogger(ScorePanel.class.getName());
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The app font. */
	Font appFont;
	
	
	Preferences appPrefs;
	
	/** The app bundle. */
	private ResourceBundle appBundle;
	
	/** The app midi. */
	private MidiController appMidi;
	
	/** The is rhythm. */
	private boolean isRhythm;

	/** The s bar. */
	public SmartBar sBar;
	
	/** The s bar height. */
	private int sBarHeight = 125;
	
	/** The score scroll panel. */
	private JScrollPane scoreScrollPanel;
	
	/** The layers. */
	private JLayeredPane layers;
	
	/** The staff layer. */
	private Staff staffLayer;
	
	/** The notes layer. */
	private NotesPanel notesLayer;
	
	/** The answers layer. */
	private AnswersPanel answersLayer;
	
	/** The game bar. */
	private GameBar gameBar;
	
	/** The score accidentals. */
	private Accidentals scoreAccidentals;
	
	/** The score ng. */
	private NoteGenerator scoreNG;
	
	/** The stats. */
	private Statistics stats;
	
	/** The game notes. */
	Vector<Note> gameNotes = new Vector<Note>(); // array of random notes (1st clef) composing the game
	
	/** The game notes2. */
	Vector<Note> gameNotes2 = new Vector<Note>(); // array of random notes (2nd clef) composing the game
	
	/** The user notes. */
	Vector<Integer> userNotes = new Vector<Integer>(); // array of notes hit by the user

	// Graphics metrics 
	/** The staff h margin. */
	private int staffHMargin = 50;
	
	/** The staff v margin. */
	private int staffVMargin = 120;
	
	/** The staff height. */
	private int staffHeight = 400;
	
	/** The g bar height. */
	private int gBarHeight = 40;
	
	/** The rows distance. */
	private int rowsDistance = 90; // distance in pixel between staff rows
		
	// Time variables
	/** The current speed. */
	private int currentSpeed = 120;
	
	/** The latency. */
	private int latency = 0;
	
	/** The time numerator. */
	private int timeNumerator = 4;
	
	/** The time denominator. */
	private int timeDenominator = 4;
	
	/** The time division. */
	private int timeDivision = 1;

	// MIDI controllers
	/** The metronome. */
	private Sequencer metronome;
	
	/** The playback. */
	private Sequencer playback;
	
	// Game variables
	/** The game thread. */
	private ScoreGameThread gameThread = null;
	
	/** The game started. */
	private boolean gameStarted = false; // variable to control thread job
	
	/** The game type. */
	private int gameType = -1; // type of game. See prefernces for values
	
	/** The start time. */
	private long startTime; // timestamp of cursor at the beginning of a row
	
	/** The current note index. */
	private int currentNoteIndex = -1; // index of the currently playing note (first clef)
	
	/** The current note2 index. */
	private int currentNote2Index = -1; // index of the currently playing note (second clef)
	
	/** The cursor start x. */
	private int cursorStartX; // X start position of cursor for each row
	
	/** The cursor x. */
	private int cursorX; // X position of cursor during game
	
	/** The cursor y. */
	private int cursorY; // Y position of cursor during game
	
	/** The is key pressed. */
	private boolean isKeyPressed = false;
	
	// Variables to check notes validity
	/** The accuracy. */
	private int accuracy = 24; // a note is valid within 24 pixels around the note X position 
	//private int releaseXpos; // on key press, save the release X position for key release check
	
	/** The exercise mode. */
	private boolean exerciseMode = false;
	
	/** The curr ex. */
	private Exercise currEx = null;
	
	/**
	 * Instantiates a new score panel.
	 *
	 * @param f the f
	 * @param b the b
	 * @param p the p
	 * @param mc the mc
	 * @param d the d
	 * @param rhythm the rhythm
	 */
	public ScorePanel(Font f, ResourceBundle b, Preferences p, MidiController mc, Dimension d, boolean rhythm)
	{
		appFont = f;
		appBundle = b;
		appPrefs = p;
		appMidi = mc;
		isRhythm = rhythm;
		
		setBackground(Color.white);
		setSize(d);
		setLayout(null);
		if (appPrefs.globalExerciseMode == true)
		{
			exerciseMode = true;
			currEx = appPrefs.currentExercise;
			scoreAccidentals = new Accidentals(currEx.acc.getType(), currEx.acc.getNumber(), appPrefs);
		}
		else
		{
			scoreAccidentals = new Accidentals("", 0, appPrefs);
			scoreNG = new NoteGenerator(appPrefs, scoreAccidentals, rhythm);
		}
		stats = new Statistics();
		
		sBar = new SmartBar(new Dimension(d.width-8, sBarHeight), b, f, p, false, false, false);
		sBar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt)
			{
				if (evt.getPropertyName() == "updateParameters")
				{
					logger.debug("SCORE panel update parameters !");
					refreshPanel();
				}
			}
		});
		
		sBar.refreshBtn.addActionListener(this);
		sBar.playBtn.addActionListener(this);
		sBar.listenBtn.addActionListener(this);
		
		if (exerciseMode == true)
			sBar.tempoSlider.setValue(currEx.speed);
		
		int panelsWidth = d.width - (staffHMargin * 2);
		staffHeight = getHeight() - sBarHeight - gBarHeight;
		
		layers = new JLayeredPane();
		layers.setPreferredSize( new Dimension(panelsWidth, staffHeight));
		//layers.setBounds(staffHMargin, 0, panelsWidth, staffHeight);
		
		staffLayer = new Staff(appFont, appBundle, appPrefs, scoreAccidentals, false, true);
		staffLayer.setPreferredSize( new Dimension(panelsWidth, staffHeight));
		staffLayer.setBounds(0, 0, panelsWidth, staffHeight);
		staffLayer.setOpaque(true);
		
		notesLayer = new NotesPanel(appFont, appPrefs, gameNotes, gameNotes2, false);
		notesLayer.setPreferredSize( new Dimension(panelsWidth, staffHeight));
		notesLayer.setBounds(0, 0, panelsWidth, staffHeight);
		notesLayer.setOpaque(false);
		
		answersLayer = new AnswersPanel();
		answersLayer.setPreferredSize( new Dimension(panelsWidth, staffHeight));
		answersLayer.setBounds(0, 0, panelsWidth, staffHeight);
		answersLayer.setOpaque(false);
		
		layers.add(staffLayer, new Integer(1));
		layers.add(notesLayer, new Integer(2));
		layers.add(answersLayer, new Integer(3));
		
		scoreScrollPanel = new JScrollPane(layers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scoreScrollPanel.getViewport().setBackground(Color.white);
        scoreScrollPanel.setBounds(0, staffVMargin - 10, panelsWidth + (staffHMargin * 2), staffHeight);
		
		gameBar = new GameBar(new Dimension(d.width, gBarHeight), b, f, p, false);
		gameBar.setBounds(0, getHeight() - gBarHeight, getWidth(), gBarHeight);
		
		gameType = appPrefs.GAME_STOPPED;
		
		if (isRhythm)
			this.addKeyListener(this);

		add(sBar);
		add(scoreScrollPanel);
		add(gameBar);
		refreshPanel();
	}
	
	/**
	 * Refresh panel.
	 */
	public void refreshPanel()
	{
		int tsIdx = 0;
		if (exerciseMode == false)
		{
			scoreNG.update();

			if (isRhythm == false)
				rowsDistance = scoreNG.getRowsDistance();
			else
				rowsDistance = 90;
			staffLayer.setRowsDistance(rowsDistance);
			notesLayer.setRowsDistance(rowsDistance);
			staffLayer.setClefs(scoreNG.getClefMask());
			notesLayer.setClefs(scoreNG.getClefMask());
			logger.debug("[ScorePanel] Staff width: " + staffLayer.getStaffWidth() + ", rowsDistance: " + rowsDistance);
			tsIdx = Integer.parseInt(appPrefs.getProperty("timeSignature"));
		}
		else
		{
			staffLayer.setClefs(currEx.clefMask);
			notesLayer.setClefs(currEx.clefMask);
			tsIdx = currEx.timeSign;
		}
		
		timeDenominator = 4;
		if (tsIdx <= 0) timeNumerator = 4;
		else if (tsIdx == 1) timeNumerator = 2;
		else if (tsIdx == 2) timeNumerator = 3;
		else if (tsIdx == 3) { timeNumerator = 6; timeDenominator = 8; }
		else if (tsIdx == 4) timeNumerator = 6;
		else if (tsIdx == 5) { timeNumerator = 3; timeDenominator = 8; }

		staffLayer.setTimeSignature(timeNumerator, timeDenominator);
		timeDivision = timeDenominator / 4;
		notesLayer.setFirstNoteXPosition(staffLayer.getFirstNoteXPosition());

		notesLayer.setStaffWidth(staffLayer.getStaffWidth());
		answersLayer.clearSurface();
		
		if (Integer.parseInt(appPrefs.getProperty("showBeats")) == 1)
			answersLayer.enableCursor(true);
		else
			answersLayer.enableCursor(false);
		
		latency = Integer.parseInt(appPrefs.getProperty("latency"));
		if (latency < 0) latency = 0;
		
		if (exerciseMode == false)
			createNewSequence();
		else
		{
			gameNotes = currEx.notes;
			gameNotes2 = currEx.notes2;
			double totalDuration = currEx.notes.get(currEx.notes.size() - 1).timestamp + currEx.notes.get(currEx.notes.size() - 1).duration;
	        staffLayer.setMeasuresNumber((int)Math.ceil(totalDuration / (timeNumerator / (timeDenominator / 4))));
	        notesLayer.setNotesSequence(currEx.notes, currEx.notes2);
			rowsDistance = notesLayer.getRowsDistance();
			staffLayer.setRowsDistance(rowsDistance);
			notesLayer.setRowsDistance(rowsDistance);
			notesLayer.setNotesPositions();
		}
	}
	
	/**
	 * Update language.
	 *
	 * @param bundle the bundle
	 */
	public void updateLanguage(ResourceBundle bundle)
	{
		appBundle = bundle;
		sBar.updateLanguage(appBundle);
		gameBar.updateLanguage(appBundle);
	}

	/**
	 * Creates the new sequence.
	 */
	public void createNewSequence()
	{
		gameNotes.clear();
		gameNotes2.clear();
		scoreNG.getRandomSequence(gameNotes, staffLayer.getMeasuresNumber(), isRhythm, 1);
		if (scoreNG.getClefsNumber() == 2)
			scoreNG.getRandomSequence(gameNotes2, staffLayer.getMeasuresNumber(), isRhythm, 2);
		notesLayer.setNotesPositions();
	}

	/**
	 * Update game stats.
	 *
	 * @param answType the answ type
	 */
	private void updateGameStats(int answType)
	{
		int score = -50;
		if (answType == 1)
		{
			score = 100;
			score *= (currentSpeed / 40); // multiply by speed factor
		}

		stats.notePlayed(answType, score);
		gameBar.precisionCnt.setText(Integer.toString(stats.getAveragePrecision()) + "%");
		gameBar.scoreCnt.setText(Integer.toString(stats.getTotalScore()));
	}
	
	/**
	 * Game finished.
	 */
	private void gameFinished()
	{
		String title;
		int type = 0;
		int correct = stats.getCorrectNumber();
		int wrong = stats.getWrongNumber();
		int rhythms = stats.getWrongRhythms();
		
		if (wrong == 0 && correct > 0)
		{
			title = appBundle.getString("_congratulations");
			type = JOptionPane.INFORMATION_MESSAGE;
		}
		else if (correct > wrong)
		{
			title = appBundle.getString("_sorry");
			type = JOptionPane.WARNING_MESSAGE;
		}
		else
		{
             title = appBundle.getString("_sorry");
			 type = JOptionPane.ERROR_MESSAGE;
		}
		
		JOptionPane.showMessageDialog(this.getParent(), "<html><b>" + stats.getNotesPlayed() + " " + appBundle.getString("_menuNotes") +
				" : "+ stats.getCorrectNumber() + " "+ appBundle.getString("_correct")+
                " / " + stats.getWrongNumber() + " " + appBundle.getString("_wrong") + 
                " ( " + rhythms + " "+ appBundle.getString("_wrongrhythm") + " )</b></html>",
                title, type);
		
		if (Integer.parseInt(appPrefs.getProperty("saveStats")) == 1)
		{
			if (gameType == appPrefs.RHTYHM_GAME_USER)
				stats.storeData(1);
			else if (gameType == appPrefs.SCORE_GAME_USER)
				stats.storeData(2);
		}
	}
	
	/**
	 * Check note.
	 *
	 * @param cursorPos the cursor pos
	 * @param pitch the pitch
	 * @param press the press
	 */
	private void checkNote(int cursorPos, int pitch, boolean press)
	{
		int delta1 = -1, delta2 = -1;
		if (currentNoteIndex < 0 || currentNoteIndex >= gameNotes.size())
			return;
		// find the closest pitch against current clefs notes
		delta1 = Math.abs(pitch - gameNotes.get(currentNoteIndex).pitch);
		if (gameNotes2.size() > 0 && currentNote2Index != -1)
			delta2 = Math.abs(pitch - gameNotes2.get(currentNote2Index).pitch);
		if (delta2 == -1 || delta1 < delta2)
			checkAnswer(cursorPos, currentNoteIndex, gameNotes, pitch, press, false);
		else
			checkAnswer(cursorPos, currentNote2Index, gameNotes2, pitch, press, true);
	}

	/**
	 * Check answer.
	 *
	 * @param cursorPos the cursor pos
	 * @param noteIdx the note idx
	 * @param n the n
	 * @param pitch the pitch
	 * @param press the press
	 * @param secondClef the second clef
	 */
	private void checkAnswer(int cursorPos, int noteIdx, Vector<Note> n, int pitch, boolean press, boolean secondClef)
	{
		if (noteIdx < 0 || noteIdx >= n.size())
			return;
		
		int answerY = cursorY;
		if (secondClef == true)
			answerY += rowsDistance / 2;

		// adjust X position if latency is on
		if (latency > 0)
			cursorPos -= ((staffLayer.getNotesDistance() * latency) / (60000 / currentSpeed));

		if (press == true) // check key press
		{
			if (n.size() == 0) return; // security check
			int lookupIndex = noteIdx;
			int noteMargin = n.get(lookupIndex).xpos - (accuracy / 2);
			boolean noteFound = false;

			// check against current note X position + margins
			if (cursorPos > noteMargin && cursorPos < noteMargin + accuracy)
				noteFound = true;

			// maybe the user pressed the key too early. Check on the next note
			if (noteFound == false && lookupIndex < n.size())
			{
				lookupIndex++;
				if (n.size() == 0 || lookupIndex >= n.size()) 
					return; // security check
				noteMargin = n.get(lookupIndex).xpos - (accuracy / 2);
					
				if (cursorPos > noteMargin && cursorPos < noteMargin + accuracy)
					noteFound = true;
			}
			
			// still not found ? Display a warning
			if (noteFound == false)
			{
				answersLayer.drawAnswer(2, cursorPos, answerY);
				lookupIndex = noteIdx;
			}
			else
			{
				if (n.size() == 0) return; // security check

				if ((pitch == n.get(lookupIndex).pitch || 
					gameType == appPrefs.RHTYHM_GAME_USER) && // any pitch is OK for rhythm game 
					n.get(lookupIndex).type != 5)  
					{
						answersLayer.drawAnswer(1, cursorPos, answerY); // pitch and rhythm correct
						updateGameStats(1);
					}
				else
				{
					answersLayer.drawAnswer(0, cursorPos, answerY); // wrong pitch
					updateGameStats(0);
				}
			}

			if (n.size() == 0) return; // security check
		    logger.debug("[checkNote *pressed*] noteIdx: " + noteIdx + 
					   ", xpos: " + n.get(noteIdx).xpos +
					   ", cursor: " + cursorPos +
			           ", noteMargin: " + noteMargin);
		}
		else // check release
		{
			int idx = noteIdx;
			/* at high speed, it might happen that two notes get overlapped, so look at the previous one */
			if (idx > 0 && pitch != n.get(idx).pitch) 
				idx--;
			int releaseXpos = n.get(idx).xpos + (int)(72 * n.get(idx).duration) + (accuracy / 2);
			logger.debug("[checkNote *release*] cursorPos: " +  cursorPos + 
						", noteXpos: " + n.get(idx).xpos + ", releaseXpos: " + releaseXpos);
			 
			if (((cursorPos < releaseXpos - (accuracy * 2) && cursorPos > cursorStartX + accuracy) || cursorPos > releaseXpos) && 
				  cursorPos > n.get(idx).xpos + accuracy)
			//if (cursorPos < releaseXpos - (accuracy * 2) || cursorPos > releaseXpos)
			{
				answersLayer.drawAnswer(2, cursorPos, answerY);
				updateGameStats(2);
			}
		}
	}

	/**
	 * Note event.
	 *
	 * @param pitch the pitch
	 * @param velocity the velocity
	 */
	public void noteEvent(int pitch, int velocity)
	{
		if (gameType != appPrefs.SCORE_GAME_LISTEN)
		{
			if (velocity != 0)
			{
				appMidi.playNote(pitch, 90);
				checkNote(cursorX, pitch, true);
			}
			else
			{
				appMidi.stopNote(pitch, 0);
				checkNote(cursorX, pitch, false);
			}			
		}
	}

	/**
	 * Handle async mid ievent.
	 *
	 * @param msg the msg
	 */
	private void handleAsyncMIDIevent(MetaMessage msg)
	{
		byte[] metaData = msg.getData();
        String strData = new String(metaData);
       
        //logger.debug("*SCOREPANEL* META message: text= " + strData);

        if ("beat".equals(strData)) 
        {
        	if (gameType != appPrefs.SCORE_GAME_LISTEN && sBar.metronomeCheckBox.isSelected() == true)
        		answersLayer.drawMetronome(cursorX, cursorY);
        }
        else if ("gameOn".equals(strData))
        {
        	// this is a workaround
        	answersLayer.drawCursor(cursorX, cursorY, true);
        }
        else if ("cursorOn".equals(strData))
        {
        	cursorStartX = staffLayer.getFirstNoteXPosition() - staffLayer.getNotesDistance();
        	cursorX = cursorStartX;
        	cursorY = 10;
        	startTime = System.currentTimeMillis();
        }
        else if ("nOn".equals(strData))
        {
        	if (gameType == appPrefs.SCORE_GAME_LISTEN)
        	{
        		notesLayer.highlightNote(currentNoteIndex, 1, true);
	        	if (scoreScrollPanel.getVerticalScrollBar().isVisible() == true)
	        	{
	        		int scrollAmount = scoreScrollPanel.getVerticalScrollBar().getMaximum() + rowsDistance - scoreScrollPanel.getVerticalScrollBar().getVisibleAmount();
	        		int newPos = (scrollAmount * ((currentNoteIndex * 100) / gameNotes.size())) / 100;
	        		//logger.debug("Scrollbar amount: " + newPos);
	        		scoreScrollPanel.getVerticalScrollBar().setValue(newPos);
	        	}
        	}
        }
        else if ("nOff".equals(strData))
        {
        	if (gameType == appPrefs.SCORE_GAME_LISTEN)
        		notesLayer.highlightNote(currentNoteIndex, 1, false);
        	currentNoteIndex++;
        	gameBar.progress.setValue((currentNoteIndex * 100) / gameNotes.size());
        }
        else if ("n2On".equals(strData))
        {
        	if (gameType == appPrefs.SCORE_GAME_LISTEN)
        	{
        		notesLayer.highlightNote(currentNote2Index, 2, true);
	        	if (scoreScrollPanel.getVerticalScrollBar().isVisible() == true)
	        	{
	        		int scrollAmount = scoreScrollPanel.getVerticalScrollBar().getMaximum() + rowsDistance - scoreScrollPanel.getVerticalScrollBar().getVisibleAmount();
	        		int newPos = (scrollAmount * ((currentNoteIndex * 100) / gameNotes.size())) / 100;
	        		//logger.debug("Scrollbar amount: " + newPos);
	        		scoreScrollPanel.getVerticalScrollBar().setValue(newPos);
	        	}
        	}
        }
        else if ("n2Off".equals(strData))
        {
        	if (gameType == appPrefs.SCORE_GAME_LISTEN)
        		notesLayer.highlightNote(currentNote2Index, 2, false);
        	currentNote2Index++;
        	gameBar.progress.setValue((currentNoteIndex * 100) / gameNotes.size());
        }
        else if ("end".equals(strData))
        {
        	gameStarted = false;
        	if (gameType == appPrefs.SCORE_GAME_LISTEN)
        		appMidi.stopPlayback();
        	appMidi.stopMetronome();
			sBar.playBtn.setButtonImage(new ImageIcon(getClass().getResource("playback.png")).getImage());
			sBar.playBtn.repaint();
			startTime = 0;
			answersLayer.drawCursor(cursorX, cursorY, true);
			if (gameType != appPrefs.SCORE_GAME_LISTEN)
				gameFinished();
			gameType = appPrefs.GAME_STOPPED;
			currentNoteIndex = -1;
			currentNote2Index = -1;
        }
	}
	
	 /**
 	 *  Handle the key typed event from the text field.
 	 *
 	 * @param e the e
 	 */
    public void keyTyped(KeyEvent e) { }
    
    /**
     *  Handle the key-pressed event from the text field.
     *
     * @param e the e
     */
    public void keyPressed(KeyEvent e) 
    {
    	//logger.debug("GOT KEYPRESS");
    	if (isKeyPressed == false)
    	{
    		noteEvent(71, 90);
    		isKeyPressed = true;
    	}
    }

    /**
     *  Handle the key-released event from the text field.
     *
     * @param e the e
     */
    public void keyReleased(KeyEvent e) 
    {
    	//logger.debug("GOT KEYRELEASE");
    	noteEvent(71, 0);
    	isKeyPressed = false;
    }

	/**
	 * Creates the playback.
	 *
	 * @param playOnly the play only
	 */
	private void createPlayback(boolean playOnly)
	{
		sBar.playBtn.setButtonImage(new ImageIcon(getClass().getResource("stop.png")).getImage());
		sBar.playBtn.repaint();
		currentSpeed = sBar.tempoSlider.getValue();
		metronome = appMidi.createMetronome(appPrefs, currentSpeed, staffLayer.getMeasuresNumber(), timeNumerator, timeDivision);
		metronome.addMetaEventListener(new MetaEventListener() {
            public void meta(MetaMessage meta) 
            {
            	handleAsyncMIDIevent(meta);
            }
		});
		if (gameNotes2.size() > 0)
		{
			// on double clef, create a single sequence for playback
			Vector<Note> tmpSequence = new Vector<Note>(); 
			tmpSequence.addAll(gameNotes);
			tmpSequence.addAll(gameNotes2);
			playback = appMidi.createPlayback(appPrefs, currentSpeed, tmpSequence, timeDivision, playOnly, timeNumerator);
		}
		else
			playback = appMidi.createPlayback(appPrefs, currentSpeed, gameNotes, timeDivision, playOnly, timeNumerator);

		playback.addMetaEventListener(new MetaEventListener() {
          public void meta(MetaMessage meta) 
          {
          	handleAsyncMIDIevent(meta);
          }
		});
		
		currentNoteIndex = 0;
		currentNote2Index = 0;
		metronome.start();
		playback.start();
	}
	
	/**
	 * Stop game.
	 */
	public void stopGame()
	{
		if(gameType == appPrefs.GAME_STOPPED)
			return;
		appMidi.stopPlayback();
		appMidi.stopMetronome();
		if (gameType == appPrefs.SCORE_GAME_LISTEN)
		{
			notesLayer.highlightNote(currentNoteIndex, 1, false);
			currentNoteIndex = -1;
			notesLayer.highlightNote(currentNote2Index, 2, false);
			currentNote2Index = -1;			
		}
		gameBar.scoreCnt.setText("");
		gameBar.progress.setValue(0);
		sBar.playBtn.setButtonImage(new ImageIcon(getClass().getResource("playback.png")).getImage());
		sBar.playBtn.repaint();
		gameStarted = false;
		gameType = appPrefs.GAME_STOPPED;
	}
		
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == sBar.playBtn)
		{
			if (gameType == appPrefs.SCORE_GAME_LISTEN)
			{
				stopGame();
				return;
			}
			
			if (gameThread != null && gameThread.isAlive() == true)
			{
				/* ************** STOP CURRENT GAME ***************** */
				stopGame();
			}
			else
			{
				/* ************** START NEW GAME ***************** */
				if (isRhythm == true)
				{
					gameType = appPrefs.RHTYHM_GAME_USER;
					this.requestFocus();
				}
				else 
					gameType = appPrefs.SCORE_GAME_USER;
				notesLayer.repaint();
				answersLayer.clearSurface();
				gameBar.precisionCnt.setText("");
				gameBar.scoreCnt.setText("");
				gameBar.progress.setValue(0);
				stats.reset();
				gameThread = new ScoreGameThread();
				gameStarted = true;
				cursorX = cursorY = 0; 
				startTime = 0;
				createPlayback(false);
				stats.setGameSpeed(currentSpeed);
				gameThread.start();
				
			}
		}
		else if (ae.getSource() == sBar.refreshBtn)
		{
			refreshPanel();
			notesLayer.repaint();
		}
		else if (ae.getSource() == sBar.listenBtn)
		{
			gameType = appPrefs.SCORE_GAME_LISTEN;
			createPlayback(true);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) 
	{
		g.setColor(this.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		sBar.setSize(getWidth(), sBarHeight);
		int visibleStaffHeight = getHeight() - sBarHeight - gBarHeight;
		int totalStaffHeight = staffLayer.getStaffHeight() - 20;
		
		layers.setPreferredSize(new Dimension(getWidth(), totalStaffHeight));
		int w = getWidth() - (staffHMargin * 2);
		if (totalStaffHeight > visibleStaffHeight)
		{
			staffLayer.setBounds(staffHMargin, 0, w, totalStaffHeight);
			notesLayer.setBounds(staffHMargin, 0, w, totalStaffHeight);
			answersLayer.setBounds(staffHMargin, 0, w, totalStaffHeight);
		}
		else
		{
			staffLayer.setBounds(staffHMargin, 0, w, visibleStaffHeight);
			notesLayer.setBounds(staffHMargin, 0, w, visibleStaffHeight);			
			answersLayer.setBounds(staffHMargin, 0, w, visibleStaffHeight);
		}
		notesLayer.setStaffWidth(staffLayer.getStaffWidth());
		notesLayer.setNotesPositions();
		scoreScrollPanel.setBounds(0, staffVMargin - 10, getWidth(), visibleStaffHeight + 15);
		scoreScrollPanel.validate();
		gameBar.setBounds(0, getHeight() - gBarHeight, getWidth(), gBarHeight);		

		//logger.debug("--------- REFRESH PANEL ********** w: " + w + ", vH: " + visibleStaffHeight + ", tH: " + totalStaffHeight);
		/*
		rowsDistance = scoreNG.getRowsDistance();
		staffLayer.setRowsDistance(rowsDistance);
		notesLayer.setRowsDistance(rowsDistance);
		notesLayer.setStaffWidth(staffLayer.getStaffWidth());
		createNewSequence();
		*/
	}
	
	/**
	 * The Class ScoreGameThread.
	 */
	private class ScoreGameThread extends Thread 
	{
		
		/** The note distance. */
		int noteDistance = staffLayer.getNotesDistance();
		//int beatsPerRow = (staffLayer.getWidth() - staffLayer.getFirstNoteXPosition()) / noteDistance;
		/** The cursor xlimit. */
		int cursorXlimit = staffLayer.getStaffWidth();
		//int scrollAmount = scoreScrollPanel.getVerticalScrollBar().getMaximum() - scoreScrollPanel.getVerticalScrollBar().getVisibleAmount();
		//int totalPixels = (cursorXlimit - cursorStartX) * (staffLayer.getRowsNumber() - 1);
		//int drawnPixels = 0 - (cursorXlimit - cursorStartX);
		/** The scroll step. */
		int scrollStep = (scoreScrollPanel.getVerticalScrollBar().getMaximum() - scoreScrollPanel.getVerticalScrollBar().getVisibleAmount()) / (staffLayer.getRowsNumber() - 3);

		/**
		 * Instantiates a new score game thread.
		 */
		private ScoreGameThread()
		{
			//logger.debug("beatsPerRow: " + beatsPerRow);
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() 
		{
			while (gameStarted) 
			{
				try
				{
					if (startTime != 0)
					{
						cursorX = cursorStartX + ((int)(System.currentTimeMillis() - startTime) * (noteDistance*timeDivision))/(60000 / currentSpeed);
						answersLayer.drawCursor(cursorX, cursorY, false);

						if (cursorX >= cursorXlimit)
						{
							answersLayer.drawCursor(cursorX, cursorY, true);
							cursorY+=rowsDistance;
							cursorStartX = staffLayer.getFirstNoteXPosition() - 10;
							cursorX = cursorStartX;
							startTime = System.currentTimeMillis();
							if (cursorY > 10 + rowsDistance)
							{
								//logger.debug("Scrollbar min: " + scoreScrollPanel.getVerticalScrollBar().getMinimum() + ", max: "
								//		+ scoreScrollPanel.getVerticalScrollBar().getMaximum());
								//logger.debug("Scrollbar value: " + scoreScrollPanel.getVerticalScrollBar().getValue());
								//logger.debug("Scrollbar amount: " + scoreScrollPanel.getVerticalScrollBar().getVisibleAmount());
								
					        	if (scoreScrollPanel.getVerticalScrollBar().isVisible() == true)
					        	{
					        		//int newPos = (scrollAmount * (drawnPixels + cursorX - cursorStartX)) / totalPixels;
					        		//logger.debug("Scrollbar amount: " + newPos);
					        		int newPos = scoreScrollPanel.getVerticalScrollBar().getValue() + scrollStep;
					        		scoreScrollPanel.getVerticalScrollBar().setValue(newPos);
					        	}
							}
							//drawnPixels += (cursorXlimit - cursorStartX);
							continue;
						}
					}
					sleep(10);
					
				}
				catch (Exception e) {  }
			}
		}
	}
	
}
