package cn.ashsmoke;

import cn.ashsmoke.config.SpringConfig;
import cn.ashsmoke.dao.BookDao;
import cn.ashsmoke.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppForManager {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx=new AnnotationConfigApplicationContext(SpringConfig.class);
        BookDao bookDao = ctx.getBean("bookDao", BookDao.class);
        BookService bookservice = ctx.getBean("bookService", BookService.class);
        bookservice.save();
    }
}
