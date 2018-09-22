package cn.css.pinyou_page_services;

public interface PageService {
    /**
     * 根据商品ID生成静态页面
     *
     * @param goodsId
     */
    void creataHtml(Long goodsId);


    /**
     * 根据商品ID删除商品静态页面
     *
     * @param goodsId
     */
    void delHtml(Long goodsId);
}
