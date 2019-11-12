package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /*
    根据用户名  查询用户信息
     */
    public User findByUsername(String username);
 int a =10;
    /*
    用来保存用户
     */
    public void save(User user);

    User findByUsernameAndPassword(String username, String password);

    int b =200;
}
