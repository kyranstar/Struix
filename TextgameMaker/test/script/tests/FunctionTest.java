package script.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import script.GrammarTest;

public class FunctionTest extends GrammarTest{


	@Test
	public void testPrint() throws IOException {
		assertEquals(expected(), got());
	}

	@Override
	public File getInputFile() {
		return new File(FILE_PREFIX + "function.in");
	}

	@Override
	public File getOutputFile() {
		return new File(FILE_PREFIX + "function.out");
	}

}
