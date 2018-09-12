package cn.css.pinyou_shop_web.service;

import cn.css.pinyou_manager.pinyou_manager_service.SellerService;
import cn.css.pinyou_pojo.domain.TbSeller;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    // 只有服务注入使用模板生成的service，其余的实现UserDetailsService接口，补充方法
    @Reference
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //设置权限集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //1、根据用户名查询商家
        TbSeller seller = sellerService.findOne(username);

        //2、判断此商家是否存在
        if (seller != null) {
            //2.1 如果存在，判断商家状态是否审核通过
            if ("1".equals(seller.getStatus())) {//2.1.1 如果状态审核通过那么根据用户名和密码判断登录
                return new User(username, seller.getPassword(), authorities);
            }
        }
        //2.2 如果不存在直接返回null；
        return null;
    }
}
