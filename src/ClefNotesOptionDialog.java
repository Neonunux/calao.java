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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ClefNotesOptionDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -2654540587350157146L;
	ResourceBundle appBundle;
	Font appFont;
	Preferences appPrefs;
	
	ClefSelector ClefG2;
	ClefSelector ClefF4;
	ClefSelector ClefC3;
	ClefSelector ClefC4;
	
	JComboBox accCB;
	
	JCheckBox wholeCB;
	JCheckBox halfCB;
	JCheckBox halfQuarterCB;
	JCheckBox quarterCB;
	JCheckBox quarterEighthCB;
	JCheckBox eighthCB;
	JCheckBox tripletCB;
	JCheckBox silenceCB;
	
	JRadioButton fourfourButton;
	JRadioButton twofourButton;
	JRadioButton threefourButton;
	JRadioButton sixeightButton;
	JRadioButton sixfourButton;
	JRadioButton threeeightButton;

	JButton okButton;
    JButton cancelButton;
	
	public ClefNotesOptionDialog(Font f, ResourceBundle b, Preferences p)
	{
		appFont = f;
		appBundle = b;
		appPrefs = p;

		int clefSelHeight = 205;
		int clefSelWidth = 170;

		setLayout(null);
        setSize(700, 510);
		String title = appBundle.getString("_menuClef") + " & " + appBundle.getString("_menuNotes");
		setTitle(title);
		setAlwaysOnTop(true);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window on the display

        /*
         * ***** First panel: Contains the ClefSelector objects to select clefs ******
         */
		JPanel clefsPanel = new JPanel();
		clefsPanel.setLayout(null);
		clefsPanel.setBackground(Color.white);
		clefsPanel.setPreferredSize(new Dimension((clefSelWidth * 2) + 15, (clefSelHeight * 2) + 20));
		clefsPanel.setBounds(0, 0, (clefSelWidth * 2) + 15, (clefSelHeight * 2) + 20);
		
		int clefsMask = Integer.parseInt(appPrefs.getProperty("clefsMask")); 
		if (clefsMask == -1) clefsMask = appPrefs.CLEF_G2;

		ClefG2 = new ClefSelector(appBundle, "G2");
		ClefG2.setPreferredSize(new Dimension(clefSelWidth, clefSelHeight));
		ClefG2.setBounds(5, 10, clefSelWidth, clefSelHeight);
		ClefG2.setFont(appFont);
		if ((clefsMask & appPrefs.CLEF_G2) > 0)
			ClefG2.setEnabled(true);
		else
			ClefG2.setEnabled(false);
		
		NoteGenerator tmpNG = new NoteGenerator(appPrefs, null, false);
		// retrieve previously saved pitches and convert them into levels 
		int lowerPitch = Integer.parseInt(appPrefs.getProperty("ClefG2Lower"));
		if (lowerPitch == -1) lowerPitch = 64; // default, set to E3
		int higherPitch = Integer.parseInt(appPrefs.getProperty("ClefG2Upper"));
		if (higherPitch == -1) higherPitch = 77; // default, set to F4
		System.out.println("Treble Clef pitches: " + lowerPitch + " to " + higherPitch);
		ClefG2.setLevels(24 - tmpNG.getIndexFromPitch(tmpNG.CLEF_G2_BASEPITCH, lowerPitch, false), 
							 24 - tmpNG.getIndexFromPitch(tmpNG.CLEF_G2_BASEPITCH, higherPitch, false));
		
		ClefF4 = new ClefSelector(appBundle, "F4");
		ClefF4.setPreferredSize(new Dimension(clefSelWidth, clefSelHeight));
		ClefF4.setBounds(clefSelWidth + 10, 10, clefSelWidth, clefSelHeight);
		ClefF4.setFont(appFont);
		if ((clefsMask & appPrefs.CLEF_F4) > 0)
			ClefF4.setEnabled(true);
		else
			ClefF4.setEnabled(false);
		// retrieve previously saved pitches and convert them into levels 
		lowerPitch = Integer.parseInt(appPrefs.getProperty("ClefF4Lower"));
		if (lowerPitch == -1) lowerPitch = 43; // default, set to G1
		higherPitch = Integer.parseInt(appPrefs.getProperty("ClefF4Upper"));
		if (higherPitch == -1) higherPitch = 57; // default, set to A2
		System.out.println("Bass Clef pitches: " + lowerPitch + " to " + higherPitch);
		ClefF4.setLevels(24 - tmpNG.getIndexFromPitch(tmpNG.CLEF_F4_BASEPITCH, lowerPitch, false), 
						   24 - tmpNG.getIndexFromPitch(tmpNG.CLEF_F4_BASEPITCH, higherPitch, false));

		ClefC3 = new ClefSelector(appBundle, "C3");
		ClefC3.setPreferredSize(new Dimension(clefSelWidth, clefSelHeight));
		ClefC3.setBounds(5, clefSelHeight + 15, clefSelWidth, clefSelHeight);
		ClefC3.setFont(appFont);
		if ((clefsMask & appPrefs.CLEF_C3) > 0)
			ClefC3.setEnabled(true);
		else
			ClefC3.setEnabled(false);
		// retrieve previously saved pitches and convert them into levels 
		lowerPitch = Integer.parseInt(appPrefs.getProperty("ClefC3Lower"));
		if (lowerPitch == -1) lowerPitch = 53; // default, set to F2
		higherPitch = Integer.parseInt(appPrefs.getProperty("ClefC3Upper"));
		if (higherPitch == -1) higherPitch = 67; // default, set to G3
		System.out.println("C3 Clef pitches: " + lowerPitch + " to " + higherPitch);
		ClefC3.setLevels(24 - tmpNG.getIndexFromPitch(tmpNG.CLEF_C3_BASEPITCH, lowerPitch, false), 
						   24 - tmpNG.getIndexFromPitch(tmpNG.CLEF_C3_BASEPITCH, higherPitch, false));
		
		ClefC4 = new ClefSelector(appBundle, "C4");
		ClefC4.setPreferredSize(new Dimension(clefSelWidth, clefSelHeight));
		ClefC4.setBounds(clefSelWidth + 10, clefSelHeight + 15, clefSelWidth, clefSelHeight);
		ClefC4.setFont(appFont);
		if ((clefsMask & appPrefs.CLEF_C4) > 0)
			ClefC4.setEnabled(true);
		else
			ClefC4.setEnabled(false);
		// retrieve previously saved pitches and convert them into levels 
		lowerPitch = Integer.parseInt(appPrefs.getProperty("ClefC4Lower"));
		if (lowerPitch == -1) lowerPitch = 50; // default, set to D2
		higherPitch = Integer.parseInt(appPrefs.getProperty("ClefC4Upper"));
		if (higherPitch == -1) higherPitch = 64; // default, set to E3
		System.out.println("C4 Clef pitches: " + lowerPitch + " to " + higherPitch);
		ClefC4.setLevels(24 - tmpNG.getIndexFromPitch(tmpNG.CLEF_C4_BASEPITCH, lowerPitch, false), 
							24 - tmpNG.getIndexFromPitch(tmpNG.CLEF_C4_BASEPITCH, higherPitch, false));
		
		clefsPanel.add(ClefG2);
		clefsPanel.add(ClefF4);
		clefsPanel.add(ClefC3);
		clefsPanel.add(ClefC4);		

		 /*
		  * ***** Second panel: contains accidentals, notes type and time signature selections  ******
		  */
		JPanel notesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		notesPanel.setBackground(Color.white);
		notesPanel.setBounds((clefSelWidth * 2) + 15, 0, getWidth() - (clefSelWidth * 2) - 20, getHeight() - 75);
		notesPanel.setPreferredSize(new Dimension(getWidth() - (clefSelWidth * 2) - 20, getHeight() - 95));

		// ****** Sub panel of notesPanel. Contains accidental selection (label + combobox)
		RoundPanel accidentalsPanel = new RoundPanel();
		accidentalsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
		accidentalsPanel.setBackground(Color.white);
		accidentalsPanel.setPreferredSize(new Dimension(getWidth() - (clefSelWidth * 2) - 40, 50));

		JLabel accLabel = new JLabel(appBundle.getString("_accidentals") + "  ");
		accLabel.setFont(new Font("Arial", Font.BOLD, 20));
		accidentalsPanel.add(accLabel);

		accCB = new JComboBox();
		accCB.setPreferredSize(new Dimension(150, 27));
		accCB.addItem(appBundle.getString("_nosharpflat"));
		accCB.addItem("1 "+ appBundle.getString("_sharp"));
		accCB.addItem("2 "+ appBundle.getString("_sharp"));
		accCB.addItem("3 "+ appBundle.getString("_sharp"));
		accCB.addItem("4 "+ appBundle.getString("_sharp"));
		accCB.addItem("5 "+ appBundle.getString("_sharp"));
		accCB.addItem("6 "+ appBundle.getString("_sharp"));
		accCB.addItem("7 "+ appBundle.getString("_sharp"));
		accCB.addItem("1 "+ appBundle.getString("_flat"));
		accCB.addItem("2 "+ appBundle.getString("_flat"));
		accCB.addItem("3 "+ appBundle.getString("_flat"));
		accCB.addItem("4 "+ appBundle.getString("_flat"));
		accCB.addItem("5 "+ appBundle.getString("_flat"));
		accCB.addItem("6 "+ appBundle.getString("_flat"));
		accCB.addItem("7 "+ appBundle.getString("_flat"));
		//accCB.addItem(appBundle.getString("_random")); // TODO: implement random accidentals
		accidentalsPanel.add(accCB);
		int accIdx = Integer.parseInt(appPrefs.getProperty("accidentals"));
		if (accIdx == -1)
			accCB.setSelectedIndex(0);
		else
			accCB.setSelectedIndex(accIdx);

		/*
		 * ***** Sub panel of notesPanel. Contains notes type selection (label + checkboxes)
		 */
		RoundPanel notesTypePanel = new RoundPanel();
		notesTypePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		notesTypePanel.setBackground(Color.white);
		notesTypePanel.setPreferredSize(new Dimension(getWidth() - (clefSelWidth * 2) - 40, 220));

		JLabel nTypesLabel = new JLabel(appBundle.getString("_menuNotes") + "  ");
		nTypesLabel.setFont(new Font("Arial", Font.BOLD, 22));
		nTypesLabel.setPreferredSize(new Dimension(getWidth() - (clefSelWidth * 2) - 60, 40));

		wholeCB = new JCheckBox("w");
		wholeCB.setFont(appFont.deriveFont(50f));
		int noteOn = Integer.parseInt(appPrefs.getProperty("wholeNote")); 
		if (noteOn == 1 || noteOn == -1)
			wholeCB.setSelected(true);
		halfCB = new JCheckBox("h");
		halfCB.setFont(appFont.deriveFont(50f));
		if (Integer.parseInt(appPrefs.getProperty("halfNote")) == 1)
			halfCB.setSelected(true);
		halfCB.addActionListener(this);
		halfQuarterCB = new JCheckBox("d");
		halfQuarterCB.setFont(appFont.deriveFont(50f));
		if (Integer.parseInt(appPrefs.getProperty("3_4_Note")) == 1)
			halfQuarterCB.setSelected(true);
		halfQuarterCB.addActionListener(this);
		quarterCB = new JCheckBox("q");
		quarterCB.setFont(appFont.deriveFont(50f));
		if (Integer.parseInt(appPrefs.getProperty("quarterNote")) == 1)
			quarterCB.setSelected(true);
		if (halfQuarterCB.isSelected() == true)
			quarterCB.setEnabled(false);
		quarterCB.addActionListener(this);
		quarterEighthCB = new JCheckBox("j");
		quarterEighthCB.setFont(appFont.deriveFont(50f));
		if (Integer.parseInt(appPrefs.getProperty("3_8_Note")) == 1)
			quarterEighthCB.setSelected(true);
		quarterEighthCB.addActionListener(this);
		eighthCB = new JCheckBox("" + (char)0xC8);
		eighthCB.setFont(appFont.deriveFont(50f));
		if (Integer.parseInt(appPrefs.getProperty("eighthNote")) == 1)
			eighthCB.setSelected(true);
		if (quarterEighthCB.isSelected() == true)
			eighthCB.setEnabled(false);
		tripletCB = new JCheckBox("T");
		tripletCB.setFont(appFont.deriveFont(50f));
		if (Integer.parseInt(appPrefs.getProperty("tripletNote")) == 1)
			tripletCB.setSelected(true);
		silenceCB = new JCheckBox("H");
		silenceCB.setFont(appFont.deriveFont(50f));
		noteOn = Integer.parseInt(appPrefs.getProperty("silenceNote")); 
		if (noteOn == 1 || noteOn == -1)
			silenceCB.setSelected(true);

		notesTypePanel.add(nTypesLabel);
		notesTypePanel.add(wholeCB);
		notesTypePanel.add(halfCB);
		notesTypePanel.add(halfQuarterCB);
		notesTypePanel.add(quarterCB);
		notesTypePanel.add(quarterEighthCB);
		notesTypePanel.add(eighthCB);
		notesTypePanel.add(tripletCB);
		notesTypePanel.add(silenceCB);

		/*
		 * ***** Sub panel of notesPanel. Contains time signature selection (label + group of radio buttons)
		 */
		RoundPanel tsPanel = new RoundPanel();
		tsPanel.setBackground(Color.white);
		tsPanel.setPreferredSize(new Dimension(getWidth() - (clefSelWidth * 2) - 40, 125));
		
		JLabel timeSignLabel = new JLabel(appBundle.getString("_timeSignature") + "  ");
		timeSignLabel.setFont(new Font("Arial", Font.BOLD, 22));
		timeSignLabel.setPreferredSize(new Dimension(getWidth() - (clefSelWidth * 2) - 60, 40));
		ButtonGroup rbGroup = new ButtonGroup();
		
		fourfourButton = new JRadioButton("$"); // 4/4 symbol
		fourfourButton.setFont(appFont.deriveFont(45f));
		twofourButton = new JRadioButton("@"); // 2/4 symbol
		twofourButton.setFont(appFont.deriveFont(45f));
		threefourButton = new JRadioButton("#"); // 3/4 symbol
		threefourButton.setFont(appFont.deriveFont(45f));
		sixeightButton = new JRadioButton("P"); // 6/8 symbol
		sixeightButton.setFont(appFont.deriveFont(45f));
		sixfourButton = new JRadioButton("^"); // 6/4 symbol
		sixfourButton.setFont(appFont.deriveFont(45f));
		threeeightButton = new JRadioButton(")"); // 3/8 symbol
		threeeightButton.setFont(appFont.deriveFont(45f));
		
		int tsIdx = Integer.parseInt(appPrefs.getProperty("timeSignature"));
		if (tsIdx == 0 || tsIdx == -1)
			fourfourButton.setSelected(true);
		else if (tsIdx == 1)
			twofourButton.setSelected(true);
		else if (tsIdx == 2)
			threefourButton.setSelected(true);
		else if (tsIdx == 3)
			sixeightButton.setSelected(true);
		else if (tsIdx == 4)
			sixfourButton.setSelected(true);
		else if (tsIdx == 5)
			threeeightButton.setSelected(true);
		rbGroup.add(fourfourButton);
		rbGroup.add(twofourButton);
		rbGroup.add(threefourButton);
		rbGroup.add(sixeightButton);
		rbGroup.add(sixfourButton);
		rbGroup.add(threeeightButton);
		
		tsPanel.add(timeSignLabel);
		tsPanel.add(fourfourButton);
		tsPanel.add(twofourButton);
		tsPanel.add(threefourButton);
		tsPanel.add(sixeightButton);
		tsPanel.add(sixfourButton);
		tsPanel.add(threeeightButton);

		notesPanel.add(accidentalsPanel);
		notesPanel.add(notesTypePanel);
		notesPanel.add(tsPanel);
		
		 /*
		  * ***** Third panel: contains OK and Cancel buttons  ******
		  */
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(getWidth(), 50));
        buttonsPanel.setBounds(0, (clefSelHeight * 2) + 20, getWidth(), 50);

		okButton = new JButton(appBundle.getString("_buttonok"));
        okButton.setIcon(new ImageIcon(getClass().getResource("/resources/correct.png")));
        okButton.addActionListener(this);

        cancelButton = new JButton(appBundle.getString("_buttoncancel"));
        cancelButton.setIcon(new ImageIcon(getClass().getResource("/resources/wrong.png")));
        cancelButton.addActionListener(this);
        
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.setBackground(Color.white);
        
		add(clefsPanel);
		add(notesPanel);
		add(buttonsPanel);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == okButton)
		{
			int clefsMask = 0;
			NoteGenerator tmpNG = new NoteGenerator(appPrefs, null, false);
			if (ClefG2.isEnabled() == true)
			{
				int lowerPitch = tmpNG.getPitchFromLevel(tmpNG.CLEF_G2_BASEPITCH, 24 - ClefG2.getLowerLevel());
				int higherPitch = tmpNG.getPitchFromLevel(tmpNG.CLEF_G2_BASEPITCH, 24 - ClefG2.getHigherLevel());
				System.out.println("Treble Clef pitches: " + lowerPitch + " to " + higherPitch);
				clefsMask = clefsMask | appPrefs.CLEF_G2;
				appPrefs.setProperty("ClefG2Upper", Integer.toString(higherPitch));
				appPrefs.setProperty("ClefG2Lower", Integer.toString(lowerPitch));
			}
			if (ClefF4.isEnabled() == true)
			{
				int lowerPitch = tmpNG.getPitchFromLevel(tmpNG.CLEF_F4_BASEPITCH, 24 - ClefF4.getLowerLevel());
				int higherPitch = tmpNG.getPitchFromLevel(tmpNG.CLEF_F4_BASEPITCH, 24 - ClefF4.getHigherLevel());
				System.out.println("Bass Clef pitches: " + lowerPitch + " to " + higherPitch);
				clefsMask = clefsMask | appPrefs.CLEF_F4;
				appPrefs.setProperty("ClefF4Upper", Integer.toString(higherPitch));
				appPrefs.setProperty("ClefF4Lower", Integer.toString(lowerPitch));
			}
			if (ClefC3.isEnabled() == true)
			{
				int lowerPitch = tmpNG.getPitchFromLevel(tmpNG.CLEF_C3_BASEPITCH, 24 - ClefC3.getLowerLevel());
				int higherPitch = tmpNG.getPitchFromLevel(tmpNG.CLEF_C3_BASEPITCH, 24 - ClefC3.getHigherLevel());
				System.out.println("C3 Clef pitches: " + lowerPitch + " to " + higherPitch);
				clefsMask = clefsMask | appPrefs.CLEF_C3;
				appPrefs.setProperty("ClefC3Upper", Integer.toString(higherPitch));
				appPrefs.setProperty("ClefC3Lower", Integer.toString(lowerPitch));
			}
			if (ClefC4.isEnabled() == true)
			{
				int lowerPitch = tmpNG.getPitchFromLevel(tmpNG.CLEF_C4_BASEPITCH, 24 - ClefC4.getLowerLevel());
				int higherPitch = tmpNG.getPitchFromLevel(tmpNG.CLEF_C4_BASEPITCH, 24 - ClefC4.getHigherLevel());
				System.out.println("C4 Clef pitches: " + lowerPitch + " to " + higherPitch);
				clefsMask = clefsMask | appPrefs.CLEF_C4;
				appPrefs.setProperty("ClefC4Upper", Integer.toString(higherPitch));
				appPrefs.setProperty("ClefC4Lower", Integer.toString(lowerPitch));
			}
			
			if (clefsMask == 0) // if all clefs are disabled then set TREBLE clef by default
				appPrefs.setProperty("clefsMask", "1");
			else
				appPrefs.setProperty("clefsMask", Integer.toString(clefsMask));
			
			appPrefs.setProperty("accidentals", Integer.toString(accCB.getSelectedIndex()));
			
			if (wholeCB.isSelected() == true) appPrefs.setProperty("wholeNote", "1");
			else appPrefs.setProperty("wholeNote", "0");
			if (halfCB.isSelected() == true) appPrefs.setProperty("halfNote", "1");
			else appPrefs.setProperty("halfNote", "0");
			if (halfQuarterCB.isSelected() == true) appPrefs.setProperty("3_4_Note", "1");
			else appPrefs.setProperty("3_4_Note", "0");
			if (quarterCB.isSelected() == true) appPrefs.setProperty("quarterNote", "1");
			else appPrefs.setProperty("quarterNote", "0");
			if (quarterEighthCB.isSelected() == true) appPrefs.setProperty("3_8_Note", "1");
			else appPrefs.setProperty("3_8_Note", "0");
			if (eighthCB.isSelected() == true) appPrefs.setProperty("eighthNote", "1");
			else appPrefs.setProperty("eighthNote", "0");
			if (tripletCB.isSelected() == true) appPrefs.setProperty("tripletNote", "1");
			else appPrefs.setProperty("tripletNote", "0");
			if (silenceCB.isSelected() == true) appPrefs.setProperty("silenceNote", "1");
			else appPrefs.setProperty("silenceNote", "0");

			if (fourfourButton.isSelected() == true)
				appPrefs.setProperty("timeSignature", "0");
			else if (twofourButton.isSelected() == true)
				appPrefs.setProperty("timeSignature", "1");
			else if (threefourButton.isSelected() == true)
				appPrefs.setProperty("timeSignature", "2");
			else if (sixeightButton.isSelected() == true)
				appPrefs.setProperty("timeSignature", "3");
			else if (sixfourButton.isSelected() == true)
				appPrefs.setProperty("timeSignature", "4");
			else if (threeeightButton.isSelected() == true)
				appPrefs.setProperty("timeSignature", "5");

			appPrefs.storeProperties();
			
			this.firePropertyChange("updateParameters", false, true);
			this.dispose();
		}
		else if (ae.getSource() == halfQuarterCB)
		{
			if (halfQuarterCB.isSelected() == true)
			{
				quarterCB.setSelected(true);
				quarterCB.setEnabled(false);
			}
			else
				quarterCB.setEnabled(true);
		}
		else if (ae.getSource() == quarterEighthCB)
		{
			if (quarterEighthCB.isSelected() == true)
			{
				eighthCB.setSelected(true);
				eighthCB.setEnabled(false);
			}
			else
				eighthCB.setEnabled(true);
		}
		else if (ae.getSource() == cancelButton)
		{
			this.dispose();
		}
	}
}
