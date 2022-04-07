package alian.secondkill.controller;


import alian.secondkill.entity.Order;
import alian.secondkill.entity.SeckillOrder;
import alian.secondkill.entity.User;
import alian.secondkill.service.GoodsService;
import alian.secondkill.service.OrderService;
import alian.secondkill.service.SeckillOrderService;
import alian.secondkill.vo.GoodsVo;
import alian.secondkill.vo.RespBeanEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 秒杀订单表 前端控制器
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Controller
@RequestMapping("/secondkill")
public class SeckillOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private GoodsService goodsService;

    /**
    * @Description: 秒杀API
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/7
    */
    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user, Long goodsId) {

        model.addAttribute("user", user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.SECONDKILL_NOSTOCKS_ERROR.getMessage());
            return "seckillFail";
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new
                QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq(
                "goods_id",
                goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.SECONDKILL_REPECT_ERROR.getMessage());
            return "seckillFail";
        }
        Order order = orderService.seckill(user, goods);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);
        return "orderDetail";
    }
}

