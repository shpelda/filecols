package cz.shpelda.filecols;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Iterator;

import cz.shpelda.filecols.storage.Storage;

/**
 * Package local utilities.
 */
public class Utils {
	
	
	public static class IteratorAdapter<X> implements Iterator<X>{
		@Override
		public boolean hasNext() {return false;}
		@Override
		public X next() {return null;}
		@Override
		public void remove() {throw new UnsupportedOperationException();}
	}
	

	public static <E> byte[] serialize(Serializer<E> serializer, E e){
		try{
			return serializer.serialize(e);
		}catch(Exception ex){
			throw new RuntimeIoException(ex);
		}
	}
	
	public static  <E> E deserialize(Serializer<E> serializer, byte[]  data){
		try{
			return serializer.deserialize(new ByteArrayInputStream(data));
		}catch(Exception ex){
			throw new RuntimeIoException(ex);
		}
	}
	
	public static  byte[]  load(Storage storage, long address){
		try{
			return storage.load(address);
		}catch(Exception ex){
			throw new RuntimeIoException(ex);
		}
	}
	
	public static  long  store(Storage storage, byte[] data){
		try{
			return storage.store(data);
		}catch(Exception ex){
			throw new RuntimeIoException(ex);
		}
	}
	
	public static  void  delete(Storage storage, long address){
		try{
			storage.delete(address);
		}catch(Exception ex){
			throw new RuntimeIoException(ex);
		}
	}

	//if someone is going to use heterogenous collection,
	//and provides a serializer that is able to handle it, we're fine.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int hash(Serializer serializer, Object e){
		Serializer rawType = serializer;
		Integer hash=rawType.hash(e);
		if(hash!=null)
			return hash;
		return Arrays.hashCode(serialize(rawType, e));		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean equals(Serializer serializer, Object x, Object y){
		Boolean r = serializer.equals(x, y);
		if(r!=null)
			return r;
		return Arrays.equals(serialize(serializer, x), serialize(serializer,y));
	}
}
