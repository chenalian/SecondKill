package alian.secondkill.service.impl;

import alian.secondkill.entity.Order;
import alian.secondkill.entity.SeckillOrder;
import alian.secondkill.entity.SecondkillGoods;
import alian.secondkill.entity.User;
import alian.secondkill.exception.GlobalException;
import alian.secondkill.mapper.OrderMapper;
import alian.secondkill.service.GoodsService;
import alian.secondkill.service.OrderService;
import alian.secondkill.service.SeckillOrderService;
import alian.secondkill.service.SecondkillGoodsService;
import alian.secondkill.util.JsonUtil;
import alian.secondkill.vo.GoodsVo;
import alian.secondkill.vo.OrderDeatilVo;
import alian.secondkill.vo.RespBeanEnum;
import ch.qos.logback.core.joran.spi.NoAutoStart;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private SecondkillGoodsService seckillGoodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Description: 秒杀接口,生成的订单存入redis中，限制一个用户只能限购一件商品
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/7
     */
    @Override
    public Order seckill(User user, GoodsVo goods) {
        // 可能会造成超卖的情况
        //秒杀商品表减库存
        //        SecondkillGoods seckillGoods = seckillGoodsService.getOne(new
        //                QueryWrapper<SecondkillGoods>().eq("goods_id",
        //                goods.getId()));
        //        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        //        seckillGoodsService.updateById(seckillGoods);
        // 减库存的时候判断库存是否足够
        SecondkillGoods seckillGoods = seckillGoodsService.getOne(new
                QueryWrapper<SecondkillGoods>().eq("goods_id",
                goods.getId()));
//        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);

//        boolean update = seckillGoodsService.update(new UpdateWrapper<SecondkillGoods>().set("stock_count",
//                seckillGoods.getStockCount()).eq("id", seckillGoods.getId()).gt("stock_count",
//                0));
        boolean seckillGoodsResult = seckillGoodsService.update(new UpdateWrapper<SecondkillGoods>()
                .setSql("stock_count = " + "stock_count-1")
                .eq("goods_id", goods.getId())
                .gt("stock_count", 0)
        );
        if(!seckillGoodsResult) throw new GlobalException(RespBeanEnum.SECONDKILL_NOSTOCKS_ERROR);
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        // 秒杀订单存入redis中，进行判断用户只能限购一件
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(), JsonUtil.object2JsonStr(seckillOrder));
        return order;
    }
    /**
     * @Description: 查询订单详情
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/7
     */
    @Override
    public OrderDeatilVo detail(Long orderId) {
        if (null == orderId) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDeatilVo detail = new OrderDeatilVo();
        detail.setGoodsVo(goodsVo);
        detail.setTOrder(order);
        return detail;
    }
}
