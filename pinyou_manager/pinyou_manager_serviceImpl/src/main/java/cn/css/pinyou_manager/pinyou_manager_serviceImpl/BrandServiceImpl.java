package cn.css.pinyou_manager.pinyou_manager_serviceImpl;

import cn.css.pinyou_manager.pinyou_manager_service.BrandService;
import cn.css.pinyou_mapper.TbBrandMapper;
import cn.css.pinyou_pojo.domain.TbBrand;
import com.alibaba.dubbo.config.annotation.Service;
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
}
