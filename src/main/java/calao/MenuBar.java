/**
 * *********************************************
 * This file is part of the Calao project
 * (https://github.com/Neonunux/calao/wiki).
 *
 * Calao is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Calao is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Calao. If not, see <http://www.gnu.org/licenses/>.
 *
 *********************************************
 */
package calao;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class LMenuBar.
 *
 * @author Neonunux
 */
public class MenuBar extends JMenuBar implements ActionListener {

    private static final Logger logger = LogManager.getLogger(MidiController.class.getName());

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The app bundle.
     */
    private ResourceBundle appBundle;

    private Preferences appPrefs;

    // Menu Bar entries:
    //    Settings:
    /**
     * The config menu.
     */
    public JMenu configMenu;

    /**
     * The midi menu.
     */
    public JMenuItem midiMenu;

    /**
     * The stats check.
     */
    public JCheckBoxMenuItem statsCheck;

    /**
     * The lang menu.
     */
    public JMenu langMenu;

    /**
     * The exit menu.
     */
    public JMenuItem exitMenu;

    /**
     * The about menu.
     */
    public JMenu aboutMenu;

    /**
     * The website menu.
     */
    public JMenuItem websiteMenu;

    /**
     * The credits menu.
     */
    public JMenuItem creditsMenu;

    /**
     * The rblanguageen.
     */
    private JRadioButtonMenuItem rblanguageen = new JRadioButtonMenuItem();

    /**
     * The rblanguagede.
     */
    private JRadioButtonMenuItem rblanguagede = new JRadioButtonMenuItem();

    /**
     * The rblanguagees.
     */
    private JRadioButtonMenuItem rblanguagees = new JRadioButtonMenuItem();

    /**
     * The rblanguageit.
     */
    private JRadioButtonMenuItem rblanguageit = new JRadioButtonMenuItem();

    /**
     * The rblanguageda.
     */
    private JRadioButtonMenuItem rblanguageda = new JRadioButtonMenuItem();

    /**
     * The rblanguagetr.
     */
    private JRadioButtonMenuItem rblanguagetr = new JRadioButtonMenuItem();

    /**
     * The rblanguagefi.
     */
    private JRadioButtonMenuItem rblanguagefi = new JRadioButtonMenuItem();

    /**
     * The rblanguageko.
     */
    private JRadioButtonMenuItem rblanguageko = new JRadioButtonMenuItem();

    /**
     * The rblanguageeo.
     */
    private JRadioButtonMenuItem rblanguageeo = new JRadioButtonMenuItem();

    /**
     * The rblanguagepl.
     */
    private JRadioButtonMenuItem rblanguagepl = new JRadioButtonMenuItem();

    /**
     * The rblanguagept.
     */
    private JRadioButtonMenuItem rblanguagept = new JRadioButtonMenuItem();

    /**
     * The rblanguagehe.
     */
    private JRadioButtonMenuItem rblanguagehe = new JRadioButtonMenuItem();

    /**
     * The rblanguagehu.
     */
    private JRadioButtonMenuItem rblanguagehu = new JRadioButtonMenuItem();

    /**
     * The rblanguageru.
     */
    private JRadioButtonMenuItem rblanguageru = new JRadioButtonMenuItem();

    /**
     * The rblanguagefr.
     */
    private JRadioButtonMenuItem rblanguagefr = new JRadioButtonMenuItem();

    private String build = "0";

	private Version appVersion;

    /**
     * Instantiates a new l menu bar.
     *
     * @param b the b
     * @param p the p
     * @param v
     */
    public MenuBar(ResourceBundle b, Preferences p, Version v) {
        appBundle = b;
        appPrefs = p;
        appVersion = v;
        build = appVersion.getVersionString();

        configMenu = new JMenu();
        configMenu.setText(appBundle.getString("_menuPreferences"));

        midiMenu = new JMenuItem(new ImageIcon(getClass().getResource("midi.png")));
        midiMenu.setText(appBundle.getString("_menuMidi"));
        midiMenu.addActionListener(this);

        statsCheck = new JCheckBoxMenuItem(new ImageIcon(getClass().getResource("stats.png")));
        statsCheck.setText(appBundle.getString("_menuSaveStatistics"));
        statsCheck.addActionListener(this);
        if (Integer.parseInt(appPrefs.getProperty("saveStats")) == 1) {
            statsCheck.setSelected(true);
        }

        langMenu = new JMenu();
        langMenu.setText(appBundle.getString("_menuLanguage"));

        ButtonGroup bGroup = new ButtonGroup();
        String lang = appPrefs.getProperty("language");

        rblanguageda = new JRadioButtonMenuItem("Dansk");
        rblanguageda.setMnemonic(KeyEvent.VK_A);
        rblanguageda.addActionListener(this);
        bGroup.add(rblanguageda);
        langMenu.add(rblanguageda);

        rblanguagede = new JRadioButtonMenuItem("Deutsch");
        rblanguagede.setMnemonic(KeyEvent.VK_D);
        bGroup.add(rblanguagede);
        rblanguagede.addActionListener(this);
        langMenu.add(rblanguagede);

        rblanguageen = new JRadioButtonMenuItem("English");
        rblanguageen.setMnemonic(KeyEvent.VK_E);
        rblanguageen.addActionListener(this);
        bGroup.add(rblanguageen);
        langMenu.add(rblanguageen);

        rblanguagees = new JRadioButtonMenuItem("Español");
        rblanguagees.setMnemonic(KeyEvent.VK_S);
        bGroup.add(rblanguagees);
        rblanguagees.addActionListener(this);
        langMenu.add(rblanguagees);

        rblanguageeo = new JRadioButtonMenuItem("Esperanto");
        rblanguageeo.setMnemonic(KeyEvent.VK_N);
        rblanguageeo.addActionListener(this);
        bGroup.add(rblanguageeo);
        langMenu.add(rblanguageeo);

        rblanguagefr = new JRadioButtonMenuItem("Français");
        rblanguagefr.setMnemonic(KeyEvent.VK_F);
        bGroup.add(rblanguagefr);
        rblanguagefr.addActionListener(this);
        langMenu.add(rblanguagefr);

        rblanguagehe = new JRadioButtonMenuItem("Hebrew");
        rblanguagehe.setMnemonic(KeyEvent.VK_H);
        rblanguagehe.addActionListener(this);
        bGroup.add(rblanguagehe);
        langMenu.add(rblanguagehe);

        rblanguageko = new JRadioButtonMenuItem("Korean");
        rblanguageko.setMnemonic(KeyEvent.VK_K);
        rblanguageko.addActionListener(this);
        bGroup.add(rblanguageko);
        langMenu.add(rblanguageko);

        rblanguageit = new JRadioButtonMenuItem("Italiano");
        rblanguageit.setMnemonic(KeyEvent.VK_I);
        rblanguageit.addActionListener(this);
        bGroup.add(rblanguageit);
        langMenu.add(rblanguageit);

        rblanguagehu = new JRadioButtonMenuItem("Magyar");
        rblanguagehu.setMnemonic(KeyEvent.VK_H);
        rblanguagehu.addActionListener(this);
        bGroup.add(rblanguagehu);
        langMenu.add(rblanguagehu);

        rblanguagepl = new JRadioButtonMenuItem("Polski");
        rblanguagepl.setMnemonic(KeyEvent.VK_O);
        rblanguagepl.addActionListener(this);
        bGroup.add(rblanguagepl);
        langMenu.add(rblanguagepl);

        rblanguagept = new JRadioButtonMenuItem("Portugues");
        rblanguagept.setMnemonic(KeyEvent.VK_P);
        rblanguagept.addActionListener(this);
        bGroup.add(rblanguagept);
        langMenu.add(rblanguagept);

        rblanguageru = new JRadioButtonMenuItem("Russian");
        rblanguageru.setMnemonic(KeyEvent.VK_R);
        rblanguageru.addActionListener(this);
        bGroup.add(rblanguageru);
        langMenu.add(rblanguageru);

        rblanguagefi = new JRadioButtonMenuItem("Suomi");
        rblanguagefi.setMnemonic(KeyEvent.VK_F);
        rblanguagefi.addActionListener(this);
        bGroup.add(rblanguagefi);
        langMenu.add(rblanguagefi);

        rblanguagetr = new JRadioButtonMenuItem("Turk");
        rblanguagetr.setMnemonic(KeyEvent.VK_T);
        rblanguagetr.addActionListener(this);
        bGroup.add(rblanguagetr);
        langMenu.add(rblanguagetr);

        if ("es".equals(lang)) {
            rblanguagees.setSelected(true);
        } else if ("da".equals(lang)) {
            rblanguageda.setSelected(true);
        } else if ("de".equals(lang)) {
            rblanguagede.setSelected(true);
        } else if ("he".equals(lang)) {
            rblanguagehe.setSelected(true);
        } else if ("hu".equals(lang)) {
            rblanguagehu.setSelected(true);
        } else if ("fr".equals(lang)) {
            rblanguagefr.setSelected(true);
        } else if ("it".equals(lang)) {
            rblanguageit.setSelected(true);
        } else if ("fi".equals(lang)) {
            rblanguagefi.setSelected(true);
        } else if ("ko".equals(lang)) {
            rblanguageko.setSelected(true);
        } else if ("eo".equals(lang)) {
            rblanguageeo.setSelected(true);
        } else if ("pl".equals(lang)) {
            rblanguagepl.setSelected(true);
        } else if ("pt".equals(lang)) {
            rblanguagept.setSelected(true);
        } else if ("ru".equals(lang)) {
            rblanguageru.setSelected(true);
        } else if ("tr".equals(lang)) {
            rblanguagetr.setSelected(true);
        } else {
            rblanguageen.setSelected(true);
        }

        langMenu.setIcon(new ImageIcon(getClass().getResource("language.png")));
        langMenu.addActionListener(this);
        langMenu.setMnemonic(KeyEvent.VK_L);

        exitMenu = new JMenuItem(new ImageIcon(getClass().getResource("exit.png")));
        exitMenu.setText(appBundle.getString("_menuExit"));
        exitMenu.addActionListener(this);

        configMenu.add(midiMenu);
        configMenu.addSeparator();
        configMenu.add(statsCheck);
        configMenu.addSeparator();
        configMenu.add(langMenu);
        configMenu.setMnemonic(KeyEvent.VK_P);
        configMenu.addSeparator();
        configMenu.add(exitMenu);

        aboutMenu = new JMenu();
        aboutMenu.setText(appBundle.getString("_menuHelp"));

        websiteMenu = new JMenuItem(new ImageIcon(getClass().getResource("internet.png")));
        websiteMenu.setText(appBundle.getString("_menuWeb"));
        websiteMenu.addActionListener(this);

        creditsMenu = new JMenuItem(new ImageIcon(getClass().getResource("about.png")));
        creditsMenu.setText(appBundle.getString("_menuAbout"));
        creditsMenu.addActionListener(this);

        aboutMenu.add(websiteMenu);
        aboutMenu.add(creditsMenu);

        add(configMenu);
        add(aboutMenu);
    }

    /**
     * Update language.
     *
     * @param bundle the bundle
     */
    public void updateLanguage(ResourceBundle bundle) {
        appBundle = bundle;

        configMenu.setText(appBundle.getString("_menuPreferences"));
        midiMenu.setText(appBundle.getString("_menuMidi"));
        langMenu.setText(appBundle.getString("_menuLanguage"));
        statsCheck.setText(appBundle.getString("_menuSaveStatistics"));
        exitMenu.setText(appBundle.getString("_menuExit"));

        aboutMenu.setText(appBundle.getString("_menuHelp"));
        websiteMenu.setText(appBundle.getString("_menuWeb"));
        creditsMenu.setText(appBundle.getString("_menuAbout"));
    }

    // Event handler for the whole Menu Bar. 
    // It fires event to the main class using strings
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        logger.trace("LMenuBar Event received !! (" + ae.getActionCommand() + ")");
        if (ae.getSource() == rblanguageen) {
            this.firePropertyChange("langEN", false, true);
        } else if (ae.getSource() == rblanguagede) {
            this.firePropertyChange("langDE", false, true);
        } else if (ae.getSource() == rblanguagees) {
            this.firePropertyChange("langES", false, true);
        } else if (ae.getSource() == rblanguageit) {
            this.firePropertyChange("langIT", false, true);
        } else if (ae.getSource() == rblanguageda) {
            this.firePropertyChange("langDA", false, true);
        } else if (ae.getSource() == rblanguagetr) {
            this.firePropertyChange("langTR", false, true);
        } else if (ae.getSource() == rblanguagefi) {
            this.firePropertyChange("langFI", false, true);
        } else if (ae.getSource() == rblanguageko) {
            this.firePropertyChange("langKO", false, true);
        } else if (ae.getSource() == rblanguageeo) {
            this.firePropertyChange("langEO", false, true);
        } else if (ae.getSource() == rblanguagepl) {
            this.firePropertyChange("langPL", false, true);
        } else if (ae.getSource() == rblanguagept) {
            this.firePropertyChange("langPT", false, true);
        } else if (ae.getSource() == rblanguagehu) {
            this.firePropertyChange("langHU", false, true);
        } else if (ae.getSource() == rblanguagehe) {
            this.firePropertyChange("langHE", false, true);
        } else if (ae.getSource() == rblanguageru) {
            this.firePropertyChange("langRU", false, true);
        } else if (ae.getSource() == rblanguagefr) {
            this.firePropertyChange("langFR", false, true);
        } else if (ae.getSource() == midiMenu) {
            this.firePropertyChange("midiOptions", false, true);
        } else if (ae.getSource() == statsCheck) {
            if (statsCheck.isSelected() == true) {
                appPrefs.setProperty("saveStats", "1");
            } else {
                appPrefs.setProperty("saveStats", "0");
            }
            appPrefs.storeProperties();
        } else if (ae.getSource() == exitMenu) {
            this.firePropertyChange("exitProgram", false, true);
        } else if (ae.getSource() == websiteMenu) {
            openURL("https://github.com/Neonunux/calao/wiki");
        } else if (ae.getSource() == creditsMenu) {
        	JEditorPane text = new JEditorPane("text/html", "<html><b>Calao v. " + build + "<br>written by Neonunux<br><br>"
                    + "This is an open source musical educational software written in Java, inspired<br>"
                    + "by the ScoreDate project<br>"
                    + "Written by Massimo Callegari<br>"
                    + "himself inspired<br>by the Jalmus project<br><br>"
                    + "It is distributed under the <br> "
                    + "<a href='http://www.gnu.org/licenses/gpl-3.0.txt'>GPL 3.0 license</a>"
                    + "</b></html>");

			JButton splashShow = new JButton();			
			splashShow.setText("View splashscreen");
			splashShow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SplashScreen sp = new SplashScreen(-1, appVersion);
					sp.setAlwaysOnTop(true);
					logger.debug("Calao Splashscreen is displayed");
				}
			});
			JPanel panneau = new JPanel();
			panneau.setLayout(new BoxLayout(panneau, BoxLayout.PAGE_AXIS));
			
			panneau.add(text);
			panneau.add(splashShow);
			
            // Link license URL support click
            HyperlinkListener listener = new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    java.net.URL uristring = e.getURL();
                    if (e.getEventType() == EventType.ACTIVATED) {
                        openURL(uristring.toString());
                    }
                }
            };
            text.addHyperlinkListener(listener);
            text.setEditable(false);

            // show
            JOptionPane.showMessageDialog(this.getParent(), panneau,
                    appBundle.getString("_menuAbout"), JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("internet.png")));
        }
    }

    /**
     * Open url.
     *
     * @param uristring the uristring
     */
    public void openURL(String uristring) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            logger.error("Desktop is not supported (fatal)");
            return;
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            logger.error("Desktop doesn't support the browse action (fatal)");
            return;
        }

        try {
            java.net.URI uri = new java.net.URI(uristring);
            desktop.browse(uri);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
