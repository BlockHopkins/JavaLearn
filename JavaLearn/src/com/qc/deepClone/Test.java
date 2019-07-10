package com.qc.deepClone;

/**
 * 深度克隆测试类
 * @author Qc
 *
 */
public class Test {
    public static void main(String[] args) {  
        try {  
            Person p1 = new Person("Hao LUO", 33, new Car("Benz", 300));  
            Person p2 = DeepClone.clone(p1); // 深度克隆  
            p2.getCar().setBrand("BYD");  
            // 修改克隆的Person对象p2关联的汽车对象的品牌属性  
            // 原来的Person对象p1关联的汽车不会受到任何影响  
            // 因为在克隆Person对象时其关联的汽车对象也被克隆了  
            System.out.println(p1);  
            System.out.println(p2);  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
    }
    /*
     * 输出结果：
     * Person [name=Hao LUO, age=33, car=Car [brand=Benz, maxSpeed=300]]  
	 * Person [name=Hao LUO, age=33, car=Car [brand=BYD, maxSpeed=300]]  
	 * 
	 * 可以看到p1对象和p2对象都有car的实例，并且修改了p2以后不会对p1产生影响。
	 * 注意点：Person和Car都要实现序列化接口,而泛型的限定可以检查出对象是否支持序列化，这一步在编译阶段完成。
     */
}
