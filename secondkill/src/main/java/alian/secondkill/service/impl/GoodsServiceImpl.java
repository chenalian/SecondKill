package alian.secondkill.service.impl;

import alian.secondkill.entity.Goods;
import alian.secondkill.mapper.GoodsMapper;
import alian.secondkill.service.GoodsService;
import alian.secondkill.vo.GoodsVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    /**
    * @Description: 查询所有逇秒杀商品
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/7
    */
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }
    /**
    * @Description: 查询单个的秒杀商品的信息
    * @Param:
    * @return:
    * @Author: alian
    * @Date: 2022/4/7
    */
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
