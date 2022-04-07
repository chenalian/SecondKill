package alian.secondkill.controller;


import alian.secondkill.entity.Order;
import alian.secondkill.entity.SeckillOrder;
import alian.secondkill.entity.User;
import alian.secondkill.service.GoodsService;
import alian.secondkill.service.OrderService;
import alian.secondkill.service.SeckillOrderService;
import alian.secondkill.vo.GoodsVo;
import alian.secondkill.vo.RespBean;
import alian.secondkill.vo.RespBeanEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    private RedisTemplate redisTemplate;

    /**
    * @Description: 秒杀API
    * @Param: 优化前：929
     * 秒杀静态化：直接返回数据
    * @return:
    * @Author: alian
    * @Date: 2022/4/7
    */
    @RequestMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.SECONDKILL_NOSTOCKS_ERROR);
        }
        //判断是否重复抢购-redis中进行查看订单
        String seckillOrderJson = (String)
                redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (!StringUtils.isEmpty(seckillOrderJson)) {
            return RespBean.error(RespBeanEnum.SECONDKILL_REPECT_ERROR);
        }
        Order order = orderService.seckill(user, goods);
        return RespBean.success(order);
    }
}

