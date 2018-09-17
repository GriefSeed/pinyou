package cn.css.pinyou_manager.pinyou_manager_serviceImpl;

import java.util.List;
import java.util.Map;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_manager.pinyou_manager_service.TypeTemplateService;
import cn.css.pinyou_mapper.TbSpecificationOptionMapper;
import cn.css.pinyou_mapper.TbTypeTemplateMapper;
import cn.css.pinyou_pojo.domain.TbSpecificationOption;
import cn.css.pinyou_pojo.domain.TbSpecificationOptionExample;
import cn.css.pinyou_pojo.domain.TbTypeTemplate;
import cn.css.pinyou_pojo.domain.TbTypeTemplateExample;
import cn.css.pinyou_pojo.domain.TbTypeTemplateExample.*;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbTypeTemplate> findAll() {
        return typeTemplateMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbTypeTemplate typeTemplate) {
        typeTemplateMapper.insert(typeTemplate);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbTypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKey(typeTemplate);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbTypeTemplate findOne(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            typeTemplateMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbTypeTemplateExample example = new TbTypeTemplateExample();
        Criteria criteria = example.createCriteria();

        if (typeTemplate != null) {
            if (typeTemplate.getName() != null && typeTemplate.getName().length() > 0) {
                criteria.andNameLike("%" + typeTemplate.getName() + "%");
            }
            if (typeTemplate.getSpecIds() != null && typeTemplate.getSpecIds().length() > 0) {
                criteria.andSpecIdsLike("%" + typeTemplate.getSpecIds() + "%");
            }
            if (typeTemplate.getBrandIds() != null && typeTemplate.getBrandIds().length() > 0) {
                criteria.andBrandIdsLike("%" + typeTemplate.getBrandIds() + "%");
            }
            if (typeTemplate.getCustomAttributeItems() != null && typeTemplate.getCustomAttributeItems().length() > 0) {
                criteria.andCustomAttributeItemsLike("%" + typeTemplate.getCustomAttributeItems() + "%");
            }

        }

        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }


    @Override
    public List<Map> findMapList(Long id) {
        //1、先根据模板的ID，查询出模板的规格数据，并转成Map集合
        TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        //2、遍历每一个Map，把Map对象再添加上我们想要的属性options:map.get("id");根据规格Id查询规格选项列表放在map的options属性中
        List<Map> mapList = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
        for (Map map : mapList) {
            //规格选项集合，怎么获取：根据规格Id查询规格选性别集合
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            example.createCriteria().andSpecIdEqualTo(new Long((Integer) map.get("id")));
            List<TbSpecificationOption> optionList = specificationOptionMapper.selectByExample(example);
            map.put("options", optionList);
        }
        //返回结果集
        return mapList;
    }
}
