/**
 * Calao is an educational platform to get started with musical
 * reading and solfege.
 * Copyright (C) 2012-2015 R. Leloup (http://github.com/Neonunux/Calao)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package calao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpab.Callback;
import org.jpab.Device;
import org.jpab.PortAudio;
import org.jpab.PortAudioException;
import org.jpab.Stream;
import org.jpab.StreamConfiguration;
import org.jpab.StreamConfiguration.SampleFormat;

/**
 * The Class AudioInputController.
 *
 * @author Neonunux
 */
@SuppressWarnings("resource")
public class AudioInputController {
	private static final Logger logger = LogManager
			.getLogger(AudioInputController.class.getName());

	Preferences appPrefs;

	/** 
	 * 		Vector holding the generated frequencies lookup table
	 */
	Vector<Double> freqList = new Vector<Double>(); 
	 
	/** 
	 * The audio device list of available device (Java + ASIO) 
	 */
	Vector<String> audioDevList = new Vector<String>(); 
	 
	/** PortAudio variables
	 */
	Device paInputDev = null;

	/** The pa stream. */
	Stream paStream = null;
	// private float[] PortAudioFFTBuffer; // buffer on which FFT is performed.
	// Try to reach 4k
	// private int PortAudioBufferSize = 0; // buffer received from ASIO. Can
	// have any user-defined size
	// private int PortAudioBufferMax = 0; // number of ASIO buffer to
	// accumulate into asioSoundBuffer
	// private int PortAudioBufferCount = 0; // counter of cumulative ASIO
	// buffers

	/** The sample rate. */
	float sampleRate = 44100;

	/** The sample size in bits. */
	int sampleSizeInBits = 16;

	/** The buffer size. */
	int bufferSize = 4096;

	/** The input format. */
	AudioFormat inputFormat;

	/** The input line. */
	TargetDataLine inputLine;

	/** The sensitivity. */
	int sensitivity = 40;

	/** The latency. */
	long latency = 0;

	/** The previous volume. */
	int previousVolume = 0;

	/** The info enabled. */
	boolean infoEnabled = false;

	/** The audio mon. */
	AudioMonitor audioMon;

	/** The current volume. */
	int currentVolume = 0;

	// private AudioCaptureThread captureThread = null;
	/** The capture started. */
	boolean captureStarted = false;

	/**
	 * Instantiates a new audio input controller.
	 *
	 * @param p
	 *            the p
	 */
	public AudioInputController(Preferences p) {
		appPrefs = p;
		initialize();
	}

	/**
	 * Initialize device from preferences file.
	 *
	 * @return true, if successful
	 */
	public boolean initialize() {
		initFrequenciesList();

		String userAudioDev = appPrefs.getProperty("inputDevice");
		if (userAudioDev == "-1" || userAudioDev.split(",")[0].equals("MIDI")) {
			return false;
		}

		int audioDevIndex = Integer.parseInt(userAudioDev.split(",")[1]);

		audioDevList = getDevicesList(audioDevIndex);

		return true;
	}

	/**
	 * Inits the frequencies list.
	 */
	public void initFrequenciesList() {
		double freqFactor = Math.pow(2, 1.0 / 12.0); // calculate the factor
														// between frequencies
		double aFreq = 27.50;
		double currFreq = 16.35; // frequency of C0

		freqList.clear();

		for (int oct = 0; oct < 7; oct++) {
			for (int i = 0; i < 12; i++) // 12 notes. Includes semitones
			{
				if (i == 9) // back on track when encounter an A
				{
					currFreq = aFreq;
					aFreq *= 2;
				}
				freqList.add(currFreq);
				// logger.debug(" | " + currFreq);
				currFreq *= freqFactor;
			}
			// logger.debug("\n");
		}
	}

	/**
	 * Gets the devices list.
	 *
	 * @param devIdx
	 *            the dev idx
	 * @return the devices list
	 */
	public Vector<String> getDevicesList(int devIdx) {
		int tmpIdx = 0;
		Vector<String> devList = new Vector<String>();

		try {
			PortAudio.initialize();
			for (Device device : PortAudio.getDevices()) {
				if (device.getMaxInputChannels() > 0) {
					logger.debug(device);
					devList.add(device.getName());
					if (tmpIdx == devIdx) {
						paInputDev = device;
					}
					tmpIdx++;
				} else {
					logger.debug("No device detected");
				}
			}
		} catch (PortAudioException e) {
			e.printStackTrace();
		}
		;
		return devList;
	}

	/**
	 * Enable info.
	 *
	 * @param am
	 *            the am
	 */
	public void enableInfo(AudioMonitor am) {
		infoEnabled = true;
		audioMon = am;
	}

	/**
	 * Frequency lookup.
	 *
	 * @param freq
	 *            the freq
	 * @return the int
	 */
	public int frequencyLookup(double freq) {
		int startIdx = 0;
		if (freq > freqList.get(freqList.size() / 2)) {
			startIdx = freqList.size() / 2;
		}
		for (int i = startIdx; i < freqList.size(); i++) {
			if (freq < freqList.get(i)) {
				return i + 23;
			}
		}
		return 0;
	}

	/**
	 * Sets the sensitivity.
	 *
	 * @param s
	 *            the new sensitivity
	 */
	public void setSensitivity(int s) {
		logger.debug("Set new sensitivity: " + s);
		this.sensitivity = 100 - s;
	}

	/**
	 * Start capture.
	 */
	public void startCapture() {
		if (captureStarted == true) {
			try {
				paStream.stop();
			} catch (PortAudioException ex) {
			}
			paStream = null;
		}
		StreamConfiguration InputStream = new StreamConfiguration();
		InputStream.setMode(StreamConfiguration.Mode.INPUT_ONLY);
		InputStream.setInputDevice(paInputDev);
		if (sampleSizeInBits == 16) {
			InputStream.setInputFormat(SampleFormat.SIGNED_INTEGER_16);
		} else {
			InputStream.setInputFormat(SampleFormat.SIGNED_INTEGER_8);
		}
		// InputStream.setSampleRate(paInputDev.getDefaultSampleRate());
		InputStream.setSampleRate(sampleRate);
		InputStream.setInputLatency(paInputDev.getDefaultLowInputLatency());
		InputStream.setInputChannels(1);
		try {
			paStream = PortAudio.createStream(InputStream, new Callback() {
				public State callback(ByteBuffer input, ByteBuffer output) {
					logger.debug("Input buffer received ! Size: "
							+ input.capacity());
					performPeakDetection(input); // <------- perform magic here
													// :)
					return State.RUNNING;
				}
			}, new Runnable() {
				public void run() {
					try {
						PortAudio.terminate();
					} catch (PortAudioException ignore) {
						ignore.printStackTrace();
					}
				}
			});
			paStream.start();
			// Thread.sleep(24000);
		} catch (PortAudioException ex) {
		}
		captureStarted = true;
	}

	/**
	 * Stop capture.
	 */
	public void stopCapture() {
		try {
			if (paStream != null) {
				paStream.stop();
			}
		} catch (PortAudioException ex) {
		}
		paStream = null;
		captureStarted = false;
	}

	/**
	 * Save to file.
	 *
	 * @param buf
	 *            the buf
	 */
	public void saveToFile(ByteBuffer buf) {
		File file = new File("audioCap.wav");

		// Set to true if the bytes should be appended to the file;
		// set to false if the bytes should replace current bytes
		// (if the file exists)
		boolean append = true;

		try {
			// Create a writable file channel
			FileChannel wChannel = new FileOutputStream(file, append)
					.getChannel();

			// Write the ByteBuffer contents; the bytes between the ByteBuffer's
			// position and the limit is written to the file
			wChannel.write(buf);

			// Close the file
			wChannel.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Perform peak detection.
	 *
	 * @param tmpBuf
	 *            the tmp buf
	 */
	private void performPeakDetection(ByteBuffer tmpBuf) {
		int bufLength = tmpBuf.capacity();
		// saveToFile(tmpBuf); // Just for debug: this call prevents the FFT to
		// work
		logger.debug("Performing FFT on " + bufLength + " bytes");
		DoubleFFT_1D fft = new DoubleFFT_1D(bufLength);
		double[] audioDataDoubles = new double[bufLength * 2];

		currentVolume = 0;

		if (sampleSizeInBits == 8) {
			for (int i = 0, j = 0; i < bufLength; i++, j += 2) {
				byte tmpByte = tmpBuf.get();
				if (infoEnabled == true && tmpByte > currentVolume) {
					currentVolume = (int) tmpByte;
				}
				if (tmpByte < -5 || tmpByte > 5) {
					audioDataDoubles[j] = (double) tmpByte; // real part
				} else {
					audioDataDoubles[j] = 0;
				}
				audioDataDoubles[j + 1] = 0; // imaginary part
			}
		} else if (sampleSizeInBits == 16) {
			for (int j = 0; j < bufLength; j += 2) // convert audio data to
													// double[] real, imaginary
			{
				byte tmpByteMSB = tmpBuf.get();
				byte tmpByteLSB = tmpBuf.get();
				int sampleInt = tmpByteMSB << 8 + tmpByteLSB;
				if (infoEnabled == true && sampleInt > currentVolume) {
					currentVolume = sampleInt;
				}

				audioDataDoubles[j] = (double) sampleInt; // real part
				audioDataDoubles[j + 1] = 0; // imaginary part
			}
		}

		if (infoEnabled == true) {
			audioMon.showVolume(currentVolume);
		}

		fft.complexForward(audioDataDoubles);

		// calculate vector magnitude and extract highest peak
		double[] magnitude = new double[bufLength];
		double peak = 0;
		int peakIdx = 0;
		for (int j = 0, i = 0; j < bufLength * 2; j += 2, i++) {
			magnitude[i] = Math.sqrt(audioDataDoubles[j] * audioDataDoubles[j]
					+ audioDataDoubles[j + 1] * audioDataDoubles[j + 1]);
			if (magnitude[i] > peak) {
				peak = magnitude[i];
				peakIdx = i;
			}
		}

		double frequency = (sampleRate * peakIdx) / (bufLength * 2);
		if (frequency > 2000) {
			return;
		}
		if (infoEnabled == true) {
			audioMon.showSpectrum(magnitude);
		}
		// logger.debug("[AudioCaptureThread] FFT took " +
		// (System.currentTimeMillis() - time) + "ms");
		logger.debug("[AudioCaptureThread] Peak at: " + frequency
				+ "Hz (value: " + peak + ")");

		if (currentVolume - previousVolume > sensitivity) {
			int pitch = frequencyLookup(frequency);
			if (infoEnabled == true) {
				audioMon.showPitch(pitch);
			}
		}
		previousVolume = currentVolume;
	}
	/*
	 * // ************************** capture thread
	 * 
	 * private class AudioCaptureThread extends Thread { int readBytes = 0;
	 * boolean checkLatency = true;
	 * 
	 * public AudioCaptureThread() {
	 * logger.debug("[AudioCaptureThread] created"); }
	 * 
	 * public void saveToFile(byte buf[]) { FileWriter out = null; try { out =
	 * new FileWriter("audioCap.wav", true); String str = new String(buf); char
	 * cbuf[] = new char[bufferSize]; cbuf = str.toCharArray(); out.write(cbuf);
	 * } catch (IOException e) { } }
	 * 
	 * public void run() { logger.debug("[AudioCaptureThread] started");
	 * while(captureStarted) { if (checkLatency == true) latency =
	 * System.currentTimeMillis(); readBytes = inputLine.read(javaSoundBuffer,
	 * 0, javaSoundBuffer.length); if (checkLatency == true) { latency =
	 * System.currentTimeMillis() - latency;
	 * logger.debug("[AudioCaptureThread] latency = " + latency); checkLatency =
	 * false; }
	 * 
	 * if (readBytes > 0) { //long time = System.currentTimeMillis();
	 * //logger.debug("[AudioCaptureThread] got " + readBytes + " bytes);
	 * //saveToFile(buffer); performPeakDetection(); } } } }
	 */
}
