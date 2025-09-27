package cn.darkjrong.jpa.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * 公共属性父类
 *
 * @author Rong.Jia
 * @date 2023/01/31
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class BaseCommon implements Serializable {

    private static final long serialVersionUID = 7373982631198174412L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BaseCommon that = (BaseCommon) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
