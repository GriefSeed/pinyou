package cn.css.pinyou_mapper;


import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_pojo.domain.TbBrand;

import java.util.List;

public interface TbBrandMapper {

    public List<TbBrand> findAll();

    PageResult findPage(Integer pageNum, Integer pageSize);

    List<TbBrand> search(TbBrand brand);

    void add(TbBrand brand);

    void update(TbBrand brand);

    TbBrand findOne(Long id);

    void delete(Long id);
}
