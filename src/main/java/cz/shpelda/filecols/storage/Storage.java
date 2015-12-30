package cz.shpelda.filecols.storage;


import java.io.IOException;

/**
 * An Interface defining off-memory storage facility.
 * Storage only guarantees that 
 * <li> For each piece of data an unique address will be allocated using {@link #store(byte[])}.
 * <li> Data will be accessible under that address using {@link #load(long)}
 * <li> Value stored using {@link #storeReserved(long)} will be accessible using  {@link #readReserved()}.
 * <br>
 * <br>
 * Address is an positive long value. Using negative value there is considered error and leads to undefined behavior.
 */
public interface Storage {
	/**
	 * Stores an address value into reserved location, so that 
	 * it's possible to store some metadata
	 * @param  address Address is a long number that was acquired by previous call to {@link #store(byte[])}.
	 * Passing other long values is considered error and leads to undefined behavior.
	 * @throws IOException in case that there is problem with 
	 * underlying storage mechanism
	 */
	public void storeReserved(long address) throws IOException;
	
	/**
	 * Reads address from reserved location.
	 * @return Address that was stored in reserved location, or -1 if nothing was stored there.
 	 * @throws IOException in case that there is problem with 
	 * underlying storage mechanism
	 */
	public long readReserved() throws IOException;
	
	/**
	 * Store the data somewhere, returning an unique 
	 * address under which this data are accessible.
	 * Handling of null values is implementation specific.
	 * @param data data to store
	 * @return an address under which this data are accessible for load or deletion
	 * @throws IOException in case that there is problem with 
	 * underlying storage mechanism
	 */
	public long store(byte[] data) throws IOException;
	
	/**
	 * Loads data from given address
	 * Address is a long number that was acquired by previous call to {@link #store(byte[])}.
	 * Passing other long values is considered error and leads to undefined behavior.
	 * @param address Address to load data from
	 * @return data from that address.
	 */
	public byte[] load(long address) throws IOException;
	
	/**
	 * Removes data from given address.
	 * @param address Address to delete from storage. 
	 * Address is a long number that was acquired by previous call to {@link #store(byte[])}.
	 * Passing other long values is considered error and leads to undefined behavior.
	 * @throws IOException in case that there is problem with 
	 * underlying storage mechanism
	 */
	public void delete(long address) throws IOException;
	
	/**
	 * Closes this storage and release any related resources.
	 * @throws IOException in case that there is problem with 
	 * underlying storage mechanism
	 */
    public void close() throws IOException;
	
}
