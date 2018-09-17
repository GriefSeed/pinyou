package cn.css.pinyou_shop_web.controller;

import cn.css.pinyou_dto.GoodsVo;
import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_dto.Result;
import cn.css.pinyou_manager.pinyou_manager_service.GoodsService;
import cn.css.pinyou_pojo.domain.TbGoods;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody GoodsVo vo) {
        try {

            //spring-security获取当前登录者（商家）名
            String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
            vo.getGoods().setSellerId(loginName);

            goodsService.addVo(vo);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbGoods goods) {
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbGoods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        //根据当前商家登录者来查询
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(loginName);

        return goodsService.findPage(goods, page, rows);
    }

    @RequestMapping("/marketableStatus")
    public Result marketableStatus(Long[] ids, String status) {
        try {
            goodsService.marketableStatus(ids, status);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }

}
