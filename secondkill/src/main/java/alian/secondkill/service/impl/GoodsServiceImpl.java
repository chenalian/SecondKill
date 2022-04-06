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

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }
}
