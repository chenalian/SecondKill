package alian.secondkill.service.impl;

import alian.secondkill.entity.Order;
import alian.secondkill.mapper.OrderMapper;
import alian.secondkill.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
