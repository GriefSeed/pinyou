package cn.css.pinyou_page_servicesImpl;

import cn.css.pinyou_mapper.TbGoodsDescMapper;
import cn.css.pinyou_mapper.TbGoodsMapper;
import cn.css.pinyou_mapper.TbItemCatMapper;
import cn.css.pinyou_mapper.TbItemMapper;
import cn.css.pinyou_page_services.PageService;
import cn.css.pinyou_pojo.domain.TbGoods;
import cn.css.pinyou_pojo.domain.TbGoodsDesc;
import cn.css.pinyou_pojo.domain.TbItem;
import cn.css.pinyou_pojo.domain.TbItemExample;
import com.alibaba.dubbo.config.annotation.Service;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class PageServiceImpl implements PageService {
    @Autowired
    private FreeMarkerConfig freemarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 根据商品ID生成静态页面,就是将模板读进内存，将查询出来的数据与模板合并，然后使用java IO文件流输出静态页面
     *
     * @param goodsId
     */
    @Override
    public void creataHtml(Long goodsId) {
        try {
            //1、只根据freemarkerConfig获取一个对象就可以了
            Configuration configuration = freemarkerConfig.getConfiguration();
            //2、获取模板
            Template template = configuration.getTemplate("item.ftl");
            //3、根据商品Id获取商品的信息
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            //根据商品的ID查询商品详细信息
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            //根据goods对象中的categoryID查询分类的对象name, 供面包屑导航栏使用
            String itemCat1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            //根据商品ID查询SKU列表
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(goodsId);
            criteria.andStatusEqualTo("1");
            example.setOrderByClause("is_default desc");
            List<TbItem> itemList = itemMapper.selectByExample(example);
            //4、用Map对象封装商品的信息给静态页面传递
            Map map = new HashMap<>();
            map.put("goods", goods);
            map.put("goodsDesc", goodsDesc);
            map.put("itemCat1Name", itemCat1Name);
            map.put("itemCat2Name", itemCat2Name);
            map.put("itemCat3Name", itemCat3Name);
            map.put("itemList", itemList);
            //5、指定输出的路径
            FileWriter fileWriter = new FileWriter(new File("E:\\item\\" + goodsId + ".html"));
            //6、开始输出
            template.process(map, fileWriter);
            //7、关闭流
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
