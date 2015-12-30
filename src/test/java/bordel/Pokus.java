package bordel;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import cz.shpelda.filecols.SetIndex;
import cz.shpelda.filecols.storage.RAFFirstFitStorage;

public class Pokus {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		File x = File.createTempFile("storage", "bin");
		x.createNewFile();
		
		RAFFirstFitStorage storage = new RAFFirstFitStorage(x);
		try{
			Set<String> idx = new SetIndex<String>(storage);
//			idx.add("hello");
//			idx.add("world!");
			long before = System.currentTimeMillis();
			for(int i=0; i<100000 ;i++){
				try{
					idx.add("DLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOUUUHEJ STRING"+i);
				}catch(Error e){
					System.err.println(i);
					throw e;
				}
			}
			System.err.println("duration:"+(System.currentTimeMillis()-before));
			before = System.currentTimeMillis();
			System.err.println(idx.contains("DLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOUUUHEJ STRING"+512));
			System.err.println("duration:"+(System.currentTimeMillis()-before));

			int countIt=0;
			for(String z : idx){
				countIt++;
			}
			assert 100000==countIt;
		}finally{
			storage.close();
		}
	}

}
