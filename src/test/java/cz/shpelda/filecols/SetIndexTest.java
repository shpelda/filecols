package cz.shpelda.filecols;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cz.shpelda.filecols.TestUtils.StorageFactory;  

@RunWith(Parameterized.class)
public class SetIndexTest extends BaseIndexTest{
	
	public SetIndexTest(StorageFactory sFactory) {
		super(sFactory);
	}

	@Override
	public Collection<String> createEmptyIndex() throws Exception {
		return new SetIndex<String>(storage);
	}
	 	
}
