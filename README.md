# SecondKill
基于Springboot的秒杀系统
# 版本二 mian2分支
9. 压力测试
> - 下载并安装Jmeter。
> - Jmeter进行压力测试，会出现超卖的情况。
10. 页面优化
> - 页面缓存，直接将页面存储在redis中。
> - 页面静态化，将秒杀商品详情和订单详情页面进行静态化出来。
11. 解决超卖的问题
> - 减库存的时候判断库粗是否足够，
```java
方法一：不能解决超卖的问题
SecondkillGoods seckillGoods = seckillGoodsService.getOne(new
    QueryWrapper<SecondkillGoods>().eq("goods_id",
    goods.getId()));
seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
boolean update = seckillGoodsService.update(new UpdateWrapper<SecondkillGoods>().set("stock_count",
    seckillGoods.getStockCount()).eq("id", seckillGoods.getId()).gt("stock_count",
    0));
```
```java
方法二：可以解决超卖的问题
SecondkillGoods seckillGoods = seckillGoodsService.getOne(new
    QueryWrapper<SecondkillGoods>().eq("goods_id",
    goods.getId()));
    boolean seckillGoodsResult = seckillGoodsService.update(new UpdateWrapper<SecondkillGoods>()
    .setSql("stock_count = " + "stock_count-1")
    .eq("goods_id", goods.getId())
    .gt("stock_count", 0)
    );
    
    // 减库存的操作必须和判断库存的操作要满足原子性，要不然会出现不一致的问题
```
> - 解决同一用户同时秒杀多件商品。
可以通过数据库建立唯一索引避免
11. 服务优化-接口优化
> - RabbitMQ用做消息队列(centos进行安装)，对流量进行消峰
##### 具体步骤
1. 系统初始化，把商品库存数量加载到Redis
2. 收到请求，Redis预减库存。库存不足，直接返回。否则进入第3步
3. 请求入队，立即返回排队中
4. 请求出队，生成订单，减少库存
5. 客户端轮询，是否秒杀成功
> - redis做分布式锁：1，通过setIfabsent进行占位，2，通过设置过期key过期时间，3，通过Lua脚本保证原子性。
> - 可以设置内存标志，用MAP存储是否当前商品抢购完。
>
12. 接口安全
> - 接口限流：限制每个用户规定时间内访问接口的次数，用拦截器+注解的方式实现。对User对象获取进行改进，在每次请求的过程中将User存入ThreadLocal中，每次请求API都会分配一个私有的ThreadLocal
>  当ThreadLocal汇总没有User信息的时候，根据userTicket进行获取。
> - 隐藏接口地址:通过动态获取接口参数进行隐藏.
> - 验证码
13. 项目总结
>1. 项目搭建
>- 1. springboot环境搭建
>- 2. 集成tymeleaf,RespBean
>- 3. mybatis-Plus
>2. 分布式会话
>- 1. 用户登录：1，设计数据库，明文密码两次md5加密，参数校验和全局异常处理。
>- 2. 共享session：redis
>3. 功能开发
> - 1.商品列表，2，商品详情，3，秒杀，4，订单详情。
>4. 系统压测
>- 1. jmeter压测
>5. 页面优化
>- 1. 页面缓存，URL缓存，对象缓存。
>- 2. 页面静态化，前后端分离。
>- 3. rabbitmq异步下单，流量消峰。
>6. 安全优化
>- 1. 隐藏秒杀接口
>- 2. 算数验证码
>- 3. 接口防刷
14. 可能出现的问题？
缓存的一致性有哪些解决方案？
> 1. 先更新缓存，后更新数据库。
> 2. 先更新数据库，后更新缓存。
> 3. 先删除缓存，在跟新数据库，在删除缓存。
> 4. 先跟新数据库，在删除缓存。
缓存更新失败的解决方案？
> 1. 用消息队列的失败重试。
> 2. 用canal去监听binglog日志。
