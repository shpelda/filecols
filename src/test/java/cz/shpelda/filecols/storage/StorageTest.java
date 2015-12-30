package cz.shpelda.filecols.storage;

import static org.junit.Assert.assertEquals;


import java.io.File;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cz.shpelda.filecols.TestUtils;
import cz.shpelda.filecols.TestUtils.StorageFactory;
@RunWith(Parameterized.class)
public class StorageTest {
	 StorageFactory sFactory;
	 File testFile;
	 Storage storage;
	  @Parameterized.Parameters
	   public static Collection<?> storageImplementations() throws Exception{
		  return TestUtils.storageImplementations();
	   }
	  
	  
	
	public StorageTest(StorageFactory sFactory) {
		this.sFactory = sFactory;
	
	}
	
	@Before
	public void setUp() throws Exception{
		testFile = File.createTempFile(getClass().getSimpleName(), ".test");
		storage = sFactory.createStorage(testFile);
	}

	@After
	public void tearDown() throws Exception{
		storage.close();
		assert testFile.delete();
	}

	@Test
	public void testStorageStore() throws Exception{
		
		long hello = storage.store("hello this is definitely longer than 12 bytes".getBytes("UTF-8"));
		long world = storage.store("world! this is definitely longer than 12 bytes".getBytes("UTF-8"));
		
		
		assertEquals("hello this is definitely longer than 12 bytes", new String(storage.load(hello), "UTF-8"));
		assertEquals( "world! this is definitely longer than 12 bytes", new String(storage.load(world), "UTF-8"));
		
		long hello2 = storage.store("2hello this is definitely longer than 12 bytes".getBytes("UTF-8"));
		long world2 = storage.store("2world! this is definitely longer than 12 bytes".getBytes("UTF-8"));
		
		assertEquals("2hello this is definitely longer than 12 bytes", new String(storage.load(hello2), "UTF-8"));
		assertEquals( "2world! this is definitely longer than 12 bytes", new String(storage.load(world2), "UTF-8"));
	}
	
	@Test
	public void testStorageRemove() throws Exception{
		
		long hello = storage.store("hello this is definitely longer than 12 bytes".getBytes("UTF-8"));
		long world = storage.store("world! this is definitely longer than 12 bytes".getBytes("UTF-8"));
		
		storage.delete(hello);
		storage.delete(world);
		
		
	}
	
}
