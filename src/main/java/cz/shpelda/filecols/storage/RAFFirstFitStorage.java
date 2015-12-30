package cz.shpelda.filecols.storage;

import static java.lang.Math.abs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * A facility that stores data in a random access file.
 *  File consists of tuples [size, data].
 *  * If size is negative, it means that there are <size> empty bytes.
 *    First eight of those empty bytes indicate location of next empty space.
 *  * If location is negative, we're at the current EOF.
 *  * First tuple in a file holds address of first available empty space.
 */
public class RAFFirstFitStorage implements Storage{
	
	private static final byte sizeofInt=4;
	private static final byte sizeofLong=8;
	
	private static final byte reserved = 13;
	
	private RandomAccessFile file;
	
	public RAFFirstFitStorage(File file) throws IOException{
		this.file = new RandomAccessFile(file, "rw");
		if(this.file.length() == 0){//new file, initialize
			storeReserved(-1);
			writeSize(0, -sizeofLong);
			this.file.writeLong(-1);
		}
	}
	
	/**
	 * Stores an address value into reserved location, so that 
	 * it's possible to store some metadata
	 */
	public void storeReserved(long address) throws IOException{
		writeSize(reserved, sizeofLong);
		file.writeLong(address);
	}
	
	/**
	 * Reads address from reserved location
	 */
	public long readReserved() throws IOException{
		int size = readSize(reserved);
		assert size == sizeofLong;
		return file.readLong();
	}
	

	/**
	 * Store the data somewhere, returning an unique 
	 * address under which this data are accessible 
	 * @param data
	 * @return
	 * TODO: extract empty space handling strategy?
	 */
	public long store(byte[] data) throws IOException{
		//find first-fit space and store data there
		long prevSpace=0;
		long address;
		int available=0;
		for(address=readNextEmptyAdress(0);address>=0;address=readNextEmptyAdress(address)){
			available = abs(readSize(address));
			if(available < data.length){
				prevSpace = address;
				continue;
			}
			break;//this is our spot.
		}
		
		long nextSpace = -1;
		if(address <0){//appending at eof
			this.file.seek(this.file.length());
			address = this.file.getFilePointer();
			available = -1;
		}else{
			nextSpace = this.file.readLong();
		}
		
		
		
		int remainder = available - data.length;
		long newSpace = nextSpace;
		//if remaining space is big enough to be useful,
		//link it, otherwise leave it as a trash stored after the data
		//(but mentioned in record size -> no fragmentation.)
		if(available<0){//eof
			writeSize(address, data.length);
			this.file.write(data);
		}else if(remainder > data.length/2 + sizeofLong+sizeofInt){//big enugh fit
			writeSize(address, data.length);
			this.file.write(data);
			
			newSpace = this.file.getFilePointer();
			this.file.write(-1 * remainder);
			this.file.writeLong(nextSpace);
		}else{//not big enough fit
			writeSize(address, available);
			this.file.write(data);
		}

		//join our linkedlist again
		readSize(prevSpace);
		this.file.writeLong(newSpace);

		return address;
	}
	
	/**
	 * load data identified by the address
	 * @param address
	 * @return
	 */
	public byte[] load(long address) throws IOException{
		byte[] result = new byte[readSize(address)];
		file.read(result);
		return result;
	}
	
	/**
	 * remove data identified by the address
	 * @param address
	 */
	public void delete(long address) throws IOException{
		int deletedSize = readSize(address);
		writeSize(address, -1* deletedSize);
		long lastfree=0;
		
		for(long x=readNextEmptyAdress(0);x>=0;x=readNextEmptyAdress(x)){
			lastfree = x;
		}
		int ateof = readSize(lastfree);
		assert ateof <0;
		file.writeLong(address);
		readSize(address);
		file.writeLong(-1);
	}
	
	protected void markEmpty(long address) throws IOException{
		if(address == 0){
			writeSize(0, -sizeofLong);
			file.writeLong(-1);
		}
		writeSize(address, readSize(address) * -1);//mark it empty
	}
	
	
	protected long readNextEmptyAdress(long address) throws IOException{
		int size = readSize(address);
		assert size < 0;
		return file.readLong();
	}
	
	protected int readSize(long address) throws IOException{
		file.seek(address);
		return file.readInt();
	}

	protected void writeSize(long address, int size) throws IOException{
		file.seek(address);
		file.writeInt(size);
	}
	
	
	public void close() throws IOException{
		file.close();
	}
}
