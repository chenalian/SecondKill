/**
 * @program: secondkill
 * @author: alian
 * @description: 用户参数拦截
 * @create: 2022-04-06 11:57
 **/

package alian.secondkill.config;

import alian.secondkill.entity.User;
import alian.secondkill.service.UserService;
import alian.secondkill.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserService userService;

    /**
     * @Description: 方法supportsParameter很好理解，返回值是boolean类型，
     * 它的作用是判断Controller层中的参数，是否满足条件，
     * 满足条件则执行resolveArgument方法，不满足则跳过。
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/6
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
//        for(c: clazz){
//            if(c==User.class){
//                return true;
//            }
//        }
//        return false;
        return clazz == User.class;


//        return parameter.getParameterType().isAssignableFrom(User.class);
//                && parameter.hasParameterAnnotation(CurrentUser.class)
    }

    /**
     * @Description: 而resolveArgument方法呢，它只有在supportsParameter方法返回true的情况下才会被调用。
     * 用于处理一些业务，将返回值赋值给Controller层中的这个参数。
     * 方法一：通过cookie记录分配的userticket
     * 方法二：通过ThreadLocal记录当前User
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/6
     */
//    @Override
//    public Object resolveArgument(MethodParameter parameter,
//                                  ModelAndViewContainer mavContainer,
//                                  NativeWebRequest webRequest,
//                                  WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest request =
//                webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response =
//                webRequest.getNativeResponse(HttpServletResponse.class);
//
//        String ticket = CookieUtil.getCookieValue(request, "userTicket");
//        if (StringUtils.isEmpty(ticket)) {
//            // 对user不存的查询直接从定向到login页面
//            response.sendRedirect(request.getContextPath()+"/");
//            return null;
//        }
//
//        return userService.getByUserTicket(ticket, request, response);
//    }
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        //直接通过
        return UserContext.getUser();
    }
}
