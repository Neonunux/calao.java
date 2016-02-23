package calao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class VersionTest {

	@Test
	public void testParseAttributesFromVersionStringMinor() {
		Version v = new Version("3.2.1");
		
		assertThat(v.getMajor()).isEqualTo(3);
		assertThat(v.getMinor()).isEqualTo(2);
	}
	
	@Test
	public void testParseAttributesFromVersionStringRelease() {
		Version v = new Version("3.2.1150");

		assertThat(v.getBuild()).isEqualTo(1150);
	}

}
