package calao;

import java.awt.Font;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Voices {
	private static final Logger logger = LogManager.getLogger(Voices.class
			.getName());

	private ArrayList<String> voices;
	// private Font appFont;
	// private ResourceBundle appBundle;
	private Preferences appPrefs;
	private String name;

	Voices(Font f, ResourceBundle b, Preferences p) {
		// appFont = f;
		// appBundle = b;
		appPrefs = p;
		voices = new ArrayList<String>();
		this.setFromProperties(null);
	}

	void checkVoices() {
		if (this.isUnset()) {
			voices.set(0, "G2");
			appPrefs.setProperty("voice" + 0, "G2");
		}
	}

	boolean isUnset() {
		boolean unset = true;
		// check one or more clef is set
		for (int i = 0; i < 4; i++) {
			if (voices.get(i) != "NONE") {
				unset = false;
				break;
			}
		}
		return unset;
	}

	void show() {
		for (int i = 0; i < 4; i++) {
			logger.debug("[Voices.show] Voice" + i + ": " + voices.get(i));
		}
	}

	public int size() {
		return voices.size();
	}

	void setFromProperties(Preferences a) {
		voices.clear();

		String res;
		for (int i = 0; i < 4; i++) {
			if (a == null)
				res = appPrefs.getProperty("voice" + i);
			else
				res = a.getProperty("voice" + i);

			if (res == "-1")
				res = "NONE";
			voices.add(i, res);
		}
		checkVoices();
	}

	public void clear() {
		voices.clear();
	}

	public void setVoice(int index, String value) {
		voices.set(index, value);
	}

	public String getVoice(int index) {
		return voices.get(index);
	}

	public ArrayList<String> getVoices() {
		return voices;
	}

	public void setVoices(ArrayList<String> voices) {
		this.voices = voices;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}