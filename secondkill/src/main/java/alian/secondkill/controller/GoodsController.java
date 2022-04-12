package alian.secondkill.controller;


import alian.secondkill.entity.User;
import alian.secondkill.service.GoodsService;
import alian.secondkill.util.JsonUtil;
import alian.secondkill.vo.DetailVo;
import alian.secondkill.vo.GoodsVo;
import alian.secondkill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * @Description: 秒杀列表查询
     * 优化前：1016
     * 将列表信息直接存入redis
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/7
     */
    @RequestMapping(value = "/toList")
    @ResponseBody
    public Map<String,Object> toLogin(HttpServletRequest request, HttpServletResponse
            response, User user) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //Redis中获取页面，如果不为空，直接返回页面
        String goods= (String) valueOperations.get("goodsList");
        HashMap<String,Object> map=new HashMap<>();
        if (goods!=null) {
            List<GoodsVo> goodsVo = (List<GoodsVo>) JsonUtil.jsonToList(goods,GoodsVo.class);
            map.put("msg","查询成功");
            map.put("count",goodsVo.size());
            map.put("code",200);
            map.put("data",goodsVo);
            return map;
        }

        List<GoodsVo> goodsVo1 = goodsService.findGoodsVo();
        valueOperations.set("goodsList",JsonUtil.object2JsonStr(goodsVo1),60, TimeUnit.SECONDS);
        map.put("code",200);
        map.put("msg","查询成功");
        map.put("count",goodsVo1.size());
        map.put("data",goodsVo1);
        return map;
    }
    /**
     * @Description: 查询秒杀商品的详细信息
     * @Param: 优化中：资源静态化-前后端分离
     * @return:
     * @Author: alian
     * @Date: 2022/4/7
     */
    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(HttpServletRequest request, HttpServletResponse
            response, Model model, User user,
                             @PathVariable Long goodsId) {
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //剩余开始时间
        int remainSeconds = 0;
        //秒杀还未开始
        if (nowDate.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
            // 秒杀已结束
        } else if (nowDate.after(endDate)) {
            secKillStatus = 2;
            remainSeconds = -1;
            // 秒杀中
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(goods);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(secKillStatus);
        return RespBean.success(detailVo);
    }
}

