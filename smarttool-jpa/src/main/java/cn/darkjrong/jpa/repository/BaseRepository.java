package cn.darkjrong.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * 抽取持久层通用方法
 *
 * @param <T>
 * @author Rong.Jia
 * @date 2021/12/20
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    /**
     * 根据id 获取信息
     * @param id 信息唯一标识
     * @return T 获取的信息
     * @author Rong.Jia
     * @date 2019/01/14 17:00
     */
    @Override
    Optional<T> findById(Long id);

    /**
     * 获取所有的信息
     * @return 将获取的信息封装到List中 返回
     * @author Rong.Jia
     * @date 2019/01/14 17:00
     */
    @Override
    List<T> findAll();

    /**
     *  删除指定的信息
     * @param entity 实体类信息
     * @author Rong.Jia
     * @date 2019/01/14 17:00
     */
    @Override
    void delete(T entity);

    /**
     * 根据id 删除信息
     * @param id 唯一标识
     * @author Rong.Jia
     * @date 2019/01/14 17:00
     */
    @Override
    void deleteById(Long id);

}
