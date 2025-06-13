# 权限

## 使用

#### 指定系统默认鉴权方式

````
启动类增加注解，可指定多种鉴权，逻辑或
@DefaultAuth(AuthTypeEnum.NO_AUTH)
````

#### 指定接口的鉴权方式

````
接口增加注解，可指定多种鉴权，逻辑或
@CommonAuth(type = AuthTypeEnum.NO_AUTH)
````

## 扩展

- AuthTypeEnum，增加鉴权方式
- AuthService，增加对应鉴权的实现类

# 异常

## 使用

````
1.
CommonAssert.notNull(obj, ExceptionEnums.DATA_MISSING);
CommonAssert.notNull(obj, ExceptionEnums.DATA_MISSING, "id","name");
2.
throw new CommonException(ExceptionEnums.DATA_MISSING);
throw new CommonException(ExceptionEnums.DATA_MISSING, "id");
````

## 扩展

- 统一异常处理 GlobalExceptionHandler 增加处理类型
- 断言 CommonAssert 增加断言方法
- 异常枚举 ExceptionEnums

# 分页

## 使用

- 接口增加注解@CommonPage， 默认pageNum=1,pageSize=10
  **get与post支持**：parameter指定pageNum与pageSize<br>
  **post支持**：请求体继承BasePageRequest，或请求体类包含参数pageNum与pageSize<br>
- 需要分页的sql执行前，执行PageUtils.startPage();

# 返回结构

## 使用

- ControllerResponseAdvice，统一将controller返回封装为BaseResponse结构
- PageUtils.startPage()的分页结果，自动识别
- controllerHandler支持使用注解NoControllerAdvice跳过封装

# 本地缓存

## 使用

- 方法使用注解CommonCacheable,指定group。默认缓存的key为入参的值，默认失效时间1天，默认容量1024，可配置
- 方法使用注解CommonCacheEvict,指定group与key，使对应缓存失效
- CommonCachePut, 指定group与key，更新对应缓存
- CommonCacheUtil, 通过工具类手动操作缓存

## 扩展

- 增加CacheGroup

# 操作日志

## 使用

- 方法使用注解OperatorLog，指定模块、操作类型、日志登记，可通过枚举类配置

# 多数据源

## 使用

- 配置文件增加多数据源配置 spring.datasource.dynamic=true, 默认true

````
  datasource:
    dynamic: 
      enabled: false
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    druid:
      master:
        url: jdbc:mysql://ip:port/schema?useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&characterEncoding=utf-8
        username: root
        password: root
      slave:
        # 从数据源开关/默认关闭
          enabled: true
          url: jdbc:mysql://ip:port/schema?useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&characterEncoding=utf-8
          username: root
          password: root
      initial-size: 100
````

- DataSourceType增加枚举，DruidConfig增加bean配置
- 方法或类上增加自定义注解：@DataSource(value = DataSourceType.SLAVE)
