package cn.darkjrong.jpa.domain.oracle;

import cn.darkjrong.jpa.domain.BaseCommon;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 公共属性父类
 * @author Rong.Jia
 * @date 2023/01/31
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Base base = (Base) o;
        return id != null && Objects.equals(id, base.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
