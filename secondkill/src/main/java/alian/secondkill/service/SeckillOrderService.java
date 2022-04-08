package alian.secondkill.service;

import alian.secondkill.entity.SeckillOrder;
import alian.secondkill.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 秒杀订单表 服务类
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
public interface SeckillOrderService extends IService<SeckillOrder> {
    Long getResult(User user, Long goodsId);
}
