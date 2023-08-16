package cn.ashsmoke.service.impl;

import cn.ashsmoke.dao.BrandDao;
import cn.ashsmoke.pojo.Brand;
import cn.ashsmoke.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("brandService")
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;
    @Override
    public Brand selectById(int id) {
        return brandDao.selectById(id);
    }

    @Override
    public List<Brand> selectAll() {
        return brandDao.selectAll();
    }

    @Override
    public void update(Brand brand) {
        brandDao.update(brand);
    }

    @Override
    public void deleteById(int id) {
        brandDao.deleteById(id);
    }

    @Override
    public void add(Brand brand) {
        brandDao.add(brand);
    }
}
