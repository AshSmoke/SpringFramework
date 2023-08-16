package cn.ashsmoke.dao.impl;

import cn.ashsmoke.dao.BookDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Repository("bookDao")
@Scope("singleton")

public class BookDaoImpl implements BookDao {
    @Value("${connectionNum}")
    private String connectionNum;
    @Override
    public void save() {
        System.out.println("bool dao save..."+connectionNum);
    }
//    @PostConstruct
//    public void init(){
//        System.out.println("book dao init...");
//    }
//    @PreDestroy
//    public void destroy(){
//        System.out.println("book dao destroy...");
//    }
}
