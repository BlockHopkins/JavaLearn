package com.qc.nio.javanio.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * Thinking in Java 
 * 18.10.3 nio-视图缓冲器
 * @author Qc
 *
 */
public class ByteBufferViewBuffer {
	public static void main(String[] args) {
		//初始化一次数据，然后用不同视图缓冲器打开
		ByteBuffer bb = ByteBuffer.wrap(new byte[]{0, 0, 0, 0, 0, 0, 0, 'a'});//8 byte， 8个字节
//		ByteBuffer bb = ByteBuffer.wrap(new byte[]{0, 0, 0, 'a'});//4 byte， 4个字节
		bb.rewind();

		//Byte视图缓冲器，8个字节，打印8位，
		printnb("Byte Buffer : ");
		while(bb.hasRemaining()){
			printnb(bb.position() + " -> " + bb.get() + ", ");
		}
		print("");
		
		//Char视图缓冲器，一个Char占2个字节， 8个字节打印4位
		CharBuffer cb = ((ByteBuffer)bb.rewind()).asCharBuffer();
		printnb("Char Buffer : ");
		while(cb.hasRemaining()){
			printnb(cb.position() + "->" + cb.get() + ", ");
		}
		print("");
		
		//Float视图缓冲器，一个Float占4个字节，8个字节打印2位
		FloatBuffer fb = ((ByteBuffer)bb.rewind()).asFloatBuffer();
		printnb("Float Buffer : ");
		while(fb.hasRemaining()){
			printnb(fb.position() + "->" + fb.get() + ", ");
		}
		print("");
		
		//Int视图缓冲器，一个Int占4个字节，8个字节打印2位
		IntBuffer ib = ((ByteBuffer)bb.rewind()).asIntBuffer();
		printnb("Int Buffer : ");
		while(ib.hasRemaining()){
			printnb(ib.position() + "->" + ib.get() + ", ");
		}
		print("");
		
		//Long视图缓冲器，一个Long占8个字节，8个字节打印1位
		LongBuffer lb = ((ByteBuffer)bb.rewind()).asLongBuffer();
		printnb("Long Buffer : ");
		while(lb.hasRemaining()){
			printnb(lb.position() + "->" + lb.get() + ", ");
		}
		print("");
		
		//Short视图缓冲器，一个Short占2个字节，8个字节打印4位
		ShortBuffer sb = ((ByteBuffer)bb.rewind()).asShortBuffer();
		printnb("Short Buffer : ");
		while(sb.hasRemaining()){
			printnb(sb.position() + "->" + sb.get() + ", ");
		}
		print("");
		
		//Double视图缓冲器，一个Double占4个字节，8个字节打印1位
		DoubleBuffer db = ((ByteBuffer)bb.rewind()).asDoubleBuffer();
		printnb("Double Buffer : ");
		while(db.hasRemaining()){
			printnb(db.position() + "->" + db.get() + ", ");
		}
		print("");
	}
	
	
	private static void print(Object object){
		System.out.println(object);
	}
	
	private static void printnb(Object object){
		System.out.print(object);
	}
}
