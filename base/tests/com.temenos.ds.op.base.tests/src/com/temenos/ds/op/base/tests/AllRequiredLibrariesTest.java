package com.temenos.ds.op.base.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test which simply ensure that some classic non-OSGi (Orbit) libraries are
 * available to us in DS NG incl. during build.
 */
public class AllRequiredLibrariesTest {

	@Test
	public void testSLF4j() {
		Logger logger = LoggerFactory.getLogger(AllRequiredLibrariesTest.class);
		logger.info("Yo");
	}

	@Test
	public void testMockito() {
		Logger logger = org.mockito.Mockito.mock(Logger.class);
	}

}
