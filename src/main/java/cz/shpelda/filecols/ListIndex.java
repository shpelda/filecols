package cz.shpelda.filecols;

import static cz.shpelda.filecols.Utils.delete;
import static cz.shpelda.filecols.Utils.deserialize;
import static cz.shpelda.filecols.Utils.load;
import static cz.shpelda.filecols.Utils.serialize;
import static cz.shpelda.filecols.Utils.store;

import java.util.AbstractList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import cz.shpelda.filecols.storage.Storage;
/**
 * An list interface to storage.
 * 
 * <strong>Do not use {@link java.util.Collections#sort(List)} to sort this, use {@link ListIndex#sort(Comparator)} instead.</strong>
 *
 * @param <E>
 */
public class ListIndex<E> extends AbstractList<E>{
	
	protected List<Long> index = new LinkedList<Long>();
	protected final Storage storage; 
	protected final Serializer<E> serializer;
	
	

	public ListIndex(Storage storage, Serializer<E> serializer) {
		this.storage = storage;
		this.serializer = serializer;
	}
	
	@SuppressWarnings("unchecked")
	public ListIndex(Storage storage) {
		this.storage = storage;
		this.serializer = (Serializer<E>) Serializer.defaultSerializer;
	}

	@Override
	public E get(int index) {
		long address = this.index.get(index);
		return loadInternal(address);
	}

	@Override
	public E set(int index, E element) {
		long prev = this.index.set(index, storeInternal(element));
		try{
			return loadInternal(prev);
		}finally{
			delete(storage, prev);
		}
	}

	@Override
	public int size() {
		return index.size();
	}
	
	@Override
	public boolean add(E e){
		return index.add(storeInternal(e));
	}

	@Override
	public boolean remove(Object o) {
		int idx=0;
		boolean have=false;
		for(E e : this){
			if(o == null){
				if(e == null){
					have=true;
					break;
				}
				continue;
			}
			if(o.equals(e)){
				have = true;
				break;
			}
			idx++;
		}
		if(have){
			remove(idx);
		}
		return have;
	}

	@Override
	public E remove(int index) {
		E r = get(index);
		delete(storage, this.index.get(index));
		this.index.remove(index);
		return r;
	}

	protected long storeInternal(E e){
		return store(storage,(serialize(serializer, e)));
	}
	
	protected E loadInternal(long address){
		return  deserialize(serializer, load(storage, address));
	}
	
	
	protected class LazyComparator implements Comparator<Long>{
		protected Comparator<? super E> wrapped;
		
		public LazyComparator(Comparator<? super E> wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public int compare(Long o1, Long o2) {
			E e1 = loadInternal(o1);
			E e2 = loadInternal(o2);
			return wrapped.compare(e1, e2);
		}
	}
	
	public void sort(Comparator<? super E> c){
		 Collections.sort(index, new LazyComparator(c));
	}

}
