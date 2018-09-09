package cn.css.pinyou_manager.pinyou_manager_serviceImpl;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_dto.Specification;
import cn.css.pinyou_mapper.TbSpecificationMapper;
import cn.css.pinyou_mapper.TbSpecificationOptionMapper;
import cn.css.pinyou_pojo.domain.TbSpecification;
import cn.css.pinyou_pojo.domain.TbSpecificationExample;
import cn.css.pinyou_pojo.domain.TbSpecificationOption;
import cn.css.pinyou_manager.pinyou_manager_service.SpecificationService;
import cn.css.pinyou_pojo.domain.TbSpecificationOptionExample;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    @Override
    public void delete(Long[] ids) {
        for(Long id : ids){
            //规格删除，根据主键
            specificationMapper.deleteByPrimaryKey(id);
            //删除规格选项呢？
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);
        }
    }

    @Override
    public void update(Specification vo) {

        //修改规格
        specificationMapper.updateByPrimaryKeySelective(vo.getSpecification());
        //修改规格选项呢
        //先删除再添加
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(vo.getSpecification().getId());
        specificationOptionMapper.deleteByExample(example);

        List<TbSpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
        for(TbSpecificationOption option : specificationOptionList){
            //设置选项对应的规格ID
            option.setSpecId(vo.getSpecification().getId());
            specificationOptionMapper.insertSelective(option);
        }

    }

    @Override
    public void add(Specification vo) {

        //先添加规格
        specificationMapper.insertSelective(vo.getSpecification());

        //再添加规格选项
        List<TbSpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
        for(TbSpecificationOption option : specificationOptionList){
            //设置选项对应的规格ID
            option.setSpecId(vo.getSpecification().getId());
            specificationOptionMapper.insertSelective(option);
        }

    }

    @Override
    public Specification findOne(Long id) {

        //先查询规格
        TbSpecification specification = specificationMapper.selectByPrimaryKey(id);
        //再查询规格选项
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(id);
        example.setOrderByClause("orders asc");
        List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.selectByExample(example);
        //封装返回结果集
        Specification vo = new Specification();
        vo.setSpecificationOptionList(specificationOptionList);
        vo.setSpecification(specification);

        return vo;
    }

    @Override
    public PageResult search(Integer pageNum, Integer pageSize, TbSpecification specification) {

        //设置分页属性
        PageHelper.startPage(pageNum,pageSize);

        //判断条件的！
        TbSpecificationExample example = new TbSpecificationExample();
        TbSpecificationExample.Criteria criteria = example.createCriteria();
        if(specification.getId() != null){
            criteria.andIdEqualTo(specification.getId());
        }
        if(specification.getSpecName() != null){
            criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
        }
        Page<TbSpecification> page = (Page<TbSpecification>)specificationMapper.selectByExample(example);

        return new PageResult(page.getTotal(),page.getResult());
    }
}
