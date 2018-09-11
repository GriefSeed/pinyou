package cn.css.pinyou_mapper;


import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_pojo.domain.TbBrand;

import java.util.List;
import java.util.Map;

public interface TbBrandMapper {

    public List<TbBrand> findAll();

    PageResult findPage(Integer pageNum, Integer pageSize);

    List<TbBrand> search(TbBrand brand);

    void add(TbBrand brand);

    void update(TbBrand brand);

    TbBrand findOne(Long id);

    void delete(Long id);

    /**
     * 需求:查询品牌下拉列表
     * 查询数据格式：[{id:'1',text:'联想'},{id:'2',text:'华为'}]
     * 返回值：List<Map>
     *
     */
    public List<Map> findBrandList();

}
