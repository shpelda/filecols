package cz.shpelda.filecols;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Comparator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cz.shpelda.filecols.TestUtils.StorageFactory;
@RunWith(Parameterized.class)
public class ListIndexTest extends BaseIndexTest {

	public ListIndexTest(StorageFactory sFactory) {
		super(sFactory);
	}
	
	@Override
	public Collection<String> createEmptyIndex() throws Exception {
		return new ListIndex<String>(storage );
	}
	
	@Test
	public void testSort(){
		ListIndex<String> list = new ListIndex<String>(storage);
		list.add("a");
		list.add("b");
		list.add("d");
		list.add("c");
		
		list.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}			
		});
		assertTrue("d".equals(list.get(3)));
	}
	
}
