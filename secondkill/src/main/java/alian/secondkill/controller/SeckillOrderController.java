package alian.secondkill.controller;


import alian.secondkill.entity.Order;
import alian.secondkill.entity.SeckillOrder;
import alian.secondkill.entity.User;
import alian.secondkill.exception.GlobalException;
import alian.secondkill.rabbitmq.MQSender;
import alian.secondkill.service.GoodsService;
import alian.secondkill.service.OrderService;
import alian.secondkill.service.SeckillOrderService;
import alian.secondkill.util.JsonUtil;
import alian.secondkill.validator.AccessLimit;
import alian.secondkill.vo.GoodsVo;
import alian.secondkill.vo.RespBean;
import alian.secondkill.vo.RespBeanEnum;
import alian.secondkill.vo.SeckillMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 秒杀订单表 前端控制器
 * </p>
 *
 * @author alian
 * @since 2022-04-06
 */
@Slf4j
@Controller
@RequestMapping("/secondkill")
public class SeckillOrderController implements InitializingBean {
    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    //内存标记 key：秒杀商品ID，value：是否售空
    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();

    @Autowired
    private MQSender mqSender;

    // redis执行lua脚本配置
    @Autowired
    private RedisScript<Long> redisScript;

    /**
     * @Description: 秒杀API
     * @Param: 优化前：929
     * 秒杀静态化：直接返回数据
     * @return:
     * @Author: alian
     * @Date: 2022/4/7
     */
    @ResponseBody
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    public RespBean doSeckill(@PathVariable String path, Model model, User user, Long goodsId) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return RespBean.error(RespBeanEnum.SECONDKILL_REQUEST_ERROR);
        }
        //判断是否重复抢购
        String seckillOrderJson = (String) valueOperations.get("order:" +
                user.getId() + ":" + goodsId);
        if (!StringUtils.isEmpty(seckillOrderJson)) {
            return RespBean.error(RespBeanEnum.SECONDKILL_REPECT_ERROR);
        }
        //内存标记,减少Redis访问
        Boolean aBoolean = EmptyStockMap.get(goodsId);
        if (aBoolean != null && aBoolean) {
            return RespBean.error(RespBeanEnum.SECONDKILL_NOSTOCKS_ERROR);
        }
        //预减库存
        //Long stock = valueOperations.decrement("seckillGoods:" + goodsId);

        //预减库存->减库存和判断库存原子性执行
        Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);

        if (stock == null || stock < 0) {
            EmptyStockMap.put(goodsId, true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.SECONDKILL_NOSTOCKS_ERROR);
        }
        // 请求入队，立即返回排队中
        SeckillMessage message = new SeckillMessage(user, goodsId);
        mqSender.sendsecKillMessage(JsonUtil.object2JsonStr(message));
        return RespBean.success(0);
    }

    /**
     * @Description: 获取订单状态，是否下单成功
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/8
     */
    @RequestMapping(value = "/getResult", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    /**
     * @Description: 获取秒杀地址，并且5秒内最多能访问5次
     * @Param:
     * @return:
     * @Author: alian
     * @Date: 2022/4/8
     */
    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha) {
        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.SECONDKILL_CAPTCHA_ERROR);
        }
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    @ResponseBody
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
        // 设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码，将结果放入redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);

        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, captcha.text
                (), 300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }
    }

    /** 
    * @Description: 在seckillOrderController进行初始化时候就将库存存入redis中
    * @Param: 
    * @return: 
    * @Author: alian
    * @Date: 2022/4/8
    */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(),
                    goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        });
    }
}


