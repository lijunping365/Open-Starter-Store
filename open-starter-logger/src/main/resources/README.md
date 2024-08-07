# open-starter-logger

日志组件

## 功能

-[x] 写日志到文件

-[x] 读取日志文件

-[x] 支持设置日志文件写入位置

-[x] 支持设置日志文件保留天数

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.openbytecode</groupId>
    <artifactId>open-starter-logger</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置参数

```yaml
com:
  openbytecode:
    logger:
      path: /data/server/open-job/logs
```

### 3. 使用写日志功能

```java
public class OpenJobHandlerOne implements OpenJobHandler {
    private static final Logger logger = LoggerFactory.getLogger();

    @Override
    public void handler(JobParam jobParam) throws Exception {
        log.info("JobHandlerOne 处理任务");
        logger.log("任务正在处理... {}", jobParam.getJobId());
        TimeUnit.SECONDS.sleep(3);
        logger.log("已处理 3 second... {}", jobParam.getJobId());
        TimeUnit.SECONDS.sleep(5);
        //throw new RuntimeException("test exception");
        logger.log("处理完成 completed... {}", jobParam.getJobId());
    }
}
```

## 版本更新说明

### 1.0.0 版本

1. 写日志到文件，并且支持按行读取日志文件

