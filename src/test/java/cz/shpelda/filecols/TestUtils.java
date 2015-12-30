package cz.shpelda.filecols;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import cz.shpelda.filecols.storage.RAFFirstFitStorage;
import cz.shpelda.filecols.storage.Storage;

public class TestUtils {
		public interface StorageFactory{
			public Storage createStorage(File f) throws Exception;
		}
		
	
	   public static Collection<?> storageImplementations() throws Exception{
	      return Arrays.asList(new Object[][]{ {
	         new StorageFactory() {
				
				@Override
				public Storage createStorage(File f) throws Exception {
					return new RAFFirstFitStorage(f);
					
				}
			} ,
	      }});
	   }
}
