package cn.ashsmoke.dao;

import cn.ashsmoke.pojo.Brand;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface BrandDao {
    @Select("select * from tb_brand")
    @Results(id="brandResultMap",
            value = {
            @Result(column = "brand_name",property = "brandName"),
            @Result(column = "company_name",property = "companyName")
            }
    )
    List<Brand> selectAll();

    @Insert("insert into tb_brand values (null,#{brandName},#{companyName},#{ordered},#{description},#{status})")
    void add(Brand brand);
    @Select("select * from tb_brand where id=#{id}")
    @ResultMap("brandResultMap")
    @ResultType(Brand.class)
    Brand selectById(int id);
    @Update("update tb_brand set brand_name=#{brandName},company_Name=#{companyName},ordered=#{ordered}" +
            ",description=#{description},status=#{status} where id=#{id}")
    void update(Brand brand);
    @Delete("delete from tb_brand where id=#{id}")
    void deleteById(int id);
}
