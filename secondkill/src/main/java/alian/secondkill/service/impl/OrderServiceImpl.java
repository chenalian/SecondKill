package alian.secondkill.service.impl;

import alian.secondkill.entity.Order;
import alian.secondkill.entity.SeckillOrder;
import alian.secondkill.entity.SecondkillGoods;
import alian.secondkill.entity.User;
import alian.secondkill.mapper.OrderMapper;
import alian.secondkill.service.OrderService;
import alian.secondkill.service.SeckillOrderService;
import alian.secondkill.service.SecondkillGoodsService;
import alian.secondkill.vo.GoodsVo;
import ch.qos.logback.core.joran.spi.NoAutoStart;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    /** 
    * @Description: 秒杀接口
    * @Param: 
    * @return: 
    * @Author: alian
    * @Date: 2022/4/7
    */
    @Override
    public Order seckill(User user, GoodsVo goods) {
        //秒杀商品表减库存
        SecondkillGoods seckillGoods = seckillGoodsService.getOne(new
                QueryWrapper<SecondkillGoods>().eq("goods_id",
                goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsService.updateById(seckillGoods);
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
        return order;
    }
}
