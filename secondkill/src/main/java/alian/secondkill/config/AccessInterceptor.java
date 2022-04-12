/**
 * @program: secondkill
 * @author: alian
 * @description: API次数访问拦截器
 * @create: 2022-04-08 10:01
 **/

package alian.secondkill.config;

import alian.secondkill.entity.User;
import alian.secondkill.exception.GlobalException;
import alian.secondkill.service.UserService;
import alian.secondkill.util.CookieUtil;
import alian.secondkill.validator.AccessLimit;
import alian.secondkill.vo.RespBean;
import alian.secondkill.vo.RespBeanEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
@Component
public class AccessInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    public AccessInterceptor(UserService userService) {
        this.userService = userService;
    }

    /**
    * @Description: 提前处理
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/8
    */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler) throws Exception {
        // 处理方法
        if (handler instanceof HandlerMethod) {
            // 登录请求不进行拦截
            if(request.getRequestURI().equals("/")||request.getRequestURI().equals("/login/doLogin")){
                return true;
            }
            // 拦截所有请求并判断是否登录，如果登录的话，就把user信息存入Context中进行传递
            User user = getUser(request, response);
            UserContext.setUser(user);

//            //登录请求之外所有的请求度需要user
//            if(user==null){
//                throw new GlobalException(RespBeanEnum.LOGIN_OUTDATE_ERROR);
//            }

            // 在判断是否存在着AccessLimit注解
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;// 不存在的话直接返回
            }
            //在对时间和次数进行判断
            int second = accessLimit.second();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {// 需要登录
//                if (user == null) {
//                    //直接跳转到登录页
//                    throw new GlobalException(RespBeanEnum.LOGIN_OUTDATE_ERROR);
//                }
                key += ":" + user.getId();
            }
            // 判断次数和时会否过期，API名称：url:userid
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer) valueOperations.get(key);
            if (count == null) {
                valueOperations.set(key, 1, second, TimeUnit.SECONDS);
            } else if (count < maxCount) {
                valueOperations.increment(key);
            } else {
                render(response, RespBeanEnum.ORDER_LIMIT_ERROR);
                return false;
            }
        }
        return true;
    }

    /**
    * @Description: 将提示信息直接输出
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/8
    */
    private void render(HttpServletResponse response, RespBeanEnum respBeanEnum)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        RespBean bean = RespBean.error(respBeanEnum);
        out.write(new ObjectMapper().writeValueAsString(bean));
        out.flush();
        out.close();
    }

    // 根据Cookie查询用户
    private User getUser(HttpServletRequest request, HttpServletResponse
            response) throws IOException, ServletException {
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (StringUtils.isEmpty(ticket)) {
            // 对user不存的查询直接从定向到login页面
//            response.sendRedirect(request.getContextPath()+"/");
//            return null;
            throw new GlobalException(RespBeanEnum.LOGIN_OUTDATE_ERROR);
        }
        return userService.getByUserTicket(ticket, request, response);
    }
}
