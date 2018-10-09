# spring
SpringMVC的简单实现
具体思路：
1、扫描指定包下的需要实例化的class文件
2、初始化所有被扫描到的类
  1）遍历扫描到的类路径，通过反射机制将类实例化
  2）并将实例化后的对象放到map中存放
3、进行依赖注入  
  1）首先遍历所有的bean，如果当前类中有被其他类注入，将其被注入的类字段对应到当前类中完成依赖注入
4、完成方法与路径的匹配
  1）这里为了方便，将方法与路径做了一个映射到一个map中，spring源码远比这些逻辑复杂的。
 
 
 
注：这只是自己的理解，菜鸟一只，大佬请略过。