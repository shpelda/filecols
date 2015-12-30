package cz.shpelda.filecols.storage;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RAFFirstFitStorageTest {
	RAFFirstFitStorage storage;
	File testFile;
	@Before
	public void setUp() throws Exception{
		testFile = File.createTempFile(getClass().getSimpleName(), ".test");
		storage = new RAFFirstFitStorage(testFile);
	}
	@After
	public void tearDown() throws Exception{
		storage.close();
		assert testFile.delete();
	}
	
	
	@Test
	public void testEmptySpaceHandling() throws Exception{
		
		long hello = storage.store("hello this is definitely longer than 12 bytes".getBytes("UTF-8"));
		long world = storage.store("world! this is definitely longer than 12 bytes".getBytes("UTF-8"));
		
		storage.delete(hello);
		storage.delete(world);
		
		
		long world2 = storage.store("world! this is definitely longer than 12 bytes".getBytes("UTF-8"));
		long hello2 = storage.store("hello this is definitely longer than 12 bytes".getBytes("UTF-8"));
		
		
		assertTrue(hello == hello2);
		assertTrue(world == world2);
		
	}
	

}
