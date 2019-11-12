package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;

public class UserServiceImpl implements UserService {
    private UserDao userDao=new UserDaoImpl();

    /*
    注册用户的类
     */

    @Override
    public boolean regist(User user) {
        //根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());
        //判断u是否为nall
        if(u!=null){
            return false;
        }else {

            //保存用户信息
            userDao.save(user);
        }
        return true;
    }
    /*
    登录方法
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
