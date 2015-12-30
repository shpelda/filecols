package cz.shpelda.filecols;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * To be flexible enough to store any object, including those that are not serializable,
 * user can provide an Serializer instance.
 */
public interface Serializer<E> {
	public byte[] serialize(E object) throws Exception;
	public E deserialize(InputStream in) throws Exception;
	/**
	 * Collection implementations need some handle to the object.
	 * Default implementation uses hashcode of serialized object as this handle.
	 * That is safe, but has some performance costs. If you are able to calculate reasonable
	 * hash of stored object, do so.
	 * hash and equals methods must follow their contracts from {@link Object#equals(Object)} and {@link Object#hashCode()}
	 * 
	 * Custom hash is mandatory if you want to mutate stored object. The same for equals method.
	 * 
	 * hash and equals might return null to indicate that default behavior should be used.
	 * 
	 * note that Object#hashCode() is not reasonable, as it can differ after serialization/deserialization.
	 */
	public Integer hash(E object);
	
	/**
	 * This must follow general equals/hashcode contract {@link Object#equals(Object)}
	 */
	public Boolean equals(E x, E y);
	
	public final static Serializer<Object> defaultSerializer = new Serializer<Object>() {

		@Override
		public byte[] serialize(Object e) throws Exception{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(bout);
			oout.writeObject(e);
			oout.close();
			return bout.toByteArray();
		}

		
		/**Load an object from a stream. Note, that you have to be able to detect end of the object -
		 * Data can contain random stuff after object ending.
		 */
		@Override
		public Object deserialize(InputStream data)  throws Exception{
			return new ObjectInputStream(data).readObject();
		}

		@Override
		public Integer hash(Object object) {
			return null;
		}
		
		public Boolean equals(Object x, Object y){
			return null;
		}
		
	};
	
}
