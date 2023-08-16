package cn.ashsmoke.service;

import cn.ashsmoke.pojo.Brand;

import java.util.List;

public interface BrandService {
    Brand selectById(int id);
    List<Brand> selectAll();
    void update(Brand brand);
    void deleteById(int id);
    void add(Brand brand);
}
