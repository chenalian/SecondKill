package alian.secondkill.controller;


import alian.secondkill.entity.User;
import alian.secondkill.service.GoodsService;
import alian.secondkill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;


    /**
     * 跳转登录页
     *
     * @return
     */
    // 方法一：没有参数拦截器
//    @RequestMapping("/toList")
//    public String toLogin(HttpServletRequest request,HttpServletResponse
//            response, Model model,
//                          @CookieValue("userTicket") String ticket) {
//        if (StringUtils.isEmpty(ticket)) {
//            return "login";
//        }
//        // 必须之前判断用户是否存在
//        User user=userService.getByUserTicket(ticket,request,response);
//        if (null == user) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//        return "goodsList";
//    }
    // 存在参数拦截器
    @RequestMapping("/toList")
    public String toLogin(User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        return "goodsList";
    }
}

