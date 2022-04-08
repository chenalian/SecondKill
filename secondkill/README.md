# 工程简介
> 基于Springboot的秒杀系统
# 项目搭建
1. lombok是一种可以省去基本的set/get方法和注解插件。
2. 自定义注解
>   - 自定义类的注解，创建类的过程中自动生成，设置方法在setting->Editor->File and  Code Templates->class进行添加
```java
/**
* @program: ${PROJECT_NAME}
*
* @author: alian
*
* @description: ${description}
*
* @create: ${YEAR}-${MONTH}-${DAY} ${HOUR}:${MINUTE}
  **/
>```
>  - 自定义方法注解，file->setting->editor->Live templates->添加自定义模板，并添加如下模板信息，触发方式：*+tab
```java
/*
**
* @Description: $description$
* @Param: $params$
* @return: $returns$
* @Author: lydms
* @Date: $date$
  */
>```

3. MD5加密
>MD5加密，防止明文进行传输，md5本质是一种哈希算法且不可逆，采用前端密码+salt->md5加密->传输->后端->+salt+md5加密->数据库存储
前后加密和后端加密的密匙可以使不一样的，数据库存储的是后端加密的密匙！  
常见的加密算法有：Base64，Md5，AES，DES
```
<!-- md5 依赖 -->
<dependency>
<groupId>commons-codec</groupId>
<artifactId>commons-codec</artifactId>
</dependency>
<dependency>
<groupId>org.apache.commons</groupId>
<artifactId>commons-lang3</artifactId>
<version>3.6</version>
</dependency>
```
4. 自定义验证注解
   
>自定义电话号码验证注解
5. 统一异常处理
>第一种 使用@ControllerAdvice和@ExceptionHandler注解
> 
> 第二种 使用ErrorController
> 
> 区别
1.注解@ControllerAdvice方式只能处理控制器抛出的异常。此时请求已经进入控制器中。
2.类ErrorController方式可以处理所有的异常，包括未进入控制器的错误，比如404,401等错误
3.如果应用中两者共同存在，则@ControllerAdvice方式处理控制器抛出的异常，类ErrorController方式未进入控制器的异常。
4.@ControllerAdvice方式可以定义多个拦截方法，拦截不同的异常类，并且可以获取抛出的异常信息，自由度更大
6. 分布式session
> 1. 采用cookie和session记录用户信息。
> 2. 分布式session-用redis存储
> -  1.使用springsession,添加以下依赖
```
<!-- spring data redis 依赖 -->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- commons-pool2 对象池依赖 -->
<dependency>
<groupId>org.apache.commons</groupId>
<artifactId>commons-pool2</artifactId>
</dependency>
<!-- spring-session 依赖 -->
<dependency>
<groupId>org.springframework.session</groupId>
<artifactId>spring-session-data-redis</artifactId>
</dependency>

```
> - 将用户信息存入Redis
```
<!-- spring data redis 依赖 -->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- commons-pool2 对象池依赖 -->
<dependency>
<groupId>org.apache.commons</groupId>
<artifactId>commons-pool2</artifactId>
</dependency
```
>！！！需要配置redistemplate的序列化
```java
@Configuration
public class RedisConfig {
   @Bean
   public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory
                                                             connectionFactory){
      RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
      //key序列器
      redisTemplate.setKeySerializer(new StringRedisSerializer());
      //value序列器
      redisTemplate.setValueSerializer(new
              GenericJackson2JsonRedisSerializer());
      //Hash类型 key序列器
      redisTemplate.setHashKeySerializer(new StringRedisSerializer());
      //Hash类型 value序列器
      redisTemplate.setHashValueSerializer(new
                GenericJackson2JsonRedisSerializer());
      redisTemplate.setConnectionFactory(connectionFactory);
      return redisTemplate;
   }
}
```
7. 查询User信息优化
>- 每次接口请求都会根据userticket查询用户的信息，进行判断，可以使用HandlerMethodArgumentResolver进行统一处理，
>- 可以对controller接口包含特定的参数可以提前统一解析并且传递，这是是根据userticket在redis中查询出user信息进行传递。
>- 在查询用户是否合法的过程中，若用户不和法可以直接通过response.sendRedirect(request.getContextPath()+"/");从定向到login.html页面中去。
8. 秒杀功能
> - 秒杀商品信息查询API
> - 秒杀商品详情查询API
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

