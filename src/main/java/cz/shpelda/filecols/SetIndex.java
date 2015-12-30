package cz.shpelda.filecols;

import static cz.shpelda.filecols.Utils.delete;
import static cz.shpelda.filecols.Utils.deserialize;
import static cz.shpelda.filecols.Utils.hash;
import static cz.shpelda.filecols.Utils.load;
import static cz.shpelda.filecols.Utils.serialize;
import static cz.shpelda.filecols.Utils.store;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.shpelda.filecols.Utils.IteratorAdapter;
import cz.shpelda.filecols.storage.Storage;


public class SetIndex<E> extends AbstractSet<E> {
	protected Map<Integer, List<Long>> index = new HashMap<Integer, List<Long>>();
	protected final Storage storage; 
	protected final Serializer<E> serializer;
	
	@SuppressWarnings("unchecked")
	public SetIndex(Storage storage){
		this(storage, (Serializer<E>)Serializer.defaultSerializer);
	}
	
	public SetIndex(Storage storage, Serializer<E> serializer) {
		this.storage = storage;
		this.serializer = serializer;
	}	

	
	@Override
	public Iterator<E> iterator() {
		return new IteratorAdapter<E>(){
			Iterator<Long> addressIt = addressIterator();

			@Override
			public boolean hasNext() {
				return addressIt.hasNext();
			}

			@Override
			public E next() {
				return deserialize(serializer, load(storage, addressIt.next()));
			}
			
		};
	}
	protected Iterator<Long> addressIterator(){
		return new IteratorAdapter<Long>() {
			Iterator<List<Long>> ii = index.values().iterator();
			Iterator<Long> vi = new IteratorAdapter<Long>();//nullobject
			{
				if(ii.hasNext()){
					vi = ii.next().iterator();
				}
			}
			@Override
			public boolean hasNext() {
				return vi.hasNext() || ii.hasNext();
			}

			@Override
			public Long next() {
				if(vi.hasNext()){
					return  vi.next();
				}
				if(ii.hasNext()){
					vi = ii.next().iterator();
					return next();
				}
				throw new AssertionError("an empty placements list in index");
			}

		};
	}
	
	@Override
	public boolean remove(Object o) {
		int hash = hash(serializer, o);
		long adr = seek(o, hash);
		if(adr<0){
			return false;
		}
		delete(storage, adr);
		List<Long> placements = index.get(hash);
		placements.remove(adr);
		if(placements.isEmpty()){
			index.remove(hash);
		}
		return true;
	}
	
	@Override
	 public boolean contains(Object o) {
		return seek(o) > 0;
	 }
	
	protected long seek(Object o){
		return seek(o, hash(serializer,o));
	}
	/**
		find an address of particular object. return -1 on not found
	*/
	protected long seek(Object o,int hash){
	 List<Long> placements =  index.get(hash(serializer,o));
	 if(placements == null){
		 return -1;
	 }
	 for(long p : placements){
		 if(Utils.equals(serializer,deserialize(serializer, load(storage,p)),o)){
			 return p;
		 }
	 }
	 return -1;
	}

	@Override
	public int size() {
		int cnt = 0;
		for(List<?> l:index.values()){
			cnt += l.size();
		}
		return cnt;
	}

	@Override
	public boolean add(E o) {

		int hash = hash(serializer,o);
		long address = seek(o, hash);
		if(address > 0){
			return false;
		}
		List<Long> placements = index.get(hash);
	
		if(placements == null){
			placements = new ArrayList<Long>(2);
			index.put(hash, placements);
		}
		placements.add(store(storage, serialize(serializer,o))); 
			
		return true;
	}
	
}
