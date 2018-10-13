package cn.css.pinyou_search_serivce;

import java.util.Map;

public interface SearchService {
    /**
     * 根据Map参数查询搜索库数据
     * @param map 使用Map来代替DTO传输数据，Map具有通用性
     * @return
     */
    Map search(Map map);

    /**
     * 根据商品ID查询tbItem的SKU列表并导入索引库
     * @param goodsId
     */
    public void importItem(Long goodsId);

    /**
     * 根据商品ID删除索引库数据
     * @param goodsId
     */
    void deleteItem(Long goodsId);
}
