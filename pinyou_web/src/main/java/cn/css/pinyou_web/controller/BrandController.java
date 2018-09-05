package cn.css.pinyou_web.controller;

import cn.css.pinyou_manager.pinyou_manager_service.BrandService;
import cn.css.pinyou_pojo.domain.TbBrand;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("findAll")
    public List<TbBrand> findAll(){
        List<TbBrand> brandList = brandService.findAll();
        return brandList;
    }
}
