package cn.css.pinyou_manager.pinyou_manager_serviceImpl;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_manager.pinyou_manager_service.BrandService;
import cn.css.pinyou_mapper.TbBrandMapper;
import cn.css.pinyou_pojo.domain.TbBrand;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {


    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.findAll();
    }

    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //先设置分页的属性
        PageHelper.startPage(pageNum,pageSize);
        //搜索
        Page<TbBrand> page = (Page<TbBrand>)brandMapper.findAll();
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public PageResult search(Integer pageNum, Integer pageSize, TbBrand brand) {
        //设置分页属性
        PageHelper.startPage(pageNum,pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.search(brand);
        return new PageResult(page.getTotal(),page.getResult());
    }


    @Override
    public void add(TbBrand brand) {
        brandMapper.add(brand);
    }


    @Override
    public void update(TbBrand brand) {
        brandMapper.update(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.findOne(id);
    }

    @Override
    public void delete(Long[] ids) {
        for(Long id : ids){
            brandMapper.delete(id);
        }
    }
}
