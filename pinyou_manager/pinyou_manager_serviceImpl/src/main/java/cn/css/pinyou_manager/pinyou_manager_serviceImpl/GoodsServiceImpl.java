package cn.css.pinyou_manager.pinyou_manager_serviceImpl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.css.pinyou_dto.GoodsVo;
import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_manager.pinyou_manager_service.GoodsService;
import cn.css.pinyou_mapper.*;
import cn.css.pinyou_pojo.domain.TbBrand;
import cn.css.pinyou_pojo.domain.TbGoods;
import cn.css.pinyou_pojo.domain.TbGoodsExample;
import cn.css.pinyou_pojo.domain.TbGoodsExample.Criteria;

import cn.css.pinyou_pojo.domain.TbItem;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbGoods goods) {
		goodsMapper.insert(goods);
	}

	/**
	 * 上面那个没用，这个才是添加，将描述和主体分离，保证事务，在service层插入所有
	 * @param vo
	 */
	@Override
	public void addVo(GoodsVo vo) {
		//1、添加商品基本信息
		//设置没有的属性
		vo.getGoods().setAuditStatus("0");
		goodsMapper.insertSelective(vo.getGoods());
		//主键返回
		//2、添加商品描述信息
		vo.getGoodsDesc().setGoodsId(vo.getGoods().getId());
		goodsDescMapper.insertSelective(vo.getGoodsDesc());

        //3、添加商品SKU
        //List<TbItem> itemList = vo.getItemList();
        for(TbItem item : vo.getItemList()){

            //在商品中标题是商品SPU名+规格名
            String title = vo.getGoods().getGoodsName();
            Map<String,Object> map = JSON.parseObject(item.getSpec(), Map.class);
            for(String key : map.keySet()){
                title += "  " + map.get(key);
            }
            item.setTitle(title);
            item.setCategoryid(vo.getGoods().getCategory3Id());
            item.setStatus("1");
            item.setCreateTime(new Date());
            item.setUpdateTime(new Date());
            item.setGoodsId(vo.getGoods().getId());
            item.setSellerId(vo.getGoods().getSellerId());

            //添加品牌名
            TbBrand brand = brandMapper.findOne(vo.getGoods().getBrandId());
            item.setBrand(brand.getName());
            //分类名
            item.setCategory(itemCatMapper.selectByPrimaryKey(vo.getGoods().getCategory3Id()).getName());
            //商家名
            item.setSeller(sellerMapper.selectByPrimaryKey(vo.getGoods().getSellerId()).getNickName());
            //图片
            List<Map> mapList = JSON.parseArray(vo.getGoodsDesc().getItemImages(), Map.class);
            if(mapList.size() > 0){
                item.setImage((String) mapList.get(0).get("url"));
            }
            //添加
            itemMapper.insertSelective(item);
        }
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
