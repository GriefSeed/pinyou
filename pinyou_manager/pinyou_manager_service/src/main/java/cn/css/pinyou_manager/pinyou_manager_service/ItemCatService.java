package cn.css.pinyou_manager.pinyou_manager_service;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_pojo.domain.TbItemCat;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface ItemCatService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbItemCat> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(TbItemCat itemCat);


    /**
     * 修改
     */
    public void update(TbItemCat itemCat);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public TbItemCat findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids, ArrayList<Long> idsChilds);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize);


    /**
     * 需求：根据父id查询子节点
     */
    public List<TbItemCat> findItemCatList(Long parentId);

}
