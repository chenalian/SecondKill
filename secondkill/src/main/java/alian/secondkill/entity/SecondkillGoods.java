package alian.secondkill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 秒杀商品表
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Getter
@Setter
@TableName("t_secondkill_goods")
public class SecondkillGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private LocalDateTime startDate;

    /**
     * 秒杀结束时间
     */
    private LocalDateTime endDate;


}
