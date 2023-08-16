package cn.ashsmoke;

import cn.ashsmoke.config.SpringConfig;
import cn.ashsmoke.dao.BookDao;
import cn.ashsmoke.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppForAnnotation {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx=new AnnotationConfigApplicationContext(SpringConfig.class);
        BookDao boodDao = ctx.getBean("bookDao", BookDao.class);
        System.out.println(boodDao);
        BookService bookService = ctx.getBean("bookService", BookService.class);
        System.out.println(bookService);
    }
}
