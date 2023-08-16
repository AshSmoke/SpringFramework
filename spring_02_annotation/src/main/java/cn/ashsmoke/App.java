package cn.ashsmoke;

import cn.ashsmoke.dao.BookDao;
import cn.ashsmoke.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookDao bookDao = ctx.getBean("bookDao", BookDao.class);

        BookService bookservice = ctx.getBean("bookService", BookService.class);

    }
}
