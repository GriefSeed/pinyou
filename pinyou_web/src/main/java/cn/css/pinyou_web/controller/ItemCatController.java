package cn.css.pinyou_web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_dto.Result;
import cn.css.pinyou_manager.pinyou_manager_service.ItemCatService;
import cn.css.pinyou_pojo.domain.TbItemCat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbItemCat> findAll() {
        return itemCatService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return itemCatService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbItemCat itemCat) {
        try {
            itemCatService.add(itemCat);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbItemCat itemCat) {
        try {
            itemCatService.update(itemCat);
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
    public TbItemCat findOne(Long id) {
        return itemCatService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody Long[] idss) {
        try {
            ArrayList<Long> delIdList = new ArrayList<>();
            for (Long parentId : idss) {
                // 子一级单个所有
                List<TbItemCat> list = itemCatService.findItemCatList(parentId);
                if (list != null && !list.isEmpty()) {
                    // 递归遍历添加所有子级
                    myDelMethod(list, delIdList);
                }
            }
            // 删除父级和所有相关子级Id
            itemCatService.delete(idss, delIdList);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }


    public void myDelMethod(List<TbItemCat> list, ArrayList<Long> delIdList) {
        List<TbItemCat> temp;
        for (TbItemCat tt : list) {
            // 加入单个子级Id
            delIdList.add(tt.getId());
            // 获取单个子级Id的子级列表
            temp = itemCatService.findItemCatList(tt.getId());
            if (temp != null && !temp.isEmpty()) {
                for (TbItemCat ttChilds : temp) {
                    // 将子级Id的子级Id加入
                    delIdList.add(ttChilds.getId());
                    // 递归，继续获取子级的单个子级的子级列表
                    myDelMethod(temp, delIdList);
                }
            }
        }
    }


    /**
     * 查询+分页
     *
     * @param brand
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbItemCat itemCat, int page, int rows) {
        return itemCatService.findPage(itemCat, page, rows);
    }

    /**
     * 需求：根据父id查询子节点
     */
    @RequestMapping("/findByParentId")
    public List<TbItemCat> findByParentId(Long parentId) {
        return itemCatService.findItemCatList(parentId);
    }


}
