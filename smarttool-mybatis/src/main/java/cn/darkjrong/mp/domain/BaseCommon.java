package cn.darkjrong.mp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公共属性父类
 *
 * @author Rong.Jia
 * @date 2023/01/31
 */
@Data
public class BaseCommon implements Serializable {

    private static final long serialVersionUID = 7373982631198174412L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    protected Long id;

    /**
     * 添加人
     */
    @ApiModelProperty("添加人")
    protected String createdUser;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    protected String updatedUser;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    protected String description;


}
