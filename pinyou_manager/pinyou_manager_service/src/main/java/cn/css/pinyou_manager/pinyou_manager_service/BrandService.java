package cn.css.pinyou_manager.pinyou_manager_service;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_pojo.domain.TbBrand;
import java.util.List;
import java.util.Map;

public interface BrandService {

    /**
     * 好习惯：在接口上加注释
     * 品牌列表
     * @return
     */
    public List<TbBrand> findAll();

    /**
     * 分页查找所有品牌，已经没用，使用search代替findpage，一进来就是条件为空的搜索，这样就不用做两个服务了
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @return
     */
    PageResult findPage(Integer pageNum, Integer pageSize);

    PageResult search(Integer pageNum, Integer pageSize, TbBrand brand);

    /**
     * 新增品牌
     * @param brand
     * @return
     */
    void add(TbBrand brand);

    /**
     * 修改
     * @param brand
     */
    void update(TbBrand brand);

    /**
     * 修改功能，前端跟新增功能共用，就是返回一个数据，代替新增功能entity而已，所以要查找
     * @param id
     * @return
     */
    TbBrand findOne(Long id);

    /**
     * 根据多个ID来删除品牌
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 需求:查询品牌下拉列表
     * 查询数据格式：[{id:'1',text:'联想'},{id:'2',text:'华为'}]
     * 返回值：List<Map>
     *
     */
    public List<Map> findBrandList();

}
