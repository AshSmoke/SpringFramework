# Spring

### Spring简介

##### 核心概念

- **IoC（Inversion of Control）控制反转**
  - 使用对象时，由主动new产生对象转换为由**外部**提供对象，此过程中对象创建控制权由程序转移到**外部**，此思想成为控制反转
- Spring技术对IoC思想进行了实现
  - Spring提供了一个容器。，成为IoC容器，用来充当IoC思想中的“**外部**”
  - IoC容器负责对象的创建、初始化等一系列工作，被创建或被管理的对象在IoC容器中统称为**Bean**
- **DI（Dependency Injection）依赖注入**
  - 在容器中建立bean与bean之间的依赖关系的整个过程，称为依赖注入
- 目标：充分**解耦**
  - 使用IoC容器管理bean（IoC）
  - 在IoC容器内将有依赖关系的bean进行关系绑定（DI）
- 最终效果
  - 使用对象时不仅可以直接从IoC容器中获取，并且获取到的bean已经绑定了所有的依赖关系

### 快速入门

##### IoC注意事项

- 管理什么？（Service与Dao）
- 如何将被管理的对象告知IoC容器？（配置）
- 被管理的对象交给IoC容器，如何获取到IoC容器？（接口）
- IoC容器得到后，如何从容器中获取bean？（接口方法）
- 使用Spring导入那些坐标？（pom.xml）

##### 实现流程

- 导入Spring坐标

```xml
<dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.2.10.RELEASE</version>
</dependency>
```

- 配置bean

  - resource下创建applicationContext.xml文件
  - bean标签表示配置bean，id属性表示给bean取名字，class属性表示给bean定义类型
  - bean定义时id属性在同一个上下文中不能重复
    
  
  > 围堵标签：有开始标签和结尾标签，一般用于有标签体内容的标签
  >
  > 空标签：只有一个标签，不分开头结尾，一般用于不需要写标签体内容的标签

```xml
<bean id="bookDao" class="cn.ashsmoke.dao.impl.BookDaoImpl"></bean>
<bean id="bookService" class="cn.ashsmoke.service.impl.BookServiceImpl"></bean>
```

- 获取IoC容器

```java
ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
```

- 获取bean

```java
BookService bookService =ctx.getBean("bookService",BookService.class);
```

##### DI注意事项

- 基于IoC管理bean
- Service中使用new形式创建的Dao对象不再保留
- Service中提供方法将需要的Dao对象加载进Service中
- Service与Dao间的关系通过配置描述

##### 实现流程

- 删除业务层中使用new的方式创建的dao对象

```java
private BookDaoImpl bookDao;
```

- 提供对应的set方法

```java
public void setBookDao(BookDaoImpl bookDao) {
        this.bookDao = bookDao;
}
```

- 配置Service与Dao的关系
  - property标签表示配置当前bean的属性
  - name属性表示哪一个具体的属性
  - ref属性表示参照哪一个bean

```xml
<bean id="bookService" class="cn.ashsmoke.service.impl.BookServiceImpl">
        <property name="bookDao" ref="bookDao"/>
</bean>
```

### bean配置

##### bean基础配置

|   类别   | 描述                                                         |
| :------: | :----------------------------------------------------------- |
|   名称   | bean                                                         |
|   类型   | 标签                                                         |
|   所属   | beans标签                                                    |
|   功能   | 定义Spring核心容器管理的对象                                 |
| 属性列表 | id：bean的id，使用容器可以通过id值获取对应的bean，在一个容器中id值唯一  <br />class：bean的类型，即配置的bean的**全路径类名**<br />name:定义bean的别名，可以定义多个，使用逗号（，）分号（；）空格（ ）分隔 |

##### bean范围配置

Spring默认情况下为单例模式

```xml
<bean id="bookDao" name="dao" class="cn.ashsmoke.dao.impl.BookDaoImpl" scope="prototype"></bean>
<bean id="bookService" name="service" class="cn.ashsmoke.service.impl.BookServiceImpl" scope="singleton">
        <property name="bookDao" ref="dao"/>
</bean>
```

- 为什么bean默认为单例？
  - 管理重复使用的对象可以提高效率
- 适合交给容器进行管理的bean
  - 表现层对象（Servlet）
  - 业务层对象（Service）
  - 数据层对象（Dao）
  - 工具对象（util）
- 不适合交给容器进行管理的bean
  - 封装实体的域对象

### bean实例化

##### 构造方法

- bean本质上就是对象，创建bean使用构造方法完成

  - 私有的构造方法也可以获取（原理：反射）

- 提供可访问的构造方法

  - ```java
    public BookDaoImpl() {
            System.out.println("bookDao constructor");
    }
    ```

- 配置

  - ```xml
    <bean id="bookDao" name="dao" class="cn.ashsmoke.dao.impl.BookDaoImpl"></bean>
    ```

- 无参构造方法如果不存在，将抛出异常BeanCreationException

##### 静态工厂

- 常规使用静态工厂代码

  - ```java
    public class OrderDaoFactory{
        public static OrderDao getOrderDao(){
            return new OrderDaoImpl();
        }
    }
    ```

- Spring配置静态工厂类

  - ```xml
    <bean id="orderDao" class="cn.ashsmoke.factory.OrderDaoFactory" factory-method="getOrderDao"/>
    ```

##### 实例工厂

- 实例工厂

  - ```java
    public class UserDaoFactory{
        public UserDao getUserDao(){
            return new UserDaoImpl();
        }
    }
    ```

- Spring配置实例工厂

  - ```xml
    <bean id="userFactory" class="cn.ashsmoke.factory.UserDaoFactory"/>
    <bean id="userDao" factory-method="getUserDao" factory-bean="userFactory"/>
    ```

##### FactoryBean实例化

- 替换原始实例工厂

  - ```java
    public class UserDaoFactoryBean implements FactoryBean<UserDao>{
        public UserDao getObject() throws Exception{
            return new UserDaoImpl();
        }
        public Class<?> getObjectType(){
            return UserDao.class;
        }
        public boolean isSingleton(){
            //是单例模式
            //return true;
            //不是单例模式
            return false;
        }
    }
    ```

- Spring配置FactoryBean

  - ```xml
    <bean id="userDao" class="cn.ashsmoke.factory.UserDaoFactoryBean"/>
    ```

### bean生命周期

- 生命周期：从创建到消亡的完整过程
- bean生命周期：bean从创建到销毁的整个过程
- bean生命周期控制：在bean创建后到销毁前做一些事情

##### bean生命周期

- 初始化容器
  1. 创建对象（内存分配）
  2. 执行构造方法
  3.  执行属性注入（set操作）
  4. 执行bean初始化方法
- 使用bean
  1. 执行业务操作
- 关闭/销毁容器
  1. 执行bean销毁方法

##### bean销毁时机

- 容器关闭前触发bean的销毁
- 关闭容器方式
  - 手工关闭容器
    CondigurableApplicationContext接口close()操作
  - 注册关闭钩子，在虚拟机退出前先关闭容器在退出虚拟机
    ConfigurableApplicationContext接口registerShutdownHook()操作

##### bean生命周期控制

- init-method：设置bean初始化生命周期回调函数
- destory-method：设置bean销毁生命周期回调函数，仅适用于单例对象
- 配置代码：

```xml
<bean id="bookDao" class="cn.ashsmoke.dao.impl.BookDaoImpl" init-method="init" destroy-method="destory"/>

<bean id="bookService" class="cn.ashsmoke.service.impl.BookServiceImpl">
    <property name="bookDao" ref="bookDao"/>
</bean>
```

- 控制代码：

```java
ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
BookDao bookDao = ctx.getBean("bookDao", BookDao.class);
bookDao.save();
//注册关闭钩子函数，在虚拟机退出之前回调此函数，关闭容器
//ctx.registerShutdownHook();
//关闭容器
ctx.close();
```

##### bean相关

```xml
<bean
      id="bookDao"                                              bean的id
      name="dao bookDaoImpl daoImpl"                            bean别名
      class="an.ashsmoke.dao.impl.BookDaoImpl"                  bean类型，静态工厂类，FactoryBean类
      scope="singleton"                                         控制bean的实例数量
      init-method="init"                                        生命周期初始化方法
      destory-method="destory"                                  生命周期销毁方法
      autowire="buType"                                         自动装配类型
      factory-method="getInstance"                              bean工厂方法，应用于静态工厂或实例工厂
      factory-bean="cn.ashsmoke.factory.BookDaoFactory"         实例工厂bean
      lazy-init="true"                                          控制bean延迟加载
      />
```



### 依赖注入

> 依赖注入描述了在容器中建立bean与bean之间依赖关系的过程

- 依赖注入方式
  - setter注入
    - 简单类型
    - 引用类型
  - 构造器注入
    - 简单类型
    - 引用类型

##### setter注入----引用类型

- 在bean中定义引用类型属性并提供**可供访问**的set方法

- ```java
  private BookDaoImpl bookDao;
  public void setBookDao(BookDaoImpl bookDao) {
          this.bookDao = bookDao;
  }
  ```

- 配置中使用property标签ref属性注入引用类型对象

- ```xml
  <bean id="bookService" class="cn.ashsmoke.service.impl.BookServiceImpl">
          <property name="bookDao" ref="bookDao"/>
  </bean>
  ```

- 引用类型可以注入多个

##### setter注入----简单类型

- 在bean中定义简单类型属性并提供可供访问的set方法

- ```java
  public class BookDaoImpl implements BookDao{
      private int connectionNumber;
      public void setConnectionNumber(int connectionNumber){
          this.connectionNumber=connectionNumber;
      }
  }
  ```

- 配置中使用property标签value属性注入简单类型数据

- ```xml
  <bean id="bookDao" class="cn.ashsmoke.dao.impl.BookDaoImpl">
      <property name="connectionNumber" value="10"/>
  </bean>
  ```

##### 构造器注入----引用类型

- 在bean中定义引用类型属性并提供可供访问的**构造**方法

- ```java
  private BookDaoImpl bookDao;
  private UserDaoImpl userDao;
  
  public BookServiceImpl(BookDaoImpl bookDao, UserDaoImpl userDao) {
      this.bookDao = bookDao;
      this.userDao = userDao;
  }
  ```

- 配置中使用**constructor-arg**标签**ref**属性注入引用类型对象

- ```xml
  <bean id="bookService" name="service" class="cn.ashsmoke.service.impl.BookServiceImpl" scope="singleton">
          <constructor-arg name="bookDao" ref="dao"/>
          <constructor-arg name="userDao" ref="userDao"/>
  </bean>
  ```

##### 构造器注入----简单类型

- 在bean中定义简单类型属性并提供可供访问的**构造**方法

- ```java
  private String databaseName;
  private int connectionNumber;
  
  public BookDaoImpl(String databaseName, int connectionNumber) {
      this.databaseName = databaseName;
      this.connectionNumber = connectionNumber;
  }
  ```

- 配置中使用**constructor-arg**标签**value**属性设置俺形参名称注入

- ```xml
  <bean id="bookDao" class="cn.ashsmoke.dao.impl.BookDaoImpl">
          <constructor-arg name="databaseName" value="mysql"/>
          <constructor-arg name="connectionNumber" value="10"/>
  </bean>
  ```

- > type属性：消除因参数名带来的耦合
  >
  > index属性：消除多个同类型参数无法使用type解耦

- 配置中使用**constructor-arg**标签**type**属性设置按形参类型注入

- ```xml
  <bean id="bookDao" class="cn.ashsmoke.dao.impl.BookDaoImpl">
          <constructor-arg type="int" value="10"/>
          <constructor-arg type="java.lang.String" value="mysql"/>
  </bean>
  ```

- 配置中使用**constructor-arg**标签**index**属性设置按形参位置注入

- ```xml
  <bean id="bookDao" class="cn.ashsmoke.dao.impl.BookDaoImpl">
          <constructor-arg index="1" value="10"/>
          <constructor-arg index="0" value="mysql"/>
  </bean>
  ```

##### 依赖注入方式选择

1. 强制依赖使用构造器进行，使用setter注入有概率不进行注入导致null对象出现
2. 可选依赖使用setter注入进行，灵活性强
3. Spring框架倡导使用构造器，第三方框架内部大多数采用构造器注入的形式进行数据初始化，相对严谨
4. 如有必要刻意两者同时使用，使用构造器注入完成强制依赖的注入，使用setter注入完成可选依赖的注入
5. 实际开发过程中还要根据实际情况分析，如果受控对象没有提供setter方法就必须使用构造器注入
6. **自己开发的模块推荐使用setter注入**

##### 依赖自动装配

- IoC容器根据bean所依赖的资源在容器中自动查找并注入到bean中的过程称为自动装配

- 自动装配方式

  - **按类型（常用）**
  - 按名称
  - 按构造方法
  - 不启用自动装配

- 配置中使用**bean**标签**autowire**属性设置自动装配的类型

- ```xml
  <bean id="bookDao" class="cn.ashsmoke.dao.impl.BookDaoImpl"></bean>
  <bean id="bookService" class="cn.ashsmoke.service.impl.BookServiceImpl" autowire="byType"></bean>
  ```

- 自动装配用于引用类型依赖注入，**不能对简单类型进行操作**

- 使用按类型装配时（byType）必须保障容器中相同类型的bean唯一，推荐使用

- 使用按名称装配时（byName）必须保障容器中具有指定名称的bean，因变量名与配置耦合，不推荐使用

- 自动装配优先级低于setter注入与构造器注入，同时出现时自动装配配置失效

##### 集合注入

- bean中定义私有集合并创建setter方法

- ```java
  private int[] array;
  private List<String> list;
  private Set<String> set;
  private Map<String,String> map;
  private Properties properties;
  //setter方法省略
  ```

- array数组注入

- ```xml
  <property name="array">
      <array>
          <value>100</value>
          <value>200</value>
          <value>300</value>
      </array>
  </property>
  ```

- List数组注入

- ```xml
  <property name="list">
      <list>
          <value>111</value>
          <value>222</value>
          <value>333</value>
      </list>
  </property>
  ```

- set集合注入

- ```xml
  <property name="set">
      <set>
          <value>cn</value>
          <value>ashsmoke</value>
          <value>spring</value>
      </set>
  </property>
  ```

- map集合注入

- ```xml
  <property name="map">
      <map>
          <entry key="country" value="china"/>
          <entry key="province" value="henan"/>
          <entry key="city" value="kaifeng"/>
      </map>
  </property>
  ```

- properties注入

- ```xml
  <property name="properties">
      <props>
          <prop key="country">china</prop>
          <prop key="province">henan</prop>
          <prop key="city">kaifeng</prop>
      </props>
  </property>
  ```

##### 数据源对象管理

- 导入druid坐标

- ```xml
  <dependency>
              <groupId>com.alibaba</groupId>
              <artifactId>druid</artifactId>
              <version>1.2.16</version>
  </dependency>
  ```

- 配置数据源对象作为spring管理的bean

- ```xml
  <bean class="com.alibaba.druid.pool.DruidDataSource">
          <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
          <property name="url" value="jdbc:mysql://localhost:3306/mybatis"/>
          <property name="username" value="root"/>
          <property name="password" value="admin"/>
  </bean>
  ```

##### 加载properties文件

- 开启context命名空间

- ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
             http://www.springframework.org/schema/beans
             http://www.springframework.org/schema/beans/spring-beans.xsd
             http://www.springframework.org/schema/context
             http://www.springframework.org/schema/context/spring-context.xsd
             ">
  ```

- 使用context命名空间，加载指定properties文件

- ```xml
  <context:property-placeholder location="jdbc.properties"/>
  ```

- 使用${}读取加载的属性值

- ```xml
  <property name="driverClassName" value="${jdbc.driver}"/>
  <property name="url" value="${jdbc.url}"/>
  <property name="username" value="${jdbc.username}"/>
  <property name="password" value="${jdbc.password}"/>
  ```

- 不加载系统属性

- ```xml
  <context:property-placeholder location="jdbc.properties" system-properties-mode="NEVER"/>
  ```

- 加载多个properties文件

- ```xml
  <context:property-placeholder location="jdbc.properties,msg.properties"/>
  ```

- 加载所有properties文件

- ```xml
  <context:property-placeholder location="*.properties"/>
  ```

- 加载properties文件**标准格式**

- ```xml
  <context:property-placeholder location="classpath:*.properties"/>
  ```

- 从类路径或jar包中搜索并加载properties文件

- ```xml
  <context:property-placeholder location="classpath*:*.properties"/>
  ```

### 容器

##### 创建容器

- 方式一：类路径加载配置文件

- ```java
  ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
  ```

- 方式二：文件路径加载配置文件

- ```java
  ApplicationContext ctx=new FileSystemXmlApplicationContext("D:\\applicationContext.xml");
  ```

- 加载多个配置文件

- ```java
  ApplicationContext ctx=new ClassPathXmlApplicationContext("bean1.xml","bean2.xml");
  ```

##### 获取bean

- 方式一：使用bean名称获取

- ```java
  BookService bookService =(BookService)ctx.getBean("bookService");
  ```

- 方式二：使用bean名称获取并制定类型

- ```java
  BookService bookService =ctx.getBean("bookService",BookService.class);
  ```

- 方式三：使用bean类型获取

- ```java
  BookService bookService =ctx.getBean(BookService.class);
  //同一类型bean只能有一个
  ```

##### 容器相关

- BeanFactory是IoC容器的顶层接口，初始化BeanFactory对象时，加载的bean延迟加载
- ApplicationContext接口是Spring容器的核心接口，初始化bean立即加载
- ApplicationContext接口提供基础的bean操作相关方法，通过其他接口扩展其功能
- ApplicationContext接口常用初始化类
  - ClassPathXmlApplicationContext
  - FileSystemXmlApplicationContext

### 注解开发

##### 定义bean

- 使用@Component定义bean

- ```java
  @Component("bookDao")
  //Repository("bookDao")
  public class BookDaoImpl implements BookDao {}
  @Component("bookService")
  //@Service("bookService")
  public class BookServiceImpl implements BookService {}
  ```

- 核心配置文件中通过组件扫描加载bean(需要context命名空间)

- ```xml
  <context:component-scan base-package="cn.ashsmoke"/>
  ```

- Spring提供@Component注解的三个衍生注解

  - @Controller：用于表现层bean定义
  - @Service：用于业务层bean定义
  - @Repository：用于数据层bean定义

##### 纯注解开发

- Spring3.0开启了纯注解开发模式，使用Java类替代配置文件

- Java类替代Spring核心配置文件

- ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
         http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd
         ">
      <context:component-scan base-package="cn.ashsmoke"/>
  </beans>
  ```

- ```java
  @Configuration
  @ComponentScan("cn.ashsmoke")
  public class SpringConfig {
  }
  ```

- @Configuration注解用于设定当前类为配置类

- @ComponentScan注解用于设定扫描路径，此注解**只能添加一次**，多个数据用数组格式

- ```java
  @Componenet({"cn.ashsmoke.service","cn.ashsmoke.dao"})
  ```

- 读取Spring核心配置文件初始化容器对象切换为读取Java配置类初始化容器对象

- ```java
  //加载配置文件初始化容器
  ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
  //加载配置类初始化容器
  ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
  ```

##### bean管理

- 使用@Scope定义bean作用范围

- ```java
  @Repository("bookDao")
  @Scope("singleton")
  public class BookDaoImpl implements BookDao {}
  ```

- 使用@PostConstruct、@PreDestroy定义bean生命周期

- ```java
  	@Override
      public void save() {
          System.out.println("bool dao save...");
      }
      @PostConstruct
      public void init(){
          System.out.println("book dao init...");
      }
      @PreDestroy
      public void destroy(){
          System.out.println("book dao destroy...");
      }
  ```

- 多例模式下Spring不负责销毁（管理bean的生命周期），所以没有调用destroy方法

##### 依赖注入

- 使用@Autowired注解开启自动装配模式（按类型）

- ```java
  @Service("bookService")
  public class BookServiceImpl implements BookService {
      @Autowired
      private BookDao bookDao;
  
  //    public void setBookDao(BookDao bookDao) {
  //        this.bookDao = bookDao;
  //    }
  
      @Override
      public void save() {
          System.out.println("book service save...");
          bookDao.save();
      }
  }
  ```

- 注意：自动装配基于反射设计创建对象并 暴力反射对应属性 为 私有属性 初始化数据，因此无需提供setter方法

- 注意：自动装配建议使用无参构造方法创建对象（默认），如果不提供对应构造方法，请提供唯一的构造方法
              tips：没有构造方法时默认提供一个无参构造

------

- 使用@Qualifier注解开启指定名称装配bean

- ```java
  @Service("bookService")
  public class BookServiceImpl implements BookService {
      @Autowired
      @Qualifier("bookDao")
      private BookDao bookDao;
  }
  ```

- 注意：@Qualifier注解无法单独使用，必须配合@Autowired注解使用

  ------

- 使用@Value实现简单类型注入

- ```java
  @Repository("bookDao")
  public class BookDaoImpl implements BookDao {
      @Value("100")
      private String connectionNum;
  }
  ```

------

- 使用@PropertySource注解加载properties文件

- ```java
  @Configuration
  @ComponentScan("cn.ashsmoke")
  @PropertySource("classpath:jdbc.properties")
  public class SpringConfig {
  }
  ```

- 注意：路径仅支持单一文件配置，多文件请使用数组格式配置，不允许使用通配符*

##### 第三方bean管理

- 使用@Bean配置第三方bean

- ```java
  public class SpringConfig {
  	//1、定义一个方法要管理的对象
      //2、添加@Bean，表示当前方法返回值是一个Bean
      @Bean
      public DataSource dataSource(){
          DruidDataSource ds=new DruidDataSource();
          ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
          ds.setUrl("jdbc:mysql://localhost:3306/mybatis");
          ds.setUsername("root");
          ds.setPassword("admin");
          return ds;
      }
  }
  ```

- 使用独立的配置类管理第三方bean

- ```java
  public class JdbcConfig {
      //1、定义一个方法要管理的对象
      //2、添加@Bean，表示当前方法返回值是一个Bean
      @Bean
      public DataSource dataSource(){
          DruidDataSource ds=new DruidDataSource();
          ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
          ds.setUrl("jdbc:mysql://localhost:3306/mybatis");
          ds.setUsername("root");
          ds.setPassword("admin");
          return ds;
      }
  }
  ```

- 将独立的配置类加入核心配置

- 方式一：导入式

- ```java
  public class JdbcConfig {
      //1、定义一个方法要管理的对象
      //2、添加@Bean，表示当前方法返回值是一个Bean
      @Bean
      public DataSource dataSource(){
          DruidDataSource ds=new DruidDataSource();
          ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
          ds.setUrl("jdbc:mysql://localhost:3306/mybatis");
          ds.setUsername("root");
          ds.setPassword("admin");
          return ds;
      }
  }
  ```

- 使用@Import注解手动加入配置类到核心配置，此注解只能添加一次，多个数据用数组格式

- ```java
  @Configuration
  @ComponentScan("cn.ashsmoke")
  @Import(JdbcConfig.class)
  public class SpringConfig {
  
  }
  ```

------

- 方式二：扫描式

- ```java
  @Configuration
  public class JdbcConfig {
      //1、定义一个方法要管理的对象
      //2、添加@Bean，表示当前方法返回值是一个Bean
      @Bean
      public DataSource dataSource(){
          DruidDataSource ds=new DruidDataSource();
          ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
          ds.setUrl("jdbc:mysql://localhost:3306/mybatis");
          ds.setUsername("root");
          ds.setPassword("admin");
          return ds;
      }
  }
  ```

- 使用@ComponentScan注解扫描配置类所在的包，加载对应的配置类信息

- ```java
  @Configuration
  @ComponentScan({"cn.ashsmoke.config","cn.ashsmoke.dao","cn.ashsmoke.service"})
  public class SpringConfig {
  
  }
  ```

##### 第三方bean依赖注入

- 简单类型依赖注入

- ```java
  public class JdbcConfig {
      @Value("${jdbc.driver}")
      private String driver;
      @Value("${jdbc.url}")
      private String url;
      @Value("${jdbc.username}")
      private String userName;
      @Value("${jdbc.password}")
      private String passWord;
      //1、定义一个方法要管理的对象
      //2、添加@Bean，表示当前方法返回值是一个Bean
      @Bean
      public DataSource dataSource(){
          DruidDataSource ds=new DruidDataSource();
          ds.setDriverClassName(driver);
          ds.setUrl(url);
          ds.setUsername(userName);
          ds.setPassword(passWord);
          return ds;
      }
  }
  ```

- 引用类型依赖注入

- ```java
  @Bean
      public DataSource dataSource(BookDao bookDao){
          System.out.println(bookDao)
          DruidDataSource ds=new DruidDataSource();
          ds.setDriverClassName(driver);
          ds.setUrl(url);
          ds.setUsername(userName);
          ds.setPassword(passWord);
          return ds;
      }
  ```

- 引用类型注入只需要为bean定义方法设置形参即可，容器会根据**类型**自动装配对象

##### Xml配置注解配置对比

|      功能      | Xml配置                                                      | 注解                                                         |
| :------------: | ------------------------------------------------------------ | ------------------------------------------------------------ |
|    定义bean    | bean标签<br />    id属性<br />    class属性                  | @Component<br />    @Controller<br />    **@Service**<br />    @Repository<br />**@ComponentScan** |
|  设置依赖注入  | setter注入（set方法）<br />    引用/简单<br />构造器注入（构造方法）<br />    引用/简单<br />自动装配 | **@Autowired<br />**    @Qualifier<br />@Value               |
| 配置第三方bean | bean标签<br />静态工厂、实例工厂、FactoryBean                | **@Bean**                                                    |
|    作用范围    | scope属性                                                    | @Scope                                                       |
|    生命周期    | 标准接口<br />    init-method<br />    destroy-method        | @PostConstructor<br />@PreDestroy                            |

### Spring整合Mybatis

##### 原始Mybatis使用

- Mybatis程序核心对象分析

- **初始化SqlSessionFactory**

- ```java
      // 1. 创建SqlSessionFactoryBuilder对象
      SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
      // 2. 加载SqlMapConfig.xml配置文件
      InputStream inputStream = Resources.getResourceAsStream("Mybatis-config.xml");
      // 3. 创建SqlSessionFactory对象
      SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
  ```

- 获取连接，获取实现

- ```java
  	// 4. 获取SqlSession
  	SqlSession sqlSession = sqlSessionFactory.openSession();
      // 5. 执行SqlSession对象执行查询，获取结果Brand
  	BrandDao brandDao = sqlSession.getMapper(BrandDao.class);
  ```

- 获取数据层接口

- ```java
  	Brand ac = brandDao.findById(2);
  	System.out.println(ac);
  ```

- 关闭连接

- ```java
  	sqlSession.close();
  ```

------

- 整合MyBatis

- 初始化类型别名

- ```xml
  	<typeAliases>
          <package name="cn.ashsmoke.pojo"/>
      </typeAliases>
  ```

- 初始化dataSource

- ```xml
  	<environments default="development">
          <environment id="development">
              <transactionManager type="JDBC"/>
              <dataSource type="POOLED">
                  <property name="driver" value="com.mysql.jdbc.Driver"/>
                  <property name="url" value="jdbc:mysql:///Mybatis?			     useSSL=false&amp;useServerPrepStmts=true"/>
                  <property name="username" value="root"/>
                  <property name="password" value="admin"/>
              </dataSource>
          </environment>
      </environments>
  ```

- 初始化映射配置

- ```xml
  <configuration>
      <!--上文所述配置 -->
      <mappers>
          <!--扫描mapper-->
          <package name="cn.ashsmoke.dao"/>
      </mappers>
  </configuration>
  ```

##### Spring使用Mybatis

- Maven导入坐标

- ```xml
  	<dependencies>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-context</artifactId>
              <version>6.0.11</version>
          </dependency>
          <dependency>
              <groupId>com.alibaba</groupId>
              <artifactId>druid</artifactId>
              <version>1.2.16</version>
          </dependency>
          <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis</artifactId>
              <version>3.5.13</version>
          </dependency>
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>8.0.33</version>
          </dependency>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-jdbc</artifactId>
              <version>6.0.11</version>
          </dependency>
          <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis-spring</artifactId>
              <version>3.0.2</version>
          </dependency>
      </dependencies>
  ```

- 初始化类型别名以及dataSource

- ```java
  	@Bean
      public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
          SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
          ssfb.setTypeAliasesPackage("cn.ashsmoke.pojo");
          ssfb.setDataSource(dataSource);
          return ssfb;
      }
  ```

- 初始化映射配置

- ```java
      @Bean
      public MapperScannerConfigurer mapperScannerConfigurer(){
          MapperScannerConfigurer msc = new MapperScannerConfigurer();
          msc.setBasePackage("cn.ashsmoke.dao");
          return msc;
      }
  ```

- SpringConfig

- ```java
  @Configuration
  @ComponentScan("cn.ashsmoke")
  @PropertySource("classpath:jdbc.properties")
  @Import({JdbcConfig.class, MybatisConfig.class})
  public class SpringConfig {
  }
  ```

- JdbcConfig

- ```java
  public class JdbcConfig {
      @Value("${jdbc.driver}")
      private String driver;
      @Value("${jdbc.url}")
      private String url;
      @Value("${jdbc.username}")
      private String userName;
      @Value("${jdbc.password}")
      private String passWord;
      //1、定义一个方法要管理的对象
      //2、添加@Bean，表示当前方法返回值是一个Bean
      @Bean
      public DataSource dataSource(){
          DruidDataSource ds=new DruidDataSource();
          ds.setDriverClassName(driver);
          ds.setUrl(url);
          ds.setUsername(userName);
          ds.setPassword(passWord);
          return ds;
      }
  }
  ```

- 获取连接，获取实现

- ```java
  AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
  BrandService brandService = ctx.getBean("brandService",BrandService.class);
  List<Brand> brands = brandService.selectAll();
  brands.forEach(brand -> System.out.println(brand));
  ```

- > Mybatis使用注解进行开发
  >
  > Service层私有属性使用@Autowired自动装配

### Spring整合Junit

- 导入依赖

- ```xml
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-test</artifactId>
              <version>6.0.11</version>
          </dependency>
          <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.13.2</version>
              <scope>test</scope>
          </dependency>
  ```

- 使用Spring整合Junit专用的类加载器

- ```java
  @RunWith(SpringJUnit4ClassRunner.class)
  @ContextConfiguration(classes = SpringConfig.class)
  public class BrandServiceTest {
      @Autowired
      private BrandService brandService;
      @Test
      public void testSelectById(){
          System.out.println(brandService.selectById(1));
      }
  }
  ```

### AOP

##### AOP简介

- AOP（Aspect Oriented Programming）面向切面编程，一种编程范式，指导开发者如何组织程序结构
  - OOP（Object Oriented Programming）面向对象编程
- 作用：在不惊动原始设计的基础上为其进行功能增强
- Spring理念：无入侵式/无侵入式

##### AOP核心概念

- 连接点（JoinPoint）：程序执行过程中的任意位置，粒度为执行方法、抛出异常、设置变量等
  - 在SpringAOP中，理解为方法的执行
- 切入点（Pointcut）:匹配连接点的式子
  - 在SpringAOP中，一个切入点可以只描述一个具体方法，也可以匹配多个方法
    - 一个具体方法：cn.ashsmoke.dao包下的BookDao接口中的无形参无返回值的save方法
    - 匹配多个方法：所有的save方法，所有的get开头的方法，所有以Dao结尾的接口中的任意方法，所有带有一个参数的方法
- 通知（Advice）：在切入点处执行的操作，也就是共性功能
  - 在SpringAOP中，功能最终以方法的形式呈现
- 通知类：定义通知的类
- 切面（Aspect）：描述通知与切入点的对应关系

##### AOP快速入门

案例设定：测定接口执行效率

简化设定：在接口执行前输出当前系统时间

思路分析：

1. 导入坐标（pom.xml）
2. 制作连接点方法（原始操作，Dao接口与实现类）
3. 制作共性功能（通知类与通知）
4. 定义切入点
5. 绑定切入点与通知关系（切面）

- 导入坐标

- ```xml
      <dependencies>
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-context</artifactId>
              <version>6.0.11</version>
          </dependency>
          <dependency>
              <groupId>org.aspectj</groupId>
              <artifactId>aspectjweaver</artifactId>
              <version>1.9.19</version>
          </dependency>
      </dependencies>
  ```

  -  说明：spring-context坐标依赖spring-aop坐标

- 定义接口与实现类

- ```java
  public interface BookDao {
      void save();
      void update();
  }
  
  @Repository
  public class BookDaoImpl implements BookDao {
      @Override
      public void save() {
          System.out.println(System.currentTimeMillis());
          System.out.println("book dao save...");
      }
  
      @Override
      public void update() {
          System.out.println("book dao update...");
      }
  }
  ```

- 定义通知类，制作通知

- ```java
  public class MyAdvice {
      public void method(){
          System.out.println(System.currentTimeMillis());
      }
  }
  ```

- 定义切入点

- ```java
  public class MyAdvice {
      @Pointcut("execution(void cn.ashsmoke.dao.BookDao.update())")
      private void pt(){}
  }
  ```

  - 切入点定义依托一个不具有实际意义的方法进行，即无参数，无返回值，方法体无实际逻辑

- 绑定切入点与通知关系，并指定通知添加到原始连接点的具体执行**位置**(@Before（“pt（）”）)

- ```java
  @Component
  @Aspect
  public class MyAdvice {
      @Pointcut("execution(void cn.ashsmoke.dao.BookDao.update())")
      private void pt(){}
      @Before("pt()")
      public void method(){
          System.out.println(System.currentTimeMillis());
      }
  }
  ```

- 定义通知类受Spring容器管理，并定义当前类为切面类

- 开启Spring对AOP注解驱动支持

- ```java
  @Configuration
  @ComponentScan("cn.ashsmoke")
  @EnableAspectJAutoProxy
  public class SpringConfig {
  }
  ```

##### AOP工作流程

1. Spring容器启动

2. 读取所有切面配置中的切入点
   ```java
   @Component
   @Aspect
   public class MyAdvice {
       @Pointcut("execution(void cn.ashsmoke.dao.BookDao.save())")
       private void ptx(){}
       @Pointcut("execution(void cn.ashsmoke.dao.BookDao.update())")
       private void pt(){}
       @Before("pt()")
       public void method(){
           System.out.println(System.currentTimeMillis());
       }
   }
   ```

3. 初始化bean，判定bean对应的类中的方法是否匹配到任意切入点

   - 匹配失败，创建对象
   - 匹配成功，创建原始对象（**目标对象**）的**代理**对象

4. 获取bean执行的方法

   - 获取bean，调用方法并执行，完成操作
   - 获取的bean是代理对象时，根据代理对象的运行模式运行与原始方法与增强的内容，完成操作

##### AOP核心概念

- 目标对象（Target）：原始功能去掉共性功能对应的类产生的对象，这种对象是无法直接完成最终工作的
- 代理（Proxy）：目标对象无法直接完成工作，需要对其进行功能回填，通过原始对象的代理对象实现

##### AOP切入点表达式

- 切入点：要进行增强的方法

- 切入点表达式：要进行增强的方法的描述方式

  - 描述方式一：执行cn.ashsmoke.dao包下BookDao**接口**中无参数update方法

  - `execution(void cn.ashsmoke.dao.BookDao.update())`

  - ```java
    public interface BookDao {
        void update();
    }
    ```

  - 描述方式二：执行cn.ashsmoke.dao.impl包下的BookDaoImpl**类**中的无参数update方法

  - `execution(void cn.ashsmoke.dao.impl.BookDaoImpl.update())`

- 切入点表达式准确格式：动作关键字（访问修饰符  返回值  包名.类/接口名.方法名（参数）异常名）
  `execution(void cn.ashsmoke.service.BrandService.selectById(int))`

  - 动作关键字：描述切入点的行为动作，例如execution表示执行到指定切入点
  - 访问修饰符：public，private等，可以省略
  - 返回值
  - 包名
  - 类/接口名
  - 方法名
  - 参数
  - 异常名：方法定义中抛出指定异常，可以省略

- 可以使用通配符描述切入点，快速描述

  - `*`：单个独立的任意符号，可以独立出现，也可以作为前缀或者后缀的匹配符出现
    `execution(public * cn.ashsmoke.*。BrandService.select*(*))`
    匹配cn.ashsmoke.包下的任意包中的BrandService类或接口中所有select开头的带有一个参数的方法
  - `..`：多个连续的任意符号，可以独立出现，常用于简化包名与参数的书写
    `execution(public Brand cn..BrandService.findById(..))`
    匹配cn包下的任意包中的BrandService类或接口中所有返回类型为Brand、名称为findById的方法
  - `+`：专用于匹配子类类型
    `execution(* *..*Service+.*(..))`
    匹配所有业务层方法

- 书写技巧

  - 所有代码按照标准规范开发，否则以下技巧全部失效
  - 描述切入点**通常描述接口**，而不描述实现类
  - 访问控制修饰符针对接口开发均采用public描述（**可省略访问控制修饰符描述**）
  - 返回值类型对于增删改类使用精准类型加速匹配，对于查询类使用`*`通配快速描述
  - **包名**书写**尽量不使用`..`匹配**，常用`*`做单个包描述匹配，或精准匹配
  - **接口名**/类名书写名称与模块相关的**采用`*`匹配**，例如`BrandService`书写成`*Service`，绑定业务层接口名
  - **方法名**书写以**动词**进行**精准匹配**，名词采用`*`匹配，例如`getBy*`，`selectAll`书写成`selectAll`
  - 参数规则较为复杂，根据业务方法灵活调整
  - 通常**不使用异常**作为**匹配**规则

##### AOP通知类型

- AOP通知描述了抽取的共性功能，根据共性功能抽取的位置不同，最终运行代码时要将其加入到合理的位置
- AOP通知共分5中类型
  - 前置通知
  - 后置通知
  - 环绕通知（重点）
  - 返回后通知（了解）
  - 抛出异常后通知（了解）

------

- 名称：@Before

- 类型：**方法注解**

- 位置：通知方法定义上方

- 作用：设置当前通知方法与切入点之前的绑定关系，当前通知方法在原始切入点方法前运行

- 范例：
  ```java
  @before("pt()")
  public void before(){
      System.out.println("before advice ...");
  }
  ```

- 相关属性：value（默认）：切入点方法名，格式为类名.方法名（）

------

- 名称：@After

- 类型：**方法注解**

- 位置：通知方法定义上方

- 作用：设置当前通知方法与切入点之前的绑定关系，当前通知方法在原始切入点方法后运行

- 范例：

  ```java
  @After("pt()")
  public void after(){
      System.out.println("after advice ...");
  }
  ```

- 相关属性：value（默认）：切入点方法名，格式为类名.方法名（）

------

- 名称：@Around(重点，常用)

- 类型：**方法注解**

- 位置：通知方法定义上方

- 作用：设置当前通知方法与切入点之前的绑定关系，当前通知方法在原始切入点方法前后运行

- 范例：

  ```java
  @After("pt()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable{
      System.out.println("around before advice ...");
      Object ret=pjp.proceed();
      System.out.println("around after advice ...");
      return ret;
  }
  ```

- @Around注意事项

  1. 环绕通知必须依赖形参ProceedingJoinPoint才能实现对原始方法的调用，进而实现原始方法调用前后同时添加通知
  2. 通知中如果未使用ProceedingJoinPoint对原始方法进行调用将跳过原始方法的执行
  3. 对原始方法的调用可以不接收返回值，通知方法设置成void即可，如果接收返回值，必须设定为Object类型
  4. 原始方法的返回值如果是void类型，通知方法的返回值类型可以设置成void，也可以设置成Object
  5. 由于无法预知原始方法运行后是否会抛出异常，因此环绕通知方法必须抛出Throwable对象

------

- 名称：@AfterReturning（了解）

- 类型：**方法注解**

- 位置：通知方法定义上方

- 作用：设置当前通知方法与切入点之前的绑定关系，当前通知方法在原始切入点方法正常执行完毕后运行

- 范例：

  ```java
  @AfterReturning("pt()")
  public void afterReturning(){
      System.out.println("after Returning advice ...");
  }
  ```

- 相关属性：value（默认）：切入点方法名，格式为类名.方法名（）

------

- 名称：@AfterThrowing（了解）

- 类型：**方法注解**

- 位置：通知方法定义上方

- 作用：设置当前通知方法与切入点之前的绑定关系，当前通知方法在原始切入点方法运行抛出异常后执行

- 范例：

  ```java
  @AfterThrowing("pt()")
  public void afterThrowing(){
      System.out.println("after Throwing advice ...");
  }
  ```

- 相关属性：value（默认）：切入点方法名，格式为类名.方法名（）

------

##### AOP通知获取数据

- 获取切入点方法的参数

  - JoinPoint：适用于前置、后置、返回后、抛出异常后通知
  - ProceedJointPoint：适用于环绕通知

- 获取切入点方法返回值

  - 返回后通知
  - 环绕通知

- 获取切入点方法运行异常信息

  - 抛出异常后通知（@AfterThrowing）
  - 环绕通知

  ------

- JoinPoint对象描述了连接点方法的运行状态，可以获取到原始方法的调用参数
  ```java
  @before("pt()")
  public void before(JoinPoint jp){
      Object[] args=jp.getArgs();
      System.out.println(Arrays.toString(args));
  }
  ```

- ProceedJointPoint是JoinPoint的子类

- 环绕通知中可以手工书写对原始方法的调用，得到的结果即为原始方法的返回值

  ```java
  @Around("sendIdPt()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable{
      Object[] args = pjp.getArgs();
      System.out.println(Arrays.toString(args));
      Object ret = pjp.proceed();
      return ret;
  }
  ```

- 返回后通知可以获取切入点方法的返回信息，使用形参可以接收对应的返回对象
  ```java
  @AfterReturning(value = "sendIdPt()",returning = "ret")
  public void afterReturning(Object ret){
      System.out.println("afterReturning advice ..."+ret);
  }
  ```

- 抛出异常后通知可以获取切入点方法的出现的异常信息，使用形参可以接收对应的异常对象
  ```java
  @AfterThrowing(value = "sendIdPt()",throwing = "th")
  public void afterThrowing(Throwable th){
      System.out.println("afterThrowing advice ..."+th);
  }
  ```

##### AOP小结

- 概念：AOP（Aspect Oriented Programming）面向切面编程，一种编程范式
- 作用：在不惊动原始设计的基础上为方法进行功能**增强**
- 核心概念
  - 代理（Proxy）：SpringAOP的核心本质是采用代理模式实现的
  - 连接点（JoinPoint）：在SpringAOP中，理解为任意方法的执行
  - 切入点（PointCut）：匹配连接点的式子，也是具有共性功能的方法描述
  - 通知（Advice）：若干方法的共性功能，在切入点处执行，最终体现为一个方法
  - 切面（Aspect）：描述通知与切入点的对应关系
  - 目标对象（Target）：被代理的原始对象称为目标对象
- 切入点表达式标准格式：动作关键字 （访问修饰符  返回值  包名.类/接口名.方法名（参数） 异常名）
  - `execturion(* cn.ashsmoke.service.*Service.*(..))`
- 切入点表达式描述通配符：
  - 作用：用于快速描述，范围描述
  - `*`：匹配任意符号（**常用**）
  - `..`：匹配多个连续的任意符号（**常用**）
  - `+`：匹配子类类型
- 切入点表达式书写技巧
  1. 按**标准规范**开发
  2. 查询操作的返回值建议使用*匹配
  3. 减少使用..的形式描述包
  4. **对接口进行描述**，使用*表示模块名，例如BrandService的匹配描述为*Service
  5. 方法名书写保留动词，例如get，使用`*`表示名词，例如getById匹配描述为getBy`*`
  6. 参数根据实际情况灵活调整
- 通知类型
  - 前置通知
  - 后置通知
  - 环绕通知（重点）
    - 环绕通知依赖形参ProceedingJoinPoint才能实现对原始方法的调用
    - 环绕通知可以隔离原始方法的调用执行
    - 环绕通知返回值设置为Object类型
    - 环绕通知中可以对原始方法调用过程中出现的异常进行处理
  - 返回后通知（了解）
  - 抛出异常后通知（了解）
- 获取切入点方法的参数
  - JoinPoint：适用于前置、后置、返回后、抛出异常后通知
  - ProceedJointPoint：适用于环绕通知
- 获取切入点方法返回值
  - 返回后通知
  - 环绕通知
- 获取切入点方法运行异常信息
  - 抛出异常后通知（@AfterThrowing）
  - 环绕通知

### Spring事务

##### 事务简介

- 实物作用：在数据层保障一系列的数据库操作同成功同失败
- Spring事务作用：在数据层或**业务层**保障一系列的数据库操作同成功同失败

##### 快速入门

- 在业务接口上添加Spring事务管理
  ```java
  public interface AccountService{
      @Transactional
      public void transfer(String out,String in,Double money);
  }
  ```

  > Spring注解式事务通常添加在业务层接口中而不会添加到业务层实现类中，降低耦合
  >
  > 注解式事务可以添加到业务方法上表示当前方法开启事务，也可以添加到接口上表示当前接口所有方法开启事务

- 设置事务管理器
  ```java
  @Bean
  public PlatformTransactionManager transactionManager(Datasource datasource){
      DataSourceTransactionManager ptm=new DataSourceTransactionManager();
      ptm.serDataSource(dataSource);
      return ptm;
  }
  ```

  > 事务管理器要根据实现技术进行选择
  >
  > Mybatis框架使用的是JDBC事务

- 开启注解式事务驱动
  ```java
  @Configuration
  @ComponentScan("cn.ashsmoke")
  @PropertySource("classpath:jdbc.properties")
  @Import({JdbcConfig.class, MybatisConfig.class})
  @EnableAspectJAutoProxy
  @EnableTransactionManagement
  public class SpringConfig {
  }
  ```

##### 事务角色

- 事务角色
  - 事务管理员：发起事务方，在Spring中通常指代业务层开启事务的方法
  - 事务协调员：加入事务方，在Spring中通常指代数据层方法，也可以是业务层方法

##### 事务属性

**事务相关配置**

|          属性          |             作用             |                   示例                   |
| :--------------------: | :--------------------------: | :--------------------------------------: |
|        readOnly        |      设置是否为只读事务      |        readOnly=true **只读事务**        |
|        timeout         |       设置实物超时时间       |        timeout=-1（**永不超时**）        |
|    **rollbackFor**     |  设置实物回滚异常（class）   |  rollbackFor={NullPointException.class}  |
|  rollbackForClassName  |  设置事务回滚异常（String）  |           **同上格式为字符串**           |
|     noRollbackFor      | 设置事务不回滚异常（class）  | noRollbackFor={NullPointException.class} |
| noRollbackForClassName | 设置事务不回滚异常（String） |           **同上格式为字符串**           |
|      propagation       |       设置事务传播行为       |                  ......                  |

- 事务传播的行为：事务协调员对事务管理员所携带事务的处理态度

  1. 在业务层接口上添加Spring事务，设置事务传播行为**REQUIRES_NEW**（需要新事务）
     ```java
     @Service
     public class LogServiceImpl implements LogService{
         @Autowired
         private LogDao logDao;
         @Transactional(propagation=Propagation.REQUIRES_NEW)
         public void log(String out,String in,Double money){
             logDao.log("转账操作由"+out+"到"+in+",金额"+money)
         }
     }
     ```


- 传播属性

| 传播属性       | 事务管理员 | 事务协调员 |
| -------------- | ---------- | ---------- |
| REQUIRED       | 开启T      | 加入T      |
|                | 无         | 新建T2     |
| REQUIRES\_NEW  | 开启T      | 新建T2     |
|                | 无         | 新建T2     |
| SUPPORTS       | 开启T      | 加入T      |
|                | 无         | 无         |
| NOT\_SUPPORTED | 开启T      | 无         |
|                | 无         | 无         |
| MANDATORY      | 开启T      | 加入T      |
|                | 无         | ERROR      |
| NEVER          | 开启T      | ERROR      |
|                | 无         | 无         |
| NESTED         |            |            |

> nested:设置savePoint，一旦事务回滚，事务将回滚到savePoint处，交由客户响应提交/回滚
