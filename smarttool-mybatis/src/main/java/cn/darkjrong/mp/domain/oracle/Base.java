package cn.darkjrong.mp.domain.oracle;

import cn.darkjrong.mp.domain.BaseCommon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 公共属性父类
 *
 * @author Rong.Jia
 * @date 2023/01/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Base extends BaseCommon implements Serializable {

    private static final long serialVersionUID = -7519418012137093264L;

    /**
     * 添加时间
     */
    @ApiModelProperty("添加时间")
    protected Timestamp createdTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    protected Timestamp updatedTime;


}
