package cn.ashsmoke;

import cn.ashsmoke.config.SpringConfig;
import cn.ashsmoke.dao.BookDao;
import cn.ashsmoke.dao.impl.BookDaoImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        BookDao bookDao = ctx.getBean(BookDao.class);
        bookDao.sendId(20);
    }
}
