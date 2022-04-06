package alian.secondkill.service;

import alian.secondkill.entity.Goods;
import alian.secondkill.vo.GoodsVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
public interface GoodsService extends IService<Goods> {
    List<GoodsVo> findGoodsVo();
}
