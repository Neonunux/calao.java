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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.*;

/**
* @author Neonunux
*/
@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class AccidentalsTest {
//	@Mock
//	private Preferences appPrefs;
//
//	@Mock
//	private int count;
// 
//	@Mock
//	private Preferences p;
//
//	@Mock
//	private String t;
//	@InjectMocks
//	private Accidentals accidentals;

	@Test
	public void testAccidentals() throws Exception {
		return;
	}

	@Test
	public void testSetTypeAndCount() throws Exception {
		return;
	}

	@Test
	public void testGetNumber() throws Exception {
		return;
	}

	@Test
	public void testGetType() throws Exception {
		return;
	}

	@Test
	public void testGetTonality() throws Exception {
		return;
	}

	@Test
	public void testGetClefOffset() throws Exception {
		Preferences appPrefs = new Preferences();
		Accidentals acc = new Accidentals("b", 1, appPrefs);
		
		assertThat(acc.getClefOffset(appPrefs.CLEF_F4)).isEqualTo(10);
		assertThat(acc.getClefOffset(appPrefs.CLEF_G2)).isEqualTo(0);
		assertThat(acc.getClefOffset(appPrefs.CLEF_C3)).isEqualTo(5);
		assertThat(acc.getClefOffset(appPrefs.CLEF_C4)).isEqualTo(-5);
	}

	@Test
	public void testGetXYAlterations() throws Exception {
		Preferences appPrefs = new Preferences();
		Accidentals acc = new Accidentals("b", 7, appPrefs);
		
		assertThat(acc.getXYAlterations(appPrefs.CLEF_F4))
		.containsExactly(0, 5, 9, -10, 18, 10, 27, -5, 36, 15, 45, 0, 54, 20);
		assertThat(acc.getXYAlterations(appPrefs.CLEF_C4))
		.containsExactly(0, 5, 9, -10, 18, 10, 27, -5, 36, 15, 45, 0, 54, 20);
		assertThat(acc.getXYAlterations(appPrefs.CLEF_C3))
		.containsExactly(0, 5, 9, -10, 18, 10, 27, -5, 36, 15, 45, 0, 54, 20);
		assertThat(acc.getXYAlterations(appPrefs.CLEF_G2))
		.containsExactly(0, 5, 9, -10, 18, 10, 27, -5, 36, 15, 45, 0, 54, 20);
	}	
	@Test
	public void testGetXYSharpAlterations() throws Exception {
		Preferences appPrefs = new Preferences();
		Accidentals acc = new Accidentals("#", 7, appPrefs);

		assertThat(acc.getXYAlterations(appPrefs.CLEF_F4))
		.containsExactly(0, -15, 10, 0, 20, -20, 30, -5, 40, 10, 50, -10, 60, 5);
		assertThat(acc.getXYAlterations(appPrefs.CLEF_C4))
		.containsExactly(0, -15, 10, 0, 20, 15, 30, -5, 40, 10, 50, 25, 60, 5);
		assertThat(acc.getXYAlterations(appPrefs.CLEF_C3))
		.containsExactly(0, -15, 10, 0, 20, -20, 30, -5, 40, 10, 50, -10, 60, 5);
		assertThat(acc.getXYAlterations(appPrefs.CLEF_G2))
		.containsExactly(0, -15, 10, 0, 20, -20, 30, -5, 40, 10, 50, -10, 60, 5);
	}
}