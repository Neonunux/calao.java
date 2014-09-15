package calao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class AudioInputControllerTest {
	@Mock
	private Preferences appPrefs;
//
//	@Mock
//	private Vector<String> audioDevList;
//
//	@Mock
//	private AudioMonitor audioMon;
//
//	@Mock
//	private int bufferSize;
//
//	@Mock
//	private boolean captureStarted;
//
//	@Mock
//	private int currentVolume;
//
//	@Mock
//	private Vector<Double> freqList;
//
//	@Mock
//	private boolean infoEnabled;
//
//	@Mock
//	private AudioFormat inputFormat;
//
//	@Mock
//	private TargetDataLine inputLine;
//
//	@Mock
//	private long latency;
//
//	@Mock
//	private Preferences p;
//
//	@Mock
//	private Device paInputDev;
//
//	@Mock
//	private Stream paStream;
//
//	@Mock
//	private int previousVolume;
//
//	@Mock
//	private float sampleRate;
//
//	@Mock
//	private int sampleSizeInBits;
//
//	@Mock
//	private int sensitivity;
//	@InjectMocks
//	private AudioInputController audioInputController;

	@Test
	public void testAudioInputController() throws Exception {
		return;
	}

	@Test
	public void testInitializeWithNoFile() throws Exception {
		Mockito.when(appPrefs.getProperty("inputDevice")).thenReturn("-1");
		
		AudioInputController aic = new AudioInputController(appPrefs);
		
		Assert.assertFalse(aic.initialize());
	}
	@Test
	public void testInitializeWithWrongMidiDevice() throws Exception {
		Mockito.when(appPrefs.getProperty("inputDevice")).thenReturn("MIDI,5");
		AudioInputController aic = new AudioInputController(appPrefs);
		assertThat(aic.initialize()).isFalse();
	}

	@Test
	public void testInitFrequenciesList() throws Exception {
		return;
	}

	@Test
	public void testGetDevicesList() throws Exception {
		return;
	}

	@Test
	public void testEnableInfo() throws Exception {
		return;
	}

	@Test
	public void testFrequencyLookup() throws Exception {
		return;
	}

	@Test
	public void testSetSensitivity() throws Exception {
		return;
	}

	@Test
	public void testStartCapture() throws Exception {
		return;
	}

	@Test
	public void testStopCapture() throws Exception {
		return;
	}

	@Test
	public void testSaveToFile() throws Exception {
		return;
	}

}
