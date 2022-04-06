package alian.secondkill.service.impl;

import alian.secondkill.entity.User;
import alian.secondkill.exception.GlobalException;
import alian.secondkill.mapper.UserMapper;
import alian.secondkill.service.UserService;
import alian.secondkill.util.*;
import alian.secondkill.vo.LoginVo;
import alian.secondkill.vo.RespBean;
import alian.secondkill.vo.RespBeanEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author alian
 * @since 2022-04-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    
    @Override
    public RespBean login(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        /*
        *
        * 参数合法性判断，已经由自定义注解完成验证
        *
        * */
        //        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
        //            return RespBean.error(RespBeanEnum.LOGIN_EMPTY_ERROR);
        //        }
        //        if(!ValidatorUtil.isMobile(mobile)){
        //            return RespBean.error(RespBeanEnum.LOGIN_MOBILE_ERROR);
        //        }
        User user = userMapper.selectById(mobile);
        /*
        *
        * 统一异常处理
        *
        * */
        if(user==null){
//            return RespBean.error(RespBeanEnum.LOGIN_NOT_EXIST_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_NOT_EXIST_ERROR);
        }
        // 校验密码
        if(!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
//             return RespBean.error(RespBeanEnum.LOGIN_PASSWD_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_PASSWD_ERROR);
        }

        // 方法一：直接用cookie进行存储
//        //生成cookie
//        String ticket= UUIDUtil.uuid();
//        request.getSession().setAttribute(ticket,user);
//        //用户的表示也存入cookie
//        CookieUtil.setCookie(request,response,"userTicket",ticket);
        // 方法二：采用redis进行存储，user存储在redis中，userticket存储在cookie中
        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:" + ticket,
                JsonUtil.object2JsonStr(user));
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success(ticket);
    }

    /**
    * @Description: 根据userticket获取user
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/6
    */
    @Override
    public User getByUserTicket(String userTicket, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        String userJson = (String) redisTemplate.opsForValue().get("user:" +
                userTicket);
        if(userJson==null){
            // 对user不存的查询直接从定向到login页面
            response.sendRedirect(request.getContextPath()+"/");
            return null;
        }
        User user = JsonUtil.jsonStr2Object(userJson, User.class);
        if (null != user) {
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }
}
