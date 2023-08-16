package cn.ashsmoke.service.impl;

import cn.ashsmoke.dao.impl.BookDaoImpl;
import cn.ashsmoke.dao.impl.UserDaoImpl;
import cn.ashsmoke.service.BookService;

public class BookServiceImpl implements BookService {
    //删除业务层中使用new的方式创建的dao对象
    private BookDaoImpl bookDao;
    private UserDaoImpl userDao;

    public BookServiceImpl(BookDaoImpl bookDao, UserDaoImpl userDao) {
        this.bookDao = bookDao;
        this.userDao = userDao;
    }

    @Override
    public void save() {
        System.out.println("bookService save");
        bookDao.save();
        userDao.save();
    }

    public void setBookDao(BookDaoImpl bookDao) {
        this.bookDao = bookDao;
    }
}
