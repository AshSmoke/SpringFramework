package cn.ashsmoke.dao.impl;

import cn.ashsmoke.dao.UserDao;

public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("UserDao save");
    }
}
