package com.qc.nio.javanio.bytebuffer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

/**
 * Thinking in Java
 * 
 * 18.10.6 内存映射文件
 * 
 * 与传统IO性能比较
 * @author Qc
 *
 */
public class MappedIOPerformance {
	private static int numOfInts = 4000000;
	private static int numOfUbuffInts = 200000;
	
	//静态内部抽象类作为测试框架
	private abstract static class Tester {
		private String name;
		public Tester(String name){ this.name = name; }
		
		public void runTest(){
			System.out.print(name+": ");
			try {
				long start = System.nanoTime();
				test();
				double duration = System.nanoTime() - start;
				System.out.format("%.2f\n", duration/1.0e9);
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		//测试方法，供子类覆写
		public abstract void test() throws IOException;
	}
	
	private static Tester[] testers = {
		new Tester("Stream Write") {//1.传统IO流：写
			@Override
			public void test() throws IOException {
				DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("temp.txt"))));
				for(int i = 0; i<numOfInts; i++){
					dos.writeInt(i);
				}
				dos.close();
			}
		},
		new Tester("Mapped Write"){//2.NIO内存映射文件：写
			@Override
			public void test() throws IOException {
				RandomAccessFile raf = new RandomAccessFile(new File("temp.txt"),"rw");
				FileChannel fc = raf.getChannel();
				IntBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size()).asIntBuffer();
				for(int i=0; i<numOfInts; i++){
					ib.put(i);
				}
				fc.close();
				raf.close();
			}
		},
		new Tester("Stream Read"){//3.传统IO流：读
			@Override
			public void test() throws IOException {
				DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream("temp.txt")));
				for(int i=0; i<numOfInts; i++){
					dis.readInt();
				}
				dis.close();
			};
		},
		new Tester("Mapped Read"){//4.NIO内存映射文件：读
			@Override
			public void test() throws IOException {
				FileInputStream fis = new FileInputStream(new File("temp.txt"));
				FileChannel fc = fis.getChannel();
				IntBuffer ib = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).asIntBuffer();
				while(ib.hasRemaining()){
					ib.get();
				}
				fc.close();
				fis.close();
			};
		},
		new Tester("Stream Read/Write"){//5.传统IO流：读/写
			@Override
			public void test() throws IOException {
				RandomAccessFile raf = new RandomAccessFile(new File("temp.txt"),"rw");
				raf.writeInt(1);
				for(int i=0; i<numOfUbuffInts; i++){
					raf.seek(raf.length() - 4);
					raf.write(raf.readInt());
				}
				raf.close();
			};
		},
		new Tester("Mapped Read/Write") {//6.NIO内存映射文件：读/写
			@Override
			public void test() throws IOException {
				RandomAccessFile raf = new RandomAccessFile(new File("temp.txt"),"rw");
				FileChannel fc = raf.getChannel();
				IntBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size()).asIntBuffer();
				ib.put(0);
				for(int i=1; i<numOfUbuffInts; i++){
					ib.put(ib.get(i-1));
				}
				fc.close();
				raf.close();
			}
		}
	};
	
	public static void main(String[] args) {
		for(Tester tester : testers){
			tester.runTest();
		}
	}
}
