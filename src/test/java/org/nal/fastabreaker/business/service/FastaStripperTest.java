package org.nal.fastabreaker.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FastaStripperTest {

	@Before
	public void before() {

	}

	@Test
	public void test1() {
		assertTrue(true);
	}

	@Test
	public void testStrip() {
		final File fastaFile = new File(this.getClass().getResource("/fastafiles/gisaidfile.fasta").getFile());

		assertNotNull(fastaFile);
		assertTrue(fastaFile.exists());

		final List<FastaFile> files = FastaStripper.strip(fastaFile);
		assertNotNull(files);
		assertFalse(files.isEmpty());
		assertEquals(4, files.size());
		assertEquals("EPI_ISL_514135", files.get(0).getFileName());
		assertEquals(29970, files.get(0).getContent().toCharArray().length, 0);

	}

	@After
	public void after() {

	}

}
