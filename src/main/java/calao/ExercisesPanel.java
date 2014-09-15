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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequencer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class ExercisesPanel.
 *
 * @author Neonunux
 */
public class ExercisesPanel extends JPanel implements TreeSelectionListener, ActionListener, PropertyChangeListener
{
	
	
	private static final Logger logger =  LogManager.getLogger(ExercisesPanel.class.getName());
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1142716145008143198L;
	
	/** The app font. */
	Font appFont;
	
	
	Preferences appPrefs;
	
	/** The app bundle. */
	private ResourceBundle appBundle;
	
	/** The app midi. */
	private MidiController appMidi;
	
	/** The left panel. */
	private JPanel leftPanel;
	
	/** The top bar. */
	public RoundPanel topBar;
	
	/** The home btn. */
	public RoundedButton homeBtn;
	
	/** The new exercise btn. */
	public RoundedButton newExerciseBtn;
	
	/** The edit exercise btn. */
	public RoundedButton editExerciseBtn;
	
	/** The listen btn. */
	public RoundedButton listenBtn;
	
	/** The tree scroll panel. */
	private JScrollPane treeScrollPanel = null;
	
	/** The exercises list. */
	private JTree exercisesList;
	
	/** The exercise title. */
	private JLabel exerciseTitle;
	
	/** The exercise scroll panel. */
	private JScrollPane exerciseScrollPanel;
	
	/** The layers. */
	private JLayeredPane layers;
	
	/** The score staff. */
	private Staff scoreStaff;
	
	/** The notes layer. */
	private NotesPanel notesLayer;
	
	/** The ex score btn. */
	public RoundedButton exLineBtn, exRhythmBtn, exScoreBtn;
	
	/** The new exercise. */
	private Exercise newExercise;
	
	/** The selected exercise. */
	private Exercise selectedExercise;
	
	/** The exercise type dialog. */
	private ExerciseWizard exerciseTypeDialog;
	
	/** The exercise score setup dialog. */
	private ExerciseScoreWizard exerciseScoreSetupDialog;
	
	/** The exercise score editor dialog. */
	private ExerciseScoreEditor exerciseScoreEditorDialog;
	
	/** The exercises dir. */
	File exercisesDir; // the Calao directory path
	
	/** The playback speed. */
	int playbackSpeed = 80;
	
	/** The time numerator. */
	int timeNumerator = 4;
    
    /** The time denominator. */
    int timeDenominator = 4;

	/** The is playing. */
	private boolean isPlaying = false;
	
	/** The playback. */
	private Sequencer playback;
	
	/**
	 * Instantiates a new exercises panel.
	 *
	 * @param f the f
	 * @param b the b
	 * @param p the p
	 * @param mc the mc
	 * @param d the d
	 */
	public ExercisesPanel(Font f, ResourceBundle b, Preferences p, MidiController mc, Dimension d)
	{
		appFont = f;
		appBundle = b;
		appPrefs = p;
		appMidi = mc;

		setBackground(Color.white);
		setSize(d);
		setLayout(null);

		leftPanel = new JPanel();
		leftPanel.setLayout(null);
		leftPanel.setBackground(Color.decode("0xCCF5FF"));
		Border defBorder = UIManager.getBorder(leftPanel);
		leftPanel.setBorder(BorderFactory.createTitledBorder(defBorder, "", TitledBorder.LEADING, TitledBorder.TOP));
		leftPanel.setBounds(5, 10, 330, getHeight() - 25);

		topBar = new RoundPanel(Color.decode("0xA3C7FF"), Color.decode("0xA2DDFF"));
		topBar.setBorderColor(Color.decode("0xA4D6FF"));
		topBar.setBounds(10, 7, 310, 75);
		topBar.setLayout(null);

		homeBtn = new RoundedButton("", appBundle);
		homeBtn.setBounds(10, 5, 64, 64);
		homeBtn.setBackground(Color.decode("0x8FC6E9"));
		homeBtn.setButtonImage(new ImageIcon(getClass().getResource("home.png")).getImage());
		//homeBtn.setImagSize(42, 42); // keep as a reminder

		newExerciseBtn = new RoundedButton("", appBundle);
		newExerciseBtn.setBounds(92, 5, 64, 64);
		newExerciseBtn.setBackground(Color.decode("0x8FC6E9"));
		newExerciseBtn.setButtonImage(new ImageIcon(getClass().getResource("new_exercise.png")).getImage());
		newExerciseBtn.addActionListener(this);
		
		editExerciseBtn = new RoundedButton("", appBundle);
		editExerciseBtn.setBounds(159, 5, 64, 64);
		editExerciseBtn.setBackground(Color.decode("0x8FC6E9"));
		editExerciseBtn.setButtonImage(new ImageIcon(getClass().getResource("edit.png")).getImage());
		editExerciseBtn.setEnabled(false);
		editExerciseBtn.addActionListener(this);

		listenBtn = new RoundedButton("", appBundle);
		listenBtn.setBounds(240, 5, 64, 64);
		listenBtn.setBackground(Color.decode("0x8FC6E9"));
		listenBtn.setButtonImage(new ImageIcon(getClass().getResource("playback.png")).getImage());
		listenBtn.setEnabled(false);
		listenBtn.addActionListener(this);

		topBar.add(homeBtn);
		topBar.add(newExerciseBtn);
		topBar.add(editExerciseBtn);
		topBar.add(listenBtn);

		leftPanel.add(topBar);

		exerciseTitle = new JLabel("", null, JLabel.LEFT);
		exerciseTitle.setFont(new Font("Arial", Font.BOLD, 20));
		exerciseTitle.setBounds(350, 10, getWidth() - 360, 30);

		int panelsWidth = getWidth() - 360;
		int panelsHeight = getHeight() - 95;
		int scaledWidth = (int)(panelsWidth * 1.66) - 10;
		int scaledHeight = (int)(panelsHeight * 1.66);

		layers = new JLayeredPane();
		layers.setPreferredSize(new Dimension(panelsWidth, panelsHeight));
		//layers.setBounds(350, 40, panelsWidth, panelsHeight);

		scoreStaff = new Staff(appFont, appBundle, appPrefs, null, false, true);
		scoreStaff.setPreferredSize( new Dimension(scaledWidth, scaledHeight));
		scoreStaff.setBounds(10, 0, scaledWidth, scaledHeight);
		scoreStaff.setScale(0.6);
		scoreStaff.setMeasuresNumber(10);
		scoreStaff.setOpaque(true);

		notesLayer = new NotesPanel(appFont, appPrefs, null, null, false);
		notesLayer.setPreferredSize( new Dimension(scaledWidth, scaledHeight));
		notesLayer.setBounds(10, 0, scaledWidth, scaledHeight);
		notesLayer.setScale(0.6);
		notesLayer.setOpaque(false);

		layers.add(scoreStaff, new Integer(1));
		layers.add(notesLayer, new Integer(2));
		
		exerciseScrollPanel = new JScrollPane(layers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		exerciseScrollPanel.getViewport().setBackground(Color.white);
		exerciseScrollPanel.setBounds(340, 40, panelsWidth + 10, panelsHeight);
		
		exLineBtn = new RoundedButton(appBundle.getString("_menuNotereading"), appBundle, Color.decode("0xA2DDFF"));
		exLineBtn.setBounds(230 + ((getWidth() - 330) / 2), getHeight() - 100, 200, 40);
		exLineBtn.setBackground(Color.decode("0xA1C5FF"));
		exLineBtn.setFont(new Font("Arial", Font.BOLD, 16));
		exLineBtn.setVisible(false);
		
		exRhythmBtn = new RoundedButton(appBundle.getString("_menuRythmreading"), appBundle, Color.decode("0xA2DDFF"));
		exRhythmBtn.setBounds(230 + ((getWidth() - 330) / 2), getHeight() - 100, 200, 40);
		exRhythmBtn.setBackground(Color.decode("0xA1C5FF"));
		exRhythmBtn.setFont(new Font("Arial", Font.BOLD, 16));
		exRhythmBtn.setVisible(false);
		
		exScoreBtn = new RoundedButton(appBundle.getString("_menuScorereading"), appBundle, Color.decode("0xA2DDFF"));
		exScoreBtn.setBounds(350 + ((getWidth() - 330) / 2), getHeight() - 100, 200, 40);
		exScoreBtn.setBackground(Color.decode("0xA1C5FF"));
		exScoreBtn.setFont(new Font("Arial", Font.BOLD, 16));
		exScoreBtn.setVisible(false);

		add(leftPanel);
		add(exerciseTitle);
		add(exerciseScrollPanel);

		add(exLineBtn);
		add(exRhythmBtn);
		add(exScoreBtn);

		updateTreeList();
	}
	
	/**
	 * Update language.
	 *
	 * @param bundle the bundle
	 */
	public void updateLanguage(ResourceBundle bundle)
	{
		logger.debug("EXERCISE - update language");
		appBundle = bundle;
	}
	
    /**
     * The Class ExNodeInfo.
     */
    private class ExNodeInfo 
    {
        
        /** The ex label. */
        public String exLabel;
        
        /** The file path. */
        public String filePath;

        /**
         * Instantiates a new ex node info.
         *
         * @param lbl the lbl
         * @param path the path
         */
        public ExNodeInfo(String lbl, String path) 
        {
        	exLabel = lbl;
        	filePath = path;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return exLabel;
        }
    }
	
	/**
	 * Walk.
	 *
	 * @param path the path
	 * @param parentNode the parent node
	 */
	public void walk( String path, DefaultMutableTreeNode parentNode ) 
	{
        File root = new File( path );
        File[] list = root.listFiles();

        for ( File f : list ) 
        {
            if ( f.isDirectory() ) 
            {
            	DefaultMutableTreeNode dirNode = new DefaultMutableTreeNode(f.getName());
            	parentNode.add(dirNode);
                walk( f.getAbsolutePath(), dirNode );
                logger.debug( "Dir: " + f.getAbsolutePath() );
            }
            else 
            {
            	if (f.getAbsolutePath().endsWith(".xml"))
            	{
            		ExNodeInfo nInfo = new ExNodeInfo(f.getName().substring(0, f.getName().length() - 4), f.getAbsolutePath());
            		DefaultMutableTreeNode dirNode = new DefaultMutableTreeNode(nInfo);
            		dirNode.setUserObject(nInfo);
            		parentNode.add(dirNode);
            		logger.debug( "File: " + f.getAbsolutePath() );
            	}
            }
        }
    }

	/**
	 * Update tree list.
	 */
	private void updateTreeList()
	{
		exercisesDir = new File("Exercises");
		File EXdir = new File(exercisesDir.getAbsolutePath());
		File[] list = EXdir.listFiles();
		
		if (treeScrollPanel != null)
			leftPanel.remove(treeScrollPanel);
		
		if (list == null || list.length == 0)
		{
			DefaultMutableTreeNode mainNode = new DefaultMutableTreeNode(appBundle.getString("_exNotFound"));
			exercisesList = new JTree(mainNode);
		}
		else
		{
			DefaultMutableTreeNode mainNode = new DefaultMutableTreeNode("Exercises");
			walk(EXdir.getAbsolutePath(), mainNode);
			exercisesList = new JTree(mainNode);
			exercisesList.setRootVisible(false);
			exercisesList.setShowsRootHandles(true);
			exercisesList.addTreeSelectionListener(this);
		}
		
		exercisesList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		exercisesList.setBackground(Color.decode("0xCCF5FF"));
		exercisesList.setBounds(0, 0, 280, getHeight() - 180);
		
		treeScrollPanel = new JScrollPane(exercisesList);
		//Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		//treeScrollPanel.setBorder(border);
		treeScrollPanel.setBounds(10, 80, 300, getHeight() - 170);
		leftPanel.add(treeScrollPanel);
	}
	
	/**
	 * Show exercise setup.
	 *
	 * @param type the type
	 */
	private void showExerciseSetup(int type)
	{
		exerciseTypeDialog.dispose();
		newExercise.setType(type);
		exerciseScoreSetupDialog = new ExerciseScoreWizard(appBundle, appPrefs, appFont, newExercise);
		exerciseScoreSetupDialog.setVisible(true);
		exerciseScoreSetupDialog.addPropertyChangeListener(this);
	}
	
	/**
	 * Gets the selected exercise.
	 *
	 * @return the selected exercise
	 */
	public Exercise getSelectedExercise()
	{
		return selectedExercise;
	}
	
	/**
	 * Stop playback.
	 */
	public void stopPlayback()
	{
		if (isPlaying == false)
			return;
		
		appMidi.stopPlayback();
		isPlaying = false;
	}

// **************************************************************************************************
// *                                             EVENTS                                             *
// **************************************************************************************************
	/* (non-Javadoc)
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == newExerciseBtn)
		{
			newExercise = new Exercise(appPrefs);
			exerciseTypeDialog = new ExerciseWizard(appBundle, appPrefs, appFont);
			exerciseTypeDialog.setVisible(true);
			exerciseTypeDialog.inlineExBtn.addActionListener(this);
			exerciseTypeDialog.rhythmExBtn.addActionListener(this);
			exerciseTypeDialog.scoreExBtn.addActionListener(this);
		}
		else if (ae.getSource() == editExerciseBtn)
		{
			exerciseScoreEditorDialog = new ExerciseScoreEditor(appBundle, appPrefs, appFont, appMidi, selectedExercise);
			exerciseScoreEditorDialog.setVisible(true);
			exerciseScoreEditorDialog.addPropertyChangeListener(this);
		}
		if (exerciseTypeDialog != null)
		{
			if(ae.getSource() == exerciseTypeDialog.inlineExBtn)
			{
				showExerciseSetup(0);
			}
			else if(ae.getSource() == exerciseTypeDialog.rhythmExBtn)
			{
				showExerciseSetup(1);
			}
			else if(ae.getSource() == exerciseTypeDialog.scoreExBtn)
			{
				showExerciseSetup(2);
			}
		}
		if (ae.getSource() == listenBtn && selectedExercise != null)
		{
			if (isPlaying == false)
			{
				Vector<Note> tmpSequence = new Vector<Note>(); 
				tmpSequence.addAll(selectedExercise.notes);
				tmpSequence.addAll(selectedExercise.notes2);
				playback = appMidi.createPlayback(appPrefs, selectedExercise.speed, tmpSequence, timeDenominator / 4, true, 0);
				playback.addMetaEventListener(new MetaEventListener() {
			          public void meta(MetaMessage meta) 
			          {
			        	  byte[] metaData = meta.getData();
			              String strData = new String(metaData);
			        	  if ("end".equals(strData))
			              {
			        		appMidi.stopPlayback();
			  				listenBtn.setButtonImage(new ImageIcon(getClass().getResource("playback.png")).getImage());
			  				listenBtn.repaint();
			  				isPlaying = false;
			              }
			          }
					});
				listenBtn.setButtonImage(new ImageIcon(getClass().getResource("stop.png")).getImage());
				playback.start();
				isPlaying = true;
			}
			else
			{
				appMidi.stopPlayback();
				listenBtn.setButtonImage(new ImageIcon(getClass().getResource("playback.png")).getImage());
				listenBtn.repaint();
				isPlaying = false;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName() == "gotoScoreEditor")
		{
			exerciseScoreEditorDialog = new ExerciseScoreEditor(appBundle, appPrefs, appFont, appMidi, newExercise);
			exerciseScoreEditorDialog.setVisible(true);
			exerciseScoreEditorDialog.addPropertyChangeListener(this);
		}
		else if (evt.getPropertyName() == "exerciseSaved")
		{
			updateTreeList();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) 
	{
		logger.debug("valueChanged---");
		//Returns the last path element of the selection.
	    DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)exercisesList.getLastSelectedPathComponent();
	    
	    if (selNode == null || selNode.isLeaf() == false)
	    {
	    	listenBtn.setEnabled(false);
	    	editExerciseBtn.setEnabled(false);
	    	return;
	    }

	    ExNodeInfo nInfo = (ExNodeInfo)selNode.getUserObject();
        logger.debug("Tree node clicked. Absolute path: " + nInfo.filePath);
        selectedExercise = new Exercise(appPrefs);
        selectedExercise.loadFromFile(nInfo.filePath);
        
        exerciseTitle.setText(selectedExercise.title);
        scoreStaff.setClefs(selectedExercise.clefMask);

        scoreStaff.setAccidentals(selectedExercise.acc);
        timeNumerator = 4;
        timeDenominator = 4;
        if (selectedExercise.timeSign == 1)
        	timeNumerator = 2;
        else if (selectedExercise.timeSign == 2)
        	timeNumerator = 3;
        else if (selectedExercise.timeSign == 3)
        	{ timeNumerator = 6; timeDenominator = 8; }
        else if (selectedExercise.timeSign == 4)
    		{ timeNumerator = 6; timeDenominator = 4; }
        else if (selectedExercise.timeSign == 5)
    		{ timeNumerator = 3; timeDenominator = 8; }

        scoreStaff.setTimeSignature(timeNumerator, timeDenominator);
        double totalDuration = selectedExercise.notes.get(selectedExercise.notes.size() - 1).timestamp + selectedExercise.notes.get(selectedExercise.notes.size() - 1).duration;
        scoreStaff.setMeasuresNumber((int)Math.ceil(totalDuration / (timeNumerator / (timeDenominator / 4))));
        playbackSpeed = selectedExercise.speed;
        
        notesLayer.setClefs(selectedExercise.clefMask);
        notesLayer.setStaffWidth(scoreStaff.getStaffWidth());
        notesLayer.setFirstNoteXPosition(scoreStaff.getFirstNoteXPosition());
        notesLayer.setNotesSequence(selectedExercise.notes, selectedExercise.notes2);
        scoreStaff.setRowsDistance(notesLayer.getRowsDistance());
        //notesLayer.setRowsDistance(notesLayer.getRowsDistance());
        notesLayer.setNotesPositions();
        listenBtn.setEnabled(true);
    
        exLineBtn.setVisible(false);
        exRhythmBtn.setVisible(false);
        exScoreBtn.setVisible(false);

        if (selectedExercise.type == 0)
        {
        	exLineBtn.setBounds(exRhythmBtn.getX(), exRhythmBtn.getY(), 200, 40);
        	exLineBtn.setVisible(true);
        }
        else if (selectedExercise.type == 1)
        	exRhythmBtn.setVisible(true);
        else if (selectedExercise.type == 2)
        {
        	exScoreBtn.setVisible(true);
        	exLineBtn.setBounds(exRhythmBtn.getX() - 120, exRhythmBtn.getY(), 200, 40);
        	exLineBtn.setVisible(true);
        }
        editExerciseBtn.setEnabled(true);
        exerciseScrollPanel.getVerticalScrollBar().setValue(0);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) 
	{
		g.setColor(this.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		leftPanel.setBounds(5, 10, 330, getHeight() - 20);
		exercisesList.setBounds(0, 0, 290, getHeight() - 120);
		treeScrollPanel.setBounds(10, 85, 310, getHeight() - 110);
		exercisesList.scrollPathToVisible(exercisesList.getSelectionPath());
		treeScrollPanel.getHorizontalScrollBar().setValue(0);

		exerciseTitle.setBounds(350, 10, getWidth() - 360, 30);
		int panelsWidth = getWidth() - 355;
		int panelsHeight = getHeight() - 95;
		int totalStaffHeight = (int)(scoreStaff.getStaffHeight() / 1.66) - 20;
		int scaledWidth = (int)(panelsWidth * 1.66) - 10;
		//int scaledHeight = (int)(totalStaffHeight * 1.66);

		//logger.debug("panelsW: " + panelsWidth + ", panelsH: " + panelsHeight + ", totalH:" + totalStaffHeight + ", scaledW: " + scaledWidth + ", scaledH: " + scaledHeight);

		layers.setPreferredSize(new Dimension(panelsWidth, totalStaffHeight));
		scoreStaff.setBounds(10, 0, scaledWidth, totalStaffHeight);
		notesLayer.setBounds(10, 0, scaledWidth, totalStaffHeight);
		notesLayer.setStaffWidth(scoreStaff.getStaffWidth());
		notesLayer.setNotesPositions();
		exerciseScrollPanel.setBounds(340, 40, panelsWidth + 10, panelsHeight);
		exerciseScrollPanel.validate();

		if (selectedExercise != null && selectedExercise.type == 0)
			exLineBtn.setBounds(230 + ((getWidth() - 330) / 2), getHeight() - 50, 200, 40);
		else
			exLineBtn.setBounds(130 + ((getWidth() - 330) / 2), getHeight() - 50, 200, 40);
		exRhythmBtn.setBounds(230 + ((getWidth() - 330) / 2), getHeight() - 50, 200, 40);
		exScoreBtn.setBounds(350 + ((getWidth() - 330) / 2), getHeight() - 50, 200, 40);
	}
}
