package cn.css.pinyou_content_serviceImpl;

import java.util.List;

import cn.css.pinyou_content_service.ContentCategoryService;
import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_mapper.TbContentCategoryMapper;
import cn.css.pinyou_pojo.domain.TbContentCategory;
import cn.css.pinyou_pojo.domain.TbContentCategoryExample;
import cn.css.pinyou_pojo.domain.TbContentCategoryExample.Criteria;
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
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbContentCategory> findAll() {
        return contentCategoryMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContentCategory> page = (Page<TbContentCategory>) contentCategoryMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContentCategory contentCategory) {
        contentCategoryMapper.insert(contentCategory);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContentCategory contentCategory) {
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContentCategory findOne(Long id) {
        return contentCategoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            contentCategoryMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbContentCategory contentCategory, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria criteria = example.createCriteria();

        if (contentCategory != null) {
            if (contentCategory.getName() != null && contentCategory.getName().length() > 0) {
                criteria.andNameLike("%" + contentCategory.getName() + "%");
            }

        }

        Page<TbContentCategory> page = (Page<TbContentCategory>) contentCategoryMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
