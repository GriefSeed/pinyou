package cn.css.pinyou_dto;


import cn.css.pinyou_pojo.domain.TbGoods;
import cn.css.pinyou_pojo.domain.TbGoodsDesc;
import cn.css.pinyou_pojo.domain.TbItem;

import java.io.Serializable;
import java.util.List;

public class GoodsVo implements Serializable {

    private TbGoods goods;

    private TbGoodsDesc goodsDesc;

    private List<TbItem> itemList;

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
