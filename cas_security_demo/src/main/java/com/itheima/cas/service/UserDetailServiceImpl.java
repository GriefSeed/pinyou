package com.itheima.cas.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //设置权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // 因为密码验证已经交给CAS了，所以spring-security只需要直接根据用户名查询权限即可，但这里数据表没有设计权限字段，所以直接手工写上了
        /*List<SysUser> list = userDao.findByUserName(username);
        if(list == null || list.size()==0){
            return null;
        }
        SysUser sysUser = list.get(0);
        String pwd = sysUser.getPassword();
        // 2. 封装用户具有的角色
        // 2.1 返回角色集合
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        // 2.2 获取用户角色，
        List<Role> roles = sysUser.getRoles();
        // 2.3 遍历角色
        if (roles!=null && roles.size()>0) {
            for(Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            }
        }
        User user = new User(username,pwd,authorities);
        return user;*/
        return new User(username, "", authorities);
    }
}
