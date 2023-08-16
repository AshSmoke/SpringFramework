package cn.ashsmoke.dao.impl;

import cn.ashsmoke.dao.BookDao;

import java.util.*;

public class BookDaoImpl implements BookDao {
//    private String databaseName;
//    private int connectionNumber;
//
//    public BookDaoImpl(String databaseName, int connectionNumber) {
//        this.databaseName = databaseName;
//        this.connectionNumber = connectionNumber;
//    }
//
//    public BookDaoImpl() {
//    }
//
//    @Override
//    public void save() {
//        System.out.println("bookDao save"+databaseName+","+connectionNumber);
//    }
    private int[] array;
    private List<String> list;
    private Set<String> set;
    private Map<String,String> map;
    private Properties properties;

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "BookDaoImpl{" +
                "array=" + Arrays.toString(array) +
                ", list=" + list +
                ", set=" + set +
                ", map=" + map +
                ", properties=" + properties +
                '}';
    }

    @Override
    public void save() {
        System.out.println(this);
    }
}
