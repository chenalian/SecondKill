package alian.secondkill.controller;


import alian.secondkill.entity.User;
import alian.secondkill.service.OrderService;
import alian.secondkill.vo.OrderDeatilVo;
import alian.secondkill.vo.RespBean;
import alian.secondkill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单详情
     *
     * @param user
     * @param orderId
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        OrderDeatilVo detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }
}

