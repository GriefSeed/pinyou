package cn.css.pinyou_web.controller;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_dto.Result;
import cn.css.pinyou_dto.Specification;
import cn.css.pinyou_pojo.domain.TbSpecification;
import cn.css.pinyou_manager.pinyou_manager_service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;


    @RequestMapping("search")
    public PageResult search(Integer pageNum, Integer pageSize, @RequestBody TbSpecification specification){
        return specificationService.search(pageNum,pageSize,specification);
    }

    @RequestMapping("add")
    public Result add(@RequestBody Specification vo){
        try {
            specificationService.add(vo);
            return new Result(true,"操作成功" );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("findOne")
    public Specification findOne(Long id){
        return this.specificationService.findOne(id);
    }

    @RequestMapping("update")
    public Result update(@RequestBody Specification vo){
        try {
            specificationService.update(vo);
            return new Result(true,"操作成功" );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("delete")
    public Result delete(Long[] ids){
        try {
            specificationService.delete(ids);
            return new Result(true,"操作成功" );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    /**
     * 需求：查询规格值，进行下拉列表展示
     */
    @RequestMapping("findSpecList")
    public List<Map> findSpecList() {
        return specificationService.findSpecList();
    }


}
