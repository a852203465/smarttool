# 核心包模块

## 1. 使用方式
### 1.1 引入依赖
```xml
<dependencies>
    <dependency>
        <groupId>cn.darkjrong</groupId>
        <artifactId>smarttool-core</artifactId>
        <version>${latestversion}</version>
    </dependency>
</dependencies>
```

### 1.2 异常包说明
 - 该版本增加三个基础的Exception类
   - cn.darkjrong.core.exceptions.StlException -> 用于方法声明异常，需要调用者处理；也可自定义异常继承该类
   - cn.darkjrong.core.exceptions.StlRuntimeException -> 用于程序逻辑抛出异常，可无需调用者处理；也可自定义异常继承该类
   - cn.darkjrong.core.exceptions.StlStatefulException -> 用于需抛出带状态异常
   - cn.darkjrong.core.exceptions.StlWebException -> 用于程序抛出逻辑异常；也可自定义异常继承该类
- 后期无需再项目自定义异常


### 1.3 异常处理说明
 - 只需在项目中定义集中存放异常枚举，实现'cn.darkjrong.core.enums.ExceptionEnum'并重写对应方法

```java
package cn.darkjrong.mescommon.enums;

import cn.darkjrong.core.enums.ExceptionEnum;
import lombok.Getter;

/**
 * 响应枚举
 *
 * @author Rong.Jia
 * @date 2023/05/12
 */
@Getter
public enum ResponseEnum implements ExceptionEnum {

   REQUEST_PARAMETER_FORMAT_IS_INCORRECT(1000, "请求参数格式不正确"),
   THE_ID_CANNOT_BE_EMPTY(1002, "ID 不能为空"),
   THE_NAME_CANNOT_BE_EMPTY(1003, "名称不能为空"),
   DATA_QUOTE(1004, "数据被引用，无法执行操作"),
   TIME_IS_EMPTY(1005, "时间为空"),
   INVALID_SPECIFIED_STATE(1006, "指定状态无效");

   private final Integer code;
   private final String message;

   @Override
   public Integer getCode() {
      return code;
   }

   @Override
   public String getMessage() {
      return message;
   }

}
```

### 1.4 项目序列化说明
- 在具体的项目中无需定义'ResponseVO'类

### 1.5 'ResponseUtils' 获取返回信息
- cn.darkjrong.core.utils.ResponseUtils










































