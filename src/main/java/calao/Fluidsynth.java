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
import java.nio.ByteBuffer;
import java.util.List;

import javax.sound.midi.ShortMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class Fluidsynth.
 *
 * @author Neonunux
 */
public class Fluidsynth {
	
	
	private static final Logger logger =  LogManager.getLogger(Fluidsynth.class.getName());

	/** The Constant NAME_MAX_LENGTH. */
	private static final int NAME_MAX_LENGTH = 32;
	
	/** The context. */
	private ByteBuffer context;

	/**
	 * Instantiates a new fluidsynth.
	 *
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Fluidsynth() throws IllegalStateException, IOException {
		this("", 16, null);
	}

	/**
	 * Instantiates a new fluidsynth.
	 *
	 * @param name the name
	 * @param channels the channels
	 * @param audioDriver the audio driver
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Fluidsynth(String name, int channels, String audioDriver)
			throws IllegalStateException, IOException {
		this(name, 1, channels, 256, 44100.0f, audioDriver, null, -1, 8, 512, 0.5f,
				0.5f, 0.5f, 0.5f, 0.5f);
	}

	/**
	 * Instantiates a new fluidsynth.
	 *
	 * @param name the name
	 * @param cores the cores
	 * @param channels the channels
	 * @param polyphony the polyphony
	 * @param sampleRate the sample rate
	 * @param audioDriver the audio driver
	 * @param audioDevice the audio device
	 * @param deviceIndex the device index
	 * @param buffers the buffers
	 * @param bufferSize the buffer size
	 * @param overflowAge the overflow age
	 * @param overflowPercussion the overflow percussion
	 * @param overflowReleased the overflow released
	 * @param overflowSustained the overflow sustained
	 * @param overflowVolume the overflow volume
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Fluidsynth(String name, int cores, int channels, int polyphony,
			float sampleRate, String audioDriver, String audioDevice, int deviceIndex,
			int buffers, int bufferSize, float overflowAge,
			float overflowPercussion, float overflowReleased,
			float overflowSustained, float overflowVolume) throws IOException {

		name = name.substring(0, Math.min(name.length(), NAME_MAX_LENGTH));

		context = init(name, cores, channels, polyphony, sampleRate,
				audioDriver, audioDevice, deviceIndex, buffers, bufferSize, 
				overflowAge, overflowPercussion, overflowReleased, 
				overflowSustained, overflowVolume);
	}

	/**
	 * Sound font load.
	 *
	 * @param soundfont the soundfont
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void soundFontLoad(File soundfont) throws IOException {
		soundFontLoad(context, soundfont.getAbsolutePath());
	}
	
	/**
	 * Gets the soundfont programs.
	 *
	 * @return the soundfont programs
	 */
	public List<String> getSoundfontPrograms()
	{
		return getProgramsList(context);
	}

	/**
	 * Sets the gain.
	 *
	 * @param gain the new gain
	 */
	public void setGain(float gain) {
		setGain(context, gain);
	}

	/**
	 * Sets the interpolate.
	 *
	 * @param number the new interpolate
	 */
	public void setInterpolate(int number) {
		setInterpolate(context, number);
	}

	/**
	 * Sets the reverb on.
	 *
	 * @param b the new reverb on
	 */
	public void setReverbOn(boolean b) {
		setReverbOn(context, b);
	}

	/**
	 * Sets the reverb.
	 *
	 * @param roomsize the roomsize
	 * @param damping the damping
	 * @param width the width
	 * @param level the level
	 */
	public void setReverb(double roomsize, double damping, double width,
			double level) {
		setReverb(context, roomsize, damping, width, level);
	}

	/**
	 * Sets the chorus on.
	 *
	 * @param b the new chorus on
	 */
	public void setChorusOn(boolean b) {
		setChorusOn(context, b);
	}

	/**
	 * Sets the chorus.
	 *
	 * @param nr the nr
	 * @param level the level
	 * @param speed the speed
	 * @param depth_ms the depth_ms
	 * @param type the type
	 */
	public void setChorus(int nr, double level, double speed, double depth_ms,
			int type) {
		setChorus(context, nr, level, speed, depth_ms, type);
	}

	/**
	 * Sets the tuning.
	 *
	 * @param tuningBank the tuning bank
	 * @param tuningProgram the tuning program
	 * @param name the name
	 * @param derivations the derivations
	 */
	public void setTuning(int tuningBank, int tuningProgram, String name,
			double[] derivations) {
		if (derivations == null || derivations.length != 12) {
			throw new IllegalArgumentException();
		}
		setTuning(context, tuningBank, tuningProgram, name, derivations);
	}

	/**
	 * Send.
	 *
	 * @param channel the channel
	 * @param command the command
	 * @param data1 the data1
	 * @param data2 the data2
	 */
	public void send(int channel, int command, int data1, int data2) {
		switch (command) {
		case ShortMessage.NOTE_ON:
			noteOn(context, channel, data1, data2);
			break;
		case ShortMessage.NOTE_OFF:
			noteOff(context, channel, data1);
			break;
		case ShortMessage.PROGRAM_CHANGE:
			programChange(context, channel, data1);
			break;
		case ShortMessage.CONTROL_CHANGE:
			controlChange(context, channel, data1, data2);
			break;
		case ShortMessage.PITCH_BEND:
			pitchBend(context, channel, (data2 * 128) + data1);
			break;
		}
	}

	/**
	 * Destroy.
	 */
	public void destroy() {
		destroy(context);
		context = null;
	}

	/**
	 * Inits the.
	 *
	 * @param name the name
	 * @param cores the cores
	 * @param channels the channels
	 * @param polyphony the polyphony
	 * @param sampleRate the sample rate
	 * @param audioDriver the audio driver
	 * @param audioDevice the audio device
	 * @param deviceIndex the device index
	 * @param buffers the buffers
	 * @param bufferSize the buffer size
	 * @param overflowAge the overflow age
	 * @param overflowPercussion the overflow percussion
	 * @param overflowReleased the overflow released
	 * @param overflowSustained the overflow sustained
	 * @param overflowVolume the overflow volume
	 * @return the byte buffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static native ByteBuffer init(String name, int cores, int channels,
			int polyphony, float sampleRate, String audioDriver, String audioDevice, 
			int deviceIndex, int buffers, int bufferSize, float overflowAge,
			float overflowPercussion, float overflowReleased,
			float overflowSustained, float overflowVolume) throws IOException;

	/**
	 * Destroy.
	 *
	 * @param context the context
	 */
	private static native void destroy(ByteBuffer context);

	/**
	 * Sound font load.
	 *
	 * @param context the context
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private native void soundFontLoad(ByteBuffer context, String filename)
			throws IOException;
	
	/**
	 * Gets the programs list.
	 *
	 * @param context the context
	 * @return the programs list
	 */
	private static native List<String> getProgramsList(ByteBuffer context);

	/**
	 * Note on.
	 *
	 * @param context the context
	 * @param channel the channel
	 * @param key the key
	 * @param velocity the velocity
	 */
	private static native void noteOn(ByteBuffer context, int channel, int key,	int velocity);

	/**
	 * Note off.
	 *
	 * @param context the context
	 * @param channel the channel
	 * @param key the key
	 */
	private static native void noteOff(ByteBuffer context, int channel, int key);

	/**
	 * Control change.
	 *
	 * @param context the context
	 * @param channel the channel
	 * @param controller the controller
	 * @param value the value
	 */
	private static native void controlChange(ByteBuffer context, int channel, int controller, int value);

	/**
	 * Pitch bend.
	 *
	 * @param context the context
	 * @param channel the channel
	 * @param bend the bend
	 */
	private static native void pitchBend(ByteBuffer context, int channel, int bend);

	/**
	 * Program change.
	 *
	 * @param context the context
	 * @param channel the channel
	 * @param program the program
	 */
	private static native void programChange(ByteBuffer context, int channel, int program);

	/**
	 * Sets the gain.
	 *
	 * @param context the context
	 * @param gain the gain
	 */
	private static native void setGain(ByteBuffer context, float gain);

	/**
	 * Sets the interpolate.
	 *
	 * @param context the context
	 * @param number the number
	 */
	private static native void setInterpolate(ByteBuffer context, int number);

	/**
	 * Sets the reverb on.
	 *
	 * @param context the context
	 * @param b the b
	 */
	private static native void setReverbOn(ByteBuffer context, boolean b);

	/**
	 * Sets the reverb.
	 *
	 * @param context the context
	 * @param roomsize the roomsize
	 * @param damping the damping
	 * @param width the width
	 * @param level the level
	 */
	private static native void setReverb(ByteBuffer context, double roomsize,
			double damping, double width, double level);

	/**
	 * Sets the chorus on.
	 *
	 * @param context the context
	 * @param b the b
	 */
	private static native void setChorusOn(ByteBuffer context, boolean b);

	/**
	 * Sets the chorus.
	 *
	 * @param context the context
	 * @param nr the nr
	 * @param level the level
	 * @param speed the speed
	 * @param depth_ms the depth_ms
	 * @param type the type
	 */
	private static native void setChorus(ByteBuffer context, int nr,
			double level, double speed, double depth_ms, int type);

	/**
	 * Sets the tuning.
	 *
	 * @param context the context
	 * @param tuningBank the tuning bank
	 * @param tuningProgram the tuning program
	 * @param name the name
	 * @param derivations the derivations
	 */
	private static native void setTuning(ByteBuffer context, int tuningBank,
			int tuningProgram, String name, double[] derivations);

	/**
	 * Get the available {@link #getAudioDriver()}s.
	 * 
	 * @return possible options for audio drivers
	 */
	public native static List<String> getAudioDrivers();

	/**
	 * Get the available {@link #getAudioDevice()}s.
	 * 
	 * @param audioDriver
	 *            the audio driver to get possible devices for
	 * @return possible options for audio devices
	 */
	public native static List<String> getAudioDevices(String audioDriver);

	/**
	 * Load the native library "fluidsynth" from the local path .
	 */

	public static void loadLibraries()
	{
		String LIBS_PATH = "libs";
		String WIN32_ARCH_PATH = "win32";
		String WIN64_ARCH_PATH = "win64";
		String LINUX_ARCH_PATH = "linux";
		File directory = null;
		String arch = System.getProperty("sun.arch.data.model");
		logger.debug("Running on " + arch + "bit system");

		if (NativeUtils.isWindows()) {
			if (arch.equals("64"))
				directory = new File(LIBS_PATH + File.separator + WIN64_ARCH_PATH + File.separator);
			else
				directory = new File(LIBS_PATH + File.separator + WIN32_ARCH_PATH + File.separator);
			
			try {
				NativeUtils.load(new File(directory, "libintl-8.dll"));
				NativeUtils.load(new File(directory, "libglib-2.0-0.dll"));
				NativeUtils.load(new File(directory, "libgthread-2.0-0.dll"));
				if (arch.equals("64"))
					NativeUtils.load(new File(directory, "portaudio_x64.dll"));
				else
					NativeUtils.load(new File(directory, "portaudio_x86.dll"));
				NativeUtils.load(new File(directory, "libfluidsynth.dll"));
			} catch (UnsatisfiedLinkError error) {
				logger.debug("Dependencies not provided" + error);
			}
		}

		if (NativeUtils.isMac()) {
			// libraries on mac include their install name, thus we cannot load
			// the dependecies explicitly. Instead we depend on tweaked loader
			// locations, see ./lib/mac/install_name_tool.sh
		}
		
		if (NativeUtils.isLinux()) {
			directory = new File(LIBS_PATH + File.separator + LINUX_ARCH_PATH + File.separator);
		}

		try {
			NativeUtils.load(directory, "fluidsynthJNI");
			//NativeUtils.load(new File(directory, "fluidsynthJNI.dll"));
		} catch (UnsatisfiedLinkError error) {
			logger.error("Fluidsynth error: " + error);
			throw new NoClassDefFoundError();
		}
	}	

}
