package cn.ashsmoke;

import cn.ashsmoke.service.BookService;
import cn.ashsmoke.service.impl.BookServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.sql.DataSource;

public class App {
    public static void main(String[] args) {
        //获取IoC容器
        ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
        //FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("D:\\JavaProject\\SpringFramework\\spring_01_quickstart\\src\\main\\resources\\applicationContext.xml");
       // ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("bean1.xml", "bean2.xml");
        //获取bean
//        BookService bookService =ctx.getBean("service",BookService.class);
//        bookService.save();
        DataSource dadaSource = ctx.getBean("dataSource", DataSource.class);
        System.out.println(dadaSource);
    }
}
