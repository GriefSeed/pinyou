package cn.css.pinyou_manager.pinyou_manager_service;


import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_dto.Specification;
import cn.css.pinyou_pojo.domain.TbSpecification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {

    /**
     * 根据条件分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param specification
     * @return
     */
    PageResult search(Integer pageNum, Integer pageSize, TbSpecification specification);

    /**
     * 新增规格
     *
     * @param vo
     */
    void add(Specification vo);

    /**
     * 根据规格ID查询规格信息
     *
     * @param id
     * @return
     */
    public Specification findOne(Long id);

    /**
     * 修改
     *
     * @param vo
     */
    void update(Specification vo);

    /**
     * 根据ID删除规格
     *
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 需求：查询规格值，进行下拉列表展示
     */
    public List<Map> findSpecList();

}
