package alian.secondkill.mapper;

import alian.secondkill.entity.Goods;
import alian.secondkill.vo.GoodsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /** 
    * @Description: 获取商品列表
    * @Param: 
    * @return: 
    * @Author: alian
    * @Date: 2022/4/7
    */
    List<GoodsVo> findGoodsVo();
    /** 
    * @Description: 根据秒杀商品的id获取商品的详细信息
    * @Param: 
    * @return: 
    * @Author: alian
    * @Date: 2022/4/7
    */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
