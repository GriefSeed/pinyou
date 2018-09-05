package cn.css.pinyou_manager.pinyou_manager_service;

import cn.css.pinyou_pojo.domain.TbBrand;
import java.util.List;
public interface BrandService {

    /**
     * 好习惯：在接口上加注释
     * 品牌列表
     * @return
     */
    public List<TbBrand> findAll();
}
