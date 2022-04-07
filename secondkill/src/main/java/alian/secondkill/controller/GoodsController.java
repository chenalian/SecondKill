package alian.secondkill.controller;


import alian.secondkill.entity.User;
import alian.secondkill.service.GoodsService;
import alian.secondkill.service.UserService;
import alian.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

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
     * @Description: 秒杀列表查询
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/7
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
    public String toLogin(User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }

    /**
     * @Description: 查询秒杀商品的详细信息
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/7
     */
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable Long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        // 设置秒杀状态和秒杀剩余时间
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //剩余开始时间
        int remainSeconds = 0;
        //秒杀还未开始
        if (nowDate.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
            // 秒杀已结束
        } else if (nowDate.after(endDate)) {
            secKillStatus = 2;
            remainSeconds = -1;
            // 秒杀中
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goodsDetail";
    }
}

