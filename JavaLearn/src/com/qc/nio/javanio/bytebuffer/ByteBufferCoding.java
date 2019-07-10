package com.qc.nio.javanio.bytebuffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.SortedMap;

/**
 * Thinking in Java
 * 18.10.1 nio-转换数据
 * @author Qc
 */
public class ByteBufferCoding {
	private static final int BSIZE = 1024;
	public static void main(String[] args) throws IOException {
		//1. 将ByteBuffer中的数据输出文本到系统文件
		FileChannel fc = new FileOutputStream("data.txt").getChannel();
		fc.write(ByteBuffer.wrap("Some text".getBytes()));
		fc.close();
		
		//通过ByteBuffer从系统文件中读取数据
		fc = new FileInputStream("data.txt").getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
		fc.read(buffer);
		buffer.flip();
		
		//1.1 由于没有解码，通过ByteBuffer.asCharBuffer()只能读取到字节流，结果为乱码，无法正常显示文本
		print(buffer.asCharBuffer());
		buffer.rewind();//一次读取后，需要重新恢复开始读取状态
		
		//1.2 使用系统默认编码解码，可以正常显示文本，如：Decoded using UTF-8: Some text
		String encoding = System.getProperty("file.encoding");
		print("Decoded using " + encoding + ": " + Charset.forName(encoding).decode(buffer));
		
		//2. 也可以在使用ByteBuffer输出文本到文件系统时先进行编码，读取时就可以不用再解码。
		fc = new FileOutputStream("data2.txt").getChannel();
		fc.write(ByteBuffer.wrap("Some text".getBytes("UTF-16BE")));
		fc.close();
		//通过ByteBuffer从系统文件中读取数据，不需要再手动解码，使用ByteBuffer.asCharBuffer()可以正常显示文本：
		fc = new FileInputStream("data2.txt").getChannel();
		buffer.clear();
		fc.read(buffer);
		buffer.flip();
		print("encode before writing: " + buffer.asCharBuffer());
		
		//3.也可以输出前，初始化ByteBuffer时先转换为CharBuffer再赋值。
		fc = new FileOutputStream("data3.txt").getChannel();
		buffer = ByteBuffer.allocate(24);
		buffer.asCharBuffer().put("Some text");
		fc.write(buffer);
		fc.close();
		//通过ByteBuffer从系统文件中读取数据
		fc = new FileInputStream("data3.txt").getChannel();
		buffer.clear();
		fc.read(buffer);
		buffer.flip();
		print("use a CharBuffer: " + buffer.asCharBuffer());

		printAvailableCharSets();
	}
	
	/**
	 * 打印出所有可用的字符集：
	 */
	public static void printAvailableCharSets(){
		print("\n\n<---- print all the avilable charsets ---->");
		SortedMap<String, Charset> charSetMap = Charset.availableCharsets();
		Iterator<String> iterator = charSetMap.keySet().iterator();
		while(iterator.hasNext()){
			String csName = iterator.next();
			printnb(csName);
			Iterator<String> aliases = charSetMap.get(csName).aliases().iterator();
			if(aliases.hasNext()){
				printnb(": ");
			}
			while(aliases.hasNext()){
				printnb(aliases.next());
				if(aliases.hasNext()){
					printnb(", ");
				}
			}
			print("");
		}
	}
	
	
	
	private static void print(Object object){
		System.out.println(object);
	}
	
	private static void printnb(Object object){
		System.out.print(object);
	}
}
