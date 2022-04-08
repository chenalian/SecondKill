package alian.secondkill.service.impl;

import alian.secondkill.entity.SeckillOrder;
import alian.secondkill.entity.User;
import alian.secondkill.mapper.SeckillOrderMapper;
import alian.secondkill.service.SeckillOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀订单表 服务实现类
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements SeckillOrderService {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /** 
    * @Description: 查询订单状态，是否下单成功
    * @Param: 
    * @return: 
    * @Author: alian
    * @Date: 2022/4/8
    */
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new
                QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id",
                goodsId));
        if (null != seckillOrder) {
            return seckillOrder.getId();
        } else {
            if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
                return -1L;
            } else {
                return 0L;
            }
        }
    }
}
