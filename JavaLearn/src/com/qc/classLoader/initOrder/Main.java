package com.qc.classLoader.initOrder;

/**
 * 测试父类及派生类初始化顺序
 * 
 * 初始化顺序：
 * 1． 父类静态成员和静态初始化块 ，按在代码中出现的顺序依次执行
 * 2． 子类静态成员和静态初始化块 ，按在代码中出现的顺序依次执行
 * 3． 父类实例成员和实例初始化块 ，按在代码中出现的顺序依次执行
 * 4． 父类构造方法
 * 5． 子类实例成员和实例初始化块 ，按在代码中出现的顺序依次执行
 * 6． 子类构造方法

 * @author Qc
 *
 */
public class Main {
	public static void main(String[] args) {
		Base obj = new Derived();
	}
}

/**
 * 基类
 */
class Base{
	private String name = "base";
	
	public Base(){
		printInfo1();
		printInfo2();
	}
	
	public void printInfo1() {
		System.out.println("Base printInfo1 " + name);
	}
	
	public void printInfo2() {
		System.out.println("Base printInfo2 " + name);
	}
}

/**
 * 派生类
 */
class Derived extends Base{
	private String name = "derived";
	
	public Derived(){
		printInfo1();
		printInfo2();
	}
	
	public void printInfo1() {
		System.out.println("Derived printInfo1 " + name);
	}
	
	public void printInfo2() {
		System.out.println("Derived printInfo2 " + name);
	}
}


/*
 * 运行结果：
 *  Derived printInfo1 null
 *  Derived printInfo2 null
 *  Derived printInfo1 derived
 *  Derived printInfo2 derived
 */
