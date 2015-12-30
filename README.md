# filecols
Simple java collections persistence

Filecols allows you to trivially persist any kind of serialiazable data, and access them
using java collections API. Typical usage is a process-bound cache.
This is plain old java library, not being bound to any kind of framework, giving you freedom
of choice. 

## Building
Assuming you have [graddle] installed, just run 

  graddle build

## Usage
Currently only a PoC implementation is available providing java.util.List and java.util.Set indexes on
RandomAccessFile. So it is as easy as

 * java.util.Set mySet = new SetIndex(new RAFFirstFitStorage(new File("data.bin"))); 
 * java.util.List myList = new ListIndex(new RAFFirstFitStorage(new File("data.bin")));

Current implementation is not thread safe, provides no transactional support, and even file writes are not (explicitly)atomic. So if IO fails for some reason, persisted data will likely get corrupt. Futhermore, implementation persist just content of the collections. Not control structures. So it is currently not possible to rebuild indexes based on existing file.  

[//]:*
   [graddle]: http://gradle.org/ 

