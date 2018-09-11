package cn.css.pinyou_web.controller;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_dto.Result;
import cn.css.pinyou_manager.pinyou_manager_service.BrandService;
import cn.css.pinyou_pojo.domain.TbBrand;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        List<TbBrand> brandList = brandService.findAll();
        return brandList;
    }

    // 分页查询的方法，已经没用了，使用search代替findPage，一进来就使用搜索刷新,不搜索时条件为空就行，Mybatis做个动态SQL
    /*@RequestMapping("/findPage")
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        PageResult pr = brandService.findPage(pageNum, pageSize);
        System.out.println(pr + "==============================");
        return pr;
    }*/

    @RequestMapping("/search")
    public PageResult search(Integer pageNum, Integer pageSize, @RequestBody TbBrand brand) {
        return this.brandService.search(pageNum, pageSize, brand);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand brand) {
        try {
            this.brandService.add(brand);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand brand) {
        try {
            this.brandService.update(brand);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }


    @RequestMapping("findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    @RequestMapping("delete")
    public Result delete(Long[] ids) {
        try {
            this.brandService.delete(ids);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }
    /**
     * 查询数据格式：[{id:'1',text:'联想'},{id:'2',text:'华为'}]
     * 返回值：List<Map>
     *
     */
    @RequestMapping("findBrandList")
    public List<Map> findBrandList() {
        //调用远程service服务方法
        List<Map> brandList = brandService.findBrandList();
        return  brandList;
    }

}
