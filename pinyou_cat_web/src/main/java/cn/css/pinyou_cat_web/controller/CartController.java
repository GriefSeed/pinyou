package cn.css.pinyou_cat_web.controller;

import cn.css.pinyou_cat_service.CartService;
import cn.css.pinyou_dto.Cart;
import cn.css.pinyou_dto.Result;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    //http://cart.pinyougou,com/cart/addGoodsToCartList?itemId=1369292&num=1
    //是商品详情页面过来添加商品到购物车中的，那么你需要告诉它添加是否成功
    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(HttpSession session, Long itemId, Integer num){
        try {
            //1、获取SessionID
            String key = session.getId();
            //获取当前登录者用户名
            String name = SecurityContextHolder.getContext().getAuthentication().getName();

            if("anonymousUser".equals(name)){
                //2、根据SessionID去redis中获取购物车集合
                List<Cart> cartList = cartService.queryCartListByRedis(key);
                //3、把商品加到购物车集合中
                cartList = cartService.addGoodsToCartList(cartList,itemId,num);
                //4、再新的购物车集合放在redis中
                cartService.addCartListToRedis(key, cartList);
                //5、返回结果集
                return new Result(true,"操作成功");
            } else{

                List<Cart> cartList = cartService.queryCartListByRedis(name);
                //3、把商品加到购物车集合中
                cartList = cartService.addGoodsToCartList(cartList,itemId,num);
                //4、再新的购物车集合放在redis中
                cartService.addCartListToRedis(name, cartList);
                //5、返回结果集
                return new Result(true,"操作成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("/findCartList")
    public List<Cart> findCartList(HttpSession session){
        //1、获取HTTPSessionId
        String key = session.getId();
        //获取当前登录者用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        //2、根据sessionID去redis中获取数据
        List<Cart> cartList_session = cartService.queryCartListByRedis(key);

        if("anonymousUser".equals(name)){
            //3、返回结果集
            return cartList_session;
        }
        //上面的Session中的集合有没有，如果没有直接跳过，如果有：此时应该和sessionID对应的购物车集合整合
        //获取当前登录者对应的数据
        List<Cart> cartList = cartService.queryCartListByRedis(name);
        if(cartList_session.size() > 0){
            //合并购物车
            cartList=cartService.mergeCartList(cartList, cartList_session);
            //清除redis中sessionId的数据
            cartService.delCartListToRedis(key);
            //将合并后的数据存入redis
            cartService.addCartListToRedis(name, cartList);
        }
        return cartList;


    }


}
