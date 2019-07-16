package com.qc.nio.javanio.bytebuffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 
 * 18.10.7 文件加锁
 * 
 * 文件映射很多情况下用于极大的文件。我们可能需要对这种巨大的文件进行部分加锁，
 * 以便其他进程可以修改文件中未被加锁的部分。（数据库就是这样，因此多个用户可以同时访问到它）
 * @author Qc
 *
 */
public class LockingMappedFiles {
	static final int LENGTH = 0x8FFFFFF; //128MB
	static FileChannel fc;
	
	public static void main(String[] args){
		RandomAccessFile raf = null;
		try{
			//初始化文件
			raf = new RandomAccessFile("test.dat", "rw");
			fc = raf.getChannel();
			MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, LENGTH);
			for(int i=0; i<LENGTH; i++){
				out.put((byte)'x');
			}
			//开启两个线程，分别加锁文件的不同部分
			new LockAndModify(out, 0, 0 + LENGTH/3);
			new LockAndModify(out, LENGTH/2, LENGTH/2 + LENGTH/4);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static class LockAndModify extends Thread{
		private ByteBuffer buffer;
		private int start,end;
		LockAndModify(ByteBuffer mb, int start, int end){
			this.start = start;
			this.end = end;
			mb.limit(end);
			mb.position(start);
			buffer = mb.slice();
			start();
		}
		
		public void run(){
			try{
				//加锁 Exclusive lock with no overlap:
				FileLock fl = fc.lock(start, end, false);
				System.out.println("Locked: "+start+" to "+end);
				//修改 Perform modification:
				while (buffer.position()<buffer.limit()-1) {
					buffer.put((byte)(buffer.get()+1));
				}
				fl.release();
				System.out.println("Released: "+start+" to "+end);
			}catch(IOException e){
				throw new RuntimeException(e);
			}
		}
	}
}
