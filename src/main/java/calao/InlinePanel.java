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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class InlinePanel.
 *
 * @author Neonunux
 */
public class InlinePanel extends JPanel implements ActionListener, PropertyChangeListener {

	private static final Logger logger = LogManager.getLogger(InlinePanel.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The app font. */
	private Font appFont;

	/** The app bundle. */
	private ResourceBundle appBundle;

	private Preferences appPrefs;

	/** The app midi. */
	private MidiController appMidi;

	/** The inline accidentals. */
	private Accidentals inlineAccidentals;

	/** The inline ng. */
	private NoteGenerator inlineNG;

	/** The stats. */
	private Statistics stats;

	/** The game thread. */
	private InlineGameThread gameThread = null;

	/** The game started. */
	private boolean gameStarted = false;

	// GUI elements
	/** The s bar. */
	public SmartBar sBar;

	/** The s bar height. */
	private int sBarHeight = 130;

	/** The layers. */
	private JLayeredPane layers;

	/** The inline staff. */
	private Staff inlineStaff;

	/** The notes layer. */
	private NotesPanel notesLayer;

	/** The piano. */
	private Piano piano;

	/** The game bar. */
	private GameBar gameBar;

	/** The g bar height. */
	private int gBarHeight = 40;

	/** The piano height. */
	private int pianoHeight = 80;

	/** The staff h margin. */
	private int staffHMargin = 180;

	/** The staff v margin. */
	private int staffVMargin = 150;

	/** The staff height. */
	private int staffHeight = 260;

	/** The rows distance. */
	private int rowsDistance = 90; // distance in pixel between staff rows

	/** The game notes. */
	Vector<Note> gameNotes = new Vector<Note>();

	/** The user notes. */
	Vector<Integer> userNotes = new Vector<Integer>();

	/** The game type. */
	private int gameType = -1;

	/** The game sub type. */
	private int gameSubType = -1;

	/** The game interval. */
	private int gameInterval = -1;

	/** The progress step. */
	private int progressStep = 1;

	/** The current speed. */
	private int currentSpeed = 120;

	/** The note x start pos. */
	private int noteXStartPos = 0; // X position of the notes appearing on the
									// staff

	/** The exercise mode. */
	private boolean exerciseMode = false;

	/** The curr ex. */
	private Exercise currEx = null;

	private TimerPanel timerPanel;
	Voices voices;

	private int pieSize = 70;

	/**
	 * Instantiates a new inline panel.
	 *
	 * @param f
	 *            the f
	 * @param b
	 *            the b
	 * @param p
	 *            the p
	 * @param mc
	 *            the mc
	 * @param d
	 *            the d
	 */
	public InlinePanel(Font f, ResourceBundle b, Preferences p, MidiController mc, Dimension d) {
		appFont = f;
		appBundle = b;
		appPrefs = p;
		appMidi = mc;

		voices = new Voices(f, b, p);

		String color = appPrefs.getProperty("colors.background");
		setBackground(Color.decode(color));
		setSize(d);
		setLayout(null);
		if (appPrefs.globalExerciseMode == true) {
			exerciseMode = true;
			currEx = appPrefs.currentExercise;
			inlineAccidentals = new Accidentals(currEx.acc.getType(), currEx.acc.getNumber(), appPrefs);
		} else {
			inlineAccidentals = new Accidentals("", 0, appPrefs);
		}

		inlineNG = new NoteGenerator(appPrefs, inlineAccidentals, false);
		inlineNG.setVoices(voices);

		stats = new Statistics();
		timerPanel = new TimerPanel(appPrefs);

		gameType = appPrefs.GAME_STOPPED;

		sBar = new SmartBar(new Dimension(d.width, sBarHeight), b, f, p, true, false, false);

		sBar.clefNotesDialog.addPropertyChangeListener(this);
		// sBar.clefNotesDialog.okButton.addActionListener(this);

		sBar.clefNoteBtn.addActionListener(this);
		sBar.playBtn.addActionListener(this);
		if (exerciseMode == true)
			sBar.tempoSlider.setValue(currEx.speed);

		int panelsWidth = d.width - (staffHMargin * 2);

		layers = new JLayeredPane();
		layers.setPreferredSize(new Dimension(panelsWidth, staffHeight));
		layers.setBounds(staffHMargin, staffVMargin, panelsWidth, staffHeight);

		inlineStaff = new Staff(appFont, appBundle, appPrefs, inlineAccidentals, true, true);
		inlineStaff.setPreferredSize(new Dimension(panelsWidth, staffHeight));
		inlineStaff.setBounds(0, 0, panelsWidth, staffHeight);
		inlineStaff.setVoices(voices);
		inlineStaff.setOpaque(true);

		// notesLayer = new NotesPanel(appFont, appPrefs, gameNotes, null,
		// true);
		// notesLayer.setPreferredSize(new Dimension(panelsWidth, staffHeight));
		// notesLayer.setBounds(0, 0, panelsWidth, staffHeight);
		// notesLayer.setOpaque(false);

		layers.add(inlineStaff, new Integer(1));
		// layers.add(notesLayer, new Integer(2));

		int pianoKeysNum = Integer.parseInt(appPrefs.getProperty("keyboardlength"));
		if (pianoKeysNum == -1)
			pianoKeysNum = 73;
		piano = new Piano(pianoKeysNum);
		piano.setPreferredSize(new Dimension(d.width, pianoHeight));
		piano.setBounds(0, staffVMargin + 240, d.width, pianoHeight);

		for (int i = 0; i < pianoKeysNum; i++) {
			piano.keys.get(i).addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					pianoKeyPressed((Key) e.getSource(), true);
				}

				public void mouseReleased(MouseEvent e) {
					pianoKeyPressed((Key) e.getSource(), false);
				}
			});
		}

		gameBar = new GameBar(new Dimension(d.width, gBarHeight), b, f, p, true);
		gameBar.setBounds(0, getHeight() - gBarHeight, getWidth(), gBarHeight);
		gameBar.progress.setValue(20);

		sBar.gameSelector.addActionListener(this);

		add(sBar);
		add(layers);
		add(piano);
		add(gameBar);
		add(timerPanel);
		refreshPanel();

	}

	/**
	 * Refresh panel.
	 */
	public void refreshPanel() {
		this.repaint();
		piano.reset(true);
		inlineStaff.setVoices(inlineNG.getVoices());

		if (exerciseMode == false) {
			inlineNG.update();

			int lowerPitch = inlineNG.getFirstLowPitch();
			int higherPitch = inlineNG.getFirstHighPitch();
			piano.setNewBound(lowerPitch, higherPitch);
			lowerPitch = inlineNG.getSecondLowPitch();
			higherPitch = inlineNG.getSecondHighPitch();
			piano.setNewBound(lowerPitch, higherPitch);

			// notesLayer.setClefs(inlineNG.getVoices());
			rowsDistance = inlineNG.getRowsDistance();
		} else {
			// notesLayer.setClefs(currEx.clefMask);
			if (currEx.randomize == 1)
				inlineNG.setNotesList(currEx.notes, currEx.notes2, true);
			else
				inlineNG.setNotesList(currEx.notes, currEx.notes2, false);
			rowsDistance = inlineNG.getRowsDistanceFromClefs(currEx.clefMask);
			logger.debug("[INLINE] rowsDistance: " + rowsDistance);

		}
		rowsDistance = 160; // TODO remove this debug line
		sBar.tempoContainer.setEnabled(false);
		inlineStaff.setRowsDistance(rowsDistance);
		inlineStaff.repaint();

		// notesLayer.setRowsDistance(rowsDistance);
		// notesLayer.setFirstNoteXPosition(inlineStaff.getFirstNoteXPosition());
		setLearningInfo(false, -1);
	}

	/**
	 * Update language.
	 *
	 * @param bundle
	 *            the bundle
	 */
	public void updateLanguage(ResourceBundle bundle) {
		logger.debug("INLINE - update language");
		appBundle = bundle;
		sBar.updateLanguage(appBundle);
		gameBar.updateLanguage(appBundle);
	}

	/**
	 * Sets the game type.
	 */
	public void setGameType() {
		gameInterval = -1;
		int gameIdx = sBar.gameSelector.getSelectedIndex();
		switch (gameIdx) {
		case 0:
			gameType = appPrefs.INLINE_SINGLE_NOTES;
			break;
		case 1:
			gameType = appPrefs.INLINE_MORE_NOTES;
			break;
		case 2:
			gameType = appPrefs.INLINE_LEARN_NOTES;
			break;
		case 3:
			gameType = appPrefs.INLINE_NORMAL_MODE;
			break;
		}

		int subGameIdx = sBar.gameType.getSelectedIndex();
		switch (subGameIdx) {
		case 0:
			gameSubType = appPrefs.NOTE_NORMAL;
			break;
		case 1:
			gameSubType = appPrefs.NOTE_ACCIDENTALS;
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			// case 9:
			// case 10:
			gameSubType = appPrefs.NOTE_INTERVALS;
			gameInterval = subGameIdx;
			break;
		case 9:
			gameSubType = appPrefs.NOTE_CHORDS;
			break;
		}
		int notesNum = gameBar.notesNumber.getSelectedIndex();
		switch (notesNum) {
		case 0:
			progressStep = 8;
			break; // 10 notes
		case 1:
			progressStep = 4;
			break; // 20 notes
		case 2:
			progressStep = 2;
			break; // 40 notes
		case 3:
			progressStep = 1;
			break; // 80 notes
		}
	}

	/**
	 * Sets the learning info.
	 *
	 * @param enable
	 *            the enable
	 * @param chordType
	 *            the chord type
	 */
	public void setLearningInfo(boolean enable, int chordType) {
		if (gameNotes.size() == 0)
			return;
		int noteIdx = piano.highlightKey(gameNotes.get(0).pitch, enable);
		String noteInfo = "";
		String altInfo = "";
		String chord = "";

		if (gameNotes.size() > 1)
			for (int i = 1; i < gameNotes.size(); i++)
				piano.highlightKey(gameNotes.get(i).pitch, enable);

		if (enable == true) {
			// find out if there are accidentals first
			int altType = 0;
			if (gameSubType == appPrefs.NOTE_ACCIDENTALS) {
				altType = gameNotes.get(0).altType;
				if (altType == 0) // maybe the alteration is on the clef. Check
									// it out
					altType = inlineNG.getAlteration(gameNotes.get(0).pitch);
			} else
				altType = inlineNG.getAlteration(gameNotes.get(0).pitch);

			if (altType == 1) {
				if (noteIdx != 2 && noteIdx != 6 && piano.isSelectedBlack() == true) // E
																						// and
																						// B
																						// are
																						// already
																						// OK
					altInfo = "#";
			} else if (gameNotes.get(0).altType == -1) {
				altInfo = "b";
				noteIdx++;
				if (noteIdx == 7)
					noteIdx = 0;
			}

			if (gameSubType != appPrefs.NOTE_INTERVALS) {
				switch (noteIdx) {
				case 0:
					noteInfo = appBundle.getString("_do");
					break;
				case 1:
					noteInfo = appBundle.getString("_re");
					break;
				case 2:
					noteInfo = appBundle.getString("_mi");
					break;
				case 3:
					noteInfo = appBundle.getString("_fa");
					break;
				case 4:
					noteInfo = appBundle.getString("_sol");
					break;
				case 5:
					noteInfo = appBundle.getString("_la");
					break;
				case 6:
					noteInfo = appBundle.getString("_si");
					break;
				}
				if (gameSubType == appPrefs.NOTE_CHORDS && chordType != -1) {
					chord = " ";
					switch (chordType) {
					case 0:
						chord += appBundle.getString("_major");
						break;
					case 1:
						chord += appBundle.getString("_minor");
						break;
					case 2:
						chord += appBundle.getString("_diminished");
						break;
					case 3:
						chord += appBundle.getString("_augmented");
						break;
					}
				}
			}

			if (gameSubType == appPrefs.NOTE_INTERVALS) {
				chord = " ";
				int intervalType = sBar.gameType.getSelectedIndex();
				String keyStr = "";
				switch (intervalType) {
				case 2:
					keyStr = "_second";
					break;
				case 3:
					keyStr = "_third";
					break;
				case 4:
					keyStr = "_fourth";
					break;
				case 5:
					keyStr = "_fifth";
					break;
				case 6:
					keyStr = "_sixth";
					break;
				case 7:
					keyStr = "_seventh";
					break;
				case 8:
					keyStr = "_octave";
					break;
				}
				switch (chordType) {
				case -2:
					keyStr += "dim";
					break;
				case -1:
					keyStr += "min";
					break;
				case 0:
					if (intervalType == 4 || intervalType == 5 || intervalType == 8)
						keyStr += "per";
					else
						keyStr += "maj";
					break;
				case 1:
					keyStr += "aug";
					break;
				}
				chord += appBundle.getString(keyStr);
			}
		}

		notesLayer.setLearningTips(noteInfo + altInfo + chord, enable);
	}

	/**
	 * Start game.
	 */
	public void startGame() {
		currentSpeed = sBar.tempoSlider.getValue();
		piano.reset(false);
		gameNotes.clear();
		gameBar.precisionCnt.setText("");
		gameBar.scoreCnt.setText("");
		gameBar.progress.setValue(20);
		setGameType();
		noteXStartPos = inlineStaff.getFirstNoteXPosition();
		// notesLayer.setFirstNoteXPosition(noteXStartPos);
		// notesLayer.setStaffWidth(inlineStaff.getStaffWidth());
		stats.reset();
		stats.setGameSpeed(currentSpeed);
		gameThread = new InlineGameThread();
		gameStarted = true;
		gameThread.start();
		sBar.playBtn.setButtonImage(new ImageIcon(getClass().getResource("stop.png")).getImage());
		sBar.playBtn.repaint();
		timerPanel.start();
	}

	/**
	 * Stop game.
	 */
	public void stopGame() {
		if (gameType == appPrefs.GAME_STOPPED)
			return;

		if (gameThread != null && gameThread.isAlive() == true) {
			/* ************** STOP CURRENT GAME ***************** */
			gameStarted = false;
			sBar.playBtn.setButtonImage(new ImageIcon(getClass().getResource("playback.png")).getImage());
			for (int i = 0; i < gameNotes.size(); i++)
				appMidi.stopNote(gameNotes.get(i).pitch, 0);
			gameNotes.clear();
			gameType = appPrefs.GAME_STOPPED;
		}
		timerPanel.stop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == sBar.playBtn) {
			if (gameThread != null && gameThread.isAlive() == true) {
				/* ************** STOP CURRENT GAME ***************** */
				stopGame();
				refreshPanel();
			} else {
				/* ************** START NEW GAME ***************** */
				startGame();
			}
		} else if (ae.getSource() == sBar.gameSelector) {
			int gameIdx = sBar.gameSelector.getSelectedIndex();
			stopGame();
			if (gameIdx == 2) {
				gameBar.notesNumber.setEnabled(false);
				gameBar.progress.setEnabled(false);
			} else {
				gameBar.notesNumber.setEnabled(true);
				gameBar.progress.setEnabled(true);
			}
			if (gameIdx == 1) {
				staffHMargin = 80;
				refreshPanel();
			} else {
				staffHMargin = 180;
				refreshPanel();
			}
			if (gameIdx == 3) {
				// sBar.tempoContainer.
				logger.debug("temps : " + System.currentTimeMillis());
				// sBar.setTraining(true);
			}
		}
	}

	/**
	 * Piano key pressed.
	 *
	 * @param k
	 *            the k
	 * @param pressed
	 *            the pressed
	 */
	private void pianoKeyPressed(Key k, boolean pressed) {
		logger.debug("[pianoKeyPressed] pitch = " + k.pitch);
		if (pressed) {
			if (gameType == appPrefs.GAME_STOPPED)
				appMidi.playNote(k.pitch, 90);
			noteEvent(k.pitch, 90, true);
		} else {
			appMidi.stopNote(k.pitch, 0);
			noteEvent(k.pitch, 0, true);
		}
	}

	/**
	 * Note event.
	 *
	 * @param pitch
	 *            the pitch
	 * @param velocity
	 *            the velocity
	 * @param fromPiano
	 *            the from piano
	 */
	public void noteEvent(int pitch, int velocity, boolean fromPiano) {
		if (velocity == 0) {
			appMidi.stopNote(pitch, 0);
			piano.keyPressed(pitch, false);
			if (fromPiano == true && gameSubType != appPrefs.NOTE_CHORDS && gameSubType != appPrefs.NOTE_INTERVALS
					&& userNotes.size() != 0) {
				int idx = userNotes.indexOf(pitch);
				if (idx != -1)
					userNotes.removeElementAt(idx);
			} else if (fromPiano == false && userNotes.size() != 0) {
				int idx = userNotes.indexOf(pitch);
				if (idx != -1)
					userNotes.removeElementAt(idx);
			}
			if (pitch == 60 && gameType == appPrefs.GAME_STOPPED)
				startGame();
		} else {
			if (gameType != appPrefs.GAME_STOPPED) {
				userNotes.add(pitch);
				boolean match = checkGameStatus(gameNotes, userNotes);
				if (match == true) {
					updateGameStats(1);
					if (gameType != appPrefs.INLINE_MORE_NOTES) {
						for (int i = 0; i < gameNotes.size(); i++) {
							appMidi.stopNote(gameNotes.get(i).pitch, 0);
							if (gameType == appPrefs.INLINE_LEARN_NOTES)
								setLearningInfo(false, -1);
						}
						gameNotes.clear();
						userNotes.clear();
					} else
						gameNotes.remove(0);

					gameThread.needNewNote = true;
				} else {
					appMidi.playNote(pitch, 90);
					if ((gameSubType == appPrefs.NOTE_CHORDS && userNotes.size() == 3)
							|| gameSubType != appPrefs.NOTE_CHORDS)
						updateGameStats(0);
					piano.keyPressed(pitch, true);
				}
			} else {
				appMidi.playNote(pitch, 90);
				piano.keyPressed(pitch, true);
			}
		}
	}

	/**
	 * Check game notes against user notes
	 *
	 * @param game
	 *            the game
	 * @param user
	 *            the user
	 * @return true, if successful
	 */
	public boolean checkGameStatus(Vector<Note> game, Vector<Integer> user) {
		int matchCount = 0;
		int checkSize = 1;

		/*
		 * // enable to debug logger.debug("[checkGameStatus] "); for (int i =
		 * 0; i < game.size(); i++) logger.debug(game.get(i).pitch + " ");
		 * logger.debug("  "); logger.debug(user); logger.debug("");
		 */
		if (gameType != appPrefs.INLINE_MORE_NOTES) {
			if (game.size() != user.size())
				return false;
			checkSize = game.size();
		}

		for (int i = 0; i < checkSize; i++) {
			for (int j = 0; j < user.size(); j++) {
				if (game.get(i).pitch == user.get(j)) {
					matchCount++;
					break;
				}
			}
		}
		if (matchCount == checkSize)
			return true;

		return false;
	}

	/**
	 * Update game stats.
	 *
	 * @param answType
	 *            the answ type
	 */
	private void updateGameStats(int answType) {
		if (gameNotes.size() == 0) {
			return;
		}
		int score = 0;
		if (gameType != appPrefs.INLINE_LEARN_NOTES) {
			int xdelta = gameNotes.get(0).xpos - noteXStartPos;
			// find linear score based on note position
			score = (xdelta * 100) / (getWidth() - (staffHMargin * 2)); 
			score = 100 - score; // invert it to scale from 0 to 100
			score *= (currentSpeed / 40); // multiply by speed factor
		}

		if (answType == 0) {
			score = score - (score * 2); // turn to negative
		}

		stats.notePlayed(answType, score);
		gameBar.precisionCnt.setText(Integer.toString(stats.getAveragePrecision()) + "%");
		gameBar.scoreCnt.setText(Integer.toString(stats.getTotalScore()));
		if (gameType != appPrefs.INLINE_LEARN_NOTES) {
			if (answType == 1) {
				gameBar.progress.setValue(gameBar.progress.getValue() + progressStep);
			} else {
				gameBar.progress.setValue(gameBar.progress.getValue() - 4);
			}
			if (gameBar.progress.getValue() == 100) {
				gameFinished(true);
			} else if (gameBar.progress.getValue() == 0) {
				gameFinished(false);
			}
		}
	}

	/**
	 * Game finished.
	 *
	 * @param win
	 *            the win
	 */
	private void gameFinished(boolean win) {
		gameStarted = false;
		sBar.playBtn.setButtonImage(new ImageIcon(getClass().getResource("playback.png")).getImage());
		refreshPanel();
		for (int i = 0; i < gameNotes.size(); i++) {
			appMidi.stopNote(gameNotes.get(i).pitch, 0);
			// if (gameType == appPrefs.INLINE_LEARN_NOTES)
			// setLearningInfo(gameNotes.get(i).pitch, false);
		}
		gameNotes.clear();
		gameType = appPrefs.GAME_STOPPED;

		String title = "";
		int type = 0;

		if (win == true) {
			title = appBundle.getString("_congratulations");
			type = JOptionPane.INFORMATION_MESSAGE;
		} else {
			title = appBundle.getString("_sorry");
			type = JOptionPane.ERROR_MESSAGE;
		}

		JOptionPane
				.showMessageDialog(
						this.getParent(), "  " + stats.getCorrectNumber() + " " + appBundle.getString("_correct")
								+ " / " + stats.getWrongNumber() + " " + appBundle.getString("_wrong") + "  ",
						title, type);

		if (Integer.parseInt(appPrefs.getProperty("saveStats")) == 1) {
			stats.storeData(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		sBar.setSize(getWidth(), sBarHeight);
		layers.setBounds(staffHMargin, staffVMargin, getWidth() - (staffHMargin * 2), staffHeight);
		inlineStaff.setBounds(0, 0, getWidth() - (staffHMargin * 2), staffHeight);
		inlineStaff.repaint();
		logger.debug("[InlinePanel.paintComponent] repainted");
		// notesLayer
		// .setBounds(0, 0, getWidth() - (staffHMargin * 2), staffHeight);
		piano.setBounds(0, staffVMargin + staffHeight, getWidth(), pianoHeight);
		gameBar.setBounds(0, getHeight() - gBarHeight, getWidth(), gBarHeight);

		timerPanel.setBounds(100, 225, pieSize, pieSize);
	}

	/**
	 * The Class InlineGameThread.
	 */
	private class InlineGameThread extends Thread {

		/** The need new note. */
		boolean needNewNote = true;

		/** The note xincrement. */
		int noteXincrement = (currentSpeed < 121) ? 1 : 2; // above 120 the
															// increment is of 2
															// pixels
		/** The sleep val. */
		int sleepVal = 0;

		/** The margin x. */
		int marginX = inlineStaff.getStaffWidth();

		/**
		 * Instantiates a new inline game thread.
		 */
		private InlineGameThread() {
			if (currentSpeed <= 120) {
				sleepVal = ((120 - currentSpeed) * 10 / 80);
			} else {
				sleepVal = ((200 - currentSpeed) * 10 / 80);
			}
			if (gameType == appPrefs.INLINE_MORE_NOTES) {
				// change sign here
				noteXincrement = noteXincrement - (noteXincrement * 2);
				noteXStartPos = marginX;
				marginX = inlineStaff.getFirstNoteXPosition();
				sleepVal *= 2; // slow down baby !
			}
			logger.debug("Thread increment: " + noteXincrement + ", sleep: " + sleepVal);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			while (gameStarted) {
				try {
					// logger.debug("Thread is running");
					if (needNewNote == true) {
						if (gameType != appPrefs.INLINE_MORE_NOTES) {
							sleep(100);
						}
						needNewNote = false;
						if (gameSubType == appPrefs.NOTE_CHORDS) {
							int chordType = inlineNG.getRandomChordOrInterval(gameNotes, noteXStartPos, true, -1);
							if (gameType == appPrefs.INLINE_LEARN_NOTES) {
								setLearningInfo(true, chordType);
							}
							if (gameType != appPrefs.INLINE_MORE_NOTES) {
								for (int j = 0; j < gameNotes.size(); j++) {
									appMidi.playNote(gameNotes.get(j).pitch, 90);
								}
							}
						} else if (gameSubType == appPrefs.NOTE_INTERVALS) {
							int intervalType = inlineNG.getRandomChordOrInterval(gameNotes, noteXStartPos, false,
									gameInterval);
							if (gameType == appPrefs.INLINE_LEARN_NOTES) {
								setLearningInfo(true, intervalType);
							}
							if (gameType != appPrefs.INLINE_MORE_NOTES) {
								for (int j = 0; j < gameNotes.size(); j++) {
									appMidi.playNote(gameNotes.get(j).pitch, 90);
								}
							}
						} else {
							Note newNote;
							if (gameSubType == appPrefs.NOTE_ACCIDENTALS) {
								newNote = inlineNG.getRandomNote(0, true, -1);
							} else {
								newNote = inlineNG.getRandomNote(0, false, -1);
							}
							newNote.duration = 0; // set duration to 0 not to
													// mess up X position
							newNote.xpos = noteXStartPos;
							gameNotes.add(newNote);
							if (gameType == appPrefs.INLINE_LEARN_NOTES) {
								setLearningInfo(true, -1);
							}
							logger.trace("Got note with pitch: " + newNote.pitch + " (level:" + newNote.level + ")");
							if (gameType != appPrefs.INLINE_MORE_NOTES) {
								appMidi.playNote(newNote.pitch, 90);
							}
						}
						notesLayer.setNotesPositions();
					} else {
						for (int i = 0; i < gameNotes.size(); i++) {
							if (gameType != appPrefs.INLINE_NORMAL_MODE) {
								gameNotes.get(i).xpos += noteXincrement;
							}

							if ((gameType != appPrefs.INLINE_MORE_NOTES && gameNotes.get(i).xpos > marginX)
									|| (gameType == appPrefs.INLINE_MORE_NOTES && i == 0
											&& gameNotes.get(i).xpos < marginX)) {
								if (gameType != appPrefs.INLINE_MORE_NOTES) {
									appMidi.stopNote(gameNotes.get(i).pitch, 0);
								}
								updateGameStats(0);
								if (gameType == appPrefs.INLINE_LEARN_NOTES) {
									setLearningInfo(false, -1);
								}
								if (gameType == appPrefs.INLINE_NORMAL_MODE) {
									setLearningInfo(false, -1);
								}
								gameNotes.removeElementAt(i);
							}
						}
						if (gameNotes.size() == 0) {
							needNewNote = true;
						}

						if (gameType == appPrefs.INLINE_MORE_NOTES
								&& gameNotes.lastElement().xpos < noteXStartPos - 50) {
							needNewNote = true;
						}
					}
					// sleep(260 - currentSpeed);
					notesLayer.repaint();
					inlineStaff.setVoices(voices);
					inlineStaff.repaint();
					logger.debug("[InlinePanelThread] repainted");
					sleep(10 + sleepVal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName() == "updateParameters") {
			logger.debug("[InlinePanelProperty] repainted");
			refreshPanel();
		}
	}

}
