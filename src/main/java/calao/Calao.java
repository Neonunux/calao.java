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
import java.awt.MediaTracker;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main Class.
 *
 * @author Neonunux
 * @author Massimo Callegari
 * @email regis.leloup@colombbus.org
 * @homepage https://github.com/Neonunux/calao/wiki
 */
public class Calao extends JFrame implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0x5C03EDA7EL;

	private static final Logger logger = LogManager.getLogger(Calao.class
			.getName());

	/** The prefs. */
	private Preferences prefs;

	/** The bundle. */
	private ResourceBundle bundle;

	/** The Musi sync. */
	Font MusiSync; // font used to render scores

	/** The language. */
	private String language = "en";

	/** The supported languages. */
	private String[] supportedLanguages = { "de", "da", "en", "eo", "es", "it",
			"fi", "ko", "pl", "pt", "hu", "he", "fr", "tr", "ru" };

	// GUI elements
	/** The menu bar. */
	LMenuBar menuBar;

	/** The home panel. */
	private HomePanel homePanel;

	/** The inline panel. */
	private InlinePanel inlinePanel = null;

	/** The rhythm panel. */
	private ScorePanel rhythmPanel = null;

	/** The score panel. */
	private ScorePanel scorePanel = null;

	/** The stats panel. */
	private StatsPanel statsPanel = null;

	/** The exs panel. */
	private ExercisesPanel exsPanel = null;

	/** The ear panel. */
	private EarTrainingPanel earPanel = null;

	// MIDI Resources
	/** The midi control. */
	public MidiController midiControl = null;

	/** The midi dev. */
	private MidiDevice midiDev = null;

	// Audio Resources
	/** The audio control. */
	AudioInputController audioControl;

	// MIDI option dialog
	/** The midi options. */
	private MidiOptionsDialog midiOptions;

	/**
	 * The current context
	 * 
	 */

	private int currentContext; // HOMEPANEL, NOTEREADING, RHYTHMREADING,
								// SCOREREADING
	private static int HOMEPANEL = 0;
	private static int NOTEREADING = 1;
	private static int RHYTHMREADING = 2;
	private static int SCOREREADING = 3;
	private static int STATISTICS = 4;
	private static int EXERCISES = 5;
	private static int EARTRAINING = 6;

	/** The transposition. */
	private int transposition = 0;

	/**
	 * Instantiates a new main Calao class.
	 */
	public Calao() {
		// first of all try to change the application look & feel using Nimbus
		try {
			LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
			for (int i = 0; i < info.length; i++) {
				if ("Nimbus".equals(info[i].getName())) {
					UIManager.setLookAndFeel(info[i].getClassName());
					break;
				}
			}
			logger.debug("Nimbus theme will be used");

		} catch (Exception e) {
			// If Nimbus is not available, use default look & feel (metal)
			logger.debug("Default theme will be used");
		}

		this.setIconImage(new ImageIcon(getClass().getResource("icon.png"))
				.getImage());
		prefs = new Preferences();
		language = prefs.getProperty("language");
		// if no language is set yet, try to set the system one
		if (language == "") {
			Locale locale = Locale.getDefault();
			logger.info("Got system language: " + locale.getLanguage());
			language = locale.getLanguage();
			if (isLanguageSupported(language) == false)
				language = "en"; // if not supported, fall back to english
			else {
				prefs.setProperty("language", language);
				prefs.storeProperties();
			}
		}
		if ("he".equals(language) || "hu".equals(language)
				|| "ru".equals(language) || "eo".equals(language)) {
			try {
				bundle = new PropertyResourceBundle(new InputStreamReader(
						getClass().getResourceAsStream(
								"translation.language_" + language
										+ ".properties"), "UTF-8"));
			} catch (IOException e) {
				logger.debug("Cannot load UTF8 language: " + language);
				language = "en";
				bundle = ResourceBundle.getBundle("language", new Locale(
						language));
			}
		} else {
			//logger.error(ResourceBundle.getBundle("translation.language"));
			bundle = ResourceBundle.getBundle("translation.language",
					new Locale(language));
		}

		setTitle("Calao");
		Dimension wSize = new Dimension(800, 600);
		setSize(wSize); // default size is 0,0
		setMinimumSize(wSize);
		setBackground(Color.white);
		setLocationRelativeTo(null); // Center the window on the display
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit when frame
														// closed

		midiControl = new MidiController(prefs);
		int midiError = midiControl.checkError();
		if (midiError == 2) {
			Object[] options = { bundle.getString("_yes"),
					bundle.getString("_no") };
			int ret = JOptionPane
					.showOptionDialog(
							this.getParent(),
							"<html><b>Java Soundbank not found. "
									+ "<br>Without it you will not be able to hear any note.<br><br>Would you like to download it now ?</b></html>",
							"Java error", JOptionPane.YES_NO_OPTION,
							JOptionPane.ERROR_MESSAGE, null, options,
							options[0]);
			if (ret == JOptionPane.YES_OPTION) {
				SoundbankDownloader sbDown = new SoundbankDownloader();
				sbDown.startDownload(); // blocked here until download is
										// finished
				midiControl = new MidiController(prefs); // reload the just
															// downloaded bank
			}
		}

		midiDev = midiControl.openInputDevice();

		if (midiDev != null) {
			Receiver r = new MidiReceiver();
			try {
				Transmitter t = midiDev.getTransmitter();
				t.setReceiver(r);
			} catch (MidiUnavailableException e) {
				logger.debug("Unable to connect the device's Transmitter to the Receiver:");
				logger.debug(e);
				midiDev.close();
			}
		}

		try {
			InputStream fInput = getClass().getResourceAsStream(
					"MusiSyncForCalao.ttf");
			MusiSync = Font.createFont(Font.PLAIN, fInput);
			logger.debug("MusiSync font loaded.");
		} catch (Exception e) {
			logger.debug("Cannot load MusiSync font !!");
			System.exit(0);
		}

		audioControl = new AudioInputController(prefs); // TODO: AUDIO
														// unfinished

		menuBar = new LMenuBar(bundle, prefs);
		setJMenuBar(menuBar);
		menuBar.setVisible(true);
		menuBar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				menuBarActionPerformed(evt.getPropertyName());
			}
		});

		homePanel = new HomePanel(MusiSync, bundle, wSize);
		getContentPane().add(homePanel);

		homePanel.inlineBtn.addActionListener(this);
		homePanel.rhythmBtn.addActionListener(this);
		homePanel.scoreBtn.addActionListener(this);
		homePanel.statsBtn.addActionListener(this);
		homePanel.lessonsBtn.addActionListener(this);
		homePanel.earTrainBtn.addActionListener(this);

		currentContext = HOMEPANEL;
		transposition = Integer.parseInt(prefs.getProperty("transposition"));

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				logger.debug("Calao has been resized !");
				/*
				 * if (prefs.globalExerciseMode == false) { if (rhythmPanel !=
				 * null && currentContext == RHYTHMREADING)
				 * rhythmPanel.createNewSequence(); else if (scorePanel != null
				 * && currentContext == SCOREREADING)
				 * scorePanel.createNewSequence(); }
				 */
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				logger.debug("Calao is going to be closed !");
				if (midiDev != null)
					midiDev.close();
				midiDev = null;
				if (midiControl != null)
					midiControl.close();
				midiControl = null;
				System.exit(0);
			}
		});

		setVisible(true);
		
		CalaoDragDropListener myDragDropListener = new CalaoDragDropListener();
	    // Connect the label with a drag and drop listener
	    new DropTarget(homePanel, myDragDropListener);
//	    JComponent cp = (JComponent) getContentPane();
//	    cp.setTransferHandler(new MyFileTransferHandler()); // see below
	}

	/*
	 * ACTION LISTENER - listens to all buttons that require a switch of context
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		logger.debug("Event received !! (cmd:" + ae.getActionCommand() + ")");
		Dimension wSize = new Dimension(getWidth(), getHeight());
		if (ae.getSource() == homePanel.inlineBtn) {
			homePanel.setVisible(false);
			prefs.setExerciseMode(false, null);
			inlinePanel = new InlinePanel(MusiSync, bundle, prefs, midiControl,
					wSize);
			getContentPane().add(inlinePanel);
			inlinePanel.setVisible(true);
			currentContext = NOTEREADING;
			inlinePanel.sBar.homeBtn.addActionListener(this);
		} else if (ae.getSource() == homePanel.rhythmBtn) {
			homePanel.setVisible(false);
			prefs.setExerciseMode(false, null);
			rhythmPanel = new ScorePanel(MusiSync, bundle, prefs, midiControl,
					wSize, true);
			getContentPane().add(rhythmPanel);
			rhythmPanel.setVisible(true);
			currentContext = RHYTHMREADING;
			rhythmPanel.sBar.homeBtn.addActionListener(this);
		} else if (ae.getSource() == homePanel.scoreBtn) {
			homePanel.setVisible(false);
			prefs.setExerciseMode(false, null);
			scorePanel = new ScorePanel(MusiSync, bundle, prefs, midiControl,
					wSize, false);
			getContentPane().add(scorePanel);
			scorePanel.setVisible(true);
			currentContext = SCOREREADING;
			scorePanel.sBar.homeBtn.addActionListener(this);
		} else if (ae.getSource() == homePanel.statsBtn) {
			homePanel.setVisible(false);
			prefs.setExerciseMode(false, null);
			statsPanel = new StatsPanel(MusiSync, bundle, prefs, wSize);
			getContentPane().add(statsPanel);
			statsPanel.setVisible(true);
			currentContext = STATISTICS;
			statsPanel.homeBtn.addActionListener(this);
		} else if (ae.getSource() == homePanel.lessonsBtn) {
			homePanel.setVisible(false);
			prefs.setExerciseMode(false, null);
			exsPanel = new ExercisesPanel(MusiSync, bundle, prefs, midiControl,
					wSize);
			getContentPane().add(exsPanel);
			exsPanel.setVisible(true);
			currentContext = EXERCISES;
			exsPanel.homeBtn.addActionListener(this);
			exsPanel.exLineBtn.addActionListener(this);
			exsPanel.exRhythmBtn.addActionListener(this);
			exsPanel.exScoreBtn.addActionListener(this);
		} else if (ae.getSource() == homePanel.earTrainBtn) {
			/*
			 * JOptionPane.showMessageDialog(this.getParent(),
			 * "<html><b>Coming soon !</b></html>",
			 * bundle.getString("_menuEarTraining"),
			 * JOptionPane.INFORMATION_MESSAGE);
			 */
			homePanel.setVisible(false);
			prefs.setExerciseMode(false, null);
			earPanel = new EarTrainingPanel(MusiSync, bundle, prefs,
					midiControl, wSize);
			getContentPane().add(earPanel);
			earPanel.setVisible(true);
			currentContext = EARTRAINING;
			earPanel.sBar.homeBtn.addActionListener(this);
		}

		// *************************** GAMES PANEL HOME BUTTON
		else if (inlinePanel != null
				&& ae.getSource() == inlinePanel.sBar.homeBtn) {
			inlinePanel.stopGame();
			this.remove(inlinePanel);
			inlinePanel = null;
			getContentPane().add(homePanel);
			homePanel.setVisible(true);
			currentContext = HOMEPANEL;
		} else if (rhythmPanel != null
				&& ae.getSource() == rhythmPanel.sBar.homeBtn) {
			rhythmPanel.stopGame();
			this.remove(rhythmPanel);
			rhythmPanel = null;
			getContentPane().add(homePanel);
			homePanel.setVisible(true);
			currentContext = HOMEPANEL;
		} else if (scorePanel != null
				&& ae.getSource() == scorePanel.sBar.homeBtn) {
			scorePanel.stopGame();
			this.remove(scorePanel);
			scorePanel = null;
			getContentPane().add(homePanel);
			homePanel.setVisible(true);
			currentContext = HOMEPANEL;
		} else if (earPanel != null && ae.getSource() == earPanel.sBar.homeBtn) {
			earPanel.stopGame();
			this.remove(earPanel);
			earPanel = null;
			getContentPane().add(homePanel);
			homePanel.setVisible(true);
			currentContext = HOMEPANEL;
		}
		// *************************** STATISTICS PANEL BUTTON ***************************
		else if (statsPanel != null && ae.getSource() == statsPanel.homeBtn) {
			this.remove(statsPanel);
			statsPanel = null;
			getContentPane().add(homePanel);
			homePanel.setVisible(true);
			currentContext = HOMEPANEL;
		}
		// ***************************** EXERCISE PANEL BUTTONS ***************************
		else if (exsPanel != null && ae.getSource() == exsPanel.homeBtn) {
			exsPanel.stopPlayback();
			this.remove(exsPanel);
			exsPanel = null;
			getContentPane().add(homePanel);
			homePanel.setVisible(true);
			currentContext = HOMEPANEL;
		} else if (exsPanel != null && ae.getSource() == exsPanel.exLineBtn) {
			exsPanel.stopPlayback();
			prefs.setExerciseMode(true, exsPanel.getSelectedExercise());
			this.remove(exsPanel);
			exsPanel = null;
			inlinePanel = new InlinePanel(MusiSync, bundle, prefs, midiControl,
					wSize);
			getContentPane().add(inlinePanel);
			inlinePanel.setVisible(true);
			currentContext = NOTEREADING;
			inlinePanel.sBar.homeBtn.addActionListener(this);
			this.pack();
		} else if (exsPanel != null && ae.getSource() == exsPanel.exRhythmBtn) {
			exsPanel.stopPlayback();
			prefs.setExerciseMode(true, exsPanel.getSelectedExercise());
			this.remove(exsPanel);
			exsPanel = null;
			rhythmPanel = new ScorePanel(MusiSync, bundle, prefs, midiControl,
					wSize, true);
			getContentPane().add(rhythmPanel);
			rhythmPanel.setVisible(true);
			currentContext = RHYTHMREADING;
			rhythmPanel.sBar.homeBtn.addActionListener(this);
			this.pack();
		} else if (exsPanel != null && ae.getSource() == exsPanel.exScoreBtn) {
			exsPanel.stopPlayback();
			prefs.setExerciseMode(true, exsPanel.getSelectedExercise());
			this.remove(exsPanel);
			exsPanel = null;
			scorePanel = new ScorePanel(MusiSync, bundle, prefs, midiControl,
					wSize, false);
			getContentPane().add(scorePanel);
			scorePanel.setVisible(true);
			currentContext = SCOREREADING;
			scorePanel.sBar.homeBtn.addActionListener(this);
			this.pack();
		}
	}

	/**
	 * Menu bar action performed.
	 *
	 * @param s
	 *            the s
	 */
	public void menuBarActionPerformed(String s) {
		logger.debug(s);
		String currLanguage = language;
		if (s == "langEN")
			language = "en";
		else if (s == "langDE")
			language = "de";
		else if (s == "langES")
			language = "es";
		else if (s == "langIT")
			language = "it";
		else if (s == "langDA")
			language = "da";
		else if (s == "langTR")
			language = "tr";
		else if (s == "langFI")
			language = "fi";
		else if (s == "langKO")
			language = "ko";
		else if (s == "langEO")
			language = "eo";
		else if (s == "langPL")
			language = "pl";
		else if (s == "langPT")
			language = "pt";
		else if (s == "langHU")
			language = "hu";
		else if (s == "langHE")
			language = "he";
		else if (s == "langRU")
			language = "ru";
		else if (s == "langFR")
			language = "fr";
		else if (s == "midiOptions") {
			midiOptions = new MidiOptionsDialog(bundle, prefs, midiControl,
					audioControl);
			midiOptions.setVisible(true);
			midiOptions.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName() == "newMidiDevice") {
						logger.debug("Going to reconfigure MIDI system...");
						if (midiControl != null)
							midiControl.close();
						midiControl = null;
						if (midiDev != null)
							midiDev.close();
						midiDev = null;
						midiControl = new MidiController(prefs);
						if (midiControl.checkError() != 0) {
							JOptionPane.showMessageDialog(null, "<html><b>"
									+ bundle.getString("_alertRestart")
									+ "</b></html>",
									bundle.getString("_menuMidi"),
									JOptionPane.WARNING_MESSAGE);
							return;
						}
						midiDev = midiControl.openInputDevice();

						if (midiDev != null) {
							Receiver r = new MidiReceiver();
							try {
								Transmitter t = midiDev.getTransmitter();
								t.setReceiver(r);
							} catch (MidiUnavailableException e) {
								logger.error("Unable to connect the device's Transmitter to the Receiver:");
								logger.error(e);
								e.printStackTrace();
								midiDev.close();
							}
						}
						// midiOptions.reloadDriversList(midiControl.getFluidDrivers());
						midiOptions.reloadDevicesList(midiControl
								.getFluidDevices());
						midiOptions.reloadInstruments(midiControl
								.getInstruments());
					} else if (evt.getPropertyName() == "newMidiInstrument") {
						logger.debug("Set new MIDI instrument...");
						midiControl.setNewInstrument();
					} else if (evt.getPropertyName() == "newTranspose") {
						// BUG : suposes that "transposition" key is in
						// file.properties
						logger.debug("Set new transposition...");
						transposition = Integer.parseInt(prefs
								.getProperty("transposition"));
					}
				}
			});
		} else if (s == "exitProgram") {
			// dispose(); // why this doesn't work ??
			System.exit(0);
		}

		if (language != currLanguage)
			updateLanguage();
	}

	/**
	 * Checks if is language supported.
	 *
	 * @param lang
	 *            the lang
	 * @return true, if is language supported
	 */
	private boolean isLanguageSupported(String lang) {
		for (int i = 0; i < supportedLanguages.length; i++)
			if (lang.equals(supportedLanguages[i]))
				return true;
		return false;
	}

	/**
	 * Update language.
	 */
	private void updateLanguage() {
		if ("iw".equals(language) || "he".equals(language)
				|| "hu".equals(language) || "ru".equals(language)
				|| "eo".equals(language)) {
			try {
				bundle = new PropertyResourceBundle(new InputStreamReader(
						getClass().getResourceAsStream(
								"translation.language_" + language
										+ ".properties"), "UTF-8"));
			} catch (IOException e) {
				logger.error("Cannot load UTF8 language: " + language);
				language = "en";
				bundle = ResourceBundle.getBundle("translation.language",
						new Locale(language));
			}
		} else
			bundle = ResourceBundle.getBundle("translation.language",
					new Locale(language));

		prefs.setProperty("language", language);
		prefs.storeProperties();

		// First update menu bar, which is always visible
		menuBar.updateLanguage(bundle);

		homePanel.inlineBtn.setResBundle(bundle);
		homePanel.scoreBtn.setResBundle(bundle);
		homePanel.rhythmBtn.setResBundle(bundle);
		homePanel.statsBtn.setResBundle(bundle);
		homePanel.lessonsBtn.setResBundle(bundle);
		homePanel.earTrainBtn.setResBundle(bundle);

		if (currentContext == HOMEPANEL) {
			homePanel.repaint();
		}
		if (inlinePanel != null && currentContext == NOTEREADING) {
			inlinePanel.updateLanguage(bundle);
		} else if (rhythmPanel != null && currentContext == RHYTHMREADING) {
			rhythmPanel.updateLanguage(bundle);
		} else if (scorePanel != null && currentContext == SCOREREADING) {
			scorePanel.updateLanguage(bundle);
		} else if (statsPanel != null && currentContext == STATISTICS) {
			statsPanel.updateLanguage(bundle);
		} else if (exsPanel != null && currentContext == EXERCISES) {
			exsPanel.updateLanguage(bundle);
		} else if (earPanel != null && currentContext == EARTRAINING) {
			earPanel.updateLanguage(bundle);
		}
	}

	/**
	 * The Class MidiReceiver.
	 */
	private class MidiReceiver implements Receiver {

		/**
		 * Instantiates a new midi receiver.
		 */
		public MidiReceiver() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.sound.midi.Receiver#send(javax.sound.midi.MidiMessage,
		 * long)
		 */
		public void send(MidiMessage event, long time) {
			if (event instanceof ShortMessage) {
				switch (event.getStatus() & 0xf0) {
				case 0x90:
					int pitch = ((ShortMessage) event).getData1();
					int vel = ((ShortMessage) event).getData2();
					pitch += transposition;
					if (vel == 0)
						logger.debug("   Key released - Pitch: " + pitch);
					else
						logger.debug("   Key pressed - Pitch: " + pitch
								+ " Velocity: " + vel);

					if (inlinePanel != null && inlinePanel.isVisible() == true)
						inlinePanel.noteEvent(pitch, vel, false);
					else if (rhythmPanel != null
							&& rhythmPanel.isVisible() == true)
						rhythmPanel.noteEvent(pitch, vel);
					else if (scorePanel != null
							&& scorePanel.isVisible() == true)
						scorePanel.noteEvent(pitch, vel);
					else if (earPanel != null && earPanel.isVisible() == true)
						earPanel.noteEvent(pitch, vel, false);
					break;
				/*
				 * case 0x80: logger.debug("   Note Off  Pitch: "+((ShortMessage
				 * )event).getData1()+
				 * " Velocity: "+((ShortMessage)event).getData2()); break; case
				 * 0xb0: if (((ShortMessage)event).getData1() < 120) {
				 * logger.debug("   Controller No.: " +
				 * ((ShortMessage)event).getData1()+
				 * " Value: "+((ShortMessage)event).getData2()); } else {
				 * logger.debug("   ChannelMode Message No.: "+
				 * ((ShortMessage)event).getData1() + " Value: " +
				 * ((ShortMessage)event).getData2()); } break; case 0xe0:
				 * System.
				 * out.println("   Pitch lsb: "+((ShortMessage)event).getData1
				 * ()+ " msb: "+((ShortMessage)event).getData2()); break; case
				 * 0xc0: logger.debug("   Program Change No: " +
				 * ((ShortMessage)event).getData1()+ " Just for Test: " +
				 * ((ShortMessage)event).getData2()); break; case 0xd0:
				 * logger.debug("   Channel Aftertouch Pressure: " +
				 * ((ShortMessage)event).getData1() + " Just for Test: " +
				 * ((ShortMessage)event).getData2()); break;
				 */
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.sound.midi.Receiver#close()
		 */
		public void close() {
		}
	}

	/**
	 * Displays splash.png during the loading
	 */
	public void displaySplashScreen() {
		ImageIcon splashScreenImage = new ImageIcon();
		try {
			splashScreenImage.setImage(ImageIO.read(getClass().getResource(
					"splash.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// while the image is not on screen
		while (splashScreenImage.getImageLoadStatus() == MediaTracker.LOADING) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.printf("splash screen loading interrupted", e); //$NON-NLS-1$
			}
		}
		// should normally be run in the EDT, but launched at once in order to
		// display the screen as soon as possible
		new SplashScreen(splashScreenImage, 2000);
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		new Calao().displaySplashScreen();

		logger.trace("Exiting Calao");
	}
}
