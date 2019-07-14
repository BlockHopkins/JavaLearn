package com.qc.nio.javanio.bytebuffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Thinking in Java 
 * 18.10.6 内存映射文件
 * @author Qc
 * 
 * 内存映射文件允许我们创建和修改哪些因为太大而不能放入内存的文件。
 * 创建一个128M的大文件，这可能比操作系统所允许一次载入内存的空间大，但利用NIO内存映射文件我们可以只放入一部分文件到内存来访问。
 * 利用这种方式，很大的文件（可达2GB）也可以很容易地修改。
 */
public class LargeMappedFiles {
	static int length = 0x8FFFFFF;//128MB
	public static void main(String[] args){
		MappedByteBuffer mappedByteBuffer = null;
		RandomAccessFile file = null;
		try{
			//创建一个大文件
			file = new RandomAccessFile("test.dat","rw");
			mappedByteBuffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
			for(int i=0; i<length; i++){
				mappedByteBuffer.put((byte)'x');
			}
			print("Finished writing");
			
			//读取时只读取其中的六个字节的内容
			for(int i=length/2; i<length/2+6; i++){
				printnb((char)mappedByteBuffer.get(i));
			}
			
//			mappedByteBuffer.rewind();
//			for(int i=0; i<12; i=i+2){
//				mappedByteBuffer.putChar(i,'哈');
//			}
//			print("Finished writing");
//			//读取时只读取其中的六个字节的内容
//			for(int i=0; i<12; i=i+2){
//				printnb(mappedByteBuffer.getChar(i));
//			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(file!=null){
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void print(Object object){
		System.out.println(object);
	}
	
	private static void printnb(Object object){
		System.out.print(object);
	}
}
