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

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Collection of utility methods for native code.
 * @author Neonunux
 */
public class NativeUtils {
	
	
	private static final Logger logger =  LogManager.getLogger(NativeUtils.class.getName());

    /**
     * Load a named library from a directory.<br>
     * Note: Loading of a JNI library should always be done in the corresponding
     * Java class or otherwise native methods may result in
     * {@link UnsatisfiedLinkError}s if different {@link ClassLoader}s are
     * involved.
     *
     * @param directory            directory the library is located in
     * @param name            name of library
     * @throws UnsatisfiedLinkError the unsatisfied link error
     */
    public static void load(File directory, String name)
                    throws UnsatisfiedLinkError {
            load(new File(directory, System.mapLibraryName(name)));
    }

    /**
     * Load a library from a file.
     *
     * @param file            the library file
     * @throws UnsatisfiedLinkError the unsatisfied link error
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