package cn.ashsmoke;

import cn.ashsmoke.config.SpringConfig;
import cn.ashsmoke.pojo.Brand;
import cn.ashsmoke.service.BrandService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        BrandService brandService = ctx.getBean("brandService",BrandService.class);
        List<Brand> brands = brandService.selectAll();
        brands.forEach(brand -> System.out.println(brand));
    }
}
