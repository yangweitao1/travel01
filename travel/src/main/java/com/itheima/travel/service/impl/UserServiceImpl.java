package com.itheima.travel.service.impl;

import com.itheima.travel.constant.Constant;
import com.itheima.travel.dao.UserDao;
import com.itheima.travel.domain.User;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.UserService;
import com.itheima.travel.utils.MailUtil;
import com.itheima.travel.utils.Md5Util;
import com.itheima.travel.utils.UuidUtil;

/**
 * 包名:com.itheima.travel.service.impl
 * 作者:Leevi
 * 日期2019-07-16  08:41
 */
public class UserServiceImpl implements UserService{
    private UserDao userDao = (UserDao) BeanFactory.getBean("userDao");

    @Override
    public User findUserByUsername(String username) {
        //调用dao层的方法根据username查询用户信息
        return userDao.findUserByUsername(username);
    }

    @Override
    public void doRegister(User user) throws Exception {
        //1. 调用dao层的方法，保存用户信息
        //1.1 因为status和code，客户端没有传过来，所以我们要自己设置status和code
        //status表示激活状态:如果是已激活则为"Y",如果是未激活则为"N"，code表示激活码其实就是一串唯一的字符串
        user.setStatus(Constant.UNACTIVE);
        //使用UUIDUtil获取一个唯一的字符串，作为激活码
        String code = UuidUtil.getUuid();
        user.setCode(code);

        //1.2 客户端传过来的密码，没有加密，我们应该先将密码加密之后，再存入数据库
        String password = user.getPassword();
        //使用MD5Util对password进行加密
        password = Md5Util.encodeByMd5(password);
        user.setPassword(password);

        //1.3 调用dao层的方法，将user的信息保存到数据库中
        userDao.saveUser(user);

        //2. 使用Java代码给注册用户的邮箱发送激活邮件
        MailUtil.sendMail(user.getEmail(),"欢迎注册黑马旅游网，我亲爱的用户:"+user.getName()+",请点击<a href='http://localhost:8080/travel70/user?action=active&code="+user.getCode()+"'>激活</a>");
    }

    @Override
    public int doActive(String code) {
        //1.根据code查询用户，判断激活码是否正确
        User user = userDao.findUserByCode(code);
        if (user != null) {
            //激活码正确
            //2.判断是否已经激活过了
            if (user.getStatus().equals(Constant.UNACTIVE)) {
                //未激活,对齐进行激活:调用dao层的方法，修改user的status为"Y"
                user.setStatus(Constant.ACTIVED);
                userDao.updateUser(user);
                return 1;
            }else {
                //已经激活过了，请勿重复激活
                return -1;
            }
        }else {
            //激活码错误
            return -2;
        }
    }

    @Override
    public User doLogin(String username, String password) {
        //1.调用dao层的方法，根据username查找用户信息，判断username是否正确
        User user = userDao.findUserByUsername(username);
        if (user != null) {
            //用户名正确
            //2.校验密码是否正确,同user获取的密码就是数据库中的密码
            //将客户端传入的密码进行MD5加密，然后使用加密后的密码和数据库中的密码进行比对
            try {
                password = Md5Util.encodeByMd5(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (password.equals(user.getPassword())) {
                //密码正确
                //3.校验是否已激活，也就是判断user的status属性是否为"Y"
                if (user.getStatus().equals(Constant.ACTIVED)) {
                    //已激活
                    //登录成功
                    return user;
                }else {
                    //未激活
                    throw new RuntimeException("用户未激活");
                }
            }else {
                //密码错误
                throw new RuntimeException("密码错误");
            }
        }else {
            //用户名错误
            throw new RuntimeException("用户名错误");
        }
    }
}
