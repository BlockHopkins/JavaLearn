package com.qc.nio.javanio.bytebuffer;

import java.nio.ByteBuffer;

/**
 * Thinking in Java
 * 18.10.2 nio-获取基本类型 
 * @author Qc
 */
public class ByteBufferGetBasicDataType {
	private static final int BSIZE = 1024;
	public static void main(String[] args){
		ByteBuffer byteBuffer = ByteBuffer.allocate(BSIZE);
		// 分配空间自动赋了0值， Allocation automatically zeroes the ByteBuffer;
		int i = 1024;
		while(i++ < byteBuffer.limit()){
			if(byteBuffer.get()!=0)
				print("nonzero");
		}
		//共检查了1024个位置
		print("byteBuffer.limit()= "+ byteBuffer.limit()+", i= "+i);
		byteBuffer.rewind();//一次读取后，需要重新恢复开始读取状态
		
		//1. 存储并读取char型数据 store and read a char array:
		byteBuffer.asCharBuffer().put("Howdy!");
		char c;
		printnb("char ");
		while((c = byteBuffer.getChar()) != 0){
			printnb(c + " ");
		}
		print("");
		byteBuffer.rewind();
		
		//2. 存储并读取short型数据 store and read a short
		byteBuffer.asShortBuffer().put((short)471142);
		print("short " + byteBuffer.getShort());
		byteBuffer.rewind();
		
		//3. 存储并读取int型数据 store and read an int
		byteBuffer.asIntBuffer().put(99471142);
		print("int " + byteBuffer.getInt());
		byteBuffer.rewind();
		
		//4. 存储并读取long型数据 store and read a long
		byteBuffer.asLongBuffer().put(99471142);
		print("long " + byteBuffer.getLong());
		byteBuffer.rewind();
		
		//5. 存储并读取float型数据 store and read a float
		byteBuffer.asFloatBuffer().put(99471142);
		print("float " + byteBuffer.getFloat());
		byteBuffer.rewind();
		
		//6. 存储并读取double型数据 store and read a double
		byteBuffer.asDoubleBuffer().put(99471142);
		print("double " + byteBuffer.getDouble());
		
	}
	
	
	
	private static void print(Object object){
		System.out.println(object);
	}
	
	private static void printnb(Object object){
		System.out.print(object);
	}
}
