package cz.shpelda.filecols;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import cz.shpelda.filecols.TestUtils.StorageFactory;
import cz.shpelda.filecols.storage.Storage;

public abstract class BaseIndexTest {
	 protected StorageFactory sFactory;
	 protected File testFile;
	 protected Storage storage;
	 protected Collection<String> emptyIndex;
	  @Parameterized.Parameters
	   public static Collection<?> storageImplementations() throws Exception{
		  return TestUtils.storageImplementations();
	   }
	  
	  

	
	public BaseIndexTest(StorageFactory sFactory) {
		this.sFactory = sFactory;
	}
	
	public abstract Collection<String> createEmptyIndex() throws Exception;

	@Test
	public void testEmpty() throws Exception{
		assertTrue(emptyIndex.isEmpty());
		//test that it wont crash
		for(@SuppressWarnings("unused") Object x : emptyIndex){
			fail("its empty,huh?");
		}
	}
	@Test
	public void testAddRemove()throws Exception{
		
		assertTrue(emptyIndex.add("hello"));
		assertTrue(emptyIndex.add("world!"));
		
		assertEquals(2, emptyIndex.size());
		
		assertTrue(emptyIndex.remove("world!"));
		assertTrue(emptyIndex.remove("hello"));
		assertTrue(emptyIndex.isEmpty());
		for(@SuppressWarnings("unused") String x : emptyIndex){
			fail("its empty,huh?");
		}
	}
	@Test
	public void testAddRemoveNull()throws Exception{
		assertTrue(emptyIndex.add(null));
		assertTrue(emptyIndex.remove(null));
		assertTrue(emptyIndex.isEmpty());
	}



	@Before
	public void setUp() throws Exception{
		testFile = File.createTempFile(getClass().getSimpleName(), ".test");
		storage = sFactory.createStorage(testFile);
		emptyIndex = createEmptyIndex();
	}

	@After
	public void tearDown() throws Exception{
		storage.close();
		assert testFile.delete();
	}
}
