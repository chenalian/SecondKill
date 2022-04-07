# 工程简介
> 基于Springboot的秒杀系统
# 项目架构

1. 数据库表：用户表，商品表，订单表，秒杀商品表，秒杀订单表。
2. Mybatisplus生成基本的配置。
3. 对密码进行两次MD5+salt加密，第一次服务器从前端传输到后端进行加密，服务器存入数据库又进行一次加密MD5+salt。
4. 采用分布式session对会话进行控制，用redis存储会话信息。
5. 对异常做统一处理。
6. 对验证采用拦截器验证用户是否登录。
7. 秒杀接口：先判断秒杀商品数量是否还存在，验证是否重复下单，在进行生成订单表和秒杀订单表。
8. 压力测试-jmeter
9. 多用户进行测试-查看qps

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
P56
压力测试
页面优化
服务优化
接口安全
>
>
>
> IDEA快捷键，Ctrl+Alt+L进行快速格式化，Ctrl+Shift+J快速去空格。



