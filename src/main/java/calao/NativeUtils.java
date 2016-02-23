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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Collection of utility methods for native code.
 * 
 * @author Neonunux
 */
public class NativeUtils {
	private static File getAppDataWindow() {
		ProcessBuilder builder = new ProcessBuilder(new String[] { "cmd",
				"/C echo %APPDATA%" });

		BufferedReader br = null;
		try {
			Process start = builder.start();
			br = new BufferedReader(new InputStreamReader(
					start.getInputStream()));
			String path = br.readLine();
			// TODO HACK do not know why but I get an extra '"' at the end
			if (path.endsWith("\"")) {
				path = path.substring(0, path.length() - 1);
			}
			return new File(path.trim());
		} catch (IOException ex) {
			System.out.printf("Cannot get Application Data Folder", ex);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex) {
					System.out.printf(null, ex);
				}
			}
		}
		return null;
	}

	public void getTempDir() {
		try {
			// create a temp file
			File temp = File.createTempFile("temp-file-name", ".tmp");

			System.out.println("Temp file : " + temp.getAbsolutePath());

			// Get tempropary file path
			String absolutePath = temp.getAbsolutePath();
			String tempFilePath = absolutePath.substring(0,
					absolutePath.lastIndexOf(File.separator));

			System.out.println("Temp file path : " + tempFilePath);

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
	public String getSeparator() {
		/*
		 * // peut-être écrasé par setProperty
		logger.debug("separator : " + System.getProperty("file.separator")); 
		
		// meilleur mais dépend de Java 7
		logger.debug("separator : " + FileSystems.getDefault().getSeparator());
		
		  // le plus safe
		logger.debug("separator : " + File.separator); //win 8 verif
		*/
		return File.separator;
	}

	public void getAppData() {
		/*
		 * linux XDG_DATA_HOME $HOME/.local/share/<appname>/ for user-data
		 * (saves, progress, player profile) XDG_CONFIG_HOME
		 * $HOME/.config/<appname>/ for configuration XDG_CACHE_DIR
		 * ($HOME/.cache/<appname>) for non-essential/temporary files
		 * 
		 * win XP, 2003 %USER%\Local Settings\Application Data %USER%\Local
		 * Settings Win 7,8 %USER\AppData\Roaming
		 * 
		 * 
		 * mac : ~/Library/Application Support<application name>/ This kind of
		 * data is frequently stored in . ~/Library/Preferences/<application
		 * name>/ User-specific settings are frequently stored in
		 */

		logger.debug("USER HOME : " + System.getProperty("user.home"));
		logger.debug("LOCALAPPDATA " + System.getProperty("LOCALAPPDATA"));
		logger.debug("TEMP " + System.getProperty("temp.dir"));
		logger.debug("TEMP2 " + System.getProperty("java.io.tmpdir"));
		

		System.out.println("other method : " + getAppDataWindow());

	}

	private static final Logger logger = LogManager.getLogger(NativeUtils.class
			.getName());

	/**
	 * Load a named library from a directory.<br>
	 * Note: Loading of a JNI library should always be done in the corresponding
	 * Java class or otherwise native methods may result in
	 * {@link UnsatisfiedLinkError}s if different {@link ClassLoader}s are
	 * involved.
	 *
	 * @param directory
	 *            directory the library is located in
	 * @param name
	 *            name of library
	 * @throws UnsatisfiedLinkError
	 *             the unsatisfied link error
	 */
	public static void load(File directory, String name)
			throws UnsatisfiedLinkError {
		load(new File(directory, System.mapLibraryName(name)));
	}

	/**
	 * Load a library from a file.
	 *
	 * @param file
	 *            the library file
	 * @throws UnsatisfiedLinkError
	 *             the unsatisfied link error
	 */
	public static void load(File file) throws UnsatisfiedLinkError {
		try {
			System.load(file.getCanonicalPath());
			logger.info(file.getCanonicalPath() + " loaded");
		} catch (IOException ex) {
			UnsatisfiedLinkError error = new UnsatisfiedLinkError();
			error.initCause(ex);
			throw error;
		}
	}

	/**
	 * Checks if is windows.
	 *
	 * @return true, if is windows
	 */
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}

	/**
	 * Checks if is mac.
	 *
	 * @return true, if is mac
	 */
	public static boolean isMac() {
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}

	/**
	 * Checks if is linux.
	 *
	 * @return true, if is linux
	 */
	public static boolean isLinux() {
		String os = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}

}