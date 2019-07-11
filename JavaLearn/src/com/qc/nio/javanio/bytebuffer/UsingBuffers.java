package com.qc.nio.javanio.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Thinking in Java
 * 18.10.5 用缓冲器操纵数据
 * Buffer 四个索引: mark, position, limit, capacity 的应用。
 * 
 * @author Qc
 */
public class UsingBuffers {
	/**
	 * 用一个简单的算法(交换相邻字符)，以对CharBuffer中的字符进行编码(scramble)和解码(unscramble)
	 */
	private static void symmetricScramble(CharBuffer buffer){
		while(buffer.hasRemaining()){
			buffer.mark();//将mark设置为position的值
			char c1 = buffer.get();
			char c2 = buffer.get();
			buffer.reset();//将position重新设置为mark的值
			buffer.put(c2).put(c1);//交换顺序放入数据
		}
	}
	
	public static void main(String[] args) {
		char[] data = "UsingBuffers".toCharArray();
		ByteBuffer byteBuffer = ByteBuffer.allocate(data.length*2);
		CharBuffer charBuffer = byteBuffer.asCharBuffer();
		charBuffer.put(data);
		//原文: UsingBuffers
		System.out.println(charBuffer.rewind());
		//编码  结果: sUniBgfuefsr
		symmetricScramble(charBuffer);
		System.out.println(charBuffer.rewind());
		//再执行一次算法可以解码 结果: UsingBuffers
		symmetricScramble(charBuffer);
		System.out.println(charBuffer.rewind());
	}
}
