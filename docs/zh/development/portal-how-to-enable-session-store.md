从 1.9.0 版本开始，apollo-portal 增加了 session 共享支持，从而可以在集群部署 apollo-portal 时实现 session 共享。

## 使用方式

### 1. 不启用 session 共享(默认)
默认配置即为不启用
所以清除 session 共享相关的配置即可，需要清理的配置如下  
外置配置文件(properties/yml)里面的 `spring.session.store-type` 配置项  
环境变量里面的 `SPRING_SESSION_STORE_TYPE`  
System Property 里面的 `spring.session.store-type`  

### 2. 基于 Redis 的 session 共享
有以下几种方式设置，按照优先级从高到低分别为：
注：redis 也支持集群、哨兵模式, 配置方式为标准的 `Spring Data Redis` 模式(以 `spring.redis` 开头的配置项), 具体方式请自行研究 `Spring Data Redis` 相关文档或咨询 `Spring Data` Group
#### 2.1 System Property
```bash
-Dspring.session.store-type=redis
-Dspring.redis.host=xxx
-Dspring.redis.port=xxx
-Dspring.redis.username=xxx
-Dspring.redis.password=xxx

```

#### 2.2 环境变量
```bash
export SPRING_SESSION_STORE_TYPE="redis"
export SPRING_REDIS_HOST="xxx"
export SPRING_REDIS_PORT="xxx"
export SPRING_REDIS_USERNAME="xxx"
export SPRING_REDIS_PASSWORD="xxx"

```

#### 2.3 外部配置文件
例如 `config/application-github.properties`
```properties
spring.session.store-type=redis
spring.redis.host=xxx
spring.redis.port=xxx
spring.redis.username=xxx
spring.redis.password=xxx

```

### 3. 基于 JDBC 的 session 共享
有以下几种方式设置，按照优先级从高到低分别为：
#### 3.1 System Property
```bash
-Dspring.session.store-type=jdbc
-Dspring.datasource.url=xxx
-Dspring.datasource.username=xxx
-Dspring.datasource.password=xxx

```

#### 3.2 环境变量
```bash
export SPRING_SESSION_STORE_TYPE="jdbc"
export SPRING_DATASOURCE_URL="xxx"
export SPRING_DATASOURCE_USERNAME="xxx"
export SPRING_DATASOURCE_PASSWORD="xxx"

```

#### 3.3 外部配置文件
例如 `config/application-github.properties`
```properties
spring.session.store-type=jdbc
spring.datasource.url=xxx
spring.datasource.username=xxx
spring.datasource.password=xxx

```

#### 关于初始化 session 的表
spring session 提供了自动建表功能, 请确保使用的数据库帐号具有 DDL 权限, 然后配置 `spring.session.jdbc.initialize-schema=always`(System Property, 环境变量, 外部配置文件均可) 即可。  
一共会自动创建两张表 `spring_session` 和 `spring_session_attributes` 创建完成后配置 `spring.session.jdbc.initialize-schema=never` 否则每次启动都会尝试去建表，虽然没有其它影响但是会刷一大堆错误日志。
