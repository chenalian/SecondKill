/**
 * @program: secondkill
 * @author: alian
 * @description: 查询商品表和秒杀商品表的返回对象
 * @create: 2022-04-06 23:58
 **/

package alian.secondkill.vo;

import alian.secondkill.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {
  private BigDecimal seckillPrice;
  private Integer stockCount;
  private Date startDate;
  private Date endDate;
}