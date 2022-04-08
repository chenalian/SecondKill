package alian.secondkill.service;

import alian.secondkill.entity.Order;
import alian.secondkill.entity.User;
import alian.secondkill.vo.GoodsVo;
import alian.secondkill.vo.OrderDeatilVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
public interface OrderService extends IService<Order> {
    Order seckill(User user, GoodsVo goods);

    OrderDeatilVo detail(Long orderId);

    boolean checkPath(User user, Long goodsId, String path);

    String createPath(User user, Long goodsId);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
