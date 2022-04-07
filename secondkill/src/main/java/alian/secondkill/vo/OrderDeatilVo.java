package alian.secondkill.vo;

import alian.secondkill.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单详情返回对象
 *
 * @author: alian
 * @date 2022/3/6 10:20 下午
 * @ClassName: OrderDeatilVo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeatilVo {

    private Order tOrder;

    private GoodsVo goodsVo;
}
