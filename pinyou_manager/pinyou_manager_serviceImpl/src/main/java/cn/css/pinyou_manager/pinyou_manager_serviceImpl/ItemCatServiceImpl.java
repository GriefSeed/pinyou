package cn.css.pinyou_manager.pinyou_manager_serviceImpl;

import java.util.ArrayList;
import java.util.List;

import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_manager.pinyou_manager_service.ItemCatService;
import cn.css.pinyou_mapper.TbItemCatMapper;
import cn.css.pinyou_pojo.domain.TbItemCat;
import cn.css.pinyou_pojo.domain.TbItemCatExample;
import cn.css.pinyou_pojo.domain.TbItemCatExample.*;
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
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbItemCat itemCat) {
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbItemCat itemCat) {
        itemCatMapper.updateByPrimaryKey(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids, ArrayList<Long> idsChilds) {
        for (Long id : ids) {
            itemCatMapper.deleteByPrimaryKey(id);
        }
        for (Long childIds : idsChilds) {
            itemCatMapper.deleteByPrimaryKey(childIds);
        }
    }


    @Override
    public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();

        if (itemCat != null) {
            if (itemCat.getName() != null && itemCat.getName().length() > 0) {
                criteria.andNameLike("%" + itemCat.getName() + "%");
            }

        }

        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 需求：根据父id查询子节点
     */
    @Override
    public List<TbItemCat> findItemCatList(Long parentId) {
        //创建商品分类example对象
        TbItemCatExample example = new TbItemCatExample();
        //创建criteria对象
        Criteria criteria = example.createCriteria();
        //设置查询参数
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> catList = itemCatMapper.selectByExample(example);
        return catList;
    }


}
