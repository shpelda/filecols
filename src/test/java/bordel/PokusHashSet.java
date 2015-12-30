package bordel;

import java.util.HashSet;

public class PokusHashSet {
	
	public static void main(String[] rags){
		 HashSet<String> x = new HashSet<String>();
		 long before = System.currentTimeMillis();
		for(int i=0; i<100000;i++){
			try{
				x.add("DLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOUUUHEJ STRING"+i);
			}catch(Error e){
				System.err.println(i);
				throw e;
			}
		}
		System.err.println("duration:"+(System.currentTimeMillis()-before));
	}
}
